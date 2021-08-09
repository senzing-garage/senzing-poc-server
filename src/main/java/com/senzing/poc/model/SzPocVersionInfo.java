package com.senzing.poc.model;

import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelProvider;
import com.senzing.api.model.impl.SzVersionInfoImpl;
import com.senzing.poc.BuildInfo;
import com.senzing.api.model.SzVersionInfo;
import com.senzing.poc.model.impl.SzPocVersionInfoImpl;

import javax.json.JsonObject;

/**
 * Extends {@link SzVersionInfo} to provide POC-specific fields.
 */
public interface SzPocVersionInfo extends SzVersionInfo {
  /**
   * Gets the version of the POC REST API Server implementation.
   *
   * @return The version of the POC REST API Server implementation.
   */
  String getPocServerVersion();

  /**
   * Gets the version of the POC REST API Specification that is implemented.
   *
   * @return The version of the POC REST API Specification that is implemented.
   */
  String getPocApiVersion();

  /**
   * Provides a default {@link SzVersionInfo.Provider} implementation for {@link
   * SzPocVersionInfo} that produces instances of {@link SzVersionInfoImpl}.
   */
  class Provider extends AbstractModelProvider<SzVersionInfo>
      implements SzVersionInfo.Provider
  {
    /**
     * Default constructor.
     */
    public Provider() {
      super(SzVersionInfo.class, SzPocVersionInfoImpl.class);
    }

    @Override
    public SzVersionInfo create() {
      return new SzPocVersionInfoImpl();
    }
  }
}
