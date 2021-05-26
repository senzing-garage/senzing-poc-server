package com.senzing.poc.model.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzVersionInfo;
import com.senzing.api.model.impl.SzVersionInfoImpl;
import com.senzing.poc.BuildInfo;
import com.senzing.poc.model.SzPocVersionInfo;

import javax.json.JsonObject;

/**
 * Extends {@link SzVersionInfo} to provide POC-specific fields.
 */
@JsonDeserialize
public class SzPocVersionInfoImpl extends SzVersionInfoImpl
    implements SzPocVersionInfo
{
  /**
   * The version of the POC REST API Server implementation.
   */
  private String pocServerVersion;

  /**
   * The version of the POC REST API that is implemented.
   */
  private String pocApiVersion;

  /**
   * Default constructor.
   */
  public SzPocVersionInfoImpl() {
    super();
    this.pocServerVersion = BuildInfo.MAVEN_VERSION;
    this.pocApiVersion = BuildInfo.POC_REST_API_VERSION;
  }

  /**
   * Gets the version of the POC REST API Server implementation.
   *
   * @return The version of the POC REST API Server implementation.
   */
  public String getPocServerVersion() {
    return this.pocServerVersion;
  }

  /**
   * Private setter for the version of the POC REST API Server implementation.
   * This is used for JSON serialization -- otherwise the version cannot be
   * normally set (it is inferred).
   *
   * @param pocServerVersion The version of the POC REST API Server
   *                         implementation.
   */
  private void setPocServerVersion(String pocServerVersion) {
    this.pocServerVersion = pocServerVersion;
  }

  /**
   * Gets the version of the POC REST API Specification that is implemented.
   *
   * @return The version of the POC REST API Specification that is implemented.
   */
  public String getPocApiVersion() {
    return this.pocApiVersion;
  }

  /**
   * Private setter for the POC REST API version implemented by the POC REST
   * API Server implementation.  This is used for JSON serialization --
   * otherwise the version cannot be normally set (it is inferred).
   *
   * @param pocApiVersion The version of the POC REST API Specification that is
   *                      implemented.
   */
  private void setPocApiVersion(String pocApiVersion) {
    this.pocApiVersion = pocApiVersion;
  }
}
