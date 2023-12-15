package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzSourceLoadedStats;
import com.senzing.poc.model.SzSourceLoadedStatsResponse;

/**
 * Provides a default implementation of {@link SzSourceLoadedStatsResponse}.
 */
@JsonDeserialize
public class SzSourceLoadedStatsResponseImpl extends SzBasicResponseImpl
  implements SzSourceLoadedStatsResponse
{
  /**
   * The data for this instance.
   */
  private SzSourceLoadedStats countStats;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzSourceLoadedStatsResponseImpl() {
    this.countStats = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzSourceLoadedStatsResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param countStats The {@link SzSourceLoadedStats} describing the data
   *                   for this instance.
   */
  public SzSourceLoadedStatsResponseImpl(SzMeta        meta,
                                  SzLinks       links,
                                  SzSourceLoadedStats  countStats)
  {
    super(meta, links);
    Objects.requireNonNull(countStats, "The SzSourceLoadedStats cannot be null");
    this.countStats = countStats;
  }

  /**
   * Returns the {@link SzSourceLoadedStats} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzSourceLoadedStats getData() {
    return this.countStats;
  }

  /**
   * Sets the data associated with this response with an {@link SzSourceLoadedStats}.
   *
   * @param countStats The {@link SzSourceLoadedStats} describing the statistics.
   */
  public void setData(SzSourceLoadedStats countStats) {
    this.countStats = countStats;
  }
}
