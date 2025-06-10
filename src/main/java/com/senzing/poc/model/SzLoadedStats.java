package com.senzing.poc.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzLoadedStatsImpl;

/**
 * Describes a count statistics for the repository.
 */
@JsonDeserialize(using = SzLoadedStats.Factory.class)
public interface SzLoadedStats {
  /**
   * Gets the total number of records that have been loaded to the
   * entity repository.
   *
   * @return The total number of records that have been loaded to the
   *         entity repository.
   */
  long getTotalRecordCount();

  /**
   * Sets the the total number of records that have been loaded to the
   * entity repository.
   *
   * @param recordCount The total number of records that have been loaded
   *                    to the entity repository.
   */
  void setTotalRecordCount(long recordCount);

  /**
   * Gets the total number of entities that have been resolved in the
   * entity repository.
   *
   * @return The total number of entities that have been resolved in the
   *         entity repository.
   */
  long getTotalEntityCount();

  /**
   * Sets total number of entities that have at least one record from
   * the associated data source.
   *
   * @param entityCount The total number of entities that have been loaded
   *                    for the associated data source.
   */
  void setTotalEntityCount(long entityCount);

  /**
   * Gets the total number of records that have been loaded to the
   * entity repository that did <b>not</b> match against any other
   * records. This is also the total number of entities that only
   * have a single record.
   *
   * @return The total number of records that have been loaded to the
   *         entity repository.
   */
  long getTotalUnmatchedRecordCount();

  /**
   * Sets the the total number of records that have been loaded to the
   * entity repository that did <b>not</b> match against any other
   * record. This is also the total number of entities that only have
   * a single record.
   *
   * @param recordCount The total number of records that have been loaded
   *                    to the entity repository.
   */
  void setTotalUnmatchedRecordCount(long recordCount);

  /**
   * Gets the {@link List} of {@link SzSourceLoadedStats} describing the
   * count statistics for each data source. The returned value list should
   * contain only one element for each data source.
   * 
   * @return The {@link List} of {@link SzSourceLoadedStats} describing the
   *         count statistics for each data source. The returned {@link List}
   *         should contain only one element for each data source.
   */
  List<SzSourceLoadedStats> getDataSourceCounts();

  /**
   * Sets the {@link List} of {@link SzSourceLoadedStats} describing the
   * count statistics for each data source. This clears any existing
   * data source counts before setting with those specified. The specified
   * {@link List} should contain only one element for each data source, but
   * if duplicates are encountered then later values in the {@link List}
   * take precedence, overwriting prior values from the {@link List}.
   * Specifying a <code>null</code> {@link List} is equivalent to specifying
   * an empty {@link List}.
   * 
   * @param statsList The {@link List} of {@link SzSourceLoadedStats}
   *                  describing the count statistics for each data source.
   */
  void setDataSourceCounts(List<SzSourceLoadedStats> statsList);

  /**
   * Adds the specified {@link SzSourceLoadedStats} describing count statistics
   * for a data source to the existing {@link SzSourceLoadedStats} for this
   * instance. If the specified {@link SzSourceLoadedStats} has the same
   * data source code as an existing {@link SzSourceLoadedStats} instance then
   * the specified value replaces the existing one for that data source code.
   * 
   * @param stats The {@link SzSourceLoadedStats} describing count statistics
   *              for a specific data source.
   */
  void addDataSourceCount(SzSourceLoadedStats stats);

  /**
   * A {@link ModelProvider} for instances of {@link SzLoadedStats}.
   */
  interface Provider extends ModelProvider<SzLoadedStats> {
    /**
     * Creates a new instance of {@link SzLoadedStats}.
     * 
     * @return The new instance of {@link SzLoadedStats}
     */
    SzLoadedStats create();
  }

  /**
   * Provides a default {@link Provider} implementation for {@link SzLoadedStats}
   * that produces instances of {@link SzLoadedStatsImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzLoadedStats>
      implements Provider {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzLoadedStats.class, SzLoadedStatsImpl.class);
    }

    @Override
    public SzLoadedStats create() {
      return new SzLoadedStatsImpl();
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzLoadedStats}.
   */
  class Factory extends ModelFactory<SzLoadedStats, Provider> {
    /**
     * Default constructor. This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzLoadedStats.class);
    }

    /**
     * Constructs with the default provider. This constructor is private and
     * is used for the master singleton instance.
     * 
     * @param defaultProvider The default provider.
     */
    private Factory(Provider defaultProvider) {
      super(defaultProvider);
    }

    /**
     * Creates a new instance of {@link SzLoadedStats}.
     * 
     * @return The new instance of {@link SzLoadedStats}
     */
    public SzLoadedStats create() {
      return this.getProvider().create();
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
