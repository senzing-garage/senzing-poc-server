package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzEntitySizeCount;
import com.senzing.poc.model.SzEntitySizeCountResponse;

/**
 * Provides a default implementation of {@link SzEntitySizeCountResponse}.
 */
@JsonDeserialize
public class SzEntitySizeCountResponseImpl extends SzBasicResponseImpl
  implements SzEntitySizeCountResponse
{
  /**
   * The data for this instance.
   */
  private SzEntitySizeCount sizeCount;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzEntitySizeCountResponseImpl() {
    this.sizeCount = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzEntitySizeCountResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param EntitySizeCount The {@link SzEntitySizeCount} describing the data
   *                   for this instance.
   */
  public SzEntitySizeCountResponseImpl(
    SzMeta                meta,
    SzLinks               links,
    SzEntitySizeCount sizeCount)
  {
    super(meta, links);
    Objects.requireNonNull(sizeCount, "The SzEntitySizeCount cannot be null");
    this.sizeCount = sizeCount;
  }

  /**
   * Returns the {@link SzEntitySizeCount} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzEntitySizeCount getData() {
    return this.sizeCount;
  }

  /**
   * Sets the data associated with this response with an {@link SzEntitySizeCount}.
   *
   * @param sizeCount The {@link SzEntitySizeCount} describing the statistics.
   */
  public void setData(SzEntitySizeCount sizeCount) {
    this.sizeCount = sizeCount;
  }
}
