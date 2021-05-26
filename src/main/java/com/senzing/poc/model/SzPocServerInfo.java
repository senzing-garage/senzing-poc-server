package com.senzing.poc.model;

import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.SzServerInfo;
import com.senzing.poc.model.impl.SzPocServerInfoImpl;

/**
 * Extends {@link SzServerInfo} to add flag indicating whether or not the
 * asynchronous load queue is configured.
 */
public interface SzPocServerInfo extends SzServerInfo {
  /**
   * Checks if an asynchronous load queue has been configured for loading
   * records.
   *
   * @return <tt>true</tt> if an asynchronous load queue has been configured,
   *         otherwise <tt>false</tt>.
   */
  boolean isLoadQueueConfigured();

  /**
   * Sets whether or not an asynchronous load queue has been configured for
   * loading records.
   *
   * @param configured <tt>true</tt> if an asynchronous load queue has been
   *                   configured, and <tt>false</tt> if not.
   */
  void setLoadQueueConfigured(boolean configured);

  /**
   * Provides a default {@link SzServerInfo.Provider} implementation for
   * {@link SzServerInfo} that produces instances of {@link
   * SzPocServerInfoImpl}.
   */
  class Provider extends AbstractModelProvider<SzServerInfo>
      implements SzServerInfo.Provider
  {
    /**
     * Default constructor.
     */
    public Provider() {
      super(SzServerInfo.class, SzPocServerInfoImpl.class);
    }

    @Override
    public SzServerInfo create() {
      return new SzPocServerInfoImpl();
    }
  }

}
