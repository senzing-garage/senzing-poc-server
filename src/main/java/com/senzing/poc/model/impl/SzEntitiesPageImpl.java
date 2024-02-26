package com.senzing.poc.model.impl;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;
import java.util.Set;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzEntitiesPage;
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
  private SortedSet<Long> entityIds = null;

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
    this.entityIds          = new TreeSet<>();
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
    return this.entityIds.first();
  }

  @Override
  public Long getMaximumValue() {
    return this.entityIds.last();
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
  public List<Long> getEntityIds() {
    List<Long> ids = new ArrayList<>(this.entityIds);
    ids.sort(null);
    return ids;
  }

  @Override
  public void setEntityIds(List<Long> entityIdList) {
    this.entityIds.clear();
    if (entityIdList != null) {
      entityIdList.forEach( entityId -> {
        if (entityId != null) {
          this.entityIds.add(entityId);
        }
      });
    }
  }

  @Override
  public void addEntityId(long entityId) {
    if (this.entityIds.contains(entityId)) return;
    this.entityIds.add(entityId);
  }

}
