package com.senzing.poc.model.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Objects;
import com.senzing.poc.model.SzCrossSourceSummary;
import com.senzing.poc.model.SzSummaryCounts;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Provides a basic implementation of {@link SzCrossSourceSummary}.
 */
@JsonDeserialize
public class SzCrossSourceSummaryImpl implements SzCrossSourceSummary {
    /**
     * Provides a key for a match key & principle pair that is tolerant
     * of <code>null</code> match key and/or principle values in its 
     * {@link #equals(Object)}, {@link #hashCode()} and {@link 
     * #compareTo(SummaryCountsKey)} functions including comparison against
     * <code>null</code> references to this class.  Any <code>null</code>
     * value is sorted as "less-than" a value that is <b>not</b> 
     * <code>null</code>.
     */
    private class SummaryCountsKey implements Comparable<SummaryCountsKey> {
        /**
         * The associated match key.
         */
        private String matchKey;

        /**
         * The associated principle.
         */
        private String principle;

        /**
         * Constructs with the specified match key and principle.
         * @param matchKey The match key to associate with the new instance.
         * @param principle The principle to associate with the new instance.
         */
        private SummaryCountsKey(String matchKey, String principle) {
            this.matchKey   = matchKey;
            this.principle  = principle;
        }

        /**
         * Gets the associated match key.
         * @return The associated match key.
         */
        public String getMatchKey() {
            return this.matchKey;
        }

        /**
         * Gets the associated principle.
         * @return The associated principle.
         */
        public String getPrinciple() {
            return this.principle;
        }

        /**
         * Implemented to return <code>true</code> if and only if the specified
         * parameter is a non-null reference to an object of the same class with
         * an equivalent match key and principle.
         * 
         * @param obj The object to compare with.
         * 
         * @return <code>true</code> if and only if the specified parameter is a
         *         non-null reference to an object of the same class with an
         *         equivalent match key and principle, otherwise <code>false</code>.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (this == obj) return true;
            if (this.getClass() != obj.getClass()) return false;
        
            SummaryCountsKey smk = (SummaryCountsKey) obj;
            return Objects.equals(this.getMatchKey(), smk.getMatchKey())
                && Objects.equals(this.getPrinciple(), smk.getPrinciple());
        }

        /**
         * Implemented to return a hash code based on the match key and 
         * principle.
         * 
         * @return The hash code for this instance.
         */
        @Override
        public int hashCode() {
            return Objects.hash(this.getMatchKey(), this.getPrinciple());
        }

        /**
         * Implemented to return a negative number, zero (0) or a positive
         * number depending on whether this instance compares less-than,
         * equal-to, or greater than the specified parameter with sorting 
         * first on match key and then principle with <code>null</code> 
         * values comparing less-than values that are not <code>null</code>.
         * 
         * @param key The {@link SummaryCountsKey} to compare with.
         * 
         * @return A negative number, zero (0) or a positive number
         *         depending on whether this instance compares less-than,
         *         equal-to, or greater than the specified parameter.
         */
        @Override
        public int compareTo(SummaryCountsKey key) {
            if (key == null) return 1;
            String mk1 = this.getMatchKey();
            String mk2 = key.getMatchKey();
            if (!Objects.equals(mk1, mk2)) {
                if (mk1 == null) return -1;
                if (mk2 == null) return 1;
                return mk1.compareTo(mk2);
            }
            String p1 = this.getPrinciple();
            String p2 = key.getPrinciple();
            if (Objects.equals(p1, p2)) return 0;
            if (p1 == null) return -1;
            if (p2 == null) return 1;
            return p1.compareTo(p2);
        }
        
        /**
         * Implemented to return a diagnostic {@link String} describing this
         * instance with the principle first and then the match key, separated
         * by a colon.
         * 
         * @return A diagnostic {@link String} descrbing this instance.
         */
        public String toString() {
            return this.getPrinciple() + ":" + this.getMatchKey();
        }
    }

    /**
     * The primary data source in the cross comparison.
     */
    private String dataSource = null;

    /**
     * The versus data source in the cross comparison. 
     */
    private String versusDataSource = null;

    /**
     * The array of summary statistic counts for each combination of 
     * match key and principle including the cases where either or both
     * of the match key and principle are absent or <code>null</code>
     * indicating tracking across all match keys and/or principles.
     */
    private Map<SummaryCountsKey, SzSummaryCounts> summaryCounts;

    /**
     * Default constructor.
     */
    public SzCrossSourceSummaryImpl() {
        this.dataSource         = null;
        this.versusDataSource   = null;
        this.summaryCounts      = new TreeMap<>();
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
        this.summaryCounts      = new TreeMap<>();
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
    public List<SzSummaryCounts> getSummaryCounts() {
        return new ArrayList<>(this.summaryCounts.values());
    }

    @Override
    public void setSummaryCounts(Collection<SzSummaryCounts> counts) {
        this.summaryCounts.clear();
        if (counts != null) {
            counts.forEach(sc -> {
                this.addSummaryCounts(sc);
            });
        }
    }

    @Override
    public void addSummaryCounts(SzSummaryCounts counts) {
        if (counts == null) return;
        SummaryCountsKey key = new SummaryCountsKey(
            counts.getMatchKey(), counts.getPrinciple());
        this.summaryCounts.put(key, counts);
    }

    @Override
    public void removeSummaryCounts(String matchKey, String principle) {
        SummaryCountsKey key = new SummaryCountsKey(matchKey, principle);
        this.summaryCounts.remove(key);
    }

    @Override
    public void removeAllSummaryCounts() {
        this.summaryCounts.clear();
    }
 }
