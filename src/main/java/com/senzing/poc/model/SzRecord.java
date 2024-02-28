package com.senzing.poc.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzRecordImpl;

/**
 * Describes a relationship between two entities.
 */
@JsonDeserialize(using = SzRecord.Factory.class)
public interface SzRecord {
    /**
     * Gets the data source code identifying the data source 
     * from which the record was loaded.
     * 
     * @return The data source code identifying the data source 
     *         from which the record was loaded.
     */
    String getDataSource();

    /**
     * Gets the data source code identifying the data source 
     * from which the record was loaded.
     * 
     * @param dataSourceCode The data source code identifying the data
     *                       source from which the record was loaded.
     */
    void setDataSource(String dataSourceCode);

    /**
     * Gets the record ID that uniquely identifies the record within
     * the data source from which it was loaded.
     * 
     * @return The record ID that uniquely identifies the record
     *         within the data source from which it was loaded.
     */
    String getRecordId();

    /**
     * Sets the record ID that uniquely identifies the record within
     * the data source from which it was loaded.
     * 
     * @param recordId The record ID that uniquely identifies the record
     *                 within the data source from which it was loaded.
     */
    void setRecordId(String recordId);

    /**
     * Gets the optional match key describing why the record merged
     * into the entity to which it belongs.  This returns <code>null</code>
     * if this record belongs to a single-record entity or if it was the
     * inital record of the first multi-record entity to which it belonged
     * (even if it later re-resolved into a larger entity).
     * 
     * @return The optional match key describing why the record merged
     *         into the entity to which it belongs, or <code>null</code> 
     *         if this record belongs to a single-record entity or if it
     *         was the initial record of the first multi-record entity to
     *         which it belonged.
     */
    String getMatchKey();

    /**
     * Sets the optional match key describing why the record merged
     * into the entity to which it belongs.  Set this to <code>null</code>
     * if this record belongs to a single-record entity or if it was the
     * inital record of the first multi-record entity to which it belonged
     * (even if it later re-resolved into a larger entity).
     * 
     * @param matchKey The optional match key describing why the record
     *                 merged into the entity to which it belongs, or
     *                 <code>null</code> if this record belongs to a
     *                 single-record entity or if it was the initial
     *                 record of the first multi-record entity to which
     *                 it belonged.
     */
    void setMatchKey(String matchKey);

    /**
     * Gets the optional principle describing why the record merged
     * into the entity to which it belongs.  This returns <code>null</code>
     * if this record belongs to a single-record entity or if it was the
     * inital record of the first multi-record entity to which it belonged
     * (even if it later re-resolved into a larger entity).
     * 
     * @return The optional principle describing why the record merged
     *         into the entity to which it belongs, or <code>null</code> 
     *         if this record belongs to a single-record entity or if it
     *         was the initial record of the first multi-record entity to
     *         which it belonged.
     */
    String getPrinciple();

    /**
     * Sets the optional principle describing why the record merged
     * into the entity to which it belongs.  Set this to <code>null</code>
     * if this record belongs to a single-record entity or if it was the
     * inital record of the first multi-record entity to which it belonged
     * (even if it later re-resolved into a larger entity).
     * 
     * @param principle The optional principle describing why the record
     *                  merged into the entity to which it belongs, or
     *                  <code>null</code> if this record belongs to a
     *                  single-record entity or if it was the initial
     *                  record of the first multi-record entity to which
     *                  it belonged.
     */
    void setPrinciple(String principle);

    /**
     * A {@link ModelProvider} for instances of {@link SzRecord}.
     */
    interface Provider extends ModelProvider<SzRecord> {
        /**
         * Creates a new instance of {@link SzRecord}.
         *
         * @return The new instance of {@link SzRecord}
         */
        SzRecord create();

        /**
         * Creates an instance with the specified parameters.
         * 
         * @param dataSourceCode The data source code identifying the data
         *                       source from which the record was loaded.
         * @param recordId The record ID that uniquely identifies the record
         *                 within the data source from which it was loaded.
         * 
         * @return The newly created {@link SzRecord} instance.
         */
        SzRecord create(String dataSourceCode, String recordId);
    }

    /**
     * Provides a default {@link Provider} implementation for {@link SzRecord}
     * that produces instances of {@link SzRecordImpl}.
     */
    class DefaultProvider extends AbstractModelProvider<SzRecord>
            implements Provider {
        /**
         * Default constructor.
         */
        public DefaultProvider() {
            super(SzRecord.class, SzRecordImpl.class);
        }

        @Override
        public SzRecord create() {
            return new SzRecordImpl();
        }

        @Override
        public SzRecord create(String dataSourceCode, String recordId) {
            return new SzRecordImpl(dataSourceCode, recordId);
        }
    }

    /**
     * Provides a {@link ModelFactory} implementation for {@link SzRecord}.
     */
    class Factory extends ModelFactory<SzRecord, Provider> {
        /**
         * Default constructor. This is public and can only be called after the
         * singleton master instance is created as it inherits the same state from
         * the master instance.
         */
        public Factory() {
            super(SzRecord.class);
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
         * Creates a new instance of {@link SzRecord}.
         *
         * @return A new instance of {@link SzRecord}.
         */
        public SzRecord create() {
            return this.getProvider().create();
        }

        /**
         * Creates an instance with the specified parameters.
         * 
         * @param dataSourceCode The data source code identifying the data
         *                       source from which the record was loaded.
         * @param recordId The record ID that uniquely identifies the record
         *                 within the data source from which it was loaded.
         * 
         * @return The newly created {@link SzRecord} instance.
         */
        public SzRecord create(String dataSourceCode, String recordId)
        {
            return this.getProvider().create(dataSourceCode, recordId);
        }
    }

    /**
     * The {@link Factory} instance for this interface.
     */
    Factory FACTORY = new Factory(new DefaultProvider());

}
