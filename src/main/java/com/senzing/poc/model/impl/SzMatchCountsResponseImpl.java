package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzMatchCountsResponseData;
import com.senzing.poc.model.SzMatchCountsResponse;

/**
 * Provides a default implementation of {@link SzMatchCountsResponse}.
 */
@JsonDeserialize
public class SzMatchCountsResponseImpl extends SzBasicResponseImpl
  implements SzMatchCountsResponse
{
  /**
   * The data for this instance.
   */
  private SzMatchCountsResponseData data = null;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzMatchCountsResponseImpl() {
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
  public SzMatchCountsResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the response data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param data The {@link SzMatchCountsResponseData} describing the data
   *                for this instance.
   */
  public SzMatchCountsResponseImpl(SzMeta                     meta,
                                   SzLinks                    links,
                                   SzMatchCountsResponseData  data)
  {
    super(meta, links);
    Objects.requireNonNull(
      data, "The SzMatchCountsResponseData cannot be null");
    this.data = data;
  }

  /**
   * Returns the {@link SzMatchCountsResponseData} associated with
   * this response.
   *
   * @return The data associated with this response.
   */
  public SzMatchCountsResponseData getData() {
    return this.data;
  }

  /**
   * Sets the data associated with this response with an 
   * {@link SzMatchCountsResponseData}.
   *
   * @param summary The {@link SzMatchCountsResponseData} describing
   *                the statistics.
   */
  public void setData(SzMatchCountsResponseData data) {
    this.data = data;
  }
}
