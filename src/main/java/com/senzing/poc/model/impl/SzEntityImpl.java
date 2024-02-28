package com.senzing.poc.model.impl;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.poc.model.SzEntity;
import com.senzing.poc.model.SzRecord;

/**
 * Provides a default implementation of {@link SzEntity}.
 */
@JsonDeserialize
public class SzEntityImpl implements SzEntity {
  /**
   * A unique key identifying a record.
   */
  private static class RecordKey implements Comparable<RecordKey> {
    /**
     * The data source code from the record.
     */
    private String dataSource;

    /**
     * The record ID from the record.
     */
    private String recordId;

    /**
     * Constructs with the specified data source code and record ID.
     * 
     * @param dataSourceCode The data source code from the record.
     * @param recordId The record ID from the record.
     */
    private RecordKey(String dataSourceCode, String recordId) {
      this.dataSource = dataSourceCode;
      this.recordId   = recordId;
    }

    /**
     * Gets the data source code from the record.
     * @return The data source code from the record.
     */
    public String getDataSource() {
      return this.dataSource;
    }

    /**
     * Gets the record ID from the record.
     * 
     * @return The record ID from the record.
     */
    public String getRecordId() {
      return this.recordId;
    }

    /**
     * Implemented to return <code>true</code> if and only if the 
     * specified parameter is a non-null reference to an object
     * of the same class with an equivalent data source code and 
     * record ID.
     * 
     * @param obj The object to compare with.
     * @return <code>true</code> if the objects are equal, otherwise
     *         <code>false</code>.
     */
    @Override
    public boolean equals(Object obj) {
      if (obj == null) return false;
      if (this == obj) return true;
      RecordKey key = (RecordKey) obj;
      return Objects.equals(this.getDataSource(), key.getDataSource())
        && Objects.equals(this.getRecordId(), key.getRecordId());
    }

    /**
     * Implemented to return a hash code based on the data source
     * and record ID.
     * 
     * @return The hash code for this instance.
     */
    @Override
    public int hashCode() {
      return Objects.hash(this.getDataSource(), this.getRecordId());
    }

    /**
     * Implemented to compare this instance against the specified parameter.
     * If the specified parameter is <code>null</code> then a positive 
     * number is returned indicating that <code>null</code> values sort
     * less-than non-null values, otherwise this returns a negative number,
     * zero or a positive number accordingly to sort values of this class
     * first by data source code and then by record ID with <code>null</code>
     * values shorting less-than non-null values.
     * 
     * @param key The {@link RecordKey} to compare with.
     * @return A negative number, zero (0) or a positive number depending on
     *         whether this instance compares less-than, equal-to or greater-than
     *         the specified parameter.
     */
    @Override
    public int compareTo(RecordKey key) {
      if (key == null) return 1;
      if (!Objects.equals(this.getDataSource(), key.getDataSource())) {
        String source1 = this.getDataSource();
        String source2 = key.getDataSource();
        if (source1 == null) return -1;
        if (source2 == null) return 1;
        return source1.compareTo(source2);
      }
      String id1 = this.getRecordId();
      String id2 = key.getRecordId();
      if (id1 == id2) return 0;
      if (id1 == null) return -1;
      if (id2 == null) return 1;
      return id1.compareTo(id2);
    }

    /**
     * Overridden to return a diagnostic {@link String} describing
     * this instance.
     * 
     * @return A diagnostic {@link String} describing this instance.
     */
    @Override
    public String toString() {
      return this.getDataSource() + ":" + this.getRecordId();
    }
  }

  /**
   * The entity ID that uniquely identifies the entity.
   */
  private long entityId = 0L;

  /**
   * The best name for the entity.
   */
  private String entityName = null;

  /**
   * The number of records resolved to this entity.
   */
  private Integer recordCount = null;

  /**
   * The number of entities that are related to this entity.
   */
  private Integer relationCount = null;

  /**
   * The {@link Map} of {@link Long} entity ID's identifying the entities
   * on this page.
   */
  private SortedMap<RecordKey, SzRecord> records = null;

  /**
   * Default constructor
   */
  public SzEntityImpl() {
    this(0L);
  }

  /**
   * Constructs with the specified entity ID.
   * 
   * @param entityId The entity ID for the entity.
   */
  public SzEntityImpl(long entityId) {
    this(entityId, null);
  }

  /**
   * Constructs with the specified entity ID.
   * 
   * @param entityId The entity ID for the entity.
   * @param entityName The best name for the entity.
   */
  public SzEntityImpl(long entityId, String entityName) {
    this.entityId       = entityId;
    this.entityName     = entityName;
    this.recordCount    = null;
    this.relationCount  = null;
    this.records        = new TreeMap<>();
  }

  @Override
  public long getEntityId() {
    return this.entityId;
  }

  @Override
  public void setEntityId(long entityId) {
    this.entityId = entityId;
  }

  @Override
  public String getEntityName() {
    return this.entityName;
  }

  @Override
  public void setEntityName(String name) {
    this.entityName = name;
  }

  @Override
  public Integer getRecordCount() {
    return this.recordCount;
  }

  @Override
  public void setRecordCount(Integer recordCount) {
    this.recordCount = recordCount;
  }

  @Override
  public Integer getRelationCount() {
    return this.relationCount;
  }

  @Override
  public void setRelationCount(Integer relationCount) {
    this.relationCount = relationCount;
  }

  @Override
  public List<SzRecord> getRecords() {
    List<SzRecord> records = new ArrayList<>(this.records.values());
    return records;
  }

  @Override
  public void setRecords(Collection<SzRecord> records) {
    this.records.clear();
    if (records != null) {
      records.forEach( record -> {
        if (record != null) {
          RecordKey key = new RecordKey(record.getDataSource(), 
                                        record.getRecordId());
          this.records.put(key, record);
        }
      });
    }
  }

  @Override
  public void addRecord(SzRecord record) {
    RecordKey key = new RecordKey(record.getDataSource(), 
                                  record.getRecordId());
    this.records.put(key, record);
  }

  @Override
  public void removeRecord(String dataSourceCode, String recordId) {
    RecordKey key = new RecordKey(dataSourceCode, recordId);
    this.records.remove(key);
  }

  @Override
  public void removeAllRecords() {
    this.records.clear();
  }

  /**
   * Overridden to return a diagnostic {@link String} describing this instance.
   * @return A diagnostic {@link String} describing this instance.
   */
  @Override
  public String toString() {
    return "entityId=[ " + this.getEntityId() 
      + " ], entityName=[ " + this.getEntityName()
      + " ], recordCount=[ " + this.getRecordCount()
      + " ], relationCount=[ " + this.getRelationCount()
      + " ], records=[ " + this.getRecords() + " ]";
  }
}
