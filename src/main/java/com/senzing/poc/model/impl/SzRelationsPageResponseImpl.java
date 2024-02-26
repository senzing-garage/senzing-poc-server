package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzRelationsPage;
import com.senzing.poc.model.SzRelationsPageResponse;

/**
 * Provides a default implementation of {@link SzRelationsPageResponse}.
 */
@JsonDeserialize
public class SzRelationsPageResponseImpl extends SzBasicResponseImpl
  implements SzRelationsPageResponse
{
  /**
   * The data for this instance.
   */
  private SzRelationsPage relationsPage;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzRelationsPageResponseImpl() {
    this.relationsPage = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzRelationsPageResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param RelationsPage The {@link SzRelationsPage} describing the data
   *                   for this instance.
   */
  public SzRelationsPageResponseImpl(SzMeta          meta,
                                    SzLinks         links,
                                    SzRelationsPage  relationsPage)
  {
    super(meta, links);
    Objects.requireNonNull(relationsPage, "The SzRelationsPage cannot be null");
    this.relationsPage = relationsPage;
  }

  /**
   * Returns the {@link SzRelationsPage} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzRelationsPage getData() {
    return this.relationsPage;
  }

  /**
   * Sets the data associated with this response with an {@link SzRelationsPage}.
   *
   * @param relationsPage The {@link SzRelationsPage} describing the statistics.
   */
  public void setData(SzRelationsPage relationsPage) {
    this.relationsPage = relationsPage;
  }
}
