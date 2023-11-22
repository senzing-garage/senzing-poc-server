package com.senzing.poc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzSourceCountStatsImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes the count statistics for a specific data source.
 */
@JsonDeserialize(using = SzSourceCountStats.Factory.class)
public interface SzSourceCountStats {
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
   * associated data source.  This returns <code>null</code> if the record
   * count was not requested and has not been initialized.
   *
   * @return The total number of records that have been loaded for the 
   *         associated data source, or <code>null</code> if the record
   *         count was not requested and has not been initialized.
   */
  @JsonInclude(NON_NULL)
  Long getRecordCount();

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
   * the associated data source.  This returns <code>null</code> if the 
   * entity count was not requested and has not been initialized.
   *
   * @return The total number of entities that have at least one record from
   *         the associated data source, or <code>null</code> if the entity
   *         count was not requested and has not been initialized.
   */
  @JsonInclude(NON_NULL)
  Long getEntityCount();

  /**
   * Sets total number of entities that have at least one record from
   * the associated data source.
   *
   * @param entityCount The total number of entities that have been loaded
   *                    for the associated data source.
   */
  void setEntityCount(long entityCount);

  /**
   * A {@link ModelProvider} for instances of {@link SzSourceCountStats}.
   */
  interface Provider extends ModelProvider<SzSourceCountStats> {
    /**
     * Creates a new instance of {@link SzSourceCountStats} with the
     * specified data source code.
     * 
     * @param dataSourceCode The non-null data source code identifying the 
     *                       data source to which the statistics are associated.
     *
     * @return The new instance of {@link SzSourceCountStats}
     * 
     * @throws NullPointerException If the specified data source code is 
     *                              <code>null</code>.
     */
    SzSourceCountStats create(String dataSourceCode);
  }

  /**
   * Provides a default {@link Provider} implementation for
   * {@link SzSourceCountStats} that produces instances of
   * {@link SzSourceCountStatsImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzSourceCountStats>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzSourceCountStats.class, SzSourceCountStatsImpl.class);
    }

    @Override
    public SzSourceCountStats create(String dataSourceCode) {
      return new SzSourceCountStatsImpl(dataSourceCode);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzSourceCountStats}.
   */
  class Factory extends ModelFactory<SzSourceCountStats, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzSourceCountStats.class);
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
     * Creates a new instance of {@link SzSourceCountStats} with the
     * specified data source code.
     * 
     * @param dataSourceCode The non-null data source code identifying the 
     *                       data source to which the statistics are associated.
     *
     * @return The new instance of {@link SzSourceCountStats}
     * 
     * @throws NullPointerException If the specified data source code is 
     *                              <code>null</code>.
     */
    public SzSourceCountStats create(String dataSourceCode)
    {
      return this.getProvider().create(dataSourceCode);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
