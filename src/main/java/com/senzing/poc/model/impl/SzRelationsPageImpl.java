package com.senzing.poc.model.impl;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzRelationsPage;
import com.senzing.poc.model.SzBoundType;
import com.senzing.poc.model.SzRelation;

/**
 * Provides a default implementation of {@link SzRelationsPage}.
 */
@JsonDeserialize
public class SzRelationsPageImpl implements SzRelationsPage {
  /**
   * Provides a key for uniquely identifying relationships and
   * preventing duplication.
   */
  private class RelationKey implements Comparable<RelationKey> {
    /**
     * The first entity ID.
     */
    private long entityId;

    /**
     * The related entity ID.
     */
    private long relatedId;

    /**
     * Constructs with the two entity ID's.
     * 
     * @param entityId  The first entity ID.
     * @param relatedId The related entity ID.
     */
    private RelationKey(long entityId, long relatedId) {
      this.entityId = entityId;
      this.relatedId = relatedId;
    }

    /**
     * Gets the first entity ID of the relationship.
     * 
     * @return The first entity ID of the relationship.
     */
    public long getEntityId() {
      return this.entityId;
    }

    /**
     * Gets the related entity ID of the relationship.
     * 
     * @return The related entity ID of the relationship.
     */
    public long getRelatedId() {
      return this.relatedId;
    }

    /**
     * Implemented to return <code>true</code> if and only if the
     * specified parameter is a non-null reference to an object
     * of the same class with equivalent entity ID and related ID.
     * 
     * @param obj The object to compare with.
     * @return <code>true</code> if the objects are equal,
     *         otherwise <code>false</code>.
     */
    @Override
    public boolean equals(Object obj) {
      if (obj == null)
        return false;
      if (this == obj)
        return true;
      if (this.getClass() != obj.getClass())
        return false;
      RelationKey key = (RelationKey) obj;
      return this.getEntityId() == key.getEntityId()
          && this.getRelatedId() == key.getRelatedId();
    }

    /**
     * Implemented to return a hash code based on the entity ID and
     * the related entity ID.
     * 
     * @return The hash code for this instance.
     */
    @Override
    public int hashCode() {
      return Objects.hash(this.getEntityId(), this.getRelatedId());
    }

    /**
     * Implemented to compare by sorting first on entity ID and then
     * on related entity ID (both in ascending order) with <code>null</code>
     * values sorted before non-null values.
     * 
     * @return A negative number, zero (0) or a positive number
     *         depending on whether this instance compares less-than,
     *         equal-to or greater-than the specified instance,
     *         respectively.
     */
    @Override
    public int compareTo(RelationKey key) {
      if (key == null)
        return 1;
      long diff = this.getEntityId() - key.getEntityId();
      if (diff < 0L)
        return -1;
      if (diff > 0L)
        return 1;

      diff = this.getRelatedId() - key.getRelatedId();
      if (diff < 0L)
        return -1;
      if (diff > 0L)
        return 1;
      return 0;
    }

    /**
     * Implemented to format this instance as a relation bound
     * value with the entity ID followed by the related ID,
     * separated by a colon.
     * 
     * @return A relation bound value with the entity ID followed
     *         by the related ID, separated by a colon.
     */
    public String toString() {
      return this.getEntityId() + ":" + this.getRelatedId();
    }
  }

  /**
   * The relation bound value that bounds the included relations.
   */
  private String bound = null;

  /**
   * The {@link SzBoundType} that describes how the bound value
   * was applied.
   */
  private SzBoundType boundType = null;

  /**
   * The requested page size representing the maximum number of
   * entity ID's that were requested to be included in the page.
   */
  private int pageSize = 0;

  /**
   * The requested sample size representing the number of entity ID's
   * to be randomly selected from the page of results.
   */
  private Integer sampleSize = null;

  /**
   * The page-minimum value that has been set.
   */
  private String pageMinimumValue = null;

  /**
   * The page-maximum value that has been set.
   */
  private String pageMaximumValue = null;

