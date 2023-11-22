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
   * The number of records loaded for the data source.  This is initialized
   * to <code>null</code> if it has not yet been set.
   */
  private Long recordCount = null;

  /**
   * The number of entities having at least one record from the associated
   * data source.  This is initialized
   * to <code>null</code> if it has not yet been set.
   */
  private Long entityCount = null;

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
    this.recordCount    = null;
    this.entityCount    = null;
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
  public Long getRecordCount() {
    return this.recordCount;
  }

  @Override
  public void setRecordCount(long recordCount) {
    this.recordCount = recordCount;
  }

  @Override
  public Long getEntityCount() {
    return this.entityCount;
  }

  @Override
  public void setEntityCount(long entityCount) {
    this.entityCount = entityCount;
  }
}
