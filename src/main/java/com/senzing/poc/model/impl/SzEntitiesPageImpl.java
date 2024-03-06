package com.senzing.poc.model.impl;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Collection;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzEntitiesPage;
import com.senzing.poc.model.SzEntity;
import com.senzing.poc.model.SzBoundType;

/**
 * Provides a default implementation of {@link SzEntitiesPage}.
 */
@JsonDeserialize
public class SzEntitiesPageImpl implements SzEntitiesPage {
  /**
   * The entity ID bound value that bounds the included entity ID's.
   */
  private long bound = 0L;

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
   * to be randmonly selected from the page of results.
   */
  private Integer sampleSize = null;

  /**
   * The page-minimum value that has been set.
   */
  private Long pageMinimumValue = null;

  /**
   * The page-maximum value that has been set.
   */
  private Long pageMaximumValue = null;

  /**
   * The total number of entities representing the set of all possible
   * results across all pages.
   */
  private long totalEntityCount = 0L;

  /**
   * The number of entities in the set that exist on pages before this
   * page.
   */
  private long beforePageCount = 0L;

  /**
   * The number of entities in the set that exist on pages after this
   * page.
   */
  private long afterPageCount = 0L;

  /**
   * The {@link Set} of {@link Long} entity ID's identifying the entities
   * on this page.
   */
  private SortedMap<Long, SzEntity> entities = null;

  /**
   * Default constructor
   */
  public SzEntitiesPageImpl() {
    this.bound              = 0L;
    this.boundType          = null;
    this.pageSize           = 0;
    this.sampleSize         = null;
    this.pageMinimumValue   = null;
    this.pageMaximumValue   = null;
    this.totalEntityCount   = 0L;
    this.beforePageCount    = 0L;
    this.afterPageCount     = 0L;
    this.entities           = new TreeMap<>();
  }

  @Override
  public long getBound() {
    return this.bound;
  }

  @Override
  public void setBound(long bound) {
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
  public Long getMinimumValue() {
    if (this.entities.size() == 0) return null;
    return this.entities.firstKey();
  }

  @Override
  public Long getMaximumValue() {
    if (this.entities.size() == 0) return null;
    return this.entities.lastKey();
  }

  @Override
  public Long getPageMinimumValue() {
    if (this.getSampleSize() == null && this.pageMinimumValue == null) {
      return this.getMinimumValue();
    }
    return this.pageMinimumValue;
  }

  @Override
  public void setPageMinimumValue(Long minValue) {
    this.pageMinimumValue = minValue;
  }

  @Override
  public Long getPageMaximumValue() {
    if (this.getSampleSize() == null && this.pageMaximumValue == null) {
      return this.getMaximumValue();
    }
    return this.pageMaximumValue;
  }

  @Override
  public void setPageMaximumValue(Long maxValue) {
    this.pageMaximumValue = maxValue;
  }

  @Override
  public long getTotalEntityCount() {
    return this.totalEntityCount;
  }

  @Override
  public void setTotalEntityCount(long entityCount) {
    this.totalEntityCount = entityCount;
  }

  @Override
  public long getBeforePageCount() {
    return this.beforePageCount;
  }

  @Override
  public void setBeforePageCount(long entityCount) {
    this. beforePageCount = entityCount;
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
  public List<SzEntity> getEntities() {
    return new ArrayList<>(this.entities.values());
  }

  @Override
  public void setEntities(Collection<SzEntity> entities) {
    this.entities.clear();
    if (entities != null) {
      entities.forEach( entity -> {
        if (entity != null) {
          this.entities.put(entity.getEntityId(), entity);
        }
      });
    }
  }

  @Override
  public void addEntity(SzEntity entity) {
    this.entities.put(entity.getEntityId(), entity);
  }

  @Override
  public void removeEntity(long entityId) {
    this.entities.remove(entityId);
  }

  @Override
  public void removeAllEntities() {
    this.entities.clear();
  }
}
