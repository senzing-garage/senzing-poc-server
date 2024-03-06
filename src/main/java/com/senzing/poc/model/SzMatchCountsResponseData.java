package com.senzing.poc.model;

import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzMatchCountsResponseDataImpl;

/**
 * Describes the cross-source statistics between two data sources.
 */
@JsonDeserialize(using = SzMatchCountsResponseData.Factory.class)
public interface SzMatchCountsResponseData {
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
     * Gets the {@link List} of {@link SzMatchCounts} instances for each
     * requested match key and principle combination that describe the
     * entity and record counts for matches between records from the primary
     * data source to at least one record from the "versus" data source.
     *
     * @return The {@link List} of {@link SzMatchCounts} instances for each
     *         requested match key and principle combination that describe 
     *         the entity and record counts for matches for this instance.
     */
    List<SzMatchCounts> getCounts();

    /**
     * Sets the {@link SzMatchCounts} instances for this instance using the 
     * specified {@link Collection} of {@link SzMatchCounts}.  Any current
     * {@link SzMatchCounts} are removed and replaced with the specified 
     * instances.  If any of the {@link SzMatchCounts} instances have the 
     * same match-key/principle pairs then the last one wins out replacing
     * any previously added ones.
     * 
     * @param matchCounts The {@link Collection} of {@link SzMatchCounts} 
     *                    instances to set.
     */
    void setCounts(Collection<SzMatchCounts> matchCounts);

    /**
     * Adds the specified {@link SzMatchCounts} instance to the list of
     * {@link SzMatchCounts} instances describing the match counts for
     * this instance, replacing any existing instance with the same match
     * key and principle combination.
     * 
     * @param matchCounts The {@link SzMatchCounts} instance to add.
     */
    void addCounts(SzMatchCounts matchCounts);

    /**
     * Removes the {@link SzMatchCounts} describing the match statistics
     * associated with the optionally specified match key and principle.
     * 
     * @param matchKey The match key for the {@link SzMatchCounts} to
     *                 remove, or <code>null</code> if removing the
     *                 statistics associated with any match key.
     * @param principle The principle for the {@link SzMatchCounts} to
     *                  remove, or <code>null<code> if removing the
     *                  statistics associated with any principle.
     */
    void removeCounts(String matchKey, String principle);

    /**
     * Removes all the {@link SzMatchCounts} describing all the match
     * statistics associated with every combination of match key and principle.
     */
    void removeAllCounts();

    /**
     * A {@link ModelProvider} for instances of {@link SzMatchCountsResponseData}.
     */
    interface Provider extends ModelProvider<SzMatchCountsResponseData> {
        /**
         * Creates a new instance of {@link SzMatchCountsResponseData}.
         * 
         * @return The new instance of {@link SzMatchCountsResponseData}
         */
        SzMatchCountsResponseData create();

        /**
         * Creates a new instance of {@link SzMatchCountsResponseData} with the
         * specified data source code.
         * 
         * @param dataSourceCode The non-null data source code identifying the
         *                       data source to which the statistics are associated.
         *
         * @param vsDataSourceCode The non-null "versus" data source code identifying
         *                         the "versus" data source to which the associated
         *                         are associated.
         * 
         * @return The new instance of {@link SzMatchCountsResponseData}
         */
        SzMatchCountsResponseData create(String dataSourceCode, String vsDataSource);
    }

    /**
     * Provides a default {@link Provider} implementation for
     * {@link SzMatchCountsResponseData} that produces instances of
     * {@link SzMatchCountsResponseDataImpl}.
     */
    class DefaultProvider extends AbstractModelProvider<SzMatchCountsResponseData>
            implements Provider {
        /**
         * Default constructor.
         */
        public DefaultProvider() {
            super(SzMatchCountsResponseData.class, SzMatchCountsResponseDataImpl.class);
        }

        @Override
        public SzMatchCountsResponseData create() 
        {
            return new SzMatchCountsResponseDataImpl();
        }

        @Override
        public SzMatchCountsResponseData create(String dataSourceCode, 
                                           String vsDataSourceCode) 
        {
            return new SzMatchCountsResponseDataImpl(dataSourceCode, vsDataSourceCode);
        }
    }

    /**
     * Provides a {@link ModelFactory} implementation for
     * {@link SzMatchCountsResponseData}.
     */
    class Factory extends ModelFactory<SzMatchCountsResponseData, Provider> {
        /**
         * Default constructor. This is public and can only be called after the
         * singleton master instance is created as it inherits the same state from
         * the master instance.
         */
        public Factory() {
            super(SzMatchCountsResponseData.class);
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
         * Creates a new instance of {@link SzMatchCountsResponseData} with the
         * specified data source codes.
         * 
         * @param dataSourceCode The non-null data source code identifying the
         *                       data source to which the statistics are associated.
         *
         * @param vsDataSourceCode The non-null "versus" data source code identifying
         *                         the "versus" data source to which the associated
         *                         are associated.
         *
         * @return The new instance of {@link SzMatchCountsResponseData}
         * 
         * @throws NullPointerException If the specified data source code is
         *                              <code>null</code>.
         */
        public SzMatchCountsResponseData create(String  dataSourceCode,
                                                String  vsDataSourceCode) 
        {
            return this.getProvider().create(dataSourceCode, vsDataSourceCode);
        }
    }

    /**
     * The {@link Factory} instance for this interface.
     */
    Factory FACTORY = new Factory(new DefaultProvider());
}
