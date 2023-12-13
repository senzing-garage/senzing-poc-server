package com.senzing.poc.model.impl;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzSourceCountStats;

/**
 * Provides a default implementation of {@link SzSourceCountsStats}.
 */
@JsonDeserialize
public class SzSourceCountStatsImpl implements SzSourceCountStats {
  /**
   * The data source code 
   */
  private String dataSourceCode;

  /**
   * The number of records loaded for the data source.
   */
  private long recordCount = 0L;

  /**
   * The number of entities having at least one record from the associated
   * data source.
   */
  private long entityCount = 0L;

  /**
   * The number of records loaded for the data source that failed to
   * match against any other record in the repository.
   */
  private long unmatchedRecordCount = 0L;

  /**
   * Constructs with the specified data source code.
   * 
   * @param dataSourceCode The data source code identifying the data source to
   *                       which the statistics pertain.
   * 
   * @throws NullPointerException If the specified data source code is
   *                              <code>null</code>.
   */
  public SzSourceCountStatsImpl(String dataSourceCode) 
    throws NullPointerException
  {
    Objects.requireNonNull(dataSourceCode, "Data source code cannot be null");
    this.dataSourceCode = dataSourceCode;
    this.recordCount          = 0L;
    this.entityCount          = 0L;
    this.unmatchedRecordCount = 0L;
  }

  @Override
  public String getDataSourceCode() {
    return this.dataSourceCode;
  }

  @Override
  public void setDataSourceCode(String dataSourceCode) {
    this.dataSourceCode = dataSourceCode;
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
}
