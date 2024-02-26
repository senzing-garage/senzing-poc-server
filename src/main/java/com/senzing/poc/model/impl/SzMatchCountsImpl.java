package com.senzing.poc.model.impl;

import com.senzing.poc.model.SzMatchCounts;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Provides a basic implementation of {@link SzMatchCounts}.
 */
@JsonDeserialize
public class SzMatchCountsImpl implements SzMatchCounts {
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
        this.entityCount    = 0L;
        this.recordCount    = 0L;
    }

    /**
     * Constructs with the specified parameters.
     * 
     * @param entityCount The number of entities associated with the statistic.
     * @param recordCount The number of records associated with the statistic.
     */
    public SzMatchCountsImpl(long entityCount, long recordCount)
    {
        this.entityCount    = entityCount;
        this.recordCount    = recordCount;
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
