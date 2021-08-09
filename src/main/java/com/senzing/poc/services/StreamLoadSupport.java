package com.senzing.poc.services;

import com.senzing.api.services.ServicesSupport;
import com.senzing.api.services.SzMessage;
import com.senzing.api.services.SzMessageSink;
import com.senzing.poc.server.SzPocProvider;
import com.senzing.util.Timers;

import static com.senzing.poc.services.StreamLoadUtilities.logFailedAsyncLoad;

/**
 * Provides a base interface for stream loading.
 */
public interface StreamLoadSupport extends ServicesSupport {
  /**
   * The queue name to use when logging timings for the LOAD queue.
   */
  String LOAD_QUEUE_NAME = "LOAD";

  /**
   * Loads the record described by the specified text asynchronously by adding
   * it to the load queue configured for the specified {@link SzPocProvider}.
   *
   * @param provider The {@link SzPocProvider} to use.
   * @param timers The {@link Timers} used to track timing for the operation.
   * @param recordText The JSON text describing the record.
   *
   */
  default void asyncLoadRecord(SzPocProvider  provider,
                               SzMessageSink  loadSink,
                               Timers         timers,
                               String         recordText)
    throws Exception
  {
    boolean acquiredLoadSink = false;
    if (loadSink == null) {
      loadSink = provider.acquireLoadSink();
      acquiredLoadSink = true;
    }
    SzMessage message = new SzMessage(recordText);
    try {
      this.sendingAsyncMessage(timers, LOAD_QUEUE_NAME);

      // send the info on the async queue
      loadSink.send(message, (e, msg) -> {
        StreamLoadUtilities.logFailedAsyncLoad(e, msg);
        throw e;
      });

    } catch (Exception e) {
      // failed async logger will not double-log
      logFailedAsyncLoad(e, message);
      throw e;

    } finally {
      this.sentAsyncMessage(timers, LOAD_QUEUE_NAME);
      if (acquiredLoadSink) provider.releaseLoadSink(loadSink);
    }
  }

}
