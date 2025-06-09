package com.senzing.poc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzEntityRelationsBreakdownImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes the number of entities in the entity repository at each
 * count statistics for the repository.
 */
@JsonDeserialize(using = SzEntityRelationsBreakdown.Factory.class)
public interface SzEntityRelationsBreakdown {
  /**
   * Gets the {@link List} of {@link SzEntityRelationsCount} describing the
   * number of entities having each distinct number of entity relations.
   * The returned value list should contain only one element for each
   * distinct number of entity relations that exists in the repository.
   * The {@link List} is returned in descending order of the number of
   * entity relations.
   * 
   * @return The {@link List} of {@link SzEntityRelationsCount} describing
   *         the number of entities having each distinct number of entity
   *         relations.
   */
  List<SzEntityRelationsCount> getEntityRelationsCounts();

  /**
   * Sets the {@link List} of {@link SzEntityRelationsCount} describing the
   * number of entities having each distinct number of entity relations.
   * This clears any existing entity relations counts before setting with
   * those specified. The specified {@link List} should contain only one
   * element for each distinct number of entity relations, but if duplicates
   * are encountered then later values in the {@link List} take precedence,
   * overwriting prior values from the {@link List}. Specifying a
   * <code>null</code> {@link List} is equivalent to specifying an empty
   * {@link List}.
   * 
   * @param relationsCountList The {@link List} of {@link SzEntityRelationsCount}
   *                           describing the number of entities having each
   *                           distinct number of entity relations.
   */
  void setEntityRelationsCounts(List<SzEntityRelationsCount> relationsCountList);

  /**
   * Adds the specified {@link SzEntityRelationsCount} describing the number of
   * entities in the entity repository having a specific number of entity
   * relations. If the specified {@link SzEntityRelationsCount} has the same
   * entity relations count as an existing {@link SzEntityRelationsCount}
   * instance then the specified value replaces the existing one for that number
   * of entity relations.
   * 
   * @param relationsCount The {@link SzEntityRelationsCount} describing the
   *                       number of entities in the entity repository having a
   *                       specific number of entity relations.
   */
  void addEntityRelationsCount(SzEntityRelationsCount relationsCount);

  /**
   * A {@link ModelProvider} for instances of {@link SzEntityRelationsBreakdown}.
   */
  interface Provider extends ModelProvider<SzEntityRelationsBreakdown> {
    /**
     * Creates a new instance of {@link SzEntityRelationsBreakdown}.
     * 
     * @return The new instance of {@link SzEntityRelationsBreakdown}
     */
    SzEntityRelationsBreakdown create();
  }

  /**
   * Provides a default {@link Provider} implementation for
   * {@link SzEntityRelationsBreakdown}
   * that produces instances of {@link SzEntityRelationsBreakdownImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzEntityRelationsBreakdown>
      implements Provider {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzEntityRelationsBreakdown.class, SzEntityRelationsBreakdownImpl.class);
    }

    @Override
    public SzEntityRelationsBreakdown create() {
      return new SzEntityRelationsBreakdownImpl();
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzEntityRelationsBreakdown}.
   */
  class Factory extends ModelFactory<SzEntityRelationsBreakdown, Provider> {
    /**
     * Default constructor. This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzEntityRelationsBreakdown.class);
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
     * Creates a new instance of {@link SzEntityRelationsBreakdown}.
     * 
     * @return The new instance of {@link SzEntityRelationsBreakdown}
     */
    public SzEntityRelationsBreakdown create() {
      return this.getProvider().create();
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
