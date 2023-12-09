package com.senzing.poc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzEntitiesPageImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes a count statistics for the repository.
 */
@JsonDeserialize(using = SzEntitiesPage.Factory.class)
public interface SzEntitiesPage {
  /**
   * Gets the entity ID bound value that bounds the included
   * entity ID's.
   *
   * @return The entity ID bound value that bounds the included
   *         entity ID's.
   */
  long getBound();

  /**
   * Sets the entity ID bound value that bounds the included
   * entity ID's.
   *
   * @param bound The entity ID bound value that bounds the
   *              included entity ID's.
   */
  void setBound(long bound);

  /**
   * Gets the {@link SzBoundType} that describes how the associated
   * {@linkplain #getBound() bound value} was applied.
   *
   * @return The the {@link SzBoundType} that describes how the
   *         associated {@linkplain #getBound() bound value} was
   *         applied.
   */
  SzBoundType getBoundType();

  /**
   * Sets the {@link SzBoundType} that describes how the associated
   * {@linkplain #getBound() bound value} was applied.
   *
   * @param boundType The the {@link SzBoundType} that describes how
   *                  the associated {@linkplain #getBound() bound 
   *                  value} was applied.
   */
  void setBoundType(SzBoundType boundType);

  /**
   * Gets requested page size representing the maximum number of
   * entity ID's that were requested to be returned.
   * 
   * @return The equested page size representing the maximum number
   *         of entity ID's that were requested to be returned.
   */
  int getPageSize();

  /**
   * Sets requested page size representing the maximum number of
   * entity ID's that were requested to be returned.
   * 
   * @param pageSize The equested page size representing the 
   *                 maximum number of entity ID's that were
   *                 requested to be returned.
   */
  void setPageSize(int pageSize);

  /**
   * Gets the total number of entities in the set representing
   * the set of all possible results across all pages.
   *
   * @return The the total number of entities in the set
   *         representing the set of all possible results
   *         across all pages.
   */
  long getTotalEntityCount();

  /**
   * Sets the total number of entities in the set representing
   * the set of all possible results across all pages.
   *
   * @param count The the total number of entities in the set
   *              representing the set of all possible results
   *              across all pages.
   */
  void setTotalEntityCount(long count);

  /**
   * Gets the number of entities in the set that exist on
   * pages that occur before this page.
   * 
   * @return The the number of entities in the set that exist
   *         on pages that occur before this page.
   */
  long getBeforeEntityCount();

  /**
   * Sets the number of entities in the set that exist on
   * pages that occur before this page.
   * 
   * @param count The the number of entities in the set that 
   *              exist on pages that occur before this page.
   */
  void setBeforeEntityCount(long count);

  /**
   * Gets the number of entities in the set that exist on
   * pages that occur after this page.
   * 
   * @return The the number of entities in the set that exist
   *         on pages that occur after this page.
   */
  long getAfterEntityCount();

  /**
   * Sets the number of entities in the set that exist on
   * pages that occur after this page.
   * 
   * @param count The the number of entities in the set that 
   *              exist on pages that occur after this page.
   */
  void setAfterEntityCount(long count);

  /**
   * Gets the {@link List} of {@link Long} entity Id's identifying the 
   * entities in this page of results.  The returned {@link List} will
   * be in ascending order.
   * 
   * @return The {@link List} of {@link Long} entity Id's identifying
   *         the entities in this page of results (in ascending order).
   */
  List<Long> getEntityIds();

  /**
   * Sets the {@link List} of {@link Long} entity Id's identifying the 
   * entities in this page of results.  The entity IDs will be sorted
   * and deduplicated when added to this instance.
   * 
   * @param entityIdList The {@link List} of {@link Long} entity Id's 
   *                     identifying the entities in this page of results.
    */
  void setEntityIds(List<Long> entityIdList);

  /**
   * Adds the specified entity ID to the list of entities for this page.
   * If the specified entity ID is already included, then this method
   * has no effect.
   * 
   * @param entityId The entity ID to add to the list of entities for 
   *                 this page.
   */
  void addEntityId(long entityId);

  /**
   * A {@link ModelProvider} for instances of {@link SzEntitiesPage}.
   */
  interface Provider extends ModelProvider<SzEntitiesPage> {
    /**
     * Creates a new instance of {@link SzEntitiesPage}.
     * 
     * @return The new instance of {@link SzEntitiesPage}
     */
    SzEntitiesPage create();
  }

  /**
   * Provides a default {@link Provider} implementation for {@link SzEntitiesPage}
   * that produces instances of {@link SzEntitiesPageImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzEntitiesPage>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzEntitiesPage.class, SzEntitiesPageImpl.class);
    }

    @Override
    public SzEntitiesPage create() {
      return new SzEntitiesPageImpl();
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzEntitiesPage}.
   */
  class Factory extends ModelFactory<SzEntitiesPage, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzEntitiesPage.class);
    }

    /**
     * Constructs with the default provider.  This constructor is private and
     * is used for the master singleton instance.
     * @param defaultProvider The default provider.
     */
    private Factory(Provider defaultProvider) {
      super(defaultProvider);
    }

    /**
     * Creates a new instance of {@link SzEntitiesPage}.
     * 
     * @return The new instance of {@link SzEntitiesPage}
     */
    public SzEntitiesPage create()
    {
      return this.getProvider().create();
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
