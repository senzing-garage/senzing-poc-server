package com.senzing.poc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzSourceLoadedStatsImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes the count statistics for a specific data source.
 */
@JsonDeserialize(using = SzSourceLoadedStats.Factory.class)
public interface SzSourceLoadedStats {
  /**
   * Gets the data source code identifying the data source to which the
   * statistics are associated.
   *
   * @return The data source code identifying the data source to which the
   *         statistics are associated.
   */
  String getDataSourceCode();

  /**
   * Sets the data source code identifying the data source to which the
   * statistics are associated.
   *
   * @param dataSourceCode The non-null data source code identifying the data 
   *                       source to which the statistics are associated.
   * 
   * @throws NullPointerException If the specified data source code is
   *                              <code>null</code>.
   */
  void setDataSourceCode(String dataSourceCode)
    throws NullPointerException;

  /**
   * Gets the total number of records that have been loaded for the 
   * associated data source.
   *
   * @return The total number of records that have been loaded for the 
   *         associated data source.
   */
  long getRecordCount();

  /**
   * Sets the the total number of records that have been loaded for the 
   * associated data source.
   *
   * @param recordCount The total number of records that have been loaded
   *                    for the associated data source.
   */
  void setRecordCount(long recordCount);

  /**
   * Gets the total number of entities that have at least one record from
   * the associated data source.
   *
   * @return The total number of entities that have at least one record from
   *         the associated data source.
   */
  long getEntityCount();

  /**
   * Sets total number of entities that have at least one record from
   * the associated data source.
   *
   * @param entityCount The total number of entities that have been loaded
   *                    for the associated data source.
   */
  void setEntityCount(long entityCount);

  /**
   * Gets the total number of entities that have at least one record from
   * the associated data source.  This value doubles as the number of
   * entities having a record from the associated data source where that
   * record is single <b>only</b> record in the entity.
   *
   * @return The total number of entities that have at least one record from
   *         the associated data source.
   */
  long getUnmatchedRecordCount();

  /**
   * Sets total number of records that have been loaded for the associated 
   * data source that did <b>not</b> match against any other record.  This
   * value doubles as the number of entities having a record from the 
   * associated data source where that record is single <b>only</b> record
   * in the entity.
   *
   * @param entityCount The total number of entities that have been loaded
   *                    for the associated data source.
   */
  void setUnmatchedRecordCount(long entityCount);

  /**
   * A {@link ModelProvider} for instances of {@link SzSourceLoadedStats}.
   */
  interface Provider extends ModelProvider<SzSourceLoadedStats> {
    /**
     * Creates a new instance of {@link SzSourceLoadedStats} with the
     * specified data source code.
     * 
     * @param dataSourceCode The non-null data source code identifying the 
     *                       data source to which the statistics are associated.
     *
     * @return The new instance of {@link SzSourceLoadedStats}
     * 
     * @throws NullPointerException If the specified data source code is 
     *                              <code>null</code>.
     */
    SzSourceLoadedStats create(String dataSourceCode);
  }

  /**
   * Provides a default {@link Provider} implementation for
   * {@link SzSourceLoadedStats} that produces instances of
   * {@link SzSourceLoadedStatsImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzSourceLoadedStats>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzSourceLoadedStats.class, SzSourceLoadedStatsImpl.class);
    }

    @Override
    public SzSourceLoadedStats create(String dataSourceCode) {
      return new SzSourceLoadedStatsImpl(dataSourceCode);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzSourceLoadedStats}.
   */
  class Factory extends ModelFactory<SzSourceLoadedStats, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzSourceLoadedStats.class);
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
     * Creates a new instance of {@link SzSourceLoadedStats} with the
     * specified data source code.
     * 
     * @param dataSourceCode The non-null data source code identifying the 
     *                       data source to which the statistics are associated.
     *
     * @return The new instance of {@link SzSourceLoadedStats}
     * 
     * @throws NullPointerException If the specified data source code is 
     *                              <code>null</code>.
     */
    public SzSourceLoadedStats create(String dataSourceCode)
    {
      return this.getProvider().create(dataSourceCode);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
