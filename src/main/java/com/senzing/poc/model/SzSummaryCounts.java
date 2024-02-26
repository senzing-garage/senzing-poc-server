package com.senzing.poc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzSummaryCountsImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes the summary statistics between two data sources
 * optionally associated with a principle and match key.
 */
@JsonDeserialize(using = SzSummaryCounts.Factory.class)
public interface SzSummaryCounts {
    /**
     * Gets the optional match key associated with the statistics. This may 
     * be <code>null</code> or absent if the statistics are not
     * associated with a match key.
     * 
     * @return The optional match key associated with the statistics, or
     *         <code>null</code> or absent if the statistics are not
     *         associated with a match key.
     */
    @JsonInclude(NON_NULL)
    String getMatchKey();

    /**
     * Sets the optional match key associated with the statistics. This may 
     * be <code>null</code> or absent if the statistics are not
     * associated with a match key.
     * 
     * @param matchKey The optional match key associated with the statistics,
     *                 or <code>null</code> or absent if the statistics are
     *                 not associated with a match key.
     */
    void setMatchKey(String matchKey);

    /**
     * Gets the optional principle associated with the statistics. This may 
     * be <code>null</code> or absent if the statistics are not
     * associated with a principle.
     * 
     * @return The optional principle associated with the statistics, or
     *         <code>null</code> or absent if the statistics are not
     *         associated with a principle.
     */
    @JsonInclude(NON_NULL)
    String getPrinciple();

    /**
     * Sets the optional principle associated with the statistics. This may 
     * be <code>null</code> or absent if the statistics are not
     * associated with a principle.
     * 
     * @param matchKey The optional principle associated with the statistics,
     *                 or <code>null</code> or absent if the statistics are
     *                 not associated with a principle.
     */
    void setPrinciple(String principle);

    /**
     * Gets the {@link SzMatchCounts} that describes the entity and record 
     * counts for matches between records from the primary data source
     * to at least one record from the "versus" data source.
     *
     * @return The {@link SzMatchCounts} that describes the entity and
     *         record counts for matches between records from the primary
     *         data source to at least one record from the "versus" data 
     *         source.
     */
    SzMatchCounts getMatches();

    /**
     * Gets the {@link SzRelationCounts} that describes the entity, record 
     * and relationship counts for ambiguous-match relationships between
     * entities having at least one record from the primary data source and
     * entities having at least one record from the "versus" data source.
     *
     * @return The {@link SzRelationCounts} that describes the entity, record 
     *         and relationship counts for ambiguous-match relationships 
     *         between entities having at least one record from the primary
     *         data source and entities having at least one record from the
     *         "versus" data source.
     */
    SzRelationCounts getAmbiguousMatches();

    /**
     * Gets the {@link SzRelationCounts} that describes the entity, record 
     * and relationship counts for possible-match relationships between
     * entities having at least one record from the primary data source and
     * entities having at least one record from the "versus" data source.
     *
     * @return The {@link SzRelationCounts} that describes the entity, record 
     *         and relationship counts for possible-match relationships 
     *         between entities having at least one record from the primary
     *         data source and entities having at least one record from the
     *         "versus" data source.
     */
    SzRelationCounts getPossibleMatches();

    /**
     * Gets the {@link SzRelationCounts} that describes the entity, record 
     * and relationship counts for possible relationships between
     * entities having at least one record from the primary data source and
     * entities having at least one record from the "versus" data source.
     *
     * @return The {@link SzRelationCounts} that describes the entity, record 
     *         and relationship counts for possible relationships 
     *         between entities having at least one record from the primary
     *         data source and entities having at least one record from the
     *         "versus" data source.
     */
    SzRelationCounts getPossibleRelations();

    /**
     * Gets the {@link SzRelationCounts} that describes the entity, record 
     * and relationship counts for disclosed relationships between
     * entities having at least one record from the primary data source and
     * entities having at least one record from the "versus" data source.
     *
     * @return The {@link SzRelationCounts} that describes the entity, record 
     *         and relationship counts for disclosed relationships 
     *         between entities having at least one record from the primary
     *         data source and entities having at least one record from the
     *         "versus" data source.
     */
    SzRelationCounts getDisclosedRelations();

    /**
     * A {@link ModelProvider} for instances of {@link SzSummaryCounts}.
     */
    interface Provider extends ModelProvider<SzSummaryCounts> {
        /**
         * Creates a new instance of {@link SzSummaryCounts}.
         *
         * @return The new instance of {@link SzSummaryCounts}
         */
        SzSummaryCounts create();

        /**
         * Creates a new instance of {@link SzSummaryCounts} with
         * the specified match key and principle.
         *
         * @param matchKey The match key to associate with the new
         *                 instance, or <code>null</code> if none.
         * 
         * @param principle The principle to associate with the new 
         *                 instance, or <code>null</code> if none.
         * 
         * @return A new instance of {@link SzSummaryCounts}.
         */
        public SzSummaryCounts create(String matchKey, String principle);
    }

    /**
     * Provides a default {@link Provider} implementation for {@link SzSummaryCounts}
     * that produces instances of {@link SzSummaryCountsImpl}.
     */
    class DefaultProvider extends AbstractModelProvider<SzSummaryCounts>
            implements Provider {
        /**
         * Default constructor.
         */
        public DefaultProvider() {
            super(SzSummaryCounts.class, SzSummaryCountsImpl.class);
        }

        @Override
        public SzSummaryCounts create() {
            return new SzSummaryCountsImpl();
        }

        @Override
        public SzSummaryCounts create(String matchKey, String principle) {
            return new SzSummaryCountsImpl(matchKey, principle);
        }
    }

    /**
     * Provides a {@link ModelFactory} implementation for {@link SzSummaryCounts}.
     */
    class Factory extends ModelFactory<SzSummaryCounts, Provider> {
        /**
         * Default constructor. This is public and can only be called after the
         * singleton master instance is created as it inherits the same state from
         * the master instance.
         */
        public Factory() {
            super(SzSummaryCounts.class);
        }

        /**
         * Constructs with the default provider. This constructor is private and
         * is used for the master singleton instance.
         * 
         * @param defaultProvider The default provider.
         */
        private Factory(Provider defaultProvider) {
            super(defaultProvider);
        }

        /**
         * Creates a new instance of {@link SzSummaryCounts}.
         *
         * @return A new instance of {@link SzSummaryCounts}.
         */
        public SzSummaryCounts create() {
            return this.getProvider().create();
        }

        /**
         * Creates a new instance of {@link SzSummaryCounts} with
         * the specified match key and principle.
         *
         * @param matchKey The match key to associate with the new
         *                 instance, or <code>null</code> if none.
         * 
         * @param principle The principle to associate with the new 
         *                 instance, or <code>null</code> if none.
         * 
         * @return A new instance of {@link SzSummaryCounts}.
         */
        public SzSummaryCounts create(String matchKey, String principle) {
            return this.getProvider().create(matchKey, principle);
        }
    }

    /**
     * The {@link Factory} instance for this interface.
     */
    Factory FACTORY = new Factory(new DefaultProvider());

}
