package com.senzing.poc.model.impl;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzSourceSummary;
import com.senzing.poc.model.SzCrossSourceSummary;

/**
 * Provides a default implementation of {@link SzSourceSummary}.
 */
@JsonDeserialize
public class SzSourceSummaryImpl implements SzSourceSummary {
  /**
   * The data source to which the summary statistics apply.
   */
  private String dataSource;

  /**
   * The number of entities having at least one record from the data source.
   */
  private long entityCount = 0L;

  /**
   * The number of records loaded from the data source.
   */
  private long recordCount = 0L;

  /**
   * The number of records from this data source that did not match a record from
   * the same data source. This represents the number of records from the data
   * source that are effectively unique (not duplicated).
   */
  private long unmatchedRecordCount = 0L;

  /**
   * The {@link Map} of {@link String} "versus" data source code keys to
   * {@link SzCrossSourceSummary} values describing the cross summary
   * statistics for that data source.
   */
  private Map<String, SzCrossSourceSummary> crossSummaries = null;

  /**
   * Default constructor
   */
  public SzSourceSummaryImpl() {
    this(null);
  }

  /**
   * Constructs with the specified data source code.
   * 
   * @param dataSourceCode The data source code to associated with the source
   *                       summary.
   * 
   */
  public SzSourceSummaryImpl(String dataSourceCode) {
    this.dataSource = dataSourceCode;
    this.recordCount = 0L;
    this.entityCount = 0L;
    this.unmatchedRecordCount = 0L;
    this.crossSummaries = new LinkedHashMap<>();
  }

  @Override
  public String getDataSource() {
    return this.dataSource;
  }

  @Override
  public void setDataSource(String dataSourceCode) {
    this.dataSource = dataSourceCode;
  }

  @Override
  public long getRecordCount() {
    return this.recordCount;
  }

  @Override
  public void setRecordCount(long recordCount) {
    this.recordCount = recordCount;
  }

  @Override
  public long getEntityCount() {
    return this.entityCount;
  }

  @Override
  public void setEntityCount(long entityCount) {
    this.entityCount = entityCount;
  }

  @Override
  public long getUnmatchedRecordCount() {
    return this.unmatchedRecordCount;
  }

  @Override
  public void setUnmatchedRecordCount(long recordCount) {
    this.unmatchedRecordCount = recordCount;
  }

  @Override
  public List<SzCrossSourceSummary> getCrossSourceSummaries() {
    return new ArrayList<>(this.crossSummaries.values());
  }

  @Override
  public void setCrossSourceSummaries(Collection<SzCrossSourceSummary> crossSummaries) {
    this.crossSummaries.clear();
    if (crossSummaries != null) {
      crossSummaries.forEach(summary -> {
        if (summary != null) {
          this.crossSummaries.put(summary.getVersusDataSource(), summary);
        }
      });
    }
  }

  @Override
  public void addCrossSourceSummary(SzCrossSourceSummary crossSummary) {
    if (crossSummary == null)
      return;
    this.crossSummaries.put(crossSummary.getVersusDataSource(), crossSummary);
  }

  @Override
  public void removeCrossSourceSummary(String versusDataSourceCode) {
    this.crossSummaries.remove(versusDataSourceCode);
  }

  @Override
  public void removeAllCrossSourceSummaries() {
    this.crossSummaries.clear();
  }
}
