package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzSourceSummaryResponseImpl;

/**
 * Describes a response when source summary statistics are requested.
 */
public interface SzSourceSummaryResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzSourceSummary} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzSourceSummary getData();

  /**
   * Sets the data associated with this response with an {@link SzSourceSummary}.
   *
   * @param summary The {@link SzSourceSummary} describing the statistics.
   */
  void setData(SzSourceSummary summary);

  /**
   * A {@link ModelProvider} for instances of {@link SzSourceSummaryResponse}.
   */
  interface Provider extends ModelProvider<SzSourceSummaryResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    SzSourceSummaryResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@liink SzMeta}, {@link SzLinks}
     * and {@link SzSourceSummary}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param summary The {@link SzSourceSummary} describing the data for
     *                this instance.
     */
    SzSourceSummaryResponse create(SzMeta           meta,
                                   SzLinks          links,
                                   SzSourceSummary  summary);
  }
  
  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzSourceSummaryResponse} that produces instances of
   * {@link SzSourceSummaryResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzSourceSummaryResponse>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzSourceSummaryResponse.class,
            SzSourceSummaryResponseImpl.class);
    }

    @Override
    public SzSourceSummaryResponse create(SzMeta meta, SzLinks links){
      return new SzSourceSummaryResponseImpl(meta, links);
    }

    @Override
    public SzSourceSummaryResponse create(SzMeta          meta,
                                          SzLinks         links,
                                          SzSourceSummary summary)
    {
      return new SzSourceSummaryResponseImpl(meta, links, summary);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzSourceSummaryResponse}.
   */
  class Factory extends ModelFactory<SzSourceSummaryResponse, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzSourceSummaryResponse.class);
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
     * Creates an instance of {@link SzSourceSummaryResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     */
    public SzSourceSummaryResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzSourceSummaryResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the speicified {@link
     * SzSourceSummary} describing the loaded stats.
     *
     * @param meta The response meta data.
     *
     * @param links The links for the response.
     *
     * @param summary The {@link SzSourceSummary} describing the source summary.
     */
    public SzSourceSummaryResponse create(SzMeta          meta,
                                          SzLinks         links,
                                          SzSourceSummary summary)
    {
      return this.getProvider().create(meta, links, summary);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
