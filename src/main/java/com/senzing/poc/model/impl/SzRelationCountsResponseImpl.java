package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzRelationCountsResponseData;
import com.senzing.poc.model.SzRelationCountsResponse;

/**
 * Provides a default implementation of {@link SzRelationCountsResponse}.
 */
@JsonDeserialize
public class SzRelationCountsResponseImpl extends SzBasicResponseImpl
  implements SzRelationCountsResponse
{
  /**
   * The data for this instance.
   */
  private SzRelationCountsResponseData data = null;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzRelationCountsResponseImpl() {
    this.data = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzRelationCountsResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the response data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param data The {@link SzRelationCountsResponseData} describing the data
   *                for this instance.
   */
  public SzRelationCountsResponseImpl(SzMeta                     meta,
                                   SzLinks                    links,
                                   SzRelationCountsResponseData  data)
  {
    super(meta, links);
    Objects.requireNonNull(
      data, "The SzRelationCountsResponseData cannot be null");
    this.data = data;
  }

  /**
   * Returns the {@link SzRelationCountsResponseData} associated with
   * this response.
   *
   * @return The data associated with this response.
   */
  public SzRelationCountsResponseData getData() {
    return this.data;
  }

  /**
   * Sets the data associated with this response with an 
   * {@link SzRelationCountsResponseData}.
   *
   * @param summary The {@link SzRelationCountsResponseData} describing
   *                the statistics.
   */
  public void setData(SzRelationCountsResponseData data) {
    this.data = data;
  }
}
