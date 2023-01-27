package com.senzing.poc.services;

import com.senzing.api.model.SzBulkLoadResponse;
import com.senzing.api.model.SzBulkLoadResult;
import com.senzing.api.services.BulkDataSupport;
import com.senzing.api.services.SzMessage;
import com.senzing.api.services.SzMessageSink;
import com.senzing.io.RecordReader;
import com.senzing.io.TemporaryDataCache;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.util.AccessToken;
import com.senzing.util.JsonUtilities;
import com.senzing.util.LoggingUtilities;
import com.senzing.util.Timers;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.json.JsonObject;
import javax.websocket.Session;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;
import java.io.*;
import java.util.*;
import java.util.function.Supplier;

import static com.senzing.api.model.SzBulkDataStatus.ABORTED;
import static com.senzing.api.model.SzBulkDataStatus.COMPLETED;
import static com.senzing.api.model.SzHttpMethod.POST;
import static com.senzing.poc.services.StreamLoadUtilities.logFailedAsyncLoad;
import static com.senzing.io.IOUtilities.UTF_8;
import static com.senzing.util.LoggingUtilities.*;

/**
 * Extends {@link BulkDataSupport} and {@link StreamLoadSupport} to provide
 * support functions for working with stream loading bulk data.
 */
public interface BulkDataStreamSupport
    extends BulkDataSupport, StreamLoadSupport
{
  /**
   * The maximum number of bytes for a micro batch to avoid queue limits.
   */
  int MAXIMUM_BATCH_BYTES = 224 * 1024;

  /**
   * Prepares for performing a stream-loading operation by ensuring a load
   * queue is configured, the server is not in read-only mode and a long-running
   * operation is authorized.
   *
   * @parma provider The {@link SzPocProvider} to use.
   * @param uriInfo The {@link UriInfo} for the request.
   * @param timers The {@link Timers} tracking timing for the operation.
   *
   * @throws ForbiddenException If no load queue is configured.
   * @throws ServiceUnavailableException If too many long-running operaitons are
   *                                     already running.
   */
  default AccessToken prepareStreamLoadOperation(SzPocProvider provider,
                                                 UriInfo       uriInfo,
                                                 Timers        timers)
      throws ForbiddenException, ServiceUnavailableException
  {
    if (!provider.hasLoadSink()) {
      throw newForbiddenException(
          POST, uriInfo, timers,
          "Cannot perform stream loading operations.  No load queue was "
              + "configured at startup.");
    }

    return this.prepareBulkLoadOperation(provider, uriInfo, timers);
  }

  /**
   * Appends the records in the bulk data to the load queue.
   *
   */
  default SzBulkLoadResponse streamLoadBulkRecords(
      SzPocProvider               provider,
      Timers                      timers,
      String                      dataSource,
      String                      mapDataSources,
      List<String>                mapDataSourceList,
      String                      explicitLoadId,
      int                         maxBatchCount,
      int                         maxFailures,
      MediaType                   mediaType,
      InputStream                 dataInputStream,
      FormDataContentDisposition  fileMetaData,
      UriInfo                     uriInfo,
      Long                        progressPeriod,
      SseEventSink                sseEventSink,
      Sse                         sse,
      Session                     webSocketSession)
  {
    MediaType specifiedMediaType = mediaType;

    // check if the maximum batch count is less-than or equal to zero
    if (maxBatchCount <= 0) maxBatchCount = Integer.MAX_VALUE;

    // convert the progress period to nanoseconds
    Long progressNanos = (progressPeriod == null)
        ? null : progressPeriod * 1000000L;

    OutboundSseEvent.Builder eventBuilder
        = (sseEventSink != null && sse != null) ? sse.newEventBuilder() : null;

    SzBulkLoadResult bulkLoadResult = this.newBulkLoadResult();

    // populate the entity type and data source maps
    Map<String, String> dataSourceMap = new HashMap<>();
    this.prepareBulkDataMappings(provider,
                                 uriInfo,
                                 timers,
                                 dataSource,
                                 mapDataSources,
                                 mapDataSourceList,
                                 dataSourceMap);

    ProgressState progressState = new ProgressState();

    try {
      BulkDataSet bulkDataSet = new BulkDataSet(mediaType, dataInputStream);

      TemporaryDataCache dataCache = bulkDataSet.getDataCache();

      String charset = bulkDataSet.getCharacterEncoding();

      debugLog("Bulk data character encoding: " + charset);

      String loadId = (explicitLoadId == null)
          ? formatLoadId(dataCache, fileMetaData) : explicitLoadId;

      SzMessageSink loadSink = provider.acquireLoadSink();

      // check if we need to auto-detect the media type
      try (InputStream        is  = dataCache.getInputStream(true);
           InputStreamReader  isr = new InputStreamReader(is, charset);
           BufferedReader     br  = new BufferedReader(isr))
      {
        // if format is null then RecordReader will auto-detect
        RecordReader recordReader = new RecordReader(null,
                                                     br,
                                                     dataSourceMap,
                                                     loadId);

        this.verifyBulkDataFormat(specifiedMediaType,
                                  bulkDataSet.getFormat(),
                                  recordReader.getFormat(),
                                  uriInfo,
                                  timers);

        // override the format accordingly
        bulkDataSet.setFormat(recordReader.getFormat());

        if (LoggingUtilities.isDebugLogging()) {
          System.out.println("Bulk data format: " + bulkDataSet.getFormat());
        }

        bulkLoadResult.setCharacterEncoding(charset);
        bulkLoadResult.setMediaType(bulkDataSet.getFormat().getMediaType());

        boolean       done      = false;
        SzMessage[]   failedMsg = { null };

        ByteArrayOutputStream batchBytes
            = new ByteArrayOutputStream(MAXIMUM_BATCH_BYTES);

        int             batchCount    = 0;
        String          prefix        = "[";
        List<String[]>  trackingList  = new LinkedList<>();

        boolean aborted = false;
        ProgressUpdater<SzBulkLoadResponse> progressUpdater = null;
        try {
          // loop through the records and handle each record
          while (!done) {
            JsonObject record = recordReader.readRecord();

            // check if the record is null
            if (record == null) {
              done = true;
            }

            // check if we have a data source and entity type
            String resolvedDS = (done) ? null
                : JsonUtilities.getString(record, "DATA_SOURCE");
            if ((!done)
                && (resolvedDS == null || resolvedDS.trim().length() == 0))
            {
              debugLog("Incomplete record not set: "
                           + JsonUtilities.toJsonText(record));

              bulkLoadResult.trackIncompleteRecord(resolvedDS);

            } else {
              String recordText = (done) ? null : JsonUtilities.toJsonText(record);
              byte[] recordBytes = (done) ? null : recordText.getBytes(UTF_8);
              int byteCount = (done) ? 0 : recordBytes.length + 3;

              String messageBody = null;
              List<String[]> batchTrackingList = trackingList;

              // check if adding this record to the batch does NOT exceed the
              // maximum number of bytes in a batch nor max number of records
              if ((!done) && (byteCount + batchBytes.size() < MAXIMUM_BATCH_BYTES)
                  && (batchCount < maxBatchCount)) {
                batchCount++;
                batchBytes.write(prefix.getBytes(UTF_8));
                batchBytes.write(recordBytes);
                prefix = ",";
                String[] trackParams = {resolvedDS};
                trackingList.add(trackParams);
                recordBytes = null;

                debugLog("Batching record " + batchCount
                      + " of " + maxBatchCount + " (max): " + recordText,
                         "Batch size is " + batchBytes.size() + " bytes of "
                             + MAXIMUM_BATCH_BYTES + " bytes (max)");
              }

              // now check if we are sending the current batch
              if ((batchCount > 0)
                  && (done || batchCount >= maxBatchCount
                  || (batchBytes.size() + 1) >= MAXIMUM_BATCH_BYTES)) {
                // create the batch message
                batchBytes.write("]".getBytes(UTF_8));
                messageBody = new String(batchBytes.toByteArray(), UTF_8);

                // reset the batch
                batchBytes = new ByteArrayOutputStream(MAXIMUM_BATCH_BYTES);
                trackingList = new LinkedList<>();
                batchCount = 0;
                prefix = "[";
              }

              // check if we are ready to send a batch
              if (messageBody != null) {
                // create the message object
                SzMessage message = new SzMessage(messageBody);

                // send the batch
                this.sendingAsyncMessage(timers, LOAD_QUEUE_NAME);
                try {
                  debugLog("Sending message: " + messageBody);

                  // send the info on the async queue
                  loadSink.send(message, (exception, msg) -> {
                    logFailedAsyncLoad(exception, msg);
                    if (failedMsg[0] != message) {
                      failedMsg[0] = message;
                      for (String[] trackParams : batchTrackingList) {
                        String trackDS = trackParams[0];
                        bulkLoadResult.trackFailedRecord(
                            trackDS, this.newError(exception));
                      }
                    }
                  });

                  // track that we successfully enqueued the record
                  for (String[] trackParams : batchTrackingList) {
                    String trackDS = trackParams[0];
                    bulkLoadResult.trackLoadedRecord(trackDS);
                  }

                } catch (Exception e) {
                  // failed async logger will not double-log
                  logFailedAsyncLoad(e, message);
                  if (failedMsg[0] != message) {
                    failedMsg[0] = message;

                    for (String[] trackParams : batchTrackingList) {
                      String trackDS = trackParams[0];
                      bulkLoadResult.trackFailedRecord(
                          trackDS, this.newError(e));
                    }
                  }

                } finally {
                  this.sentAsyncMessage(timers, LOAD_QUEUE_NAME);
                }
              }

              // now check if we have a record that was not added to the batch
              if (recordBytes != null) {
                // check if the individual message is simply too large to send
                if ((byteCount + 2) >= MAXIMUM_BATCH_BYTES) {
                  bulkLoadResult.trackFailedRecord(
                      resolvedDS,
                      this.newError("Maximum message size ("
                                        + MAXIMUM_BATCH_BYTES + ") exceeded: "
                                        + byteCount));
                } else {
                  // add this record to the newly created batch
                  batchCount++;
                  batchBytes.write(prefix.getBytes(UTF_8));
                  batchBytes.write(recordBytes);
                  prefix = ",";
                  String[] trackParams = {resolvedDS};
                  trackingList.add(trackParams);

                  debugLog("Batching record " + batchCount
                             + " of " + maxBatchCount + " (max): " + recordText,
                           "Batch size is " + batchBytes.size() + " bytes of "
                             + MAXIMUM_BATCH_BYTES + " bytes (max)");
                }
              }
            }

            // count the number of failures
            int failedCount = bulkLoadResult.getFailedRecordCount()
                + bulkLoadResult.getIncompleteRecordCount();

            // break if aborted
            if (maxFailures > 0 && failedCount >= maxFailures) {
              aborted = true;
              break;
            }

            // check if the timing has gone beyond the specified progress period
            if ((progressNanos != null) && (progressUpdater == null)
                && (eventBuilder != null || webSocketSession != null))
            {
              // create the update response if there is a client expecting it
              progressState.setStartTime(System.nanoTime());
              Supplier<SzBulkLoadResponse> supplier = () -> {
                return this.newBulkLoadResponse(
                    POST, 200, uriInfo, timers, bulkLoadResult);
              };
              progressUpdater = new ProgressUpdater<>(progressNanos,
                                                      progressState,
                                                      progressState, // monitor
                                                      supplier,
                                                      sseEventSink,
                                                      eventBuilder,
                                                      webSocketSession);
              progressUpdater.start();
            }
          }

        } finally {
          // make sure to clean up the progress updater
          if (progressUpdater != null) {
            // calling this should mark it complete and trigger wake-up
            progressUpdater.complete();
            try {
              // wait for the thread to complete before proceeding
              progressUpdater.join();
            } catch (InterruptedException ignore) {
              // ignore the exception
            }
          }

          // check if aborted
          if (aborted) {
            bulkLoadResult.setStatus(ABORTED);
          }
        }

        // set the status to completed if not aborted
        if (bulkLoadResult.getStatus() != ABORTED) {
          bulkLoadResult.setStatus(COMPLETED);
        }

      } finally {
        if (loadSink != null) provider.releaseLoadSink(loadSink);
        dataCache.delete();
      }

    } catch (IOException e) {
      bulkLoadResult.setStatus(ABORTED);
      SzBulkLoadResponse response = this.newBulkLoadResponse(POST,
                                                             200,
                                                             uriInfo,
                                                             timers,
                                                             bulkLoadResult);
      this.abortOperation(e,
                          response,
                          uriInfo,
                          timers,
                          progressState.nextEventId(),
                          eventBuilder,
                          sseEventSink,
                          webSocketSession);
    }

    SzBulkLoadResponse response = this.newBulkLoadResponse(POST,
                                                           200,
                                                           uriInfo,
                                                           timers,
                                                           bulkLoadResult);

    return this.completeOperation(eventBuilder,
                                  sseEventSink,
                                  progressState.nextEventId(),
                                  webSocketSession,
                                  response);
  }

}
