package com.senzing.poc.model;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzRelationsPageImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes a count statistics for the repository.
 */
@JsonDeserialize(using = SzRelationsPage.Factory.class)
public interface SzRelationsPage {
  /**
   * Gets the relation bound value that bounds the included
   * relations.  The relationship bound value contains two (2)
   * entity ID values separated by a colon (e.g.: 
   * <code>1000:5005</code>).  The first entity ID value 
   * identifies the first entity in the relationship and the
   * second entity ID value identifies the related entity in
   * the relationship.
   *
   * @return The relations bound value that bounds the included
   *         relations.
   */
  String getBound();

  /**
   * Sets the relation bound value that bounds the included
   * relations.  The relationship bound value contains two (2)
   * entity ID values separated by a colon (e.g.: 
   * <code>1000:5005</code>).  The first entity ID value 
   * identifies the first entity in the relationship and the
   * second entity ID value identifies the related entity in
   * the relationship.
   *
   * @param bound The encoded relations bound value that bounds
   *              the included relations.
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
   * Gets requested page size representing the maximum number of
   * relations that were requested to be included in the page.
   * 
   * @return The requested page size representing the maximum number
   *         of relations that were requested to be included in 
   *         the page.
   */
  int getPageSize();

  /**
   * Sets requested page size representing the maximum number of
   * relations that were requested to be included in the page.
   * 
   * @param pageSize The requested page size representing the 
   *                 maximum number of relations that were
   *                 requested to be included in the page.
   */
  void setPageSize(int pageSize);

  /**
   * Gets requested sample size representing the number of relations
   * to be randmonly selected from the page of results.
   * 
   * @return The requested page size representing the number of relations
   *         to be randomly selected from the page of results.
   */
  @JsonInclude(NON_NULL)
  Integer getSampleSize();

  /**
   * Sets requested sample size representing the number of relations
   * to be randmonly selected from the page of results.
   * 
   * @param pageSize The requested sample size representing the number
   *                 of relations to be randmonly selected from the
   *                 page of results.
   */
  void setSampleSize(Integer sampleSize);

  /**
   * Gets the minimum relation value of the returned results.
   * This is encoded the same as the {@linkplain #getBound()
   * bound} value with two (2) entity ID values separated by 
   * a colon (e.g.: <code>1000:5005</code>).  The first
   * entity ID value identifies the least value of first
   * entity in the relationship and the second entity ID
   * value identifies the least value of those entity ID's
   * related to the first entity.  This returns 
   * <code>null</code> if there are no results.
   * 
   * @return The minimum relation value of the returned results,
   *         or <code>null</code> if there are no results.
   */
  @JsonInclude(NON_NULL)
  String getMinimumValue();

  /**
   * Gets the maximum relation value of the returned results.
   * This is encoded the same as the {@linkplain #getBound()
   * bound} value with two (2) entity ID values separated by 
   * a colon (e.g.: <code>1000:5005</code>).  The first
   * entity ID value identifies the greatest value of first
   * entity in the relationship and the second entity ID
   * value identifies the greatest value of those entity ID's
   * related to the first entity.  This returns 
   * <code>null</code> if there are no results.
   * 
   * @return The maximum relation value of the returned results,
   *         or <code>null</code> if there are no results.
   */
  @JsonInclude(NON_NULL)
  String getMaximumValue();

  /**
   * Gets the minimum relation value of the entire relations page.
   * This will be the same as {@linkplain #getMinimumValue() 
   * minimum value} if {@linkplain #getSampleSize() sample size} 
   * was not specified, however, if the sample size was specified
   * then this will be the minimum relation value of all the
   * candidate relations on the page that were used for random
   * sample selection even if that relation was not randomly
   * selected.  This is encoded the same as the {@linkplain
   * #getBound() bound} value with two (2) entity ID values
   * separated by a colon (e.g.: <code>1000:5005</code>).  The
   * first entity ID value identifies the least value of first
   * entity in the relationship and the second entity ID value
   * identifies the least value of those entity ID's related to
   * the first entity.  This returns <code>null</code> if there
   * are no results.
   * 
   * @return The minimum relation value of the entire relations
   *         page, or <code>null</code> if there are no results.
   */
  @JsonInclude(NON_NULL)
  String getPageMinimumValue();

  /**
   * Sets the minimum relation value of the entire relations page.
   * This should be the same as {@linkplain #getMinimumValue() 
   * minimum value} if {@linkplain #getSampleSize() sample size} 
   * was not specified, however, if the sample size was specified
   * then this will be the minimum relation value of all the
   * candidate relations on the page that were used for random
   * sample selection even if that relation was not randomly
   * selected.  This is encoded the same as the {@linkplain
   * #getBound() bound} value with two (2) entity ID values
   * separated by a colon (e.g.: <code>1000:5005</code>).  The
   * first entity ID value identifies the least value of the 
   * first entity in the relationship and the second entity ID
   * value identifies the least value of those entity ID's
   * related to the first entity.  Set this to <code>null</code>
   * if there are no results.
   * 
   * @param minValue The minimum relation value of the entire
   *                 relations page, or <code>null</code> if
   *                 there are no results.
   */
  void setPageMinimumValue(String minValue);

