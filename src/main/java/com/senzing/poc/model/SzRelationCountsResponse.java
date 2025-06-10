package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzRelationCountsResponseImpl;

/**
 * Describes a response when source summary statistics are requested.
 */
public interface SzRelationCountsResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzRelationCountsResponseData} associated with this
   * response.
   *
   * @return The data associated with this response.
   */
  SzRelationCountsResponseData getData();

  /**
   * Sets the data associated with this response with an
   * {@link SzRelationCountsResponseData}.
   *
   * @param countsData The {@link SzRelationCountsResponseData} describing
   *                   the statistics.
   */
  void setData(SzRelationCountsResponseData countsData);

  /**
   * A {@link ModelProvider} for instances of {@link SzRelationCountsResponse}.
   */
  interface Provider extends ModelProvider<SzRelationCountsResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    SzRelationCountsResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@link SzMeta},
     * {@link SzLinks} and {@link SzRelationCountsResponseData}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     *
     * @param data  The {@link SzRelationCountsResponseData} describing the
     *              data for this instance.
     */
    SzRelationCountsResponse create(SzMeta meta,
        SzLinks links,
        SzRelationCountsResponseData data);
  }

  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzRelationCountsResponse} that produces instances of
   * {@link SzRelationCountsResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzRelationCountsResponse>
      implements Provider {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzRelationCountsResponse.class,
          SzRelationCountsResponseImpl.class);
    }

    @Override
    public SzRelationCountsResponse create(SzMeta meta, SzLinks links) {
      return new SzRelationCountsResponseImpl(meta, links);
    }

    @Override
    public SzRelationCountsResponse create(SzMeta meta,
        SzLinks links,
        SzRelationCountsResponseData data) {
      return new SzRelationCountsResponseImpl(meta, links, data);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzRelationCountsResponse}.
   */
  class Factory extends ModelFactory<SzRelationCountsResponse, Provider> {
    /**
     * Default constructor. This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzRelationCountsResponse.class);
    }

    /**
     * Constructs with the default provider. This constructor is private and
     * is used for the master singleton instance.
     * 
     * @param defaultProvider The default provider.
     */
    private Factory(Provider defaultProvider) {
      super(defaultProvider);
    }

    /**
     * Creates an instance of {@link SzRelationCountsResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    public SzRelationCountsResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzRelationCountsResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the specified {@link
     * SzRelationCountsResponseData} describing the statistics.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     *
     * @param data  The {@link SzRelationCountsResponseData} describing
     *              the statistics.
     */
    public SzRelationCountsResponse create(SzMeta meta,
        SzLinks links,
        SzRelationCountsResponseData data) {
      return this.getProvider().create(meta, links, data);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
