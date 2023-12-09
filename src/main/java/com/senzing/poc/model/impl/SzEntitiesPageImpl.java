package com.senzing.poc.model.impl;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
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
   * entity ID's that were requested to be returned.
   */
  private int pageSize = 0;

  /**
   * The total number of entities representing the set of all possible
   * results across all pages.
   */
  private long totalEntityCount = 0L;

  /**
   * The number of entities in the set that exist on pages before this
   * page.
   */
  private long beforeEntityCount = 0L;

  /**
   * The number of entities in the set that exist on pages after this
   * page.
   */
  private long afterEntityCount = 0L;

  /**
   * The {@link Set} of {@link Long} entity ID's identifying the entities
   * on this page.
   */
  private Set<Long> entityIds = null;

  /**
   * Default constructor
   */
  public SzEntitiesPageImpl() {
    this.bound              = 0L;
    this.boundType          = null;
    this.pageSize           = 0;
    this.totalEntityCount   = 0L;
    this.beforeEntityCount  = 0L;
    this.afterEntityCount   = 0L;
    this.entityIds          = new HashSet<>();
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
  public long getTotalEntityCount() {
    return this.totalEntityCount;
  }

  @Override
  public void setTotalEntityCount(long entityCount) {
    this.totalEntityCount = entityCount;
  }

  @Override
  public long getBeforeEntityCount() {
    return this.beforeEntityCount;
  }

  @Override
  public void setBeforeEntityCount(long entityCount) {
    this.beforeEntityCount = entityCount;
  }


  @Override
  public long getAfterEntityCount() {
    return this.afterEntityCount;
  }

  @Override
  public void setAfterEntityCount(long entityCount) {
    this.afterEntityCount = entityCount;
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
