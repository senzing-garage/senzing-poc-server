package com.senzing.poc.services;

import com.senzing.api.services.BlockUpgradeIfReadOnly;
import com.senzing.api.services.BulkDataWebSocket;
import com.senzing.api.services.LoadBulkDataWebSocket;
import com.senzing.api.websocket.JsonEncoder;
import com.senzing.api.websocket.StringDecoder;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.util.Timers;

import javax.websocket.server.ServerEndpoint;

/**
 * Provides an implementation of {@link BulkDataWebSocket} that loads bulk
 * data records.
 */
@ServerEndpoint(value="/load-queue/bulk-data/records",
    decoders = StringDecoder.class,
    encoders = JsonEncoder.class)
public class StreamLoadBulkDataWebSocket extends LoadBulkDataWebSocket
  implements BlockUpgradeIfReadOnly, BulkDataStreamSupport
{
  /**
   * Default constructor.
   */
  public StreamLoadBulkDataWebSocket() {
    // do nothing
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
