package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzSummaryStatsResponseImpl;

/**
 * Describes a response when count statistics are requested.
 */
public interface SzSummaryStatsResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzSummaryStats} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzSummaryStats getData();

  /**
   * Sets the data associated with this response with an {@link SzSummaryStats}.
   *
   * @param stats The {@link SzSummaryStats} describing the statistics.
   */
  void setData(SzSummaryStats stats);

  /**
   * A {@link ModelProvider} for instances of {@link SzSummaryStatsResponse}.
   */
  interface Provider extends ModelProvider<SzSummaryStatsResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    SzSummaryStatsResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@liink SzMeta}, {@link SzLinks}
     * and {@link SzSummaryStats}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzSummaryStats} describing the data for this 
     *              instance.
     */
    SzSummaryStatsResponse create(SzMeta          meta,
                                  SzLinks         links,
                                  SzSummaryStats  stats);
  }
  
  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzSummaryStatsResponse} that produces instances of
   * {@link SzSummaryStatsResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzSummaryStatsResponse>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzSummaryStatsResponse.class,
            SzSummaryStatsResponseImpl.class);
    }

    @Override
    public SzSummaryStatsResponse create(SzMeta meta, SzLinks links){
      return new SzSummaryStatsResponseImpl(meta, links);
    }

    @Override
    public SzSummaryStatsResponse create(SzMeta         meta,
                                        SzLinks         links,
                                        SzSummaryStats  stats)
    {
      return new SzSummaryStatsResponseImpl(meta, links, stats);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzSummaryStatsResponse}.
   */
  class Factory extends ModelFactory<SzSummaryStatsResponse, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzSummaryStatsResponse.class);
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
     * Creates an instance of {@link SzSummaryStatsResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    public SzSummaryStatsResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzSummaryStatsResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the speicified {@link
     * SzSummaryStats} describing the loaded stats.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param stats The {@link SzSummaryStats} describing the statistics.
     */
    public SzSummaryStatsResponse create(SzMeta         meta,
                                         SzLinks        links,
                                         SzSummaryStats stats)
    {
      return this.getProvider().create(meta, links, stats);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
