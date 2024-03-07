package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzSourceSummary;
import com.senzing.poc.model.SzSourceSummaryResponse;

/**
 * Provides a default implementation of {@link SzSourceSummaryResponse}.
 */
@JsonDeserialize
public class SzSourceSummaryResponseImpl extends SzBasicResponseImpl
  implements SzSourceSummaryResponse
{
  /**
   * The data for this instance.
   */
  private SzSourceSummary sourceSummary;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzSourceSummaryResponseImpl() {
    this.sourceSummary = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzSourceSummaryResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param summary The {@link SzSourceSummary} describing the data
   *                for this instance.
   */
  public SzSourceSummaryResponseImpl(SzMeta         meta,
                                    SzLinks         links,
                                    SzSourceSummary summary)
  {
    super(meta, links);
    Objects.requireNonNull(summary, "The SzSourceSummary cannot be null");
    this.sourceSummary = summary;
  }

  /**
   * Returns the {@link SzSourceSummary} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzSourceSummary getData() {
    return this.sourceSummary;
  }

  /**
   * Sets the data associated with this response with an {@link SzSourceSummary}.
   *
   * @param summary The {@link SzSourceSummary} describing the statistics.
   */
  public void setData(SzSourceSummary summary) {
    this.sourceSummary = summary;
  }
}
