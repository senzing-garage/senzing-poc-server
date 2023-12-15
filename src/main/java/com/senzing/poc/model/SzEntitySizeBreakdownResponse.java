package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzEntitySizeBreakdownResponseImpl;

/**
 * Describes a response when an entity size breakdown is requested.
 */
public interface SzEntitySizeBreakdownResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzEntitySizeBreakdown} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzEntitySizeBreakdown getData();

  /**
   * Sets the data associated with this response with an {@link SzEntitySizeBreakdown}.
   *
   * @param info The {@link SzEntitySizeBreakdown} describing the statistics.
   */
  void setData(SzEntitySizeBreakdown info);

  /**
   * A {@link ModelProvider} for instances of {@link SzEntitySizeBreakdownResponse}.
   */
  interface Provider extends ModelProvider<SzEntitySizeBreakdownResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    SzEntitySizeBreakdownResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@liink SzMeta}, {@link SzLinks}
     * and {@link SzEntitySizeBreakdown}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzEntitySizeBreakdown} describing the data
     *              for this instance.
     */
    SzEntitySizeBreakdownResponse create(SzMeta                 meta,
                                         SzLinks                links,
                                         SzEntitySizeBreakdown  stats);
  }
  
  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzEntitySizeBreakdownResponse} that produces instances of
   * {@link SzEntitySizeBreakdownResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzEntitySizeBreakdownResponse>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzEntitySizeBreakdownResponse.class,
            SzEntitySizeBreakdownResponseImpl.class);
    }

    @Override
    public SzEntitySizeBreakdownResponse create(SzMeta meta, SzLinks links){
      return new SzEntitySizeBreakdownResponseImpl(meta, links);
    }

    @Override
    public SzEntitySizeBreakdownResponse create(SzMeta                meta,
                                                SzLinks               links,
                                                SzEntitySizeBreakdown stats)
    {
      return new SzEntitySizeBreakdownResponseImpl(meta, links, stats);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzEntitySizeBreakdownResponse}.
   */
  class Factory extends ModelFactory<SzEntitySizeBreakdownResponse, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzEntitySizeBreakdownResponse.class);
    }

    /**
     * Constructs with the default provider.  This constructor is private and
     * is used for the master singleton instance.
     * @param defaultProvider The default provider.
     */
    private Factory(Provider defaultProvider) {
      super(defaultProvider);
    }

    /**
     * Creates an instance of {@link SzEntitySizeBreakdownResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    public SzEntitySizeBreakdownResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzEntitySizeBreakdownResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the speicified {@link
     * SzEntitySizeBreakdown} describing the bulk records.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzEntitySizeBreakdown} describing the statistics.
     */
    public SzEntitySizeBreakdownResponse create(
        SzMeta                meta,
        SzLinks               links,
        SzEntitySizeBreakdown stats)
    {
      return this.getProvider().create(meta, links, stats);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
