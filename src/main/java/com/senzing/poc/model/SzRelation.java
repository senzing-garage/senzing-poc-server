package com.senzing.poc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzRelationImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes a relationship between two entities.
 */
@JsonDeserialize(using = SzRelation.Factory.class)
public interface SzRelation {
    /**
     * Gets the {@link SzEntity} describing the first entity in 
     * the relationship.
     * 
     * @return The {@link SzEntity} describing the first entity in
     *         the relationship.
     */
    SzEntity getEntity();

    /**
     * Sets the {@link SzEntity} describing the first entity in
     * the relationship.
     * 
     * @param entity The {@link SzEntity} describing the first entity in
     *               the relationship.
     */
    void setEntity(SzEntity entity);

    /**
     * Gets the {@link SzEntity} describing the second entity in 
     * the relationship.
     * 
     * @return The {@link SzEntity} describing the second entity in
     *         the relationship.
     */
    SzEntity getRelatedEntity();

    /**
     * Sets the {@link SzEntity} describing the second entity in
     * the relationship.
     * 
     * @param entity The {@link SzEntity} describing the second entity
     *               in the relationship.
     */
    void setRelatedEntity(SzEntity related);

    /**
     * Gets the {@link SzMatchType} describing the relationship type for
     * the relation.
     * 
     * @return The {@link SzMatchType} describing the relationship type for
     *         the relation.
     */
    @JsonInclude(NON_NULL)
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
    @JsonInclude(NON_NULL)
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
    @JsonInclude(NON_NULL)
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
    }

    /**
     * The {@link Factory} instance for this interface.
     */
    Factory FACTORY = new Factory(new DefaultProvider());

}
