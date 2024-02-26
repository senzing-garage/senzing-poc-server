package com.senzing.poc.model;

import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzCrossSourceSummaryImpl;

/**
 * Describes the cross-source statistics between two data sources.
 */
@JsonDeserialize(using = SzCrossSourceSummary.Factory.class)
public interface SzCrossSourceSummary {
    /**
     * Gets the primary data source in the cross comparison.
     *
     * @return The primary data source in the cross comparison.
     */
    String getDataSource();

    /**
     * Sets the primary data source in the cross comparison.
     *
     * @param dataSource The non-null primary data source in the
     *                       cross comparison.
     * 
     * @throws NullPointerException If the specified data source code is
     *                              <code>null</code>.
     */
    void setDataSource(String dataSource)
            throws NullPointerException;

    /**
     * Gets the versus data source in the cross comparison.
     *
     * @return The versus data source in the cross comparison. 
     */
    String getVersusDataSource();

    /**
     * Sets the versus data source in the cross comparison. 
     *
     * @param dataSource The non-null versus data source in the
     *                       cross comparison. 
     * 
     * @throws NullPointerException If the specified data source code is
     *                              <code>null</code>.
     */
    void setVersusDataSource(String dataSource)
            throws NullPointerException;

    /**
     * Gets the {@link List} of {@link SzSummaryCounts} describing the
     * summary statistic counts for each combination of match key and
     * principle including the cases where either or both of the match
     * key and principle are absent or null indicating tracking across
     * all match keys and/or principles.
     * 
     * @return The {@link List} of {@link SzSummaryCounts} describing
     *         the summary statistic counts for each combination of
     *         match key and principle.
     */
    List<SzSummaryCounts> getSummaryCounts();

    /**
     * Adds the {@link SzSummaryCounts} describing the summary statistic
     * counts for a specific match key and principle combination, replacing
     * any existing statistics for that match key and principle combination.
     * 
     * @param counts The {@link SzSummaryCounts} describing the summary
     *               statistic counts for a specific match key and principle
     *               combination.
     */
    void addSummaryCounts(SzSummaryCounts counts);

    /**
     * Removes any statistics associated with the specified match key and
     * principle combination (either or both of which may be <code>null</code>).
     * 
     * @param matchKey The match key for the statistics to be removed.
     * @param principle The principle for the statistics to be removed.
     */
    void removeSummaryCounts(String matchKey, String principle);

    /**
     * Removes all summary statistic counts from this instance.
     */
    void removeAllSummaryCounts();
    
    /**
     * Removes any existing summary statistic counts and replaces them with
     * the specified {@link Collection} of {@link SzSummaryCounts} instances
     * describing the summary statistic counts for each combination of match
     * key and principle.  <b>NOTE:</b> if the specified {@link Collection}
     * has any duplicate pairs of match keys and principles then the one
     * that occurs later in the {@link Collection} wins out and replaces
     * any that occur prior to it in the {@link Collection}.
     * 
     * @param counts The {@link Collection} of {@link SzSummaryCounts} 
     *               instances describing the summary statistic counts for
     *               each match key and principle combination.
     */
    void setSummaryCounts(Collection<SzSummaryCounts> counts);

    /**
     * A {@link ModelProvider} for instances of {@link SzCrossSourceSummary}.
     */
    interface Provider extends ModelProvider<SzCrossSourceSummary> {
        /**
         * Creates a new instance of {@link SzCrossSourceSummary}.
         * 
         * @return The new instance of {@link SzCrossSourceSummary}
         */
        SzCrossSourceSummary create();

        /**
         * Creates a new instance of {@link SzCrossSourceSummary} with the
         * specified data source code.
         * 
         * @param dataSourceCode The non-null data source code identifying the
         *                       data source to which the statistics are associated.
         *
         * @param vsDataSourceCode The non-null "versus" data source code identifying
         *                         the "versus" data source to which the associated
         *                         are associated.
         * 
         * @return The new instance of {@link SzCrossSourceSummary}
         */
        SzCrossSourceSummary create(String dataSourceCode, String vsDataSource);
    }

    /**
     * Provides a default {@link Provider} implementation for
     * {@link SzCrossSourceSummary} that produces instances of
     * {@link SzCrossSourceSummaryImpl}.
     */
    class DefaultProvider extends AbstractModelProvider<SzCrossSourceSummary>
            implements Provider {
        /**
         * Default constructor.
         */
        public DefaultProvider() {
            super(SzCrossSourceSummary.class, SzCrossSourceSummaryImpl.class);
        }

        @Override
        public SzCrossSourceSummary create() 
        {
            return new SzCrossSourceSummaryImpl();
        }

        @Override
        public SzCrossSourceSummary create(String dataSourceCode, 
                                           String vsDataSourceCode) 
        {
            return new SzCrossSourceSummaryImpl(dataSourceCode, vsDataSourceCode);
        }
    }

    /**
     * Provides a {@link ModelFactory} implementation for
     * {@link SzCrossSourceSummary}.
     */
    class Factory extends ModelFactory<SzCrossSourceSummary, Provider> {
        /**
         * Default constructor. This is public and can only be called after the
         * singleton master instance is created as it inherits the same state from
         * the master instance.
         */
        public Factory() {
            super(SzCrossSourceSummary.class);
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
         * Creates a new instance of {@link SzCrossSourceSummary} with the
         * specified data source codes.
         * 
         * @param dataSourceCode The non-null data source code identifying the
         *                       data source to which the statistics are associated.
         *
         * @param vsDataSourceCode The non-null "versus" data source code identifying
         *                         the "versus" data source to which the associated
         *                         are associated.
         *
         * @return The new instance of {@link SzCrossSourceSummary}
         * 
         * @throws NullPointerException If the specified data source code is
         *                              <code>null</code>.
         */
        public SzCrossSourceSummary create(String dataSourceCode,
                                           String vsDataSourceCode) 
        {
            return this.getProvider().create(dataSourceCode, vsDataSourceCode);
        }
    }

    /**
     * The {@link Factory} instance for this interface.
     */
    Factory FACTORY = new Factory(new DefaultProvider());
}
