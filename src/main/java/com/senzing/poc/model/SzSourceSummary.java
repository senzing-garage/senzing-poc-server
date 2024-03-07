package com.senzing.poc.model;

import java.util.List;
import java.util.Collection;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzSourceSummaryImpl;

/**
 * Describes the source summary and all cross-summaries with that data source.
 */
@JsonDeserialize(using = SzSourceSummary.Factory.class)
public interface SzSourceSummary {
  /**
   * Gets the data source to which the summary statistics apply.
   *
   * @return The data source to which the summary statistics apply.
   */
  String getDataSource();

  /**
   * Sets the data source to which the summary statistics apply.
   *
   * @param dataSource The data source to which the summary statistics apply.
   * 
   * @throws NullPointerException If the specified data source code is
   *                              <code>null</code>.
   */
  void setDataSource(String dataSource) throws NullPointerException;

  /**
   * Gets number of entities having at least one record from the data source.
   *
   * @return The number of entities having at least one record from the
   *         data source.
   */
  long getEntityCount();

  /**
   * Sets number of entities having at least one record from the data source.
   *
   * @param entityCount The number of entities having at least one record
   *                    from the data source.
   */
  void setEntityCount(long entityCount);

  /**
   * Gets the number of records loaded from the data source.
   *
   * @return The number of records loaded from the data source.
   */
  long getRecordCount();

  /**
   * Sets number of records loaded from the data source.
   *
   * @param recordCount The number of records loaded from the data source.
   */
  void setRecordCount(long recordCount);

  /**
   * Gets number of records from this data source that did not match a
   * record from the same data source. This represents the number of
   * records from the data source that are effectively unique (not 
   * duplicated).
   *
   * @return The number of records from this data source that did not
   *         match a record from the same data source.
   */
  long getUnmatchedRecordCount();

  /**
   * Sets the the total number of records that have been loaded to the entity
   * repository that did <b>not</b> match against any other record. This is also
   * the total number of entities that only have a single record.
   *
   * @param recordCount The total number of records that have been loaded to the
   *                    entity repository.
   */
  void setUnmatchedRecordCount(long recordCount);

  /**
   * Gets the {@link List} of {@link SzCrossSourceSummary} instances describing
   * the summary statistics between the associated data source versus every
   * data source (including itself).
   * 
   * @return The {@link List} of {@link SzCrossSourceSummary} instances
   *         describing the summary statistics between the associated data
   *         source versus every data source (including itself).
   */
  List<SzCrossSourceSummary> getCrossSourceSummaries();

  /**
   * Sets the {@link List} of {@link SzCrossSourceSummary} instances
   * describing the summary statistics between the associated data
   * source versus every data source (including itself).
   * 
   * @param crossSummaries The {@link List} of {@link SzCrossSourceSummary}
   *                       instances describing the summary statistics between
   *                       the associated data source versus every data source.
   */
  void setCrossSourceSummaries(Collection<SzCrossSourceSummary> crossSummaries);

  /**
   * Adds the specified {@link SzCrossSourceSummary} describing the summary 
   * statistics between the associated data source versus another specific
   * data source (which may be the same).
   * 
   * @param crossSummary The {@link SzCrossSourceSummary} describing the summary 
   *                     statistics between the associated data source versus
   *                     another specific data source.
   */
  void addCrossSourceSummary(SzCrossSourceSummary crossSummary);

  /**
   * Removes the {@link SzCrossSourceSummary} associated with the specified
   * versus data source if any exists for that data source.
   * 
   * @param versusDataSourceCode The "versus" data sources code for which to
   *                             remove the cross source summary.
   */
  void removeCrossSourceSummary(String versusDataSourceCode);

  /**
   * Removes all cross source summary statistics for all the data sources.
   */
  void removeAllCrossSourceSummaries();

  /**
   * A {@link ModelProvider} for instances of {@link SzSourceSummary}.
   */
  interface Provider extends ModelProvider<SzSourceSummary> {
    /**
     * Creates a new instance of {@link SzSourceSummary}.
     * 
     * @return The new instance of {@link SzSourceSummary}
     */
    SzSourceSummary create();

    /**
     * Creates a new instance of {@link SzSourceSummary}.
     * 
     * @param dataSourceCode The data source code to associated with the
     *                       new instance.
     * 
     * @return The new instance of {@link SzSourceSummary}
     */
    SzSourceSummary create(String dataSourceCode);
  }

  /**
   * Provides a default {@link Provider} implementation for
   * {@link SzSourceSummary} that produces instances of
   * {@link SzSourceSummaryImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzSourceSummary> implements Provider {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzSourceSummary.class, SzSourceSummaryImpl.class);
    }

    @Override
    public SzSourceSummary create() {
      return new SzSourceSummaryImpl();
    }

    @Override
    public SzSourceSummary create(String dataSourceCode) {
      return new SzSourceSummaryImpl(dataSourceCode);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzSourceSummary}.
   */
  class Factory extends ModelFactory<SzSourceSummary, Provider> {
    /**
     * Default constructor. This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from the
     * master instance.
     */
    public Factory() {
      super(SzSourceSummary.class);
    }

    /**
     * Constructs with the default provider. This constructor is private and is used
     * for the master singleton instance.
     * 
     * @param defaultProvider The default provider.
     */
    private Factory(Provider defaultProvider) {
      super(defaultProvider);
    }

    /**
     * Creates a new instance of {@link SzSourceSummary}.
     * 
     * @return The new instance of {@link SzSourceSummary}
     */
    public SzSourceSummary create() {
      return this.getProvider().create();
    }

    /**
     * Creates a new instance of {@link SzSourceSummary}.
     * 
     * @param dataSourceCode The data source code to associated with the
     *                       new instance.
     * 
     * @return The new instance of {@link SzSourceSummary}
     */
    public SzSourceSummary create(String dataSourceCode) {
      return this.getProvider().create(dataSourceCode);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
