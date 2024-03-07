package com.senzing.poc.model.impl;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzSourceSummary;
import com.senzing.poc.model.SzSummaryStats;

/**
 * Provides a default implementation of {@link SzSourceSummary}.
 */
@JsonDeserialize
public class SzSummaryStatsImpl implements SzSummaryStats {
  /**
   * The {@link Map} of {@link String} "versus" data source code keys to
   * {@link SzSourceSummary} values describing the count statistics for that
   * data source.
   */
  private SortedMap<String, SzSourceSummary> sourceSummaries = null;

  /**
   * Default constructor
   */
  public SzSummaryStatsImpl() {
    this.sourceSummaries = new TreeMap<>();
  }

  @Override
  public List<SzSourceSummary> getSourceSummaries() {
    return new ArrayList<>(this.sourceSummaries.values());
  }

  @Override
  public void setSourceSummaries(Collection<SzSourceSummary> summaries) {
    this.sourceSummaries.clear();
    if (summaries != null) {
      summaries.forEach(summary -> {
        if (summary != null) {
          this.sourceSummaries.put(summary.getDataSource(), summary);
        }
      });
    }
  }

  @Override
  public void addSourceSummary(SzSourceSummary summary) {
    if (summary == null) return;
    this.sourceSummaries.put(summary.getDataSource(), summary);
  }

  @Override
  public void removeSourceSummary(String dataSourceCode) {
    this.sourceSummaries.remove(dataSourceCode);
  }

  @Override
  public void removeAllSourceSummaries() {
    this.sourceSummaries.clear();
  }
}
