package com.senzing.poc.model.impl;

import com.senzing.poc.model.SzEntity;
import com.senzing.poc.model.SzRelation;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzMatchType;

/**
 * Provides a basic implementation of {@link SzRelation}.
 */
@JsonDeserialize
public class SzRelationImpl implements SzRelation {
    /**
     * The {@link SzEntity} describing the first entity in the relation.
     */
    private SzEntity entity = null;
    
    /**
     * The {@link SzEntity} describing the second entity in the relation.
     */
    private SzEntity relatedEntity = null;

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
        this.entity         = null;
        this.relatedEntity  = null;
        this.matchType      = null;
        this.matchKey       = null;
        this.principle      = null;
    }

    @Override
    public SzEntity getEntity() {
        return this.entity;
    }

    @Override
    public void setEntity(SzEntity entity) {
        this.entity = entity;
    }

    @Override
    public SzEntity getRelatedEntity() {
        return this.relatedEntity;
    }

    @Override
    public void setRelatedEntity(SzEntity related) {
        this.relatedEntity = related;
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

    /**
     * Overridden to return a diagnostic {@link String} describing this instance.
     * @return A diagnostic {@link String} describing this instance.
     */
    @Override
    public String toString() {
        return "entity=[ " + this.getEntity() 
            + " ], relatedEntity=[ " + this.getRelatedEntity()
            + " ], matchType=[ " + this.getMatchType()
            + " ], matchKey=[ " + this.getMatchKey()
            + " ], principle=[ " + this.getPrinciple() + " ]";
    }
}
