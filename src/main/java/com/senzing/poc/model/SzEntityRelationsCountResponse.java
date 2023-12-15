package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzEntityRelationsCountResponseImpl;

/**
 * Describes a response when an entity relations count is requested.
 */
public interface SzEntityRelationsCountResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzEntityRelationsCount} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzEntityRelationsCount getData();

  /**
   * Sets the data associated with this response with an {@link SzEntityRelationsCount}.
   *
   * @param info The {@link SzEntityRelationsCount} describing the statistics.
   */
  void setData(SzEntityRelationsCount info);

  /**
   * A {@link ModelProvider} for instances of {@link SzEntityRelationsCountResponse}.
   */
  interface Provider extends ModelProvider<SzEntityRelationsCountResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    SzEntityRelationsCountResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@liink SzMeta}, {@link SzLinks}
     * and {@link SzEntityRelationsCount}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzEntityRelationsCount} describing the data 
     *              for this instance.
     */
    SzEntityRelationsCountResponse create(SzMeta                meta,
                                         SzLinks                links,
                                         SzEntityRelationsCount stats);
  }
  
  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzEntityRelationsCountResponse} that produces instances of
   * {@link SzEntityRelationsCountResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzEntityRelationsCountResponse>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzEntityRelationsCountResponse.class,
            SzEntityRelationsCountResponseImpl.class);
    }

    @Override
    public SzEntityRelationsCountResponse create(SzMeta meta, SzLinks links){
      return new SzEntityRelationsCountResponseImpl(meta, links);
    }

    @Override
    public SzEntityRelationsCountResponse create(SzMeta             meta,
                                            SzLinks                 links,
                                            SzEntityRelationsCount  stats)
    {
      return new SzEntityRelationsCountResponseImpl(meta, links, stats);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzEntityRelationsCountResponse}.
   */
  class Factory extends ModelFactory<SzEntityRelationsCountResponse, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzEntityRelationsCountResponse.class);
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
     * Creates an instance of {@link SzEntityRelationsCountResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    public SzEntityRelationsCountResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzEntityRelationsCountResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the speicified {@link
     * SzEntityRelationsCount} describing the statistics.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzEntityRelationsCount} describing the statistics.
     */
    public SzEntityRelationsCountResponse create(
        SzMeta                  meta,
        SzLinks                 links,
        SzEntityRelationsCount  stats)
    {
      return this.getProvider().create(meta, links, stats);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
