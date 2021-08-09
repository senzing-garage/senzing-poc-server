package com.senzing.poc.model.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzHttpMethod;
import com.senzing.api.model.impl.SzMetaImpl;
import com.senzing.poc.BuildInfo;
import com.senzing.poc.model.SzPocMeta;
import com.senzing.util.Timers;

/**
 * Provides a default implementation of {@link SzPocMeta}.
 */
@JsonDeserialize
public class SzPocMetaImpl extends SzMetaImpl implements SzPocMeta {
  /**
   * The server version number for the response.
   */
  private String pocServerVersion;

  /**
   * The Senzing REST API version implemented by the server.
   */
  private String pocRestApiVersion;

  /**
   * Default constructor for reconstructing from JSON.
   */
  protected SzPocMetaImpl() {
    this.pocServerVersion   = null;
    this.pocRestApiVersion  = null;
  }

  /**
   * Constructs with the specified HTTP method.
   *
   * @param httpMethod     The HTTP method with which to construct.
   * @param httpStatusCode The HTTP response code.
   */
  public SzPocMetaImpl(SzHttpMethod httpMethod, int httpStatusCode, Timers timers) {
    super(httpMethod, httpStatusCode, timers);
    this.pocServerVersion   = BuildInfo.MAVEN_VERSION;
    this.pocRestApiVersion  = BuildInfo.POC_REST_API_VERSION;
  }

  /**
   * Gets the version of the POC REST API Server implementation.
   *
   * @return The version of the POC REST API Server implementation.
   */
  @Override
  public String getPocServerVersion() {
    return this.pocServerVersion;
  }

  /**
   * Gets the version of the POC REST API Specification that is implemented.
   *
   * @return The version of the POC REST API Specification that is implemented.
   */
  @Override
  public String getPocApiVersion() {
    return this.pocRestApiVersion;
  }
}
