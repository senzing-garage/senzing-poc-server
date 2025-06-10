package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzCrossSourceSummaryResponseImpl;

/**
 * Describes a response when source summary statistics are requested.
 */
public interface SzCrossSourceSummaryResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzCrossSourceSummary} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzCrossSourceSummary getData();

  /**
   * Sets the data associated with this response with an
   * {@link SzCrossSourceSummary}.
   *
   * @param summary The {@link SzCrossSourceSummary} describing the statistics.
   */
  void setData(SzCrossSourceSummary summary);

  /**
   * A {@link ModelProvider} for instances of
   * {@link SzCrossSourceSummaryResponse}.
   */
  interface Provider extends ModelProvider<SzCrossSourceSummaryResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    SzCrossSourceSummaryResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@link SzMeta}, {@link SzLinks}
     * and {@link SzCrossSourceSummary}.
     *
     * @param meta    The response meta data.
     *
     * @param links   The links for the response.
     *
     * @param summary The {@link SzCrossSourceSummary} describing the data for
     *                this instance.
     */
    SzCrossSourceSummaryResponse create(SzMeta meta,
        SzLinks links,
        SzCrossSourceSummary summary);
  }

  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzCrossSourceSummaryResponse} that produces instances of
   * {@link SzCrossSourceSummaryResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzCrossSourceSummaryResponse>
      implements Provider {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzCrossSourceSummaryResponse.class,
          SzCrossSourceSummaryResponseImpl.class);
    }

    @Override
    public SzCrossSourceSummaryResponse create(SzMeta meta, SzLinks links) {
      return new SzCrossSourceSummaryResponseImpl(meta, links);
    }

    @Override
    public SzCrossSourceSummaryResponse create(SzMeta meta,
        SzLinks links,
        SzCrossSourceSummary summary) {
      return new SzCrossSourceSummaryResponseImpl(meta, links, summary);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzCrossSourceSummaryResponse}.
   */
  class Factory extends ModelFactory<SzCrossSourceSummaryResponse, Provider> {
    /**
     * Default constructor. This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzCrossSourceSummaryResponse.class);
    }

    /**
     * Constructs with the default provider. This constructor is private and
     * is used for the master singleton instance.
     * 
     * @param defaultProvider The default provider.
     */
    private Factory(Provider defaultProvider) {
      super(defaultProvider);
    }

    /**
     * Creates an instance of {@link SzCrossSourceSummaryResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    public SzCrossSourceSummaryResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzCrossSourceSummaryResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the specified {@link
     * SzCrossSourceSummary} describing the loaded stats.
     *
     * @param meta    The response meta data.
     *
     * @param links   The links for the response.
     *
     * @param summary The {@link SzCrossSourceSummary} describing the source
     *                summary.
     */
    public SzCrossSourceSummaryResponse create(SzMeta meta,
        SzLinks links,
        SzCrossSourceSummary summary) {
      return this.getProvider().create(meta, links, summary);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
