package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzSourceCountStatsResponseImpl;

/**
 * Describes a response when data source count statistics are requested.
 */
public interface SzSourceCountStatsResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzSourceCountStats} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzSourceCountStats getData();

  /**
   * Sets the data associated with this response with an {@link SzSourceCountStats}.
   *
   * @param info The {@link SzSourceCountStats} describing the statistics.
   */
  void setData(SzSourceCountStats info);

  /**
   * A {@link ModelProvider} for instances of {@link SzSourceCountStatsResponse}.
   */
  interface Provider extends ModelProvider<SzSourceCountStatsResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    SzSourceCountStatsResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@liink SzMeta}, {@link SzLinks}
     * and {@link SzSourceCountStats}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param countStats The {@link SzSourceCountStats} describing the data for this 
     *                  instance.
     */
    SzSourceCountStatsResponse create(SzMeta              meta,
                                      SzLinks             inks,
                                      SzSourceCountStats  countStats);
  }
  
  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzSourceCountStatsResponse} that produces instances of
   * {@link SzSourceCountStatsResponseImpl}.
   */
  class DefaultProvider 
    extends AbstractModelProvider<SzSourceCountStatsResponse>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzSourceCountStatsResponse.class,
            SzSourceCountStatsResponseImpl.class);
    }

    @Override
    public SzSourceCountStatsResponse create(SzMeta meta, SzLinks links){
      return new SzSourceCountStatsResponseImpl(meta, links);
    }

    @Override
    public SzSourceCountStatsResponse create(SzMeta             meta,
                                             SzLinks            links,
                                             SzSourceCountStats countStats)
    {
      return new SzSourceCountStatsResponseImpl(meta, links, countStats);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzSourceCountStatsResponse}.
   */
  class Factory extends ModelFactory<SzSourceCountStatsResponse, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzSourceCountStatsResponse.class);
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
     * Creates an instance of {@link SzSourceCountStatsResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    public SzSourceCountStatsResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzSourceCountStatsResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the speicified {@link
     * SzSourceCountStats} describing the bulk records.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param CountStats The {@link SzSourceCountStats} describing the
     *                     bulk records.
     */
    public SzSourceCountStatsResponse create(SzMeta             meta,
                                             SzLinks            links,
                                             SzSourceCountStats countStats)
    {
      return this.getProvider().create(meta, links, countStats);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
