package com.senzing.poc.model.impl;

import com.senzing.poc.model.SzRelation;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzMatchType;

/**
 * Provides a basic implementation of {@link SzRelation}.
 */
@JsonDeserialize
public class SzRelationImpl implements SzRelation {
    /**
     * The first entity ID for the relation.
     */
    private long entityId = 0L;

    /**
     * The second entity ID (the related ID) for the relation.
     */
    private long relatedId = 0L;

    /**
     * The {@link SzMatchType} describing the relationship type for the relation.
     */
    private SzMatchType matchType = null;

    /**
     * The match key for the relation.
     */
    private String matchKey = null;

    /**
     * The principle for the relation.
     */
    private String principle = null;

    /**
     * Default constructor.
     */
    public SzRelationImpl() {
        this.entityId   = 0L;
        this.relatedId  = 0L;
        this.matchType  = null;
        this.matchKey   = null;
        this.principle  = null;
    }

    /**
     * Constructs with the specified parameters.
     * 
     * @param entityId The first entity ID for the relation.
     * @param relatedId The second entity ID (the related ID) for the relation.
     * @param matchType The {@link SzMatchType} for the relation.
     * @param matchKey The match key for the relation.
     * @param principle The principle for the relation.
     */
    public SzRelationImpl(long          entityId,
                          long          relatedId,
                          SzMatchType   matchType,
                          String        matchKey,
                          String        principle)
    {
        this.entityId   = entityId;
        this.relatedId  = relatedId;
        this.matchType  = matchType;
        this.matchKey   = matchKey;
        this.principle  = principle;
    }

    @Override
    public long getEntityId() {
        return this.entityId;
    }

    @Override
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    @Override
    public long getRelatedId() {
        return this.relatedId;
    }

    @Override
    public void setRelatedId(long relatedId) {
        this.relatedId = relatedId;
    }

    @Override
    public SzMatchType getMatchType() {
        return this.matchType;
    }

    @Override
    public void setMatchType(SzMatchType matchType) {
        this.matchType = matchType;
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
}
