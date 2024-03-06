package com.senzing.poc.model.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Objects;
import java.util.SortedMap;

import com.senzing.poc.model.SzCrossSourceSummary;
import com.senzing.poc.model.SzMatchCounts;
import com.senzing.poc.model.SzRelationCounts;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Provides a basic implementation of {@link SzCrossSourceSummary}.
 */
@JsonDeserialize
public class SzCrossSourceSummaryImpl implements SzCrossSourceSummary {
    /**
     * The primary data source in the cross comparison.
     */
    private String dataSource = null;

    /**
     * The versus data source in the cross comparison. 
     */
    private String versusDataSource = null;

    /**
     * The {@link SortedMap} of {@link SzCountsKey} keys to {@link 
     * SzMatchCounts} values for each requested match-key/principle
     * combination that describe the entity and record counts for
     * matches between records from the primary data source to at
     * least one record from the "versus" data source.
     */
    private SortedMap<SzCountsKey, SzMatchCounts> matches = null;

    /**
     * The {@link SortedMap} of {@link SzCountsKey} keys to {@link 
     * SzRelationCounts} values for each requested match-key/principle
     * combination that describes the entity, record and relationship
     * counts for ambiguous-match relationships between entities having
     * at least one record from the primary data source and entities
     * having at least one record from the "versus" data source.
     */
    private SortedMap<SzCountsKey, SzRelationCounts> ambiguousMatches = null;

    /**
     * The {@link SortedMap} of {@link SzCountsKey} keys to {@link 
     * SzRelationCounts} values for each requested match-key/principle
     * combination that describes the entity, record and relationship
     * counts for possible-match relationships between entities having
     * at least one record from the primary data source and entities
     * having at least one record from the "versus" data source.
     */
    private SortedMap<SzCountsKey, SzRelationCounts> possibleMatches = null;

    /**
     * The {@link SortedMap} of {@link SzCountsKey} keys to {@link 
     * SzRelationCounts} values for each requested match-key/principle
     * combination that describes the entity, record and relationship
     * counts for possible-relation relationships between entities having
     * at least one record from the primary data source and entities
     * having at least one record from the "versus" data source.
     */
    private SortedMap<SzCountsKey, SzRelationCounts> possibleRelations = null;

    /**
     * The {@link SortedMap} of {@link SzCountsKey} keys to {@link 
     * SzRelationCounts} values for each requested match-key/principle
     * combination that describes the entity, record and relationship
     * counts for disclosed-relation relationships between entities having
     * at least one record from the primary data source and entities
     * having at least one record from the "versus" data source.
     */
    private SortedMap<SzCountsKey, SzRelationCounts> disclosedRelations = null;

    /**
     * Default constructor.
     */
    public SzCrossSourceSummaryImpl() {
        this.dataSource         = null;
        this.versusDataSource   = null;
        this.matches            = new TreeMap<>();
        this.ambiguousMatches   = new TreeMap<>();
        this.possibleMatches    = new TreeMap<>();
        this.possibleRelations  = new TreeMap<>();
        this.disclosedRelations = new TreeMap<>();
    }

    /**
     * Constructs with the primary and "versus" data source codes.
     * 
     * @param dataSource The data source code for the primary data source.
     * @param versusDataSource The data source code for the "versus" data source.
     */
    public SzCrossSourceSummaryImpl(String dataSource, String vsDataSource) {
        this.dataSource         = dataSource;
        this.versusDataSource   = vsDataSource;
        this.matches            = new TreeMap<>();
        this.ambiguousMatches   = new TreeMap<>();
        this.possibleMatches    = new TreeMap<>();
        this.possibleRelations  = new TreeMap<>();
        this.disclosedRelations = new TreeMap<>();
    }

    @Override
    public String getDataSource() {
        return this.dataSource;
    }

    @Override
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getVersusDataSource() {
        return this.versusDataSource;
    }

    @Override
    public void setVersusDataSource(String dataSource) {
        this.versusDataSource = dataSource;
    }

    @Override
    public List<SzMatchCounts> getMatches() {
        return new ArrayList<>(this.matches.values());
    }

    @Override
    public void setMatches(Collection<SzMatchCounts> matchCounts) {
        this.matches.clear();
        if (matchCounts != null) {
            matchCounts.forEach(counts -> {
                SzCountsKey key = new SzCountsKey(
                    counts.getMatchKey(), counts.getPrinciple());
                this.matches.put(key, counts);
            });
        }
    }

    @Override
    public void addMatches(SzMatchCounts matchCounts) {
        if (matchCounts == null) return;
        SzCountsKey key = new SzCountsKey(
            matchCounts.getMatchKey(), matchCounts.getPrinciple());
        this.matches.put(key, matchCounts);
    }

