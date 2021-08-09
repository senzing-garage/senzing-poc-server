package com.senzing.poc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzQueueInfoImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes a messaging queue that is used by server.
 */
@JsonDeserialize(using = SzQueueInfo.Factory.class)
public interface SzQueueInfo {
  /**
   * Gets a description of the of queue.
   *
   * @return A description of the of queue.
   */
  @JsonInclude(NON_NULL)
  String getDescription();

  /**
   * Sets the description of the queue.
   *
   * @param description The description of the queue.
   */
  void setDescription(String description);

  /**
   * Gets a description of the provider for the queue.  This will describe the
   * implementation technology used for the queue such as <tt>"Amazon SQS"</tt>,
   * <tt>"RabbitMQ"</tt> or <tt>"Kafka"</tt>.
   *
   * @return
   */
  @JsonInclude(NON_NULL)
  String getProviderType();

  /**
   * Sets the description of the provider of the queue.
   *
   * @param providerDescription The description of the provider.
   */
  void setProviderType(String providerDescription);

  /**
   * Gets the approximate number of messages on the queue if known.  This method
   * returns <tt>null</tt> if the approximate number of messages is not known.
   *
   * @return The approximate number of messages on the queue, or <tt>null</tt>
   *         if not known.
   */
  @JsonInclude(NON_NULL)
  Integer getMessageCount();

  /**
   * Sets the message count of the queue if known.  Set to <tt>null</tt> if
   * not known.
   *
   * @param messageCount The message count for the queue, or <tt>null</tt> if
   *                     not known.
   */
  void setMessageCount(Integer messageCount);

  /**
   * A {@link ModelProvider} for instances of {@link SzQueueInfo}.
   */
  interface Provider extends ModelProvider<SzQueueInfo> {
    /**
     * Creates a new instance of {@link SzQueueInfo}.
     *
     * @return The new instance of {@link SzQueueInfo}
     */
    SzQueueInfo create();
  }

  /**
   * Provides a default {@link Provider} implementation for {@link SzQueueInfo}
   * that produces instances of {@link SzQueueInfoImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzQueueInfo>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzQueueInfo.class, SzQueueInfoImpl.class);
    }

    @Override
    public SzQueueInfo create() {
      return new SzQueueInfoImpl();
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzQueueInfo}.
   */
  class Factory extends ModelFactory<SzQueueInfo, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzQueueInfo.class);
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
     * Creates a new instance of {@link SzQueueInfo}.
     *
     * @return A new instance of {@link SzQueueInfo}.
     */
    public SzQueueInfo create()
    {
      return this.getProvider().create();
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
