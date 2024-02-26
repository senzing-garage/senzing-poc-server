package com.senzing.poc.model;

import java.util.List;
import java.util.Collection;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzSummaryStatsImpl;

/**
 * Describes the source summary and all cross-summaries with that data source.
 */
@JsonDeserialize(using = SzSummaryStats.Factory.class)
public interface SzSummaryStats {
  /**
   * Gets the {@link List} of {@link SzSourceSummary} instances describing
   * the summary statistics for all configured data sources.
   * 
   * @return The {@link List} of {@link SzSourceSummary} instances describing
   *         the summary statistics for all configured data sources.
   */
  List<SzSourceSummary> getSourceSummaries();

  /**
   * Sets the {@link List} of {@link SzSourceSummary} instances describing
   * describing the summary statistics for all configured data sources.
   * 
   * @param summaries The {@link List} of {@link SzSourceSummary} instances
   *                  describing the summary statistics for all configured
   *                  data sources.
   */
  void setSourceSummaries(Collection<SzSourceSummary> summaries);

  /**
   * Adds the specified {@link SzSourceSummary} describing the summary 
   * statistics for a specific data source.  This will replace any existing
   * statistics for the same data source.
   * 
   * @param summary The {@link SzSourceSummary} describing the summary statistics
   *                for a specific data source.
   */
  void addSourceSummary(SzSourceSummary summary);

  /**
   * Removes the {@link SzSourceSummary} associated with the specified
   * data source if any exists for that data source.
   * 
   * @param dataSourceCode The data source code for which to remove
   *                       the source summary.
   */
  void removeSourceSummary(String versusDataSourceCode);

  /**
   * Removes all source summary statistics for all the data sources.
   */
  void removeAllSourceSummaries();

  /**
   * A {@link ModelProvider} for instances of {@link SzSummaryStats}.
   */
  interface Provider extends ModelProvider<SzSummaryStats> {
    /**
     * Creates a new instance of {@link SzSummaryStats}.
     * 
     * @return The new instance of {@link SzSummaryStats}
     */
    SzSummaryStats create();
  }

  /**
   * Provides a default {@link Provider} implementation for {@link SzSummaryStats}
   * that produces instances of {@link SzSummaryStatsImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzSummaryStats>
    implements Provider 
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzSummaryStats.class, SzSummaryStatsImpl.class);
    }

    @Override
    public SzSummaryStats create() {
      return new SzSummaryStatsImpl();
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzSummaryStats}.
   */
  class Factory extends ModelFactory<SzSummaryStats, Provider> {
    /**
     * Default constructor. This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from the
     * master instance.
     */
    public Factory() {
      super(SzSummaryStats.class);
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
     * Creates a new instance of {@link SzSummaryStats}.
     * 
     * @return The new instance of {@link SzSummaryStats}
     */
    public SzSummaryStats create() {
      return this.getProvider().create();
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
