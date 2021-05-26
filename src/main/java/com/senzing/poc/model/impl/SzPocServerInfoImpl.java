package com.senzing.poc.model.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.impl.SzServerInfoImpl;
import com.senzing.poc.model.SzPocServerInfo;

/**
 * Provides a default implementation of {@link SzPocServerInfo}.
 */
@JsonDeserialize
public class SzPocServerInfoImpl extends SzServerInfoImpl
    implements SzPocServerInfo
{
  /**
   * Whether or not the asynchronous load queue has been configured for
   * loading records.
   */
  private boolean loadQueueConfigured = false;

  /**
   * Default constructor.
   */
  public SzPocServerInfoImpl() {
    super();
    this.loadQueueConfigured = false;
  }

  /**
   * Checks if an asynchronous load queue has been configured for loading
   * records.
   *
   * @return <tt>true</tt> if an asynchronous load queue has been configured,
   *         otherwise <tt>false</tt>.
   */
  @Override
  public boolean isLoadQueueConfigured() {
    return this.loadQueueConfigured;
  }

  /**
   * Sets whether or not an asynchronous load queue has been configured for
   * loading records.
   *
   * @param configured <tt>true</tt> if an asynchronous load queue has been
   *                   configured, and <tt>false</tt> if not.
   */
  @Override
  public void setLoadQueueConfigured(boolean configured) {
    this.loadQueueConfigured = configured;
  }
}