  /**
   * The total number of relations representing the set of all possible
   * results across all pages.
   */
  private long totalRelationCount = 0L;

  /**
   * The number of relations in the set that exist on pages before this
   * page.
   */
  private long beforePageCount = 0L;

  /**
   * The number of relations in the set that exist on pages after this
   * page.
   */
  private long afterPageCount = 0L;

  /**
   * The {@link Map} of {@link String} encoded relationship entity ID's
   * keys identifying the related entities to {@link SzRelation} values
   * for those entities.
   */
  private SortedMap<RelationKey, SzRelation> relations = null;

  /**
   * Default constructor
   */
  public SzRelationsPageImpl() {
    this.bound = "0:0";
    this.boundType = null;
    this.pageSize = 0;
    this.sampleSize = null;
    this.pageMinimumValue = null;
    this.pageMaximumValue = null;
    this.totalRelationCount = 0L;
    this.beforePageCount = 0L;
    this.afterPageCount = 0L;
    this.relations = new TreeMap<>();
  }

  @Override
  public String getBound() {
    return this.bound;
  }

  @Override
  public void setBound(String bound) {
    this.bound = bound;
  }

  @Override
  public SzBoundType getBoundType() {
    return this.boundType;
  }

  @Override
  public void setBoundType(SzBoundType boundType) {
    this.boundType = boundType;
  }

  @Override
  public int getPageSize() {
    return this.pageSize;
  }

  @Override
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  @Override
  public Integer getSampleSize() {
    return this.sampleSize;
  }

  @Override
  public void setSampleSize(Integer sampleSize) {
    this.sampleSize = sampleSize;
  }

  @Override
  public String getMinimumValue() {
    if (this.relations.size() == 0)
      return null;
    RelationKey key = this.relations.firstKey();
    return key.toString();
  }

  @Override
  public String getMaximumValue() {
    if (this.relations.size() == 0)
      return null;
    RelationKey key = this.relations.lastKey();
    return key.toString();
  }

  @Override
  public String getPageMinimumValue() {
    if (this.getSampleSize() == null && this.pageMinimumValue == null) {
      return this.getMinimumValue();
    }
    return this.pageMinimumValue;
  }

  @Override
  public void setPageMinimumValue(String minValue) {
    this.pageMinimumValue = minValue;
  }

  @Override
  public String getPageMaximumValue() {
    if (this.getSampleSize() == null && this.pageMaximumValue == null) {
      return this.getMaximumValue();
    }
    return this.pageMaximumValue;
  }

  @Override
  public void setPageMaximumValue(String maxValue) {
    this.pageMaximumValue = maxValue;
  }

  @Override
  public long getTotalRelationCount() {
    return this.totalRelationCount;
  }

  @Override
  public void setTotalRelationCount(long relationCount) {
    this.totalRelationCount = relationCount;
  }

  @Override
  public long getBeforePageCount() {
    return this.beforePageCount;
  }

  @Override
  public void setBeforePageCount(long entityCount) {
    this.beforePageCount = entityCount;
  }

  @Override
  public long getAfterPageCount() {
    return this.afterPageCount;
  }

  @Override
  public void setAfterPageCount(long entityCount) {
    this.afterPageCount = entityCount;
  }

  @Override
  public List<SzRelation> getRelations() {
    List<SzRelation> relations = new ArrayList<>(this.relations.values());
    return relations;
  }

  @Override
  public void setRelations(Collection<SzRelation> relations) {
    this.relations.clear();
    if (relations != null) {
      relations.forEach(relation -> {
        if (relation != null) {
          long entityId = relation.getEntity().getEntityId();
          long relatedId = relation.getRelatedEntity().getEntityId();
          RelationKey key = new RelationKey(entityId, relatedId);

          this.relations.put(key, relation);
        }
      });
    }
  }

  @Override
  public void addRelation(SzRelation relation) {
    long entityId = relation.getEntity().getEntityId();
    long relatedId = relation.getRelatedEntity().getEntityId();
    RelationKey key = new RelationKey(entityId, relatedId);

    this.relations.put(key, relation);
  }

}
