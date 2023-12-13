package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzSourceCountStats;
import com.senzing.poc.model.SzSourceCountStatsResponse;

/**
 * Provides a default implementation of {@link SzSourceCountStatsResponse}.
 */
@JsonDeserialize
public class SzSourceCountStatsResponseImpl extends SzBasicResponseImpl
  implements SzSourceCountStatsResponse
{
  /**
   * The data for this instance.
   */
  private SzSourceCountStats countStats;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzSourceCountStatsResponseImpl() {
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
  public SzSourceCountStatsResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param countStats The {@link SzSourceCountStats} describing the data
   *                   for this instance.
   */
  public SzSourceCountStatsResponseImpl(SzMeta        meta,
                                  SzLinks       links,
                                  SzSourceCountStats  countStats)
  {
    super(meta, links);
    Objects.requireNonNull(countStats, "The SzSourceCountStats cannot be null");
    this.countStats = countStats;
  }

  /**
   * Returns the {@link SzSourceCountStats} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzSourceCountStats getData() {
    return this.countStats;
  }

  /**
   * Sets the data associated with this response with an {@link SzSourceCountStats}.
   *
   * @param countStats The {@link SzSourceCountStats} describing the statistics.
   */
  public void setData(SzSourceCountStats countStats) {
    this.countStats = countStats;
  }
}
