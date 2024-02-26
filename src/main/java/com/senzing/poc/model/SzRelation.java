package com.senzing.poc.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzRelationImpl;

/**
 * Describes a relationship between two entities.
 */
@JsonDeserialize(using = SzRelation.Factory.class)
public interface SzRelation {
    /**
     * Gets the first entity ID for the relation.
     * 
     * @return The first entity ID for the relation.
     */
    long getEntityId();

    /**
     * Sets the first entity ID for the relation.
     * 
     * @param entityId The first entity ID for the relation.
     */
    void setEntityId(long entityId);

    /**
     * Gets the second entity ID (the related ID) for the relation.
     * 
     * @return The second entity ID (the related ID) for the relation.
     */
    long getRelatedId();

    /**
     * Sets the second entity ID (the related ID) for the relation.
     * 
     * @param relatedId The second entity ID (the related ID) for the relation.
     */
    void setRelatedId(long relatedId);

    /**
     * Gets the {@link SzMatchType} describing the relationship type for
     * the relation.
     * 
     * @return The {@link SzMatchType} describing the relationship type for
     *         the relation.
     */
    SzMatchType getMatchType();

    /**
     * Sets the {@link SzMatchType} describing the relationship type for the
     * relation.
     * 
     * @param matchType The {@link SzMatchType} describing the relationship type for
     *                  the relation.
     */
    void setMatchType(SzMatchType matchType);

    /**
     * Gets the match key for the relation.
     * 
     * @return The match key for the relation.
     */
    String getMatchKey();

    /**
     * Sets the match key for the relation.
     * 
     * @param matchKey The match key for the relation.
     */
    void setMatchKey(String matchKey);

    /**
     * Gets the principle for the relation.
     * 
     * @return The principle for the relation.
     */
    String getPrinciple();

    /**
     * Sets the principle for the relation.
     * 
     * @param principle The principle for the relation.
     */
    void setPrinciple(String principle);

    /**
     * A {@link ModelProvider} for instances of {@link SzRelation}.
     */
    interface Provider extends ModelProvider<SzRelation> {
        /**
         * Creates a new instance of {@link SzRelation}.
         *
         * @return The new instance of {@link SzRelation}
         */
        SzRelation create();

        /**
         * Creates an instance with the specified parameters.
         * 
         * @param entityId  The first entity ID for the relation.
         * @param relatedId The second entity ID (the related ID) for the relation.
         * @param matchType The {@link SzMatchType} for the relation.
         * @param matchKey  The match key for the relation.
         * @param principle The principle for the relation.
         * 
         * @return The newly created {@link SzRelation} instance.
         */
        SzRelation create(long          entityId,
                          long          relatedId,
                          SzMatchType   matchType,
                          String        matchKey,
                          String        principle);

    }

    /**
     * Provides a default {@link Provider} implementation for {@link SzRelation}
     * that produces instances of {@link SzRelationImpl}.
     */
    class DefaultProvider extends AbstractModelProvider<SzRelation>
            implements Provider {
        /**
         * Default constructor.
         */
        public DefaultProvider() {
            super(SzRelation.class, SzRelationImpl.class);
        }

        @Override
        public SzRelation create() {
            return new SzRelationImpl();
        }

        @Override
        public SzRelation create(long entityId,
                long relatedId,
                SzMatchType matchType,
                String matchKey,
                String principle) {
            return new SzRelationImpl(
                    entityId, relatedId, matchType, matchKey, principle);
        }
    }

    /**
     * Provides a {@link ModelFactory} implementation for {@link SzRelation}.
     */
    class Factory extends ModelFactory<SzRelation, Provider> {
        /**
         * Default constructor. This is public and can only be called after the
         * singleton master instance is created as it inherits the same state from
         * the master instance.
         */
        public Factory() {
            super(SzRelation.class);
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
         * Creates a new instance of {@link SzRelation}.
         *
         * @return A new instance of {@link SzRelation}.
         */
        public SzRelation create() {
            return this.getProvider().create();
        }

        /**
         * Creates an instance with the specified parameters.
         * 
         * @param entityId  The first entity ID for the relation.
         * @param relatedId The second entity ID (the related ID) for the relation.
         * @param matchType The {@link SzMatchType} for the relation.
         * @param matchKey  The match key for the relation.
         * @param principle The principle for the relation.
         * 
         * @return The newly created {@link SzRelation} instance.
         */
        public SzRelation create(long           entityId,
                                 long           relatedId,
                                 SzMatchType    matchType,
                                 String         matchKey,
                                 String         principle) 
        {
            return this.getProvider().create(
                    entityId, relatedId, matchType, matchKey, principle);
        }
    }

    /**
     * The {@link Factory} instance for this interface.
     */
    Factory FACTORY = new Factory(new DefaultProvider());

}
