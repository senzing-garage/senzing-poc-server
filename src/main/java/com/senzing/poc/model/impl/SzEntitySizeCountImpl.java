package com.senzing.poc.model.impl;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzEntitySizeCount;
import com.senzing.poc.model.SzSourceCountStats;

/**
 * Provides a default implementation of {@link SzEntitySizeCount}.
 */
@JsonDeserialize
public class SzEntitySizeCountImpl implements SzEntitySizeCount {
  /**
   * The entity size described by the number of records in the entities.
   */
  private int entitySize = 0;

  /**
   * The number of entities having an entity size equal to the associated
   * record count.
   */
  private long entityCount = 0L;

  /**
   * Default constructor
   */
  public SzEntitySizeCountImpl() {
    this.entitySize   = 0;
    this.entityCount  = 0L;
  }

  @Override
  public int getEntitySize() {
    return this.entitySize;
  }

  @Override
  public void setEntitySize(int recordCount) {
    this.entitySize = recordCount;
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
