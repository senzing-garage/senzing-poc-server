package com.senzing.poc.model.impl;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzEntitySizeBreakdown;
import com.senzing.poc.model.SzEntitySizeCount;

/**
 * Provides a default implementation of {@link SzEntitySizeBreakdown}.
 */
@JsonDeserialize
public class SzEntitySizeBreakdownImpl implements SzEntitySizeBreakdown {
  /**
   * The {@link Map} of {@link Integer} entity size keys to {@link
   * SzEntitySizeCount} values describing the number of entities in
   * the entity repository having that number of constituent records.
   */
  private Map<Integer, SzEntitySizeCount> entitySizeCounts = null;

  /**
   * Default constructor
   */
  public SzEntitySizeBreakdownImpl() {
    this.entitySizeCounts = new LinkedHashMap<>();
  }

  @Override
  public List<SzEntitySizeCount> getEntitySizeCounts() {
    Collection<SzEntitySizeCount> sizeCounts = this.entitySizeCounts.values();
    List<SzEntitySizeCount> result = new ArrayList<>(sizeCounts);
    result.sort((c1, c2) -> {
      return c2.getEntitySize() - c1.getEntitySize();
    });
    return result;
  }

  @Override
  public void setEntitySizeCounts(List<SzEntitySizeCount> sizeCountList) {
    this.entitySizeCounts.clear();
    if (sizeCountList != null) {
        sizeCountList.forEach( sizeCount -> {
            if (sizeCount != null) {
                this.entitySizeCounts.put(
                    sizeCount.getEntitySize(), sizeCount);
            }
        });
    }
  }

  @Override
  public void addEntitySizeCount(SzEntitySizeCount sizeCount) {
    if (sizeCount == null) return;
    this.entitySizeCounts.put(sizeCount.getEntitySize(), sizeCount);
  }

}
