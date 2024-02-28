package com.senzing.poc.model;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzEntityImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * Describes a count statistics for the repository.
 */
@JsonDeserialize(using = SzEntity.Factory.class)
public interface SzEntity {
  /**
   * Gets the entity ID that uniquely identifies the entity.
   *
   * @return The entity ID that uniquely identifies the entity.
   */
  long getEntityId();

  /**
   * Sets the entity ID that uniquely identifies the entity.
   *
   * @param entityId The entity ID that uniquely identifies the entity.
   */
  void setEntityId(long entityId);

  /**
   * Gets the best name associated with the entity.
   * 
   * @return The best name associated with the entity.
   */
  @JsonInclude(NON_EMPTY)
  String getEntityName();

  /**
   * Sets the best name associated with the entity.
   * 
   * @param name The best name associated with the entity.
   */
  void setEntityName(String name);

  /**
   * Gets the number of records resolved to this entity.
   * 
   * @return The number of records resolved to this entity.
   */
  @JsonInclude(NON_NULL)
  Integer getRecordCount();

  /**
   * Sets the number of records that are resolved to this entity.
   * 
   * @param recordCount The number of records that are resolved 
   *                    to this entity.
   */
  void setRecordCount(Integer recordCount);

  /**
   * Gets the number of entities that are related to this entity.
   * 
   * @return The number of entities that are related to this entity.
   */
  @JsonInclude(NON_NULL)
  Integer getRelationCount();

  /**
   * Sets the number of entities that are related to this entity.
   * 
   * @param relationCount The number of entities that are related
   *                      to this entity.
   */
  void setRelationCount(Integer relationCount);

  /**
   * Gets the {@link List} of {@link SzRecord} instances describing the
   * records in the entity.
   * 
   * @return The {@link List} of {@link SzRecord} instances describing
   *         the records in the entity.
   */
  @JsonInclude(NON_EMPTY)
  List<SzRecord> getRecords();

  /**
   * Sets the {@link List} of {@link SzRecord} instances describing the 
   * records in this entity.  If two records exist in the {@link Collection}
   * with the same data source code and record ID then the later one 
   * replaces the earlier one.
   * 
   * @param records The {@link Collection} of {@link SzRecord} instances
   *                describing the records in this entity.
    */
  void setRecords(Collection<SzRecord> records);

  /**
   * Adds the specified {@link SzRecord} to the list of records for this 
   * entity. If a record already exists for this entity with the same
   * data source code and record ID, then the specified record replaces
   * the existing one.
   * 
   * @param record The {@link SzRecord} describing the record to be added.
   */
  void addRecord(SzRecord record);

  /**
   * Removes any record from this entity with the specified data source code
   * and record ID.  If there is no such record in the entity then this 
   * method has no effect.
   * 
   * @param dataSourceCode The data source code for the record to remove.
   * @param recordId The record ID for the record to remove.
   */
  void removeRecord(String dataSourceCode, String recordId);

  /**
   * Removes all records for this entity.
   */
  void removeAllRecords();

  /**
   * A {@link ModelProvider} for instances of {@link SzEntity}.
   */
  interface Provider extends ModelProvider<SzEntity> {
    /**
     * Creates a new instance of {@link SzEntity}.
     * 
     * @return The new instance of {@link SzEntity}
     */
    SzEntity create();

    /**
     * Creates a new instance of {@link SzEntity} with the
     * specified entity ID.
     * 
     * @param entityId The entity for the entity.
     */
    SzEntity create(long entityId);

    /**
     * Creates a new instance of {@link SzEntity} with the
     * specified entity ID and entity name.
     * 
     * @param entityId The entity ID for the entity.
     * @param entityName The entity name for the entity.
     */
    SzEntity create(long entityId, String entityName);
  }

  /**
   * Provides a default {@link Provider} implementation for {@link SzEntity}
   * that produces instances of {@link SzEntityImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzEntity>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzEntity.class, SzEntityImpl.class);
    }

    @Override
    public SzEntity create() {
      return new SzEntityImpl();
    }

    @Override
    public SzEntity create(long entityId) {
      return new SzEntityImpl(entityId);
    }

    @Override
    public SzEntity create(long entityId, String entityName) {
      return new SzEntityImpl(entityId, entityName);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzEntity}.
   */
  class Factory extends ModelFactory<SzEntity, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzEntity.class);
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
     * Creates a new instance of {@link SzEntity}.
     * 
     * @return The new instance of {@link SzEntity}
     */
    public SzEntity create()
    {
      return this.getProvider().create();
    }

    /**
     * Creates a new instance of {@link SzEntity} with the
     * specified entity ID.
     * 
     * @param entityId The entity ID with which to create the entity.
     * 
     * @return The new instance of {@link SzEntity}.
     */
    public SzEntity create(long entityId)
    {
      return this.getProvider().create(entityId);
    }

    /**
     * Creates a new instance of {@link SzEntity} with the
     * specified entity ID and entity name.
     * 
     * @param entityId The entity ID with which to create the entity.
     * @param entityName The entity name for the entity.
     * 
     * @return The new instance of {@link SzEntity}.
     */
    public SzEntity create(long entityId, String entityName)
    {
      return this.getProvider().create(entityId, entityName);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
