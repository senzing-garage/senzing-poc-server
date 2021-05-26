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
import com.senzing.util.JsonUtils;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.senzing.api.model.SzBulkDataStatus.ABORTED;
import static com.senzing.api.model.SzBulkDataStatus.COMPLETED;
import static com.senzing.api.model.SzHttpMethod.POST;
import static com.senzing.poc.services.StreamLoadUtilities.logFailedAsyncLoad;

/**
 * Extends {@link BulkDataSupport} and {@link StreamLoadSupport} to provide
 * support functions for working with stream loading bulk data.
 */
public interface BulkDataStreamSupport
    extends BulkDataSupport, StreamLoadSupport
{
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
      String                      entityType,
      String                      mapEntityTypes,
      List<String>                mapEntityTypeList,
      String                      explicitLoadId,
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
    OutboundSseEvent.Builder eventBuilder
        = (sseEventSink != null && sse != null) ? sse.newEventBuilder() : null;

    SzBulkLoadResult bulkLoadResult = this.newBulkLoadResult();

    // populate the entity type and data source maps
    Map<String, String> dataSourceMap = new HashMap<>();
    Map<String, String> entityTypeMap = new HashMap<>();
    this.prepareBulkDataMappings(provider,
                                 uriInfo,
                                 timers,
                                 dataSource,
                                 mapDataSources,
                                 mapDataSourceList,
                                 entityType,
                                 mapEntityTypes,
                                 mapEntityTypeList,
                                 dataSourceMap,
                                 entityTypeMap);

    ProgressState progressState = new ProgressState();

    try {
      BulkDataSet bulkDataSet = new BulkDataSet(mediaType, dataInputStream);

      TemporaryDataCache dataCache = bulkDataSet.getDataCache();

      String charset = bulkDataSet.getCharacterEncoding();

      String loadId = (explicitLoadId == null)
          ? formatLoadId(dataCache, fileMetaData) : explicitLoadId;

      SzMessageSink loadSink = provider.acquireLoadSink();

      // check if we need to auto-detect the media type
      try (InputStream        is  = dataCache.getInputStream(true);
           InputStreamReader isr = new InputStreamReader(is, charset);
           BufferedReader br  = new BufferedReader(isr))
      {
        // if format is null then RecordReader will auto-detect
        RecordReader recordReader = new RecordReader(bulkDataSet.getFormat(),
                                                     br,
                                                     dataSourceMap,
                                                     entityTypeMap,
                                                     loadId);
        bulkDataSet.setFormat(recordReader.getFormat());
        bulkLoadResult.setCharacterEncoding(charset);
        bulkLoadResult.setMediaType(bulkDataSet.getFormat().getMediaType());

        boolean       done      = false;
        SzMessage[]   failedMsg = { null };

        // loop through the records and handle each record
        while (!done) {
          JsonObject record = recordReader.readRecord();

          // check if the record is null
          if (record == null) {
            done = true;
            continue;
          }

          // check if we have a data source and entity type
          String resolvedDS = JsonUtils.getString(record, "DATA_SOURCE");
          String resolvedET = JsonUtils.getString(record, "ENTITY_TYPE");
          if (resolvedDS == null || resolvedDS.trim().length() == 0
              || resolvedET == null || resolvedET.trim().length() == 0)
          {
            bulkLoadResult.trackIncompleteRecord(resolvedDS, resolvedET);

          } else {
            String      recordText  = JsonUtils.toJsonText(record);
            SzMessage   message     = new SzMessage(recordText);

            this.sendingAsyncMessage(timers, LOAD_QUEUE_NAME);
            try {
              // send the info on the async queue
              loadSink.send(message, (exception, msg) -> {
                logFailedAsyncLoad(exception, msg);
                if (failedMsg[0] != message) {
                  failedMsg[0] = message;
                  bulkLoadResult.trackFailedRecord(
                      resolvedDS, resolvedET, this.newError(exception));
                }
              });

              // track that we successfully enqueued the record
              bulkLoadResult.trackLoadedRecord(resolvedDS, resolvedET);

            } catch (Exception e) {
              // failed async logger will not double-log
              logFailedAsyncLoad(e, message);
              if (failedMsg[0] != message) {
                failedMsg[0] = message;
                bulkLoadResult.trackFailedRecord(
                    resolvedDS, resolvedET, this.newError(e));
              }

            } finally {
              this.sentAsyncMessage(timers, LOAD_QUEUE_NAME);
            }
          }

          // check if aborted and handle reporting periodic progress
          boolean aborted = this.checkAbortLoadDoProgress(uriInfo,
                                                          timers,
                                                          bulkLoadResult,
                                                          maxFailures,
                                                          progressState,
                                                          progressPeriod,
                                                          sseEventSink,
                                                          eventBuilder,
                                                          webSocketSession);

          // break if aborted
          if (aborted) break;
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
