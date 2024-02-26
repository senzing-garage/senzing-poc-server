package com.senzing.poc.model;

import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzRelationCountsImpl;

/**
 * Describes the entity, record and relationship counts for the
 * respective relation type for entities having at least one record
 * from the primary data source to entities having at least one
 * record from the "versus" data source.
 */
public interface SzRelationCounts {
    /**
     * The number of entities having at least one record from the 
     * primary data source related by a relationship of the respective
     * relationship type to an entity with at least one record from
     * the "versus" data source.
     * 
     * @return The number of entities having at least one record from
     *         the primary data source related by a relationship of
     *         the respective relationship type to an entity with at
     *         least one record from the "versus" data source.
     */
    long getEntityCount();

    /**
     * Sets the number of entities having at least one record from the 
     * primary data source related by a relationship of the respective
     * relationship type to an entity with at least one record from
     * the "versus" data source.
     * 
     * @param entityCount The number of entities having at least one
     *                    record from the primary data source related
     *                    by a relationship of the respective
     *                    relationship type to an entity with at least
     *                    one record from the "versus" data source.
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
     * Gets the number of relationships of the respective relationship
     * type between entities having at least one record from the primary
     * data source and entities having at least one record from the
     * "versus" data source.
     *
     * @return The number of relationships of the respective relationship
     *         type between entities having at least one record from the
     *         primary data source and entities having at least one record
     *         from the "versus" data source.
     */
    long getRelationCount();

    /**
     * Sets the number of relationships of the respective relationship
     * type between entities having at least one record from the primary
     * data source and entities having at least one record from the
     * "versus" data source.
     *
     * @param relationCount The number of relationships of the respective
     *                      relationship type between entities having at
     *                      least one record from the primary data source
     *                      and entities having at least one record from
     *                      the "versus" data source.
     */
    void setRelationCount(long relationCount);

    /**
     * A {@link ModelProvider} for instances of {@link SzRelationCounts}.
     */
    interface Provider extends ModelProvider<SzRelationCounts> {
        /**
         * Creates a new instance of {@link SzRelationCounts}.
         *
         * @return The new instance of {@link SzRelationCounts}
         */
        SzRelationCounts create();

        /**
         * Creates an instance with the specified parameters.
         * 
         * @param entityCount The number of entities associated with the statistic.
         * @param recordCount The number of records associated with the statistic.
         * @param relationCount The number of relations asscociated with the statistic.
         * 
         * @return The newly created {@link SzRelationCounts} instance.
         */
        SzRelationCounts create(long entityCount,
                                long recordCount,
                                long relationCount);
    }

    /**
     * Provides a default {@link Provider} implementation for {@link SzRelationCounts}
     * that produces instances of {@link SzRelationCountsImpl}.
     */
    class DefaultProvider extends AbstractModelProvider<SzRelationCounts>
            implements Provider 
    {
        /**
         * Default constructor.
         */
        public DefaultProvider() {
            super(SzRelationCounts.class, SzRelationCountsImpl.class);
        }

        @Override
        public SzRelationCounts create() {
            return new SzRelationCountsImpl();
        }

        @Override
        public SzRelationCounts create(long entityCount, 
                                       long recordCount,
                                       long relationCount) 
        {
            return new SzRelationCountsImpl(entityCount, recordCount, relationCount);
        }
    }

    /**
     * Provides a {@link ModelFactory} implementation for {@link SzRelationCounts}.
     */
    class Factory extends ModelFactory<SzRelationCounts, Provider> {
        /**
         * Default constructor. This is public and can only be called after the
         * singleton master instance is created as it inherits the same state from
         * the master instance.
         */
        public Factory() {
            super(SzRelationCounts.class);
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
         * Creates a new instance of {@link SzRelationCounts}.
         *
         * @return A new instance of {@link SzRelationCounts}.
         */
        public SzRelationCounts create() {
            return this.getProvider().create();
        }

        /**
         * Creates an instance with the specified parameters.
         * 
         * @param entityCount The number of entities associated with the statistic.
         * @param recordCount The number of records associated with the statistic.
         * @param relationCount The number of relations asscociated with the statistic.
         * 
         * @return The newly created {@link SzRelationCounts} instance.
         */
        public SzRelationCounts create(long entityCount, 
                                       long recordCount,
                                       long relationCount)
        {
            return this.getProvider().create(entityCount, recordCount, relationCount);
        }
    }

    /**
     * The {@link Factory} instance for this interface.
     */
    Factory FACTORY = new Factory(new DefaultProvider());
}
