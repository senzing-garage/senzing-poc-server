package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzCountStatsResponseImpl;

/**
 * Describes a response when queue info is requested.
 */
public interface SzCountStatsResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzCountStats} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzCountStats getData();

  /**
   * Sets the data associated with this response with an {@link SzCountStats}.
   *
   * @param info The {@link SzCountStats} describing the statistics.
   */
  void setData(SzCountStats info);

  /**
   * A {@link ModelProvider} for instances of {@link SzCountStatsResponse}.
   */
  interface Provider extends ModelProvider<SzCountStatsResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    SzCountStatsResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@liink SzMeta}, {@link SzLinks}
     * and {@link SzCountStats}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param countStats The {@link SzCountStats} describing the data for this 
     *                  instance.
     */
    SzCountStatsResponse create(SzMeta          meta,
                                SzLinks         links,
                                SzCountStats    countStats);
  }
  
  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzCountStatsResponse} that produces instances of
   * {@link SzCountStatsResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzCountStatsResponse>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzCountStatsResponse.class,
            SzCountStatsResponseImpl.class);
    }

    @Override
    public SzCountStatsResponse create(SzMeta meta, SzLinks links){
      return new SzCountStatsResponseImpl(meta, links);
    }

    @Override
    public SzCountStatsResponse create(SzMeta       meta,
                                       SzLinks      links,
                                       SzCountStats countStats)
    {
      return new SzCountStatsResponseImpl(meta, links, countStats);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzCountStatsResponse}.
   */
  class Factory extends ModelFactory<SzCountStatsResponse, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzCountStatsResponse.class);
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
     * Creates an instance of {@link SzCountStatsResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    public SzCountStatsResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzCountStatsResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the speicified {@link
     * SzCountStats} describing the bulk records.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param CountStats The {@link SzCountStats} describing the
     *                     bulk records.
     */
    public SzCountStatsResponse create(SzMeta       meta,
                                       SzLinks      links,
                                       SzCountStats countStats)
    {
      return this.getProvider().create(meta, links, countStats);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
