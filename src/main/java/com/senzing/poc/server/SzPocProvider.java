package com.senzing.poc.server;

import com.senzing.api.services.SzApiProvider;
import com.senzing.api.services.SzMessageSink;

public interface SzPocProvider extends SzApiProvider {
  /**
   * Checks if there is a load message sink configured for asynchronous loading.
   *
   * @return <tt>true</tt> if a load message sink is configured, and
   *         <tt>false</tt> if none is configured.
   */
  boolean hasLoadSink();

  /**
   * Acquires the {@link SzMessageSink} for sending record messages for loading.
   * This returns <tt>null</tt> if an loading queue is not configured.
   *
   * @return The {@link SzMessageSink} for sending record messages for loading,
   *         or <tt>null</tt> if none is configured.
   */
  SzMessageSink acquireLoadSink();

  /**
   * Releases the {@link SzMessageSink} for sending record messages for loading.
   *
   * @param sink The {@link SzMessageSink} to be released.
   */
  void releaseLoadSink(SzMessageSink sink);

}
