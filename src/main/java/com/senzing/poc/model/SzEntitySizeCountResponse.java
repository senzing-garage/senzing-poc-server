package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzEntitySizeCountResponseImpl;

/**
 * Describes a response when an entity size count is requested.
 */
public interface SzEntitySizeCountResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzEntitySizeCount} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzEntitySizeCount getData();

  /**
   * Sets the data associated with this response with an {@link SzEntitySizeCount}.
   *
   * @param info The {@link SzEntitySizeCount} describing the statistics.
   */
  void setData(SzEntitySizeCount info);

  /**
   * A {@link ModelProvider} for instances of {@link SzEntitySizeCountResponse}.
   */
  interface Provider extends ModelProvider<SzEntitySizeCountResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    SzEntitySizeCountResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@liink SzMeta}, {@link SzLinks}
     * and {@link SzEntitySizeCount}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzEntitySizeCount} describing the data for 
     *              this instance.
     */
    SzEntitySizeCountResponse create(SzMeta                 meta,
                                         SzLinks            links,
                                         SzEntitySizeCount  stats);
  }
  
  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzEntitySizeCountResponse} that produces instances of
   * {@link SzEntitySizeCountResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzEntitySizeCountResponse>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzEntitySizeCountResponse.class,
            SzEntitySizeCountResponseImpl.class);
    }

    @Override
    public SzEntitySizeCountResponse create(SzMeta meta, SzLinks links){
      return new SzEntitySizeCountResponseImpl(meta, links);
    }

    @Override
    public SzEntitySizeCountResponse create(SzMeta            meta,
                                            SzLinks           links,
                                            SzEntitySizeCount stats)
    {
      return new SzEntitySizeCountResponseImpl(meta, links, stats);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzEntitySizeCountResponse}.
   */
  class Factory extends ModelFactory<SzEntitySizeCountResponse, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzEntitySizeCountResponse.class);
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
     * Creates an instance of {@link SzEntitySizeCountResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    public SzEntitySizeCountResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzEntitySizeCountResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the speicified {@link
     * SzEntitySizeCount} describing the statistics.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzEntitySizeCount} describing the statistics.
     */
    public SzEntitySizeCountResponse create(SzMeta            meta,
                                            SzLinks           links,
                                            SzEntitySizeCount stats)
    {
      return this.getProvider().create(meta, links, stats);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
