package com.senzing.poc.model.impl;

import com.senzing.api.model.SzBasicResponse;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.poc.model.SzQueueInfo;

/**
 *
 */
public class SzQueueInfoResponse extends SzBasicResponse {
  /**
   * The data for this instance.
   */
  private SzQueueInfo queueInfo;

  /**
   * Default constructor.
   */
  SzQueueInfoResponse() {
    this.queueInfo = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the queue info
   * data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzQueueInfoResponse(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with only the meta data and links, leaving the queue info
   * data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param queueInfo The {@link SzQueueInfo} describing the data for this 
   *                  instance.
   */
  public SzQueueInfoResponse(SzMeta meta, SzLinks links, SzQueueInfo queueInfo) 
  {
    super(meta, links);
    this.queueInfo = queueInfo;
  }

  /**
   * Returns the {@link SzQueueInfo} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzQueueInfo getData() {
    return this.queueInfo;
  }

  /**
   * Sets the data associated with this response with an {@link SzQueueInfo}.
   *
   * @param info The {@link SzQueueInfo} describing the license.
   */
  public void setData(SzQueueInfo info) {
    this.queueInfo = info;
  }
  
}
