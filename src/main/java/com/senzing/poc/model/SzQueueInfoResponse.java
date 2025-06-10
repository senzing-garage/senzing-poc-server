package com.senzing.poc.model;

import com.senzing.api.model.*;
import com.senzing.poc.model.impl.SzQueueInfoResponseImpl;

/**
 * Describes a response when queue info is requested.
 * 
 */
public interface SzQueueInfoResponse extends SzBasicResponse {
  /**
   * Returns the {@link SzQueueInfo} associated with this response.
   *
   * @return The data associated with this response.
   */
  SzQueueInfo getData();

  /**
   * Sets the data associated with this response with an {@link SzQueueInfo}.
   *
   * @param info The {@link SzQueueInfo} describing the license.
   */
  void setData(SzQueueInfo info);

  /**
   * A {@link ModelProvider} for instances of {@link SzQueueInfoResponse}.
   */
  interface Provider extends ModelProvider<SzQueueInfoResponse> {
    /**
     * Constructs with only the {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    SzQueueInfoResponse create(SzMeta meta, SzLinks links);

    /**
     * Creates an instance with the specified {@link SzMeta}, {@link SzLinks}
     * and {@link SzQueueInfo}.
     *
     * @param meta      The response meta data.
     *
     * @param links     The links for the response.
     *
     * @param queueInfo The {@link SzQueueInfo} describing the data for this
     *                  instance.
     */
    SzQueueInfoResponse create(SzMeta meta,
        SzLinks links,
        SzQueueInfo queueInfo);
  }

  /**
   * Provides a default {@link Provider} implementation for {@link
   * SzQueueInfoResponse} that produces instances of
   * {@link SzQueueInfoResponseImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzQueueInfoResponse>
      implements Provider {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzQueueInfoResponse.class,
          SzQueueInfoResponseImpl.class);
    }

    @Override
    public SzQueueInfoResponse create(SzMeta meta, SzLinks links) {
      return new SzQueueInfoResponseImpl(meta, links);
    }

    @Override
    public SzQueueInfoResponse create(SzMeta meta,
        SzLinks links,
        SzQueueInfo queueInfo) {
      return new SzQueueInfoResponseImpl(meta, links, queueInfo);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzQueueInfoResponse}.
   */
  class Factory extends ModelFactory<SzQueueInfoResponse, Provider> {
    /**
     * Default constructor. This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzQueueInfoResponse.class);
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
     * Creates an instance of {@link SzQueueInfoResponse} with the
     * specified {@link SzMeta} and {@link SzLinks}.
     *
     * @param meta  The response meta data.
     *
     * @param links The links for the response.
     */
    public SzQueueInfoResponse create(SzMeta meta, SzLinks links) {
      return this.getProvider().create(meta, links);
    }

    /**
     * Creates an instance of {@link SzQueueInfoResponse} with the
     * specified {@link SzMeta}, {@link SzLinks} and the specified {@link
     * SzQueueInfo} describing the bulk records.
     *
     * @param meta      The response meta data.
     *
     * @param links     The links for the response.
     *
     * @param queueInfo The {@link SzQueueInfo} describing the
     *                  bulk records.
     */
    public SzQueueInfoResponse create(SzMeta meta,
        SzLinks links,
        SzQueueInfo queueInfo) {
      return this.getProvider().create(meta, links, queueInfo);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