  /**
   * Gets the maximum relation value of the entire relations page.
   * This will be the same as {@linkplain #getMaximumValue() 
   * maximum value} if {@linkplain #getSampleSize() sample size} 
   * was not specified, however, if the sample size was specified
   * then this will be the maximum relation value of all the
   * candidate relations on the page that were used for random
   * sample selection even if that relation was not randomly
   * selected.  This is encoded the same as the {@linkplain
   * #getBound() bound} value with two (2) entity ID values
   * separated by a colon (e.g.: <code>1000:5005</code>).  The
   * first entity ID value identifies the greatest value of the 
   * first entity in the relationship and the second entity ID
   * value identifies the greatest value of those entity ID's
   * related to the first entity.  This returns <code>null</code>
   * if there are no results.
   * 
   * @return The maximum relation value of the entire relations
   *         page, or <code>null</code> if there are no results.
   */
  @JsonInclude(NON_NULL)
  String getPageMaximumValue();

  /**
   * Sets the maximum relation value of the entire relations page.
   * This should be the same as {@linkplain #getMaximumValue() 
   * maximum value} if {@linkplain #getSampleSize() sample size} 
   * was not specified, however, if the sample size was specified
   * then this should be the maximum relation value of all the
   * candidate relations on the page that were used for random
   * sample selection even if that relation was not randomly
   * selected.  This is encoded the same as the {@linkplain
   * #getBound() bound} value with two (2) entity ID values
   * separated by a colon (e.g.: <code>1000:5005</code>).  The
   * first entity ID value identifies the greatest value of the 
   * first entity in the relationship and the second entity ID
   * value identifies the greatest value of those entity ID's
   * related to the first entity.  Set this to <code>null</code>
   * if there are no results.
   * 
   * @param maxValue The maximum relation value of the entire
   *                 relations page, or <code>null</code> if
   *                 there are no results.
   */
  void setPageMaximumValue(String maxValue);

  /**
   * Gets the total number of relations in the set representing
   * the set of all possible results across all pages.
   *
   * @return The the total number of relations in the set
   *         representing the set of all possible results
   *         across all pages.
   */
  long getTotalRelationCount();

  /**
   * Sets the total number of relations in the set representing
   * the set of all possible results across all pages.
   *
   * @param count The the total number of relations in the set
   *              representing the set of all possible results
   *              across all pages.
   */
  void setTotalRelationCount(long count);

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
   * Gets the {@link List} of {@link SzRelation} instances desribing the
   * relationships for the page. The {@link SzRelation} instances will
   * be in ascending order of the first entity ID and then the second
   * related entity ID.
   * 
   * @return The {@link List} of {@link SzRelation} instances describing
   *         the relationships for the page.
   */
  List<SzRelation> getRelations();

  /**
   * Sets the {@link List} of {@link SzRelation} instances desribing the
   * relationships for the page. The {@link SzRelation} instances will
   * be in ascending order of the first entity ID and then the second
   * related entity ID.
   * 
   * @param relations The {@link List} of {@link SzRelation} instances
   *                  describing
   *         the relationships for the page.
   */
  void setRelations(Collection<SzRelation> relations);

  /**
   * Adds the specified {@link SzRelation} to the list of relations
   * for this page.  If an {@link SzRelation} is already included for
   * the same entity ID and related ID then the specified {@link
   * SzRelation} replaces the already existing one.
   * 
   * @param relation The {@link SzRelation} to add to the list of 
   *                 relations entities for this page.
   */
  void addRelation(SzRelation entityId);

  /**
   * A {@link ModelProvider} for instances of {@link SzRelationsPage}.
   */
  interface Provider extends ModelProvider<SzRelationsPage> {
    /**
     * Creates a new instance of {@link SzRelationsPage}.
     * 
     * @return The new instance of {@link SzRelationsPage}
     */
    SzRelationsPage create();
  }

  /**
   * Provides a default {@link Provider} implementation for {@link SzRelationsPage}
   * that produces instances of {@link SzRelationsPageImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzRelationsPage>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzRelationsPage.class, SzRelationsPageImpl.class);
    }

    @Override
    public SzRelationsPage create() {
      return new SzRelationsPageImpl();
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzRelationsPage}.
   */
  class Factory extends ModelFactory<SzRelationsPage, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzRelationsPage.class);
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
     * Creates a new instance of {@link SzRelationsPage}.
     * 
     * @return The new instance of {@link SzRelationsPage}
     */
    public SzRelationsPage create()
    {
      return this.getProvider().create();
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
