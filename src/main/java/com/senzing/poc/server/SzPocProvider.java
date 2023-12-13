package com.senzing.poc.server;

import com.senzing.api.services.SzApiProvider;
import com.senzing.datamart.SzReplicationProvider;
import com.senzing.api.services.SzMessageSink;

/**
 * The {@link SzPocProvider} used by the service operations to
 * interact with the server context.
 */
public interface SzPocProvider extends SzApiProvider {
  /**
   * Checks if this instance has a configured {@link SzMessageSink} for handling
   * INFO messages in addition to the default sql-based data mart INFO message sync
   * that all POC server's have for handling INFO messages via its embedded data 
   * mart replicator.
   * 
   * @return <code>true</code> if there is an actual {@link SzMessageSink} configured
   *         in addition to data mart message queue, otherwise <code>false</code>
   */
  boolean hasConfiguredInfoSink();

  /**
   * Return the {@link SzReplicationProvider} to use for interacting with the data
   * mart replicator context.
   * 
   * @return The {@link SzReplicationProvider} to use for interacting with the 
   *         data mart replicator context.
   */
  SzReplicationProvider getReplicationProvider();
  
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
