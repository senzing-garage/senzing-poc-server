package com.senzing.poc.model.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collection;
import java.util.SortedMap;

import com.senzing.poc.model.SzMatchCountsResponseData;
import com.senzing.poc.model.SzMatchCounts;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Provides a basic implementation of {@link SzMatchCountsResponseData}.
 */
@JsonDeserialize
public class SzMatchCountsResponseDataImpl implements SzMatchCountsResponseData {
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
    private SortedMap<SzCountsKey, SzMatchCounts> counts = null;

    /**
     * Default constructor.
     */
    public SzMatchCountsResponseDataImpl() {
        this.dataSource         = null;
        this.versusDataSource   = null;
        this.counts             = new TreeMap<>();
    }

    /**
     * Constructs with the primary and "versus" data source codes.
     * 
     * @param dataSource The data source code for the primary data source.
     * @param versusDataSource The data source code for the "versus" data source.
     */
    public SzMatchCountsResponseDataImpl(String dataSource, String vsDataSource) {
        this.dataSource         = dataSource;
        this.versusDataSource   = vsDataSource;
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
    public List<SzMatchCounts> getCounts() {
        return new ArrayList<>(this.counts.values());
    }

    @Override
    public void setCounts(Collection<SzMatchCounts> matchCounts) {
        this.counts.clear();
        if (matchCounts != null) {
            matchCounts.forEach(counts -> {
                SzCountsKey key = new SzCountsKey(
                    counts.getMatchKey(), counts.getPrinciple());
                this.counts.put(key, counts);
            });
        }
    }

    @Override
    public void addCounts(SzMatchCounts matchCounts) {
        if (matchCounts == null) return;
        SzCountsKey key = new SzCountsKey(
            matchCounts.getMatchKey(), matchCounts.getPrinciple());
        this.counts.put(key, matchCounts);
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
