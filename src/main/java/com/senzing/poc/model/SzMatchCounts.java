package com.senzing.poc.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzMatchCountsImpl;

/**
 * Describes the entity and record counts for matches between records
 * from the primary data source to at least one record from the
 * "versus" data source.
 */
public interface SzMatchCounts {
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
     * Gets the number of entities having at least one record from the
     * primary data source matching to at least one record from the
     * "versus" data source.
     * 
     * @return The number of entities having at least one record from
     *         the primary data source matching to at least one record
     *         from the "versus" data source.
     */
    long getEntityCount();

    /**
     * Sets the number of entities having at least one record from the
     * primary data source matching to at least one record from the
     * "versus" data source.
     * 
     * @param entityCount The number of entities having at least one
     *                    record from the primary data source matching
     *                    to at least one record from the "versus" data
     *                    source.
     */
    void setEntityCount(long entityCount);

    /**
     * Gets the number of records from the primary data source in the
     * entities described by the {@linkplain #getEntityCount() entityCount}.
     * <b>NOTE:</b> this is not the total number of records in those
     * entities, but only the count of those records from the primary
     * data source.
     * 
     * @return The number of records from the primary data source in the
     *         entities described by the {@linkplain #getEntityCount()
     *         entityCount}.
     */
    long getRecordCount();

    /**
     * Sets the number of records from the primary data source in the
     * entities described by the {@linkplain #getEntityCount() entityCount}.
     * <b>NOTE:</b> this is not the total number of records in those
     * entities, but only the count of those records from the primary
     * data source.
     * 
     * @param recordCount The number of records from the primary data
     *                    source in the entities described by the
     *                    {@linkplain #getEntityCount() entityCount}.
     */
    void setRecordCount(long recordCount);

    /**
     * A {@link ModelProvider} for instances of {@link SzMatchCounts}.
     */
    interface Provider extends ModelProvider<SzMatchCounts> {
        /**
         * Creates a new instance of {@link SzMatchCounts} with no 
         * associated match key or principle.
         *
         * @return The new instance of {@link SzMatchCounts}
         */
        SzMatchCounts create();

        /**
         * Creates an instance with the specified parameters.
         * 
         * @param matchKey The optionally associated match key, or 
         *                 <code>null</code> if no specific match key
         *                 is associated.
         * @param principle The optionally associated principle, or
         *                  <code>null</code> if no specific principle
         *                  is associated.
         * 
         * @return The newly created {@link SzMatchCounts} instance.
         */
        SzMatchCounts create(String matchKey, String principle);
    }

    /**
     * Provides a default {@link Provider} implementation for {@link SzMatchCounts}
     * that produces instances of {@link SzMatchCountsImpl}.
     */
    class DefaultProvider extends AbstractModelProvider<SzMatchCounts>
            implements Provider 
    {
        /**
         * Default constructor.
         */
        public DefaultProvider() {
            super(SzMatchCounts.class, SzMatchCountsImpl.class);
        }

        @Override
        public SzMatchCounts create() {
            return new SzMatchCountsImpl();
        }

        @Override
        public SzMatchCounts create(String matchKey, String principle) {
            return new SzMatchCountsImpl(matchKey, principle);
        }
    }

    /**
     * Provides a {@link ModelFactory} implementation for {@link SzMatchCounts}.
     */
    class Factory extends ModelFactory<SzMatchCounts, Provider> {
        /**
         * Default constructor. This is public and can only be called after the
         * singleton master instance is created as it inherits the same state from
         * the master instance.
         */
        public Factory() {
            super(SzMatchCounts.class);
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
         * Creates a new instance of {@link SzMatchCounts}.
         *
         * @return A new instance of {@link SzMatchCounts}.
         */
        public SzMatchCounts create() {
            return this.getProvider().create();
        }

        /**
         * Creates an instance with the specified parameters.
         * 
         * @param matchKey The optionally associated match key, or 
         *                 <code>null</code> if no specific match key
         *                 is associated.
         * @param principle The optionally associated principle, or
         *                  <code>null</code> if no specific principle
         *                  is associated.
         * 
         * @return The newly created {@link SzMatchCounts} instance.
         */
        public SzMatchCounts create(String matchKey, String principle)
        {
            return this.getProvider().create(matchKey, principle);
        }
    }

    /**
     * The {@link Factory} instance for this interface.
     */
    Factory FACTORY = new Factory(new DefaultProvider());
}
