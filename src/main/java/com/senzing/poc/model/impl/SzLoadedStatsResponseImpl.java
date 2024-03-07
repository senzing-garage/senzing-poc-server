package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.SzLinks;
import com.senzing.api.model.SzMeta;
import com.senzing.api.model.impl.SzBasicResponseImpl;
import com.senzing.poc.model.SzLoadedStats;
import com.senzing.poc.model.SzLoadedStatsResponse;

/**
 * Provides a default implementation of {@link SzLoadedStatsResponse}.
 */
@JsonDeserialize
public class SzLoadedStatsResponseImpl extends SzBasicResponseImpl
  implements SzLoadedStatsResponse
{
  /**
   * The data for this instance.
   */
  private SzLoadedStats loadedStats;

  /**
   * Default constructor for JSON deserialization.
   */
  protected SzLoadedStatsResponseImpl() {
    this.loadedStats = null;
  }

  /**
   * Constructs with only the meta data and links, leaving the 
   * count stats data to be initialized later.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   */
  public SzLoadedStatsResponseImpl(SzMeta meta, SzLinks links) {
    this(meta, links, null);
  }

  /**
   * Constructs with the meta data, links, and the count stats data.
   *
   * @param meta The response meta data.
   *
   * @param links The links for the response.
   * 
   * @param loadedStats The {@link SzLoadedStats} describing the data
   *                   for this instance.
   */
  public SzLoadedStatsResponseImpl(SzMeta         meta,
                                   SzLinks        links,
                                   SzLoadedStats  loadedStats)
  {
    super(meta, links);
    Objects.requireNonNull(loadedStats, "The SzLoadedStats cannot be null");
    this.loadedStats = loadedStats;
  }

  /**
   * Returns the {@link SzLoadedStats} associated with this response.
   *
   * @return The data associated with this response.
   */
  public SzLoadedStats getData() {
    return this.loadedStats;
  }

  /**
   * Sets the data associated with this response with an {@link SzLoadedStats}.
   *
   * @param loadedStats The {@link SzLoadedStats} describing the statistics.
   */
  public void setData(SzLoadedStats loadedStats) {
    this.loadedStats = loadedStats;
  }
}
