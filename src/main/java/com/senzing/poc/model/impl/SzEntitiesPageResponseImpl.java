package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzEntitiesPage;
import com.senzing.poc.model.SzEntitiesPageResponse;

/**
 * Provides a default implementation of {@link SzEntitiesPageResponse}.
 */
@JsonDeserialize
public class SzEntitiesPageResponseImpl extends SzBasicResponseImpl
  implements SzEntitiesPageResponse
{
  /**
   * The data for this instance.
   */
  private SzEntitiesPage entitiesPage;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzEntitiesPageResponseImpl() {
    this.entitiesPage = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzEntitiesPageResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param EntitiesPage The {@link SzEntitiesPage} describing the data
   *                   for this instance.
   */
  public SzEntitiesPageResponseImpl(SzMeta          meta,
                                    SzLinks         links,
                                    SzEntitiesPage  entitiesPage)
  {
    super(meta, links);
    Objects.requireNonNull(entitiesPage, "The SzEntitiesPage cannot be null");
    this.entitiesPage = entitiesPage;
  }

  /**
   * Returns the {@link SzEntitiesPage} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzEntitiesPage getData() {
    return this.entitiesPage;
  }

  /**
   * Sets the data associated with this response with an {@link SzEntitiesPage}.
   *
   * @param entitiesPage The {@link SzEntitiesPage} describing the statistics.
   */
  public void setData(SzEntitiesPage entitiesPage) {
    this.entitiesPage = entitiesPage;
  }
}
