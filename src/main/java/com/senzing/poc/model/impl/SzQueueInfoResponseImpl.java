package com.senzing.poc.model.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzQueueInfo;
import com.senzing.poc.model.SzQueueInfoResponse;

/**
 * Provides a default implementation of {@link SzQueueInfoResponse}.
 */
@JsonDeserialize
public class SzQueueInfoResponseImpl extends SzBasicResponseImpl
  implements SzQueueInfoResponse
{
  /**
   * The data for this instance.
   */
  private SzQueueInfo queueInfo;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzQueueInfoResponseImpl() {
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
  public SzQueueInfoResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with only the meta data, links, and the queue info data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param queueInfo The {@link SzQueueInfo} describing the data for this 
   *                  instance.
   */
  public SzQueueInfoResponseImpl(SzMeta       meta,
                                 SzLinks      links,
                                 SzQueueInfo  queueInfo)
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
   * @param info The {@link SzQueueInfo} describing the queue.
   */
  public void setData(SzQueueInfo info) {
    this.queueInfo = info;
  }
}
