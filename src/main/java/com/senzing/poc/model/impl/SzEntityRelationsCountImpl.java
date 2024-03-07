package com.senzing.poc.model.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzEntityRelationsCount;

/**
 * Provides a default implementation of {@link SzEntityRelationsCount}.
 */
@JsonDeserialize
public class SzEntityRelationsCountImpl implements SzEntityRelationsCount {
  /**
   * The number of entity relations that the entities have.
   */
  private int relationsCount = 0;

  /**
   * The number of entities having the associated number of entity relations.
   */
  private long entityCount = 0L;

  /**
   * Default constructor
   */
  public SzEntityRelationsCountImpl() {
    this.relationsCount = 0;
    this.entityCount    = 0L;
  }

  @Override
  public int getRelationsCount() {
    return this.relationsCount;
  }

  @Override
  public void setRelationsCount(int relationsCount) {
    this.relationsCount = relationsCount;
  }

  @Override
  public long getEntityCount() {
    return this.entityCount;
  }

  @Override
  public void setEntityCount(long entityCount) {
    this.entityCount = entityCount;
  }
}
