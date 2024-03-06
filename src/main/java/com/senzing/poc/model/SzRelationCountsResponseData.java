package com.senzing.poc.model;

import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzRelationCountsResponseDataImpl;

/**
 * Describes the cross-source statistics between two data sources.
 */
@JsonDeserialize(using = SzRelationCountsResponseData.Factory.class)
public interface SzRelationCountsResponseData {
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
     * Gets the {@link SzRelationType} describing the type of relationship
     * match for the associated statistics.
     * 
     * @return The {@link SzRelationType} describing the type of
     *         relationship match for the associated statistics.
     */
    SzRelationType getRelationType();

    /**
     * Sets the {@link SzRelationType} describing the type of relationship
     * match for the associated statistics.
     * 
     * @param relationType The {@link SzRelationType} describing the type
     *                     of relationship match for the associated statistics.
     */
    void setRelationType(SzRelationType relationType);

    /**
     * Gets the {@link List} of {@link SzRelationCounts} instances for each
     * requested match key and principle combination that describe the entity,
     * record and relationship counts for ambiguous-match relationships between
     * entities having at least one record from the primary data source and
     * entities having at least one record from the "versus" data source.
     *
     * @return The {@link List} of {@link SzRelationCounts} instances for 
     *         each requested match key and principle combination 
     *         describing the ambiguous-match entity, record and
     *         relationship counts for this instance.
     */
    List<SzRelationCounts> getCounts();

    /**
     * Sets the {@link SzRelationCounts} instances describing the ambiguous
     * match relation counts for one or more match-key/principle combination
     * using the specified {@link Collection} of {@link SzRelationCounts}.
     * Any current {@link SzRelationCounts} are removed and replaced with
     * the specified instances.  If any of the {@link SzRelationCounts}
     * instances have the same match-key/principle pairs then the last one
     * wins out replacing any previously added ones.
     * 
     * @param relationCounts The {@link Collection} of {@link SzRelationCounts} 
     *                       instances to set.
     */
    void setCounts(Collection<SzRelationCounts> relationCounts);

    /**
     * Adds the specified {@link SzRelationCounts} instance to the list of
     * {@link SzRelationCounts} instances describing the ambiguous-match
     * relationship counts for this instance, replacing any existing instance
     * with the same match key and principle combination.
     * 
     * @param relationCounts The {@link SzRelationCounts} instance to add.
     */
    void addCounts(SzRelationCounts relationCounts);

    /**
     * Removes the {@link SzRelationCounts} describing the ambiguous
     * match statistics associated with the optionally specified 
     * match key and principle.
     * 
     * @param matchKey The match key for the ambiguous match {@link
     *                 SzRelationCounts} to remove, or <code>null</code>
     *                 if removing the statistics associated with any 
     *                 match key.
     * @param principle The principle for the ambiguous match {@link 
     *                  SzRelationCounts} to remove, or <code>null<code>
     *                  if removing the statistics associated with any
     *                  principle.
     */
    void removeCounts(String matchKey, String principle);

    /**
     * Removes all the {@link SzRelationCounts} describing all the
     * ambiguous match statistics associated with every combination
     * of match key and principle.
     */
    void removeAllCounts();

    /**
     * A {@link ModelProvider} for instances of {@link SzRelationCountsResponseData}.
     */
    interface Provider extends ModelProvider<SzRelationCountsResponseData> {
        /**
         * Creates a new instance of {@link SzRelationCountsResponseData}.
         * 
         * @return The new instance of {@link SzRelationCountsResponseData}
         */
        SzRelationCountsResponseData create();

        /**
         * Creates a new instance of {@link SzRelationCountsResponseData} with the
         * specified data source code.
         * 
         * @param dataSourceCode The non-null data source code identifying the
         *                       data source to which the statistics are associated.
         *
         * @param vsDataSourceCode The non-null "versus" data source code identifying
         *                         the "versus" data source to which the associated
         *                         are associated.
         * @param relationType The {@link SzRelationType} identifying the type of
         *                     relations for the associated statistics.
         * @return The new instance of {@link SzRelationCountsResponseData}
         */
        SzRelationCountsResponseData create(String          dataSourceCode, 
                                            String          vsDataSource,
                                            SzRelationType  relationType);
    }

    /**
     * Provides a default {@link Provider} implementation for
     * {@link SzRelationCountsResponseData} that produces instances of
     * {@link SzRelationCountsResponseDataImpl}.
     */
    class DefaultProvider extends AbstractModelProvider<SzRelationCountsResponseData>
            implements Provider {
        /**
         * Default constructor.
         */
        public DefaultProvider() {
            super(SzRelationCountsResponseData.class, SzRelationCountsResponseDataImpl.class);
        }

        @Override
        public SzRelationCountsResponseData create() 
        {
            return new SzRelationCountsResponseDataImpl();
        }

        @Override
        public SzRelationCountsResponseData create(
            String          dataSourceCode, 
            String          vsDataSourceCode,
            SzRelationType  relationType)
        {
            return new SzRelationCountsResponseDataImpl(
                dataSourceCode, vsDataSourceCode, relationType);
        }
    }

    /**
     * Provides a {@link ModelFactory} implementation for
     * {@link SzRelationCountsResponseData}.
     */
    class Factory extends ModelFactory<SzRelationCountsResponseData, Provider> {
        /**
         * Default constructor. This is public and can only be called after the
         * singleton master instance is created as it inherits the same state from
         * the master instance.
         */
        public Factory() {
            super(SzRelationCountsResponseData.class);
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
         * Creates a new instance of {@link SzRelationCountsResponseData} with the
         * specified data source codes.
         * 
         * @param dataSourceCode The non-null data source code identifying the
         *                       data source to which the statistics are associated.
         *
         * @param vsDataSourceCode The non-null "versus" data source code identifying
         *                         the "versus" data source to which the associated
         *                         are associated.
         *
         * @return The new instance of {@link SzRelationCountsResponseData}
         * 
         * @throws NullPointerException If the specified data source code is
         *                              <code>null</code>.
         */
        public SzRelationCountsResponseData create(
            String          dataSourceCode,
            String          vsDataSourceCode,
            SzRelationType  relationType)
        {
            return this.getProvider().create(
                dataSourceCode, vsDataSourceCode, relationType);
        }
    }

    /**
     * The {@link Factory} instance for this interface.
     */
    Factory FACTORY = new Factory(new DefaultProvider());
}
