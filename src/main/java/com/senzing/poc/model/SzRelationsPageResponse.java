package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzRelationsPageResponseImpl;

/**
 * Describes a response when a page of entity ID's supporting a
 * statistic is requested.
 */
public interface SzRelationsPageResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzRelationsPage} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzRelationsPage getData();

  /**
   * Sets the data associated with this response with an {@link SzRelationsPage}.
   *
   * @param relationsPage The {@link SzRelationsPage} describing the statistics.
   */
  void setData(SzRelationsPage relationsPage);

  /**
   * A {@link ModelProvider} for instances of {@link SzRelationsPageResponse}.
   */
  interface Provider extends ModelProvider<SzRelationsPageResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    SzRelationsPageResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@link SzMeta}, {@link SzLinks}
     * and {@link SzRelationsPage}.
     *
     * @param meta          The response meta data.
     *
     * @param links         The links for the response.
     *
     * @param relationsPage The {@link SzRelationsPage} describing the data for
     *                      this instance.
     */
    SzRelationsPageResponse create(SzMeta meta,
        SzLinks links,
        SzRelationsPage relationsPage);
  }

  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzRelationsPageResponse} that produces instances of
   * {@link SzRelationsPageResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzRelationsPageResponse>
      implements Provider {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzRelationsPageResponse.class,
          SzRelationsPageResponseImpl.class);
    }

    @Override
    public SzRelationsPageResponse create(SzMeta meta, SzLinks links) {
      return new SzRelationsPageResponseImpl(meta, links);
    }

    @Override
    public SzRelationsPageResponse create(SzMeta meta,
        SzLinks links,
        SzRelationsPage relationsPage) {
      return new SzRelationsPageResponseImpl(meta, links, relationsPage);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzRelationsPageResponse}.
   */
  class Factory extends ModelFactory<SzRelationsPageResponse, Provider> {
    /**
     * Default constructor. This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzRelationsPageResponse.class);
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
     * Creates an instance of {@link SzRelationsPageResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    public SzRelationsPageResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzRelationsPageResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the specified {@link
     * SzRelationsPage} describing the bulk records.
     *
     * @param meta          The response meta data.
     *
     * @param links         The links for the response.
     *
     * @param relationsPage The {@link SzRelationsPage} describing the
     *                      bulk records.
     */
    public SzRelationsPageResponse create(
        SzMeta meta,
        SzLinks links,
        SzRelationsPage relationsPage) {
      return this.getProvider().create(meta, links, relationsPage);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
