package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzCountStats;
import com.senzing.poc.model.SzCountStatsResponse;

/**
 * Provides a default implementation of {@link SzCountStatsResponse}.
 */
@JsonDeserialize
public class SzCountStatsResponseImpl extends SzBasicResponseImpl
  implements SzCountStatsResponse
{
  /**
   * The data for this instance.
   */
  private SzCountStats countStats;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzCountStatsResponseImpl() {
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
  public SzCountStatsResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param countStats The {@link SzCountStats} describing the data
   *                   for this instance.
   */
  public SzCountStatsResponseImpl(SzMeta        meta,
                                  SzLinks       links,
                                  SzCountStats  countStats)
  {
    super(meta, links);
    Objects.requireNonNull(countStats, "The SzCountStats cannot be null");
    this.countStats = countStats;
  }

  /**
   * Returns the {@link SzCountStats} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzCountStats getData() {
    return this.countStats;
  }

  /**
   * Sets the data associated with this response with an {@link SzCountStats}.
   *
   * @param countStats The {@link SzCountStats} describing the statistics.
   */
  public void setData(SzCountStats countStats) {
    this.countStats = countStats;
  }
}
