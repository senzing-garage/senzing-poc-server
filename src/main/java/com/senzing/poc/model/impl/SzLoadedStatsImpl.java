package com.senzing.poc.model.impl;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzLoadedStats;
import com.senzing.poc.model.SzSourceLoadedStats;

/**
 * Provides a default implementation of {@link SzLoadedStats}.
 */
@JsonDeserialize
public class SzLoadedStatsImpl implements SzLoadedStats {
  /**
   * The total number of records loaded in the entity repository.
   */
  private long totalRecordCount = 0L;

  /**
   * The total number of entities that have been resolved in the entity
   * repository.
   */
  private long totalEntityCount = 0L;

  /**
   * The total number of records loaded in the entity repository that
   * failed to match against any other record.  This is also the number
   * of entities in the repository that only have a single record.
   */
  private long totalUnmatchedRecordCount = 0L;

  /**
   * The {@link Map} of {@link String} data source code keys to {@link
   * SzSourceLoadedStats} values describing the count statistics for
   * that data source.
   */
  private SortedMap<String, SzSourceLoadedStats> dataSourceCounts = null;

  /**
   * Default constructor
   */
  public SzLoadedStatsImpl() {
    this.totalRecordCount           = 0L;
    this.totalEntityCount           = 0L;
    this.totalUnmatchedRecordCount  = 0L;
    this.dataSourceCounts           = new TreeMap<>();
  }

  @Override
  public long getTotalRecordCount() {
    return this.totalRecordCount;
  }

  @Override
  public void setTotalRecordCount(long recordCount) {
    this.totalRecordCount = recordCount;
  }

  @Override
  public long getTotalEntityCount() {
    return this.totalEntityCount;
  }

  @Override
  public void setTotalEntityCount(long entityCount) {
    this.totalEntityCount = entityCount;
  }

  @Override
  public long getTotalUnmatchedRecordCount() {
    return this.totalUnmatchedRecordCount;
  }

  @Override
  public void setTotalUnmatchedRecordCount(long recordCount) {
    this.totalUnmatchedRecordCount = recordCount;
  }

  @Override
  public List<SzSourceLoadedStats> getDataSourceCounts() {
    Collection<SzSourceLoadedStats> stats = this.dataSourceCounts.values();
    return new ArrayList<>(stats);
  }

  @Override
  public void setDataSourceCounts(List<SzSourceLoadedStats> statsList) {
    this.dataSourceCounts.clear();
    if (statsList != null) {
        statsList.forEach( stats -> {
            if (stats != null) {
                this.dataSourceCounts.put(
                    stats.getDataSource(), stats);
            }
        });
    }
  }

  @Override
  public void addDataSourceCount(SzSourceLoadedStats stats) {
    if (stats == null) return;
    this.dataSourceCounts.put(stats.getDataSource(), stats);
  }

}
