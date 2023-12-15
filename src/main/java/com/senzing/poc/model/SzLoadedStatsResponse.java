package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzLoadedStatsResponseImpl;

/**
 * Describes a response when count statistics are requested.
 */
public interface SzLoadedStatsResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzLoadedStats} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzLoadedStats getData();

  /**
   * Sets the data associated with this response with an {@link SzLoadedStats}.
   *
   * @param info The {@link SzLoadedStats} describing the statistics.
   */
  void setData(SzLoadedStats info);

  /**
   * A {@link ModelProvider} for instances of {@link SzLoadedStatsResponse}.
   */
  interface Provider extends ModelProvider<SzLoadedStatsResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    SzLoadedStatsResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@liink SzMeta}, {@link SzLinks}
     * and {@link SzLoadedStats}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzLoadedStats} describing the data for this 
     *              instance.
     */
    SzLoadedStatsResponse create(SzMeta       meta,
                                SzLinks       links,
                                SzLoadedStats stats);
  }
  
  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzLoadedStatsResponse} that produces instances of
   * {@link SzLoadedStatsResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzLoadedStatsResponse>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzLoadedStatsResponse.class,
            SzLoadedStatsResponseImpl.class);
    }

    @Override
    public SzLoadedStatsResponse create(SzMeta meta, SzLinks links){
      return new SzLoadedStatsResponseImpl(meta, links);
    }

    @Override
    public SzLoadedStatsResponse create(SzMeta       meta,
                                       SzLinks      links,
                                       SzLoadedStats LoadedStats)
    {
      return new SzLoadedStatsResponseImpl(meta, links, LoadedStats);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzLoadedStatsResponse}.
   */
  class Factory extends ModelFactory<SzLoadedStatsResponse, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzLoadedStatsResponse.class);
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
     * Creates an instance of {@link SzLoadedStatsResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    public SzLoadedStatsResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzLoadedStatsResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the speicified {@link
     * SzLoadedStats} describing the loaded stats.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzLoadedStats} describing the statistics.
     */
    public SzLoadedStatsResponse create(SzMeta        meta,
                                       SzLinks        links,
                                       SzLoadedStats  stats)
    {
      return this.getProvider().create(meta, links, stats);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
