package com.senzing.poc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzCountStatsImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes a count statistics for the repository.
 */
@JsonDeserialize(using = SzCountStats.Factory.class)
public interface SzCountStats {
  /**
   * Gets the total number of records that have been loaded to the 
   * entity repository.  This returns <code>null</code> if the record
   * count was not requested and has not been initialized.
   *
   * @return The total number of records that have been loaded to the
   *         entity repository, or <code>null</code> if the record
   *         count was not requested and has not been initialized.
   */
  @JsonInclude(NON_NULL)
  Long getTotalRecordCount();

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
   * entity repository.  This returns <code>null</code> if the entity
   * count was not requested and has not been initialized.
   *
   * @return The total number of entities that have been resolved in the
   *         entity repository, or <code>null</code> if the entity count
   *         was not requested and has not been initialized.
   */
  @JsonInclude(NON_NULL)
  Long getTotalEntityCount();

  /**
   * Sets total number of entities that have at least one record from
   * the associated data source.
   *
   * @param entityCount The total number of entities that have been loaded
   *                    for the associated data source.
   */
  void setTotalEntityCount(long entityCount);

  /**
   * Gets the {@link List} of {@link SzSourceCountStats} describing the 
   * count statistics for each data source.  The returned value list sould
   * contain only one element for each data source.
   * 
   * @return The {@link List} of {@link SzSourceCountStats} describing the 
   *         count statistics for each data source.  The returned {@link List}
   *         sould contain only one element for each data source.
   */
  List<SzSourceCountStats> getDataSourceCounts();

  /**
   * Sets the {@link List} of {@link SzSourceCountStats} describing the 
   * count statistics for each data source.  This clears any existing 
   * data source counts before setting with those specified.   The specified
   * {@link List} should contain only one element for each data source, but
   * uf duplicates are encountered then later values in the {@link List}
   * take precedence, overwriting prior values from the {@link List}.
   * Specifying a <code>null</code> {@link List} is equivalent to specifying
   * an empty {@link List}.
   * 
   * @param statsList The {@link List} of {@link SzSourceCountStats} 
   *                  describing the count statistics for each data source.
   */
  void setDataSourceCounts(List<SzSourceCountStats> statsList);

  /**
   * Adds the specified {@link SzSourceCountStats} describing count statistics
   * for a data source to the existing {@link SzSourceCountStats} for this 
   * instance.  If the specified {@link SzSourceCountStats} has the same 
   * data source code as an existing {@link SzSourceCountStats} instance then
   * the specified value replaces the existing one for that data source code.
   * 
   * @param stats The {@link SzSourceCountStats} describing count statistics
   *              for a specific data source.
   */
  void addDataSourceCount(SzSourceCountStats stats);

  /**
   * A {@link ModelProvider} for instances of {@link SzCountStats}.
   */
  interface Provider extends ModelProvider<SzCountStats> {
    /**
     * Creates a new instance of {@link SzCountStats}.
     * 
     * @return The new instance of {@link SzCountStats}
     */
    SzCountStats create();
  }

  /**
   * Provides a default {@link Provider} implementation for {@link SzCountStats}
   * that produces instances of {@link SzCountStatsImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzCountStats>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzCountStats.class, SzCountStatsImpl.class);
    }

    @Override
    public SzCountStats create() {
      return new SzCountStatsImpl();
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzCountStats}.
   */
  class Factory extends ModelFactory<SzCountStats, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzCountStats.class);
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
     * Creates a new instance of {@link SzCountStats}.
     * 
     * @return The new instance of {@link SzCountStats}
     */
    public SzCountStats create()
    {
      return this.getProvider().create();
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
