package com.senzing.poc.model.impl;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzCountStats;
import com.senzing.poc.model.SzSourceCountStats;

/**
 * Provides a default implementation of {@link SzSourceCountStats}.
 */
@JsonDeserialize
public class SzCountStatsImpl implements SzCountStats {
  /**
   * The total number of records loaded in the entity repository.  This
   * is initialized to <code>null</code> if it has not yet been set.
   */
  private Long totalRecordCount = null;

  /**
   * The total number of entities that have been resolved in the entity
   * repository.  This is initialized to <code>null</code> if it has not
   * yet been set.
   */
  private Long totalEntityCount = null;

  /**
   * The {@link Map} of {@link String} data source code keys to {@link
   * SzSourceCountStat} values describing the count statistics for that
   * data source.
   */
  private Map<String, SzSourceCountStats> dataSourceCounts = null;

  /**
   * Default constructor
   */
  public SzCountStatsImpl() {
    this.totalRecordCount   = null;
    this.totalEntityCount   = null;
    this.dataSourceCounts   = new LinkedHashMap<>();
  }

  @Override
  public Long getTotalRecordCount() {
    return this.totalRecordCount;
  }

  @Override
  public void setTotalRecordCount(long recordCount) {
    this.totalRecordCount = recordCount;
  }

  @Override
  public Long getTotalEntityCount() {
    return this.totalEntityCount;
  }

  @Override
  public void setTotalEntityCount(long entityCount) {
    this.totalEntityCount = entityCount;
  }

  @Override
  public List<SzSourceCountStats> getDataSourceCounts() {
    Collection<SzSourceCountStats> stats = this.dataSourceCounts.values();
    return new ArrayList<>(stats);
  }

  @Override
  public void setDataSourceCounts(List<SzSourceCountStats> statsList) {
    this.dataSourceCounts.clear();
    if (statsList != null) {
        statsList.forEach( stats -> {
            if (stats != null) {
                this.dataSourceCounts.put(
                    stats.getDataSourceCode(), stats);
            }
        });
    }
  }

  @Override
  public void addDataSourceCount(SzSourceCountStats stats) {
    if (stats == null) return;
    this.dataSourceCounts.put(stats.getDataSourceCode(), stats);
  }

}
