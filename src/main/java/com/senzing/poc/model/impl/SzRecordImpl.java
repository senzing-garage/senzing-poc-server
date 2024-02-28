package com.senzing.poc.model.impl;

import com.senzing.poc.model.SzRecord;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Provides a basic implementation of {@link SzRecord}.
 */
@JsonDeserialize
public class SzRecordImpl implements SzRecord {
    /**
     * The data source code identifying the data source 
     * from which the record was loaded.
     */
    private String dataSource = null;

    /**
     * The record ID that uniquely identifies the record within
     * the data source from which it was loaded.
     */
    private String recordId = null;
    
    /**
     * The optional match key describing why the record merged into the 
     * entity to which it belongs.  This is <code>null</code> if this 
     * record belongs to a single-record entity or if it was the inital
     * record of the first multi-record entity to which it belonged
     * (even if it later re-resolved into a larger entity).
     */
    private String matchKey = null;

    /**
     * The optional principle describing why the record merged into the 
     * entity to which it belongs.  This is <code>null</code> if this 
     * record belongs to a single-record entity or if it was the inital
     * record of the first multi-record entity to which it belonged
     * (even if it later re-resolved into a larger entity).
     */
    private String principle = null;

    /**
     * Default constructor.
     */
    public SzRecordImpl() {
        this(null, null);
    }

    /**
     * Constructs with the specified parameters.
     * 
     * @param dataSourceCode The data source code identifying the data
     *                       source from which the record was loaded.
     * @param recordId The record ID that uniquely identifies the record
     *                 within the data source from which it was loaded.
     */
    public SzRecordImpl(String dataSourceCode, String recordId)
    {
        this.dataSource = dataSourceCode;
        this.recordId   = recordId;
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
    public String getRecordId() {
        return this.recordId;
    }

    @Override
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    @Override
    public String getMatchKey() {
        return this.matchKey;
    }

    @Override
    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    @Override
    public String getPrinciple() {
        return this.principle;
    }

    @Override
    public void setPrinciple(String principle) {
        this.principle = principle;
    }

  /**
   * Overridden to return a diagnostic {@link String} describing this instance.
   * @return A diagnostic {@link String} describing this instance.
   */
  @Override
  public String toString() {
    return "dataSource=[ " + this.getDataSource() 
      + " ], recordId=[ " + this.getRecordId()
      + " ], matchKey=[ " + this.getMatchKey()
      + " ], principle=[ " + this.getPrinciple() + " ]";
  }

}
