package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzCrossSourceSummary;
import com.senzing.poc.model.SzCrossSourceSummaryResponse;

/**
 * Provides a default implementation of {@link SzCrossSourceSummaryResponse}.
 */
@JsonDeserialize
public class SzCrossSourceSummaryResponseImpl extends SzBasicResponseImpl
  implements SzCrossSourceSummaryResponse
{
  /**
   * The data for this instance.
   */
  private SzCrossSourceSummary crossSummary;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzCrossSourceSummaryResponseImpl() {
    this.crossSummary = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzCrossSourceSummaryResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param summary The {@link SzCrossSourceSummary} describing the data
   *                for this instance.
   */
  public SzCrossSourceSummaryResponseImpl(SzMeta                meta,
                                          SzLinks               links,
                                          SzCrossSourceSummary  summary)
  {
    super(meta, links);
    Objects.requireNonNull(summary, "The SzCrossSourceSummary cannot be null");
    this.crossSummary = summary;
  }

  /**
   * Returns the {@link SzCrossSourceSummary} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzCrossSourceSummary getData() {
    return this.crossSummary;
  }

  /**
   * Sets the data associated with this response with an {@link SzCrossSourceSummary}.
   *
   * @param summary The {@link SzCrossSourceSummary} describing the statistics.
   */
  public void setData(SzCrossSourceSummary summary) {
    this.crossSummary = summary;
  }
}
