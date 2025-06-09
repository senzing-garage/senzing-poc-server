package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzEntitiesPageResponseImpl;

/**
 * Describes a response when a page of entity ID's supporting a
 * statistic is requested.
 */
public interface SzEntitiesPageResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzEntitiesPage} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzEntitiesPage getData();

  /**
   * Sets the data associated with this response with an {@link SzEntitiesPage}.
   *
   * @param entitiesPage The {@link SzEntitiesPage} describing the statistics.
   */
  void setData(SzEntitiesPage entitiesPage);

  /**
   * A {@link ModelProvider} for instances of {@link SzEntitiesPageResponse}.
   */
  interface Provider extends ModelProvider<SzEntitiesPageResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    SzEntitiesPageResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@link SzMeta}, {@link SzLinks}
     * and {@link SzEntitiesPage}.
     *
     * @param meta         The response meta data.
     *
     * @param links        The links for the response.
     *
     * @param entitiesPage The {@link SzEntitiesPage} describing the data
     *                     for this instance.
     */
    SzEntitiesPageResponse create(SzMeta meta,
        SzLinks links,
        SzEntitiesPage entitiesPage);
  }

  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzEntitiesPageResponse} that produces instances of
   * {@link SzEntitiesPageResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzEntitiesPageResponse>
      implements Provider {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzEntitiesPageResponse.class,
          SzEntitiesPageResponseImpl.class);
    }

    @Override
    public SzEntitiesPageResponse create(SzMeta meta, SzLinks links) {
      return new SzEntitiesPageResponseImpl(meta, links);
    }

    @Override
    public SzEntitiesPageResponse create(SzMeta meta,
        SzLinks links,
        SzEntitiesPage entitiesPage) {
      return new SzEntitiesPageResponseImpl(meta, links, entitiesPage);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzEntitiesPageResponse}.
   */
  class Factory extends ModelFactory<SzEntitiesPageResponse, Provider> {
    /**
     * Default constructor. This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzEntitiesPageResponse.class);
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
     * Creates an instance of {@link SzEntitiesPageResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    public SzEntitiesPageResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzEntitiesPageResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the specified {@link
     * SzEntitiesPage} describing the bulk records.
     *
     * @param meta         The response meta data.
     *
     * @param links        The links for the response.
     *
     * @param entitiesPage The {@link SzEntitiesPage} describing the
     *                     bulk records.
     */
    public SzEntitiesPageResponse create(
        SzMeta meta,
        SzLinks links,
        SzEntitiesPage entitiesPage) {
      return this.getProvider().create(meta, links, entitiesPage);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
