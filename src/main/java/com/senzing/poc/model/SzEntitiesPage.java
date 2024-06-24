package com.senzing.poc.model;

import java.util.List;
import java.util.Collection;

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
   * entity ID's.  This will return an integer {@link String}
   * or <code>"max"</code> to indicate the maximum legal value
   * for an entity ID.
   *
   * @return The entity ID bound value that bounds the included
   *         entity ID's.
   */
  String getBound();

  /**
   * Sets the entity ID bound value that bounds the included
   * entity ID's.  Set to an integer {@link String} or
   * <code>"max"</code> to indicate the maximum legal value
   * for an entity ID.
   *
   * @param bound The entity ID bound value that bounds the
   *              included entity ID's.
   */
  void setBound(String bound);

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
   * Gets the requested page size representing the maximum number of
   * entity ID's that were requested to be included in the page.
   * 
   * @return The requested page size representing the maximum number
   *         of entity ID's that were requested to be included in 
   *         the page.
   */
  int getPageSize();

  /**
   * Sets the requested page size representing the maximum number of
   * entity ID's that were requested to be included in the page.
   * 
   * @param pageSize The requested page size representing the 
   *                 maximum number of entity ID's that were
   *                 requested to be included in the page.
   */
  void setPageSize(int pageSize);

  /**
   * Gets the requested sample size representing the number of entity ID's
   * to be randmonly selected from the page of results.
   * 
   * @return The requested page size representing the number of entity ID's
   *         to be randomly selected from the page of results.
   */
  @JsonInclude(NON_NULL)
  Integer getSampleSize();

  /**
   * Sets the requested sample size representing the number of entity ID's
   * to be randmonly selected from the page of results.
   * 
   * @param pageSize The requested sample size representing the number
   *                 of entity ID's to be randmonly selected from the
   *                 page of results.
   */
  void setSampleSize(Integer sampleSize);

  /**
   * Gets the minimum entity ID of the returned results.
   * This returns <code>null</code> if there are no results.
   * 
   * @return The minimum entity ID of the returned results,
   *         or <code>null</code> if there are no results.
   */
  @JsonInclude(NON_NULL)
  Long getMinimumValue();

  /**
   * Gets the maximum entity ID of the returned results.
   * This returns <code>null</code> if there are no results.
   * 
   * @return The maximum entity ID of the returned results,
   *         or <code>null</code> if there are no results.
   */
  @JsonInclude(NON_NULL)
  Long getMaximumValue();

  /**
   * Gets the minimum entity ID of the entire entity page.
   * This will be the same as the {@linkplain #getMinimumValue() 
   * minimum value} the {@linkplain #getSampleSize() sample size}
   * was not specified, however, if sample size was specified then
   * this will be the minimum entity ID value of all the candidate
   * entities on the page that were used for random sample selection
   * even if that entity was not randomly selected.  This returns
   * <code>null</code> if there are no results.
   * 
   * @return The minimum entity ID of the entire entity page, or
   *         <code>null</code> if there are no results.
   */
  @JsonInclude(NON_NULL)
  Long getPageMinimumValue();

  /**
   * Sets the minimum entity ID of the entire entity page.
   * This will be the same as the {@linkplain #getMinimumValue() 
   * minimum value} the {@linkplain #getSampleSize() sample size}
   * was not specified, however, if sample size was specified then
   * this will be the minimum entity ID value of all the candidate
   * entities on the page that were used for random sample selection
   * even if that entity was not randomly selected.  Set this to
   * <code>null</code> if there are no results.
   * 
   * @param minValue The minimum entity ID of the entire entity page,
   *                 or <code>null</code> if there are no results.
   */
  void setPageMinimumValue(Long minValue);

  /**
   * Gets the maximum entity ID of the entire entity page.
   * This will be the same as the {@linkplain #getMaximumValue() 
   * maximum value} the {@linkplain #getSampleSize() sample size}
   * was not specified, however, if sample size was specified then
   * this will be the maximum entity ID value of all the candidate
   * entities on the page that were used for random sample selection
   * even if that entity was not randomly selected.  This returns
   * <code>null</code> if there are no results.
   * 
   * @return The maximum entity ID of the entire entity page, or
   *         <code>null</code> if there are no results.
   */
  @JsonInclude(NON_NULL)
  Long getPageMaximumValue();

  /**
   * Sets the maximum entity ID of the entire entity page.
   * This will be the same as the {@linkplain #getMaximumValue() 
   * maximum value} the {@linkplain #getSampleSize() sample size}
   * was not specified, however, if sample size was specified then
   * this will be the maximum entity ID value of all the candidate
   * entities on the page that were used for random sample selection
   * even if that entity was not randomly selected.  Set this to
   * <code>null</code> if there are no results.
   * 
   * @param maxValue The maximum entity ID of the entire entity page,
   *                 or <code>null</code> if there are no results.
   */
  void setPageMaximumValue(Long maxValue);

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
  long getBeforePageCount();

  /**
   * Sets the number of entities in the set that exist on
   * pages that occur before this page.
   * 
   * @param count The the number of entities in the set that 
   *              exist on pages that occur before this page.
   */
  void setBeforePageCount(long count);

  /**
   * Gets the number of entities in the set that exist on
   * pages that occur after this page.
   * 
   * @return The the number of entities in the set that exist
   *         on pages that occur after this page.
   */
  long getAfterPageCount();

  /**
   * Sets the number of entities in the set that exist on
   * pages that occur after this page.
   * 
   * @param count The the number of entities in the set that 
   *              exist on pages that occur after this page.
   */
  void setAfterPageCount(long count);

  /**
   * Gets the {@link List} of {@link SzEntity} instances describing the 
   * entities in this page of results.  The returned {@link List} will
   * be in ascending order of entity ID.
   * 
   * @return The {@link List} of {@link SzEntity} instances describing
   *         the entities in this page of results (in ascending order).
   */
  List<SzEntity> getEntities();

  /**
   * Sets the {@link List} of {@link SzEntity} instances describing the 
   * entities in this page of results.  The entities will be sorted in
   * ascending order of entity ID and deduplicated when added with 
   * entities with duplicate entity ID's being replaced by the one that
   * occurs last in the specified {@link Collection}. 
   * 
   * @param entityIdList The {@link List} of {@link SzEntity} instances
   *                     describing the entities in this page of results.
    */
  void setEntities(Collection<SzEntity> entities);

  /**
   * Adds the specified {@link SzEntity} to the list of entities for this
   * page.  If an entity with the same entity ID already exists on the 
   * page then the specified one replaces the existing one.
   * 
   * @param entity The {@link SzEntity} to add the page of entities.
   */
  void addEntity(SzEntity entity);

  /**
   * Removes the entity with the specified entity ID from the list of
   * entities associated with this instance.  If no entity has the
   * specified entity ID then this method has no effect.
   * 
   * @param entityId The entity ID of the entity to be removed.
   */
  void removeEntity(long entityId);

  /**
   * Removes all entities from the page of entities.
   */
  void removeAllEntities();

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
