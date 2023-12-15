package com.senzing.poc.model.impl;

import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzEntityRelationsBreakdown;
import com.senzing.poc.model.SzEntityRelationsCount;

/**
 * Provides a default implementation of {@link SzEntityRelationsBreakdown}.
 */
@JsonDeserialize
public class SzEntityRelationsBreakdownImpl implements SzEntityRelationsBreakdown {
  /**
   * The {@link Map} of {@link Integer} keys indicating the number of
   * entity relations to {@link SzEntityRelationsCount} values describing
   * the number of entities in the entity repository having that number 
   * entity relations.
   */
  private Map<Integer, SzEntityRelationsCount> relationsCounts = null;

  /**
   * Default constructor
   */
  public SzEntityRelationsBreakdownImpl() {
    this.relationsCounts = new LinkedHashMap<>();
  }

  @Override
  public List<SzEntityRelationsCount> getEntityRelationsCounts() {
    Collection<SzEntityRelationsCount> relationsCounts = this.relationsCounts.values();
    List<SzEntityRelationsCount> result = new ArrayList<>(relationsCounts);
    result.sort((c1, c2) -> {
      return c2.getRelationsCount() - c1.getRelationsCount();
    });
    return result;
  }

  @Override
  public void setEntityRelationsCounts(List<SzEntityRelationsCount> relationsCountList) {
    this.relationsCounts.clear();
    if (relationsCountList != null) {
      relationsCountList.forEach( relationsCount -> {
            if (relationsCount != null && relationsCount.getEntityCount() > 0) 
            {
                this.relationsCounts.put(
                  relationsCount.getRelationsCount(), relationsCount);
            }
        });
    }
  }

  @Override
  public void addEntityRelationsCount(SzEntityRelationsCount relationsCount) {
    if (relationsCount == null) return;
    if (relationsCount.getEntityCount() > 0) {
      this.relationsCounts.put(relationsCount.getRelationsCount(), relationsCount);
    } else {
      this.relationsCounts.remove(relationsCount.getRelationsCount());
    }
  }

}
