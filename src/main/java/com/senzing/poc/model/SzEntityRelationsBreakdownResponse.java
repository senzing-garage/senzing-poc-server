package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzEntityRelationsBreakdownResponseImpl;

/**
 * Describes a response when an entity size breakdown is requested.
 */
public interface SzEntityRelationsBreakdownResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzEntityRelationsBreakdown} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzEntityRelationsBreakdown getData();

  /**
   * Sets the data associated with this response with an
   * {@link SzEntityRelationsBreakdown}.
   *
   * @param info The {@link SzEntityRelationsBreakdown} describing the statistics.
   */
  void setData(SzEntityRelationsBreakdown info);

  /**
   * A {@link ModelProvider} for instances of
   * {@link SzEntityRelationsBreakdownResponse}.
   */
  interface Provider extends ModelProvider<SzEntityRelationsBreakdownResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    SzEntityRelationsBreakdownResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@link SzMeta}, {@link SzLinks}
     * and {@link SzEntityRelationsBreakdown}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzEntityRelationsBreakdown} describing the data
     *              for this instance.
     */
    SzEntityRelationsBreakdownResponse create(SzMeta meta,
        SzLinks links,
        SzEntityRelationsBreakdown stats);
  }

  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzEntityRelationsBreakdownResponse} that produces instances of
   * {@link SzEntityRelationsBreakdownResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzEntityRelationsBreakdownResponse>
      implements Provider {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzEntityRelationsBreakdownResponse.class,
          SzEntityRelationsBreakdownResponseImpl.class);
    }

    @Override
    public SzEntityRelationsBreakdownResponse create(SzMeta meta, SzLinks links) {
      return new SzEntityRelationsBreakdownResponseImpl(meta, links);
    }

    @Override
    public SzEntityRelationsBreakdownResponse create(SzMeta meta,
        SzLinks links,
        SzEntityRelationsBreakdown countStats) {
      return new SzEntityRelationsBreakdownResponseImpl(meta, links, countStats);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzEntityRelationsBreakdownResponse}.
   */
  class Factory extends ModelFactory<SzEntityRelationsBreakdownResponse, Provider> {
    /**
     * Default constructor. This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzEntityRelationsBreakdownResponse.class);
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
     * Creates an instance of {@link SzEntityRelationsBreakdownResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    public SzEntityRelationsBreakdownResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzEntityRelationsBreakdownResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the specified {@link
     * SzEntityRelationsBreakdown} describing the bulk records.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzEntityRelationsBreakdown} describing the
     *              statistics.
     */
    public SzEntityRelationsBreakdownResponse create(
        SzMeta meta,
        SzLinks links,
        SzEntityRelationsBreakdown stats) {
      return this.getProvider().create(meta, links, stats);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
