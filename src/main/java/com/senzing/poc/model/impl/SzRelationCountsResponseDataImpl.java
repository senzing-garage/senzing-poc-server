package com.senzing.poc.model.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collection;
import java.util.SortedMap;

import com.senzing.poc.model.SzRelationType;
import com.senzing.poc.model.SzRelationCountsResponseData;
import com.senzing.poc.model.SzRelationCounts;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Provides a basic implementation of {@link SzRelationCountsResponseData}.
 */
@JsonDeserialize
public class SzRelationCountsResponseDataImpl
    implements SzRelationCountsResponseData 
{
    /**
     * The primary data source in the cross comparison.
     */
    private String dataSource = null;

    /**
     * The versus data source in the cross comparison. 
     */
    private String versusDataSource = null;

    /**
     * The {@link SzRelationType} for the {@link SzRelationCounts} instances.
     */
    private SzRelationType relationType = null;

    /**
     * The {@link SortedMap} of {@link SzCountsKey} keys to {@link 
     * SzRelationCounts} values for each requested match-key/principle
     * combination that describes the entity, record and relationship
     * counts for ambiguous-match relationships between entities having
     * at least one record from the primary data source and entities
     * having at least one record from the "versus" data source.
     */
    private SortedMap<SzCountsKey, SzRelationCounts> counts = null;

    /**
     * Default constructor.
     */
    public SzRelationCountsResponseDataImpl() {
        this(null, null, null);
    }

    /**
     * Constructs with the primary and "versus" data source codes.
     * 
     * @param dataSource The data source code for the primary data source.
     * @param versusDataSource The data source code for the "versus" data source.
     */
    public SzRelationCountsResponseDataImpl(String          dataSource,
                                            String          vsDataSource,
                                            SzRelationType  relationType)
    {
        this.dataSource         = dataSource;
        this.versusDataSource   = vsDataSource;
        this.relationType       = relationType;
        this.counts             = new TreeMap<>();
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
    public SzRelationType getRelationType() {
        return this.relationType;
    }

    @Override
    public void setRelationType(SzRelationType relationType) {
        this.relationType = relationType;
    }

    @Override
    public List<SzRelationCounts> getCounts() {
        return new ArrayList<>(this.counts.values());
    }

    @Override
    public void setCounts(Collection<SzRelationCounts> relationCounts) {
        this.counts.clear();
        if (relationCounts != null) {
            relationCounts.forEach(relCounts -> {
                SzCountsKey key = new SzCountsKey(
                    relCounts.getMatchKey(), relCounts.getPrinciple());
                this.counts.put(key, relCounts);
            });
        }
    }

    @Override
    public void addCounts(SzRelationCounts relationCounts) {
        if (relationCounts == null) return;
        SzCountsKey key = new SzCountsKey(
            relationCounts.getMatchKey(), relationCounts.getPrinciple());
        this.counts.put(key, relationCounts);
    }

    @Override
    public void removeCounts(String matchKey, String principle) {
        this.counts.remove(new SzCountsKey(matchKey, principle));
    }
    
    @Override
    public void removeAllCounts() {
        this.counts.clear();
    }
 }
