package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzMatchCountsResponseImpl;

/**
 * Describes a response when source summary statistics are requested.
 */
public interface SzMatchCountsResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzMatchCountsResponseData} associated with this
   * response.
   *
   * @return The data associated with this response.
   */
  SzMatchCountsResponseData getData();

  /**
   * Sets the data associated with this response with an 
   * {@link SzMatchCountsResponseData}.
   *
   * @param countsData The {@link SzMatchCountsResponseData} describing 
   *                   the statistics.
   */
  void setData(SzMatchCountsResponseData countsData);

  /**
   * A {@link ModelProvider} for instances of {@link SzMatchCountsResponse}.
   */
  interface Provider extends ModelProvider<SzMatchCountsResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    SzMatchCountsResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@liink SzMeta},
     * {@link SzLinks} and {@link SzMatchCountsResponseData}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param data The {@link SzMatchCountsResponseData} describing the
     *             data for this instance.
     */
    SzMatchCountsResponse create(SzMeta                     meta,
                                 SzLinks                    links,
                                 SzMatchCountsResponseData  data);
  }
  
  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzMatchCountsResponse} that produces instances of
   * {@link SzMatchCountsResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzMatchCountsResponse>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzMatchCountsResponse.class,
            SzMatchCountsResponseImpl.class);
    }

    @Override
    public SzMatchCountsResponse create(SzMeta meta, SzLinks links){
      return new SzMatchCountsResponseImpl(meta, links);
    }

    @Override
    public SzMatchCountsResponse create(SzMeta                    meta,
                                        SzLinks                   links,
                                        SzMatchCountsResponseData data)
    {
      return new SzMatchCountsResponseImpl(meta, links, data);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzMatchCountsResponse}.
   */
  class Factory extends ModelFactory<SzMatchCountsResponse, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzMatchCountsResponse.class);
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
     * Creates an instance of {@link SzMatchCountsResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    public SzMatchCountsResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzMatchCountsResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the specified {@link
     * SzMatchCountsResponseData} describing the statistics.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param data The {@link SzMatchCountsResponseData} describing
     *             the statistics.
     */
    public SzMatchCountsResponse create(SzMeta                    meta,
                                        SzLinks                   links,
                                        SzMatchCountsResponseData data)
    {
      return this.getProvider().create(meta, links, data);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
