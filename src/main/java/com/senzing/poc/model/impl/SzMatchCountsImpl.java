package com.senzing.poc.model.impl;

import com.senzing.poc.model.SzMatchCounts;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Provides a basic implementation of {@link SzMatchCounts}.
 */
@JsonDeserialize
public class SzMatchCountsImpl implements SzMatchCounts {
    /**
     * The optional match key associated with the statistics. This may 
     * be <code>null</code> or absent if the statistics are not
     * associated with a match key.
     */
    private String matchKey = null;

    /**
     * The optional principle associated with the statistics. This may 
     * be <code>null</code> or absent if the statistics are not
     * associated with a principle.
     */
    private String principle = null;

    /**
     * The number of entities associated with the statistic.
     */
    private long entityCount = 0L;

    /**
     * The number of records associated with the statistic.
     */
    private long recordCount = 0L;

    /**
     * Default constructor.
     */
    public SzMatchCountsImpl() {
        this(null, null);
    }

    /**
     * Constructs with the optional match key and principle.
     * 
     * @param matchKey The optionally associated match key, or 
     *                 <code>null</code> if no specific match key
     *                 is associated.
     * @param principle The optionally associated principle, or 
     *                  <code>null</code> if no specific principle
     *                  is associated.
     */
    public SzMatchCountsImpl(String matchKey, String principle) {
        this.matchKey       = matchKey;
        this.principle      = principle;
        this.entityCount    = 0L;
        this.recordCount    = 0L;
    }

    @Override
    public String getMatchKey() {
        return this.matchKey;
    }

    @Override
    public String getPrinciple() {
        return this.principle;
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
    public long getRecordCount() {
        return this.recordCount;
    }

    @Override
    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }
}
