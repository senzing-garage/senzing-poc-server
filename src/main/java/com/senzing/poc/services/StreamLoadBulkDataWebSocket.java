package com.senzing.poc.services;

import com.senzing.api.services.BlockUpgradeIfReadOnly;
import com.senzing.api.services.BulkDataWebSocket;
import com.senzing.api.services.LoadBulkDataWebSocket;
import com.senzing.api.websocket.JsonEncoder;
import com.senzing.api.websocket.StringDecoder;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.util.Timers;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Provides an implementation of {@link BulkDataWebSocket} that loads bulk
 * data records.
 */
@ServerEndpoint(value="/load-queue/bulk-data/records",
    decoders = StringDecoder.class,
    encoders = JsonEncoder.class)
public class StreamLoadBulkDataWebSocket extends LoadBulkDataWebSocket
  implements BlockUpgradeIfReadOnly, BulkDataStreamSupport {
  /**
   * The maximum number of records to send in a batch.
   */
  protected int maxBatchCount = 10;

  /**
   * Default constructor.
   */
  public StreamLoadBulkDataWebSocket() {
    // do nothing
  }

  @Override
  public void onOpen(Session session)
      throws IOException, IllegalArgumentException
  {
    super.onOpen(session);

    Map<String, List<String>> params = this.session.getRequestParameterMap();
    List<String> paramList = params.get("maxBatchCount");

    if (paramList != null && paramList.size() > 0) {
      try {
        this.maxBatchCount = Integer.parseInt(paramList.get(0));
        
      } catch (IllegalArgumentException e) {
        throw new BadRequestException(
            "The specified maximum batch count (maxBatchCount) must be "
                + "an integer: " + paramList.get(0));
      }
    }

  }

  /**
   * Implemented to load the records once the thread is started.
   */
  protected void doRun() {
    SzPocProvider provider  = (SzPocProvider) this.getApiProvider();
    Timers        timers    = this.newTimers();

    this.streamLoadBulkRecords(provider,
                               timers,
                               this.dataSource,
                               this.mapDataSources,
                               this.mapDataSourceList,
                               this.entityType,
                               this.mapEntityTypes,
                               this.mapEntityTypeList,
                               this.loadId,
                               this.maxBatchCount,
                               this.maxFailures,
                               this.mediaType,
                               this.pipedInputStream,
                               null,
                               this.uriInfo,
                               this.progressPeriod,
                               null,
                               null,
                               this.session);

    // complete the run
    this.completeRun();
  }
}
