package com.senzing.poc.model.impl;

import com.senzing.poc.model.SzRelationCounts;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Provides a basic implementation of {@link SzRelationCounts}.
 */
@JsonDeserialize
public class SzRelationCountsImpl implements SzRelationCounts {
    /**
     * The number of entities associated with the statistic.
     */
    private long entityCount = 0L;

    /**
     * The number of records associated with the statistic.
     */
    private long recordCount = 0L;

    /**
     * The number of relations associated with the statistic.
     */
    private long relationCount = 0L;

    /**
     * Default constructor.
     */
    public SzRelationCountsImpl() {
        this.entityCount    = 0L;
        this.recordCount    = 0L;
        this.relationCount  = 0L;
    }

    /**
     * Constructs with the specified parameters.
     * 
     * @param entityCount The number of entities associated with the statistic.
     * @param recordCount The number of records associated with the statistic.
     * @param relationCount The number of relations associated with the statistic.
     */
    public SzRelationCountsImpl(long entityCount, long recordCount, long relationCount)
    {
        this.entityCount    = entityCount;
        this.recordCount    = recordCount;
        this.relationCount  = relationCount;
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

    @Override
    public long getRelationCount() {
        return this.relationCount;
    }

    @Override
    public void setRelationCount(long relationCount) {
        this.relationCount = relationCount;
    }
}
