package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzEntitySizeBreakdown;
import com.senzing.poc.model.SzEntitySizeBreakdownResponse;

/**
 * Provides a default implementation of {@link SzEntitySizeBreakdownResponse}.
 */
@JsonDeserialize
public class SzEntitySizeBreakdownResponseImpl extends SzBasicResponseImpl
  implements SzEntitySizeBreakdownResponse
{
  /**
   * The data for this instance.
   */
  private SzEntitySizeBreakdown sizeBreakdown;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzEntitySizeBreakdownResponseImpl() {
    this.sizeBreakdown = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzEntitySizeBreakdownResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param EntitySizeBreakdown The {@link SzEntitySizeBreakdown} describing the data
   *                   for this instance.
   */
  public SzEntitySizeBreakdownResponseImpl(
    SzMeta                meta,
    SzLinks               links,
    SzEntitySizeBreakdown sizeBreakdown)
  {
    super(meta, links);
    Objects.requireNonNull(sizeBreakdown, "The SzEntitySizeBreakdown cannot be null");
    this.sizeBreakdown = sizeBreakdown;
  }

  /**
   * Returns the {@link SzEntitySizeBreakdown} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzEntitySizeBreakdown getData() {
    return this.sizeBreakdown;
  }

  /**
   * Sets the data associated with this response with an {@link SzEntitySizeBreakdown}.
   *
   * @param sizeBreakdown The {@link SzEntitySizeBreakdown} describing the statistics.
   */
  public void setData(SzEntitySizeBreakdown sizeBreakdown) {
    this.sizeBreakdown = sizeBreakdown;
  }
}
