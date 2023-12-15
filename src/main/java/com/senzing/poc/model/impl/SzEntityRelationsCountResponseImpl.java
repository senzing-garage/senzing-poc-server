package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzEntityRelationsCount;
import com.senzing.poc.model.SzEntityRelationsCountResponse;

/**
 * Provides a default implementation of {@link SzEntityRelationsCountResponse}.
 */
@JsonDeserialize
public class SzEntityRelationsCountResponseImpl extends SzBasicResponseImpl
  implements SzEntityRelationsCountResponse
{
  /**
   * The data for this instance.
   */
  private SzEntityRelationsCount relationsCount;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzEntityRelationsCountResponseImpl() {
    this.relationsCount = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzEntityRelationsCountResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param relationsCount The {@link SzEntityRelationsCount} describing
   *                       the data for this instance.
   */
  public SzEntityRelationsCountResponseImpl(
    SzMeta                  meta,
    SzLinks                 links,
    SzEntityRelationsCount  relationsCount)
  {
    super(meta, links);
    Objects.requireNonNull(relationsCount, "The SzEntityRelationsCount cannot be null");
    this.relationsCount = relationsCount;
  }

  /**
   * Returns the {@link SzEntityRelationsCount} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzEntityRelationsCount getData() {
    return this.relationsCount;
  }

  /**
   * Sets the data associated with this response with an {@link SzEntityRelationsCount}.
   *
   * @param relationsCount The {@link SzEntityRelationsCount} describing the statistics.
   */
  public void setData(SzEntityRelationsCount relationsCount) {
    this.relationsCount = relationsCount;
  }
}
