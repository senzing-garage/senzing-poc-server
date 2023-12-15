package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzEntityRelationsBreakdown;
import com.senzing.poc.model.SzEntityRelationsBreakdownResponse;

/**
 * Provides a default implementation of {@link SzEntityRelationsBreakdownResponse}.
 */
@JsonDeserialize
public class SzEntityRelationsBreakdownResponseImpl extends SzBasicResponseImpl
  implements SzEntityRelationsBreakdownResponse
{
  /**
   * The data for this instance.
   */
  private SzEntityRelationsBreakdown relationsBreakdown;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzEntityRelationsBreakdownResponseImpl() {
    this.relationsBreakdown = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzEntityRelationsBreakdownResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param relationsBreakdown The {@link SzEntityRelationsBreakdown} 
   *                           describing the data for this instance.
   */
  public SzEntityRelationsBreakdownResponseImpl(
    SzMeta                      meta,
    SzLinks                     links,
    SzEntityRelationsBreakdown  relationsBreakdown)
  {
    super(meta, links);
    Objects.requireNonNull(
      relationsBreakdown, "The SzEntityRelationsBreakdown cannot be null");
    this.relationsBreakdown = relationsBreakdown;
  }

  @Override
  public SzEntityRelationsBreakdown getData() {
    return this.relationsBreakdown;
  }

  @Override
  public void setData(SzEntityRelationsBreakdown relationsBreakdown) {
    this.relationsBreakdown = relationsBreakdown;
  }
}
