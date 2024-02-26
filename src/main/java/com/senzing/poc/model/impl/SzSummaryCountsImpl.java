package com.senzing.poc.model.impl;

import com.senzing.poc.model.SzSummaryCounts;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzMatchCounts;
import com.senzing.poc.model.SzRelationCounts;

/**
 * Provides a basic implementation of {@link SzSummaryCounts}.
 */
@JsonDeserialize
public class SzSummaryCountsImpl implements SzSummaryCounts {
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
     * The {@link SzMatchCounts} that describes the entity and record 
     * counts for matches between records from the primary data source
     * to at least one record from the "versus" data source.
     */
    private SzMatchCounts matches = null;

    /**
     * The {@link SzRelationCounts} that describes the entity, record 
     * and relationship counts for ambiguous-match relationships between
     * entities having at least one record from the primary data source and
     * entities having at least one record from the "versus" data source.
     */
    private SzRelationCounts ambiguousMatches = null;

    /**
     * The {@link SzRelationCounts} that describes the entity, record 
     * and relationship counts for possible-match relationships between
     * entities having at least one record from the primary data source and
     * entities having at least one record from the "versus" data source.
     */
    private SzRelationCounts possibleMatches = null;

    /**
     * The {@link SzRelationCounts} that describes the entity, record 
     * and relationship counts for possible relationships between entities
     * having at least one record from the primary data source and entities
     * having at least one record from the "versus" data source.
     */
    private SzRelationCounts possibleRelations = null;

    /**
     * The {@link SzRelationCounts} that describes the entity, record and
     * relationship counts for the respective relation type for entities
     * having at least one record from the primary data source to entities
     * having at least one record from the "versus" data source.
     */
    private SzRelationCounts disclosedRelations = null;

    /**
     * Default constructor.
     */
    public SzSummaryCountsImpl() {
        this(null, null);
    }

    /**
     * Constructs with the specified optional match key and optional principle.
     * 
     * @param matchKey The match key to associate, or <code>null</code> if none.
     * @param principle The principle to associate, or <code>null</code> if none.
     */
    public SzSummaryCountsImpl(String matchKey, String principle) {
        this.matchKey   = matchKey;
        this.principle  = principle;
        this.matches            = SzMatchCounts.FACTORY.create();
        this.ambiguousMatches   = SzRelationCounts.FACTORY.create();
        this.possibleMatches    = SzRelationCounts.FACTORY.create();
        this.possibleRelations  = SzRelationCounts.FACTORY.create();
        this.disclosedRelations = SzRelationCounts.FACTORY.create();
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

    @Override
    public SzMatchCounts getMatches() {
        return this.matches;
    }

    @Override
    public SzRelationCounts getAmbiguousMatches() {
        return this.ambiguousMatches;
    }

    @Override
    public SzRelationCounts getPossibleMatches() {
        return this.possibleMatches;
    }

    @Override
    public SzRelationCounts getPossibleRelations() {
        return this.possibleRelations;
    }

    @Override
    public SzRelationCounts getDisclosedRelations() {
        return this.disclosedRelations;
    }
}
