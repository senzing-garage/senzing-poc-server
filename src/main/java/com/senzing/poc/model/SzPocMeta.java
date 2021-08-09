package com.senzing.poc.model;

import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelProvider;
import com.senzing.api.model.SzHttpMethod;
import com.senzing.api.model.SzMeta;
import com.senzing.poc.model.impl.SzPocMetaImpl;
import com.senzing.util.Timers;

/**
 * Extends {@link SzMeta} to add fields for the POC.
 */
public interface SzPocMeta extends SzMeta {
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
   * Provides a default {@link SzMeta.Provider} implementation for {@link SzMeta}
   * that produces instances of {@link SzPocMetaImpl}.
   */
  class Provider extends AbstractModelProvider<SzMeta>
      implements SzMeta.Provider
  {
    /**
     * Default constructor.
     */
    public Provider() {
      super(SzMeta.class, SzPocMetaImpl.class);
    }

    @Override
    public SzMeta create(SzHttpMethod httpMethod,
                         int          httpStatusCode,
                         Timers       timers) {
      return new SzPocMetaImpl(httpMethod, httpStatusCode, timers);
    }
  }
}
