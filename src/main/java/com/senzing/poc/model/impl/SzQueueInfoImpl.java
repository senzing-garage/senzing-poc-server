package com.senzing.poc.model.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzQueueInfo;

/**
 * Provides a default implementation of {@link SzQueueInfo}.
 */
@JsonDeserialize
public class SzQueueInfoImpl implements SzQueueInfo {
  /**
   * The description for the queue -- usually describing its purpose.
   */
  private String description;

  /**
   * The implementation provider for the queue.
   */
  private String provider;

  /**
   * The message count for the queue.
   */
  private Integer messageCount;

  /**
   * Default constructor
   */
  public SzQueueInfoImpl() {
    this.description  = null;
    this.provider     = null;
    this.messageCount = null;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getProviderType() {
    return provider;
  }

  @Override
  public void setProviderType(String providerDescription) {
    this.provider = providerDescription;
  }

  @Override
  public Integer getMessageCount() {
    return messageCount;
  }

  @Override
  public void setMessageCount(Integer messageCount) {
    this.messageCount = messageCount;
  }
}
