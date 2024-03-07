package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzSummaryStats;
import com.senzing.poc.model.SzSummaryStatsResponse;

/**
 * Provides a default implementation of {@link SzSummaryStatsResponse}.
 */
@JsonDeserialize
public class SzSummaryStatsResponseImpl extends SzBasicResponseImpl
  implements SzSummaryStatsResponse
{
  /**
   * The data for this instance.
   */
  private SzSummaryStats summaryStats;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzSummaryStatsResponseImpl() {
    this.summaryStats = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzSummaryStatsResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param stats The {@link SzSummaryStats} describing the data
   *              for this instance.
   */
  public SzSummaryStatsResponseImpl(SzMeta          meta,
                                    SzLinks         links,
                                    SzSummaryStats  stats)
  {
    super(meta, links);
    Objects.requireNonNull(stats, "The SzSummaryStats cannot be null");
    this.summaryStats = stats;
  }

  /**
   * Returns the {@link SzSummaryStats} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzSummaryStats getData() {
    return this.summaryStats;
  }

  /**
   * Sets the data associated with this response with an {@link SzSummaryStats}.
   *
   * @param stats The {@link SzSummaryStats} describing the statistics.
   */
  public void setData(SzSummaryStats stats) {
    this.summaryStats = stats;
  }
}