    @Override
    public void removeMatches(String matchKey, String principle) {
        this.matches.remove(new SzCountsKey(matchKey, principle));
    }
    
    @Override
    public void removeAllMatches() {
        this.matches.clear();
    }

    @Override
    public List<SzRelationCounts> getAmbiguousMatches() {
        return new ArrayList<>(this.ambiguousMatches.values());
    }

    @Override
    public void setAmbiguousMatches(Collection<SzRelationCounts> relationCounts) {
        this.ambiguousMatches.clear();
        if (relationCounts != null) {
            relationCounts.forEach(counts -> {
                SzCountsKey key = new SzCountsKey(
                    counts.getMatchKey(), counts.getPrinciple());
                this.ambiguousMatches.put(key, counts);
            });
        }
    }

    @Override
    public void addAmbiguousMatches(SzRelationCounts relationCounts) {
        if (relationCounts == null) return;
        SzCountsKey key = new SzCountsKey(
            relationCounts.getMatchKey(), relationCounts.getPrinciple());
        this.ambiguousMatches.put(key, relationCounts);
    }

    @Override
    public void removeAmbiguousMatches(String matchKey, String principle) {
        this.ambiguousMatches.remove(new SzCountsKey(matchKey, principle));
    }
    
    @Override
    public void removeAllAmbiguousMatches() {
        this.ambiguousMatches.clear();
    }

    @Override
    public List<SzRelationCounts> getPossibleMatches() {
        return new ArrayList<>(this.possibleMatches.values());
    }

    @Override
    public void setPossibleMatches(Collection<SzRelationCounts> relationCounts) {
        this.possibleMatches.clear();
        if (relationCounts != null) {
            relationCounts.forEach(counts -> {
                SzCountsKey key = new SzCountsKey(
                    counts.getMatchKey(), counts.getPrinciple());
                this.possibleMatches.put(key, counts);
            });
        }
    }

    @Override
    public void addPossibleMatches(SzRelationCounts relationCounts) {
        if (relationCounts == null) return;
        SzCountsKey key = new SzCountsKey(
            relationCounts.getMatchKey(), relationCounts.getPrinciple());
        this.possibleMatches.put(key, relationCounts);
    }

    @Override
    public void removePossibleMatches(String matchKey, String principle) {
        this.possibleMatches.remove(new SzCountsKey(matchKey, principle));
    }
    
    @Override
    public void removeAllPossibleMatches() {
        this.possibleMatches.clear();
    }

    @Override
    public List<SzRelationCounts> getPossibleRelations() {
        return new ArrayList<>(this.possibleRelations.values());
    }

    @Override
    public void setPossibleRelations(Collection<SzRelationCounts> relationCounts) {
        this.possibleRelations.clear();
        if (relationCounts != null) {
            relationCounts.forEach(counts -> {
                SzCountsKey key = new SzCountsKey(
                    counts.getMatchKey(), counts.getPrinciple());
                this.possibleRelations.put(key, counts);
            });
        }
    }

    @Override
    public void addPossibleRelations(SzRelationCounts relationCounts) {
        if (relationCounts == null) return;
        SzCountsKey key = new SzCountsKey(
            relationCounts.getMatchKey(), relationCounts.getPrinciple());
        this.possibleRelations.put(key, relationCounts);
    }

    @Override
    public void removePossibleRelations(String matchKey, String principle) {
        this.possibleRelations.remove(new SzCountsKey(matchKey, principle));
    }
    
    @Override
    public void removeAllPossibleRelations() {
        this.possibleRelations.clear();
    }

    @Override
    public List<SzRelationCounts> getDisclosedRelations() {
        return new ArrayList<>(this.disclosedRelations.values());
    }

    @Override
    public void setDisclosedRelations(Collection<SzRelationCounts> relationCounts) {
        this.disclosedRelations.clear();
        if (relationCounts != null) {
            relationCounts.forEach(counts -> {
                SzCountsKey key = new SzCountsKey(
                    counts.getMatchKey(), counts.getPrinciple());
                this.disclosedRelations.put(key, counts);
            });
        }
    }

    @Override
    public void addDisclosedRelations(SzRelationCounts relationCounts) {
        if (relationCounts == null) return;
        SzCountsKey key = new SzCountsKey(
            relationCounts.getMatchKey(), relationCounts.getPrinciple());
        this.disclosedRelations.put(key, relationCounts);
    }

    @Override
    public void removeDisclosedRelations(String matchKey, String principle) {
        this.disclosedRelations.remove(new SzCountsKey(matchKey, principle));
    }

    @Override
    public void removeAllDisclosedRelations() {
        this.disclosedRelations.clear();
    }
 }
