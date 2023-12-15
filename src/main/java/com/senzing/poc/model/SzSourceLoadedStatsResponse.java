package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzSourceLoadedStatsResponseImpl;

/**
 * Describes a response when data source count statistics are requested.
 */
public interface SzSourceLoadedStatsResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzSourceLoadedStats} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzSourceLoadedStats getData();

  /**
   * Sets the data associated with this response with an {@link SzSourceLoadedStats}.
   *
   * @param info The {@link SzSourceLoadedStats} describing the statistics.
   */
  void setData(SzSourceLoadedStats info);

  /**
   * A {@link ModelProvider} for instances of {@link SzSourceLoadedStatsResponse}.
   */
  interface Provider extends ModelProvider<SzSourceLoadedStatsResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    SzSourceLoadedStatsResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@liink SzMeta}, {@link SzLinks}
     * and {@link SzSourceLoadedStats}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param countStats The {@link SzSourceLoadedStats} describing the data for this 
     *                  instance.
     */
    SzSourceLoadedStatsResponse create(SzMeta              meta,
                                      SzLinks             inks,
                                      SzSourceLoadedStats  countStats);
  }
  
  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzSourceLoadedStatsResponse} that produces instances of
   * {@link SzSourceLoadedStatsResponseImpl}.
   */
  class DefaultProvider 
    extends AbstractModelProvider<SzSourceLoadedStatsResponse>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzSourceLoadedStatsResponse.class,
            SzSourceLoadedStatsResponseImpl.class);
    }

    @Override
    public SzSourceLoadedStatsResponse create(SzMeta meta, SzLinks links){
      return new SzSourceLoadedStatsResponseImpl(meta, links);
    }

    @Override
    public SzSourceLoadedStatsResponse create(SzMeta             meta,
                                             SzLinks            links,
                                             SzSourceLoadedStats countStats)
    {
      return new SzSourceLoadedStatsResponseImpl(meta, links, countStats);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzSourceLoadedStatsResponse}.
   */
  class Factory extends ModelFactory<SzSourceLoadedStatsResponse, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzSourceLoadedStatsResponse.class);
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
     * Creates an instance of {@link SzSourceLoadedStatsResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    public SzSourceLoadedStatsResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzSourceLoadedStatsResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the speicified {@link
     * SzSourceLoadedStats} describing the bulk records.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param CountStats The {@link SzSourceLoadedStats} describing the
     *                     bulk records.
     */
    public SzSourceLoadedStatsResponse create(SzMeta             meta,
                                             SzLinks            links,
                                             SzSourceLoadedStats countStats)
    {
      return this.getProvider().create(meta, links, countStats);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
