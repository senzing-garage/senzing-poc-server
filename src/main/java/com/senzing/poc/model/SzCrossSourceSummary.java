package com.senzing.poc.model;

import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzCrossSourceSummaryImpl;

/**
 * Describes the cross-source statistics between two data sources.
 */
@JsonDeserialize(using = SzCrossSourceSummary.Factory.class)
public interface SzCrossSourceSummary {
  /**
   * Gets the primary data source in the cross comparison.
   *
   * @return The primary data source in the cross comparison.
   */
  String getDataSource();

  /**
   * Sets the primary data source in the cross comparison.
   *
   * @param dataSource The non-null primary data source in the
   *                   cross comparison.
   * 
   * @throws NullPointerException If the specified data source code is
   *                              <code>null</code>.
   */
  void setDataSource(String dataSource)
      throws NullPointerException;

  /**
   * Gets the versus data source in the cross comparison.
   *
   * @return The versus data source in the cross comparison.
   */
  String getVersusDataSource();

  /**
   * Sets the versus data source in the cross comparison.
   *
   * @param dataSource The non-null versus data source in the
   *                   cross comparison.
   * 
   * @throws NullPointerException If the specified data source code is
   *                              <code>null</code>.
   */
  void setVersusDataSource(String dataSource)
      throws NullPointerException;

  /**
   * Gets the {@link List} of {@link SzMatchCounts} instances for each
   * requested match key and principle combination that describe the
   * entity and record counts for matches between records from the primary
   * data source to at least one record from the "versus" data source.
   *
   * @return The {@link List} of {@link SzMatchCounts} instances for each
   *         requested match key and principle combination that describe
   *         the entity and record counts for matches for this instance.
   */
  List<SzMatchCounts> getMatches();

  /**
   * Sets the {@link SzMatchCounts} instances for this instance using the
   * specified {@link Collection} of {@link SzMatchCounts}. Any current
   * {@link SzMatchCounts} are removed and replaced with the specified
   * instances. If any of the {@link SzMatchCounts} instances have the
   * same match-key/principle pairs then the last one wins out replacing
   * any previously added ones.
   * 
   * @param matchCounts The {@link Collection} of {@link SzMatchCounts}
   *                    instances to set.
   */
  void setMatches(Collection<SzMatchCounts> matchCounts);

  /**
   * Adds the specified {@link SzMatchCounts} instance to the list of
   * {@link SzMatchCounts} instances describing the match counts for
   * this instance, replacing any existing instance with the same match
   * key and principle combination.
   * 
   * @param matchCounts The {@link SzMatchCounts} instance to add.
   */
  void addMatches(SzMatchCounts matchCounts);

  /**
   * Removes the {@link SzMatchCounts} describing the match statistics
   * associated with the optionally specified match key and principle.
   * 
   * @param matchKey  The match key for the {@link SzMatchCounts} to
   *                  remove, or <code>null</code> if removing the
   *                  statistics associated with any match key.
   * @param principle The principle for the {@link SzMatchCounts} to
   *                  remove, or <code>null<code> if removing the
   *                  statistics associated with any principle.
   */
  void removeMatches(String matchKey, String principle);

  /**
   * Removes all the {@link SzMatchCounts} describing all the match
   * statistics associated with every combination of match key and principle.
   */
  void removeAllMatches();

  /**
   * Gets the {@link List} of {@link SzRelationCounts} instances for each
   * requested match key and principle combination that describe the entity,
   * record and relationship counts for ambiguous-match relationships between
   * entities having at least one record from the primary data source and
   * entities having at least one record from the "versus" data source.
   *
   * @return The {@link List} of {@link SzRelationCounts} instances for
   *         each requested match key and principle combination
   *         describing the ambiguous-match entity, record and
   *         relationship counts for this instance.
   */
  List<SzRelationCounts> getAmbiguousMatches();

  /**
   * Sets the {@link SzRelationCounts} instances describing the ambiguous
   * match relation counts for one or more match-key/principle combination
   * using the specified {@link Collection} of {@link SzRelationCounts}.
   * Any current {@link SzRelationCounts} are removed and replaced with
   * the specified instances. If any of the {@link SzRelationCounts}
   * instances have the same match-key/principle pairs then the last one
   * wins out replacing any previously added ones.
   * 
   * @param relationCounts The {@link Collection} of {@link SzRelationCounts}
   *                       instances to set.
   */
  void setAmbiguousMatches(Collection<SzRelationCounts> relationCounts);

  /**
   * Adds the specified {@link SzRelationCounts} instance to the list of
   * {@link SzRelationCounts} instances describing the ambiguous-match
   * relationship counts for this instance, replacing any existing instance
   * with the same match key and principle combination.
   * 
   * @param relationCounts The {@link SzRelationCounts} instance to add.
   */
  void addAmbiguousMatches(SzRelationCounts relationCounts);

  /**
   * Removes the {@link SzRelationCounts} describing the ambiguous
   * match statistics associated with the optionally specified
   * match key and principle.
   * 
   * @param matchKey  The match key for the ambiguous match {@link
   *                  SzRelationCounts} to remove, or <code>null</code>
   *                  if removing the statistics associated with any
   *                  match key.
   * @param principle The principle for the ambiguous match {@link
   *                  SzRelationCounts} to remove, or <code>null<code>
   *                  if removing the statistics associated with any
   *                  principle.
   */
  void removeAmbiguousMatches(String matchKey, String principle);

  /**
   * Removes all the {@link SzRelationCounts} describing all the
   * ambiguous match statistics associated with every combination
   * of match key and principle.
   */
  void removeAllAmbiguousMatches();

  /**
   * Gets the {@link List} of {@link SzRelationCounts} instances for each
   * requested match key and principle combination that describe the entity,
   * record and relationship counts for possible-match relationships between
   * entities having at least one record from the primary data source and
   * entities having at least one record from the "versus" data source.
   *
   * @return The {@link List} of {@link SzRelationCounts} instances for
   *         each requested match key and principle combination
   *         describing the possible-match entity, record and
   *         relationship counts for this instance.
   */
  List<SzRelationCounts> getPossibleMatches();

  /**
   * Sets the {@link SzRelationCounts} instances describing the
   * possible-match relation counts for one or more match-key/principle
   * combination using the specified {@link Collection} of {@link
   * SzRelationCounts}. Any current {@link SzRelationCounts} are removed
   * and replaced with the specified instances. If any of the {@link
   * SzRelationCounts} instances have the same match-key/principle pairs
   * then the last one wins out replacing any previously added ones.
   * 
   * @param relationCounts The {@link Collection} of {@link SzRelationCounts}
   *                       instances to set.
   */
  void setPossibleMatches(Collection<SzRelationCounts> relationCounts);

  /**
   * Adds the specified {@link SzRelationCounts} instance to the list of
   * {@link SzRelationCounts} instances describing the possible-match
   * relationship counts for this instance, replacing any existing instance
   * with the same match key and principle combination.
   * 
   * @param relationCounts The {@link SzRelationCounts} instance to add.
   */
  void addPossibleMatches(SzRelationCounts relationCounts);

  /**
   * Removes the {@link SzRelationCounts} describing the possible
   * match statistics associated with the optionally specified
   * match key and principle.
   * 
   * @param matchKey  The match key for the possible match {@link
   *                  SzRelationCounts} to remove, or <code>null</code>
   *                  if removing the statistics associated with any
   *                  match key.
   * @param principle The principle for the possible match {@link
   *                  SzRelationCounts} to remove, or <code>null<code>
   *                  if removing the statistics associated with any
   *                  principle.
   */
  void removePossibleMatches(String matchKey, String principle);

  /**
   * Removes all the {@link SzRelationCounts} describing all the
   * possible match statistics associated with every combination
   * of match key and principle.
   */
  void removeAllPossibleMatches();

  /**
   * Gets the {@link List} of {@link SzRelationCounts} instances for
   * each requested match key and principle combination that describe
   * the entity, record and relationship counts for possible-relation
   * relationships between entities having at least one record from
   * the primary data source and entities having at least one record
   * from the "versus" data source.
   *
   * @return The {@link List} of {@link SzRelationCounts} instances for
   *         each requested match key and principle combination
   *         describing the possible-relation entity, record and
   *         relationship counts for this instance.
   */
  List<SzRelationCounts> getPossibleRelations();

  /**
   * Sets the {@link SzRelationCounts} instances describing the
   * possible-relation counts for one or more match-key/principle
   * combination using the specified {@link Collection} of {@link
   * SzRelationCounts}. Any current {@link SzRelationCounts} are removed
   * and replaced with the specified instances. If any of the {@link
   * SzRelationCounts} instances have the same match-key/principle pairs
   * then the last one wins out replacing any previously added ones.
   * 
   * @param relationCounts The {@link Collection} of {@link SzRelationCounts}
   *                       instances to set.
   */
  void setPossibleRelations(Collection<SzRelationCounts> relationCounts);

  /**
   * Adds the specified {@link SzRelationCounts} instance to the list of
   * {@link SzRelationCounts} instances describing the possible-relation
   * relationship counts for this instance, replacing any existing instance
   * with the same match key and principle combination.
   * 
   * @param relationCounts The {@link SzRelationCounts} instance to add.
   */
  void addPossibleRelations(SzRelationCounts relationCounts);

  /**
   * Removes the {@link SzRelationCounts} describing the possible
   * relation statistics associated with the optionally specified
   * match key and principle.
   * 
   * @param matchKey  The match key for the possible relations {@link
   *                  SzRelationCounts} to remove, or <code>null</code>
   *                  if removing the statistics associated with any
   *                  match key.
   * @param principle The principle for the possible relations {@link
   *                  SzRelationCounts} to remove, or <code>null<code>
   *                  if removing the statistics associated with any
   *                  principle.
   */
  void removePossibleRelations(String matchKey, String principle);

  /**
   * Removes all the {@link SzRelationCounts} describing all the
   * possible relation statistics associated with every combination
   * of match key and principle.
   */
  void removeAllPossibleRelations();

  /**
   * Gets the {@link List} of {@link SzRelationCounts} instances for
   * each requested match key and principle combination that describe
   * the entity, record and relationship counts for disclosed-relation
   * relationships between entities having at least one record from
   * the primary data source and entities having at least one record
   * from the "versus" data source.
   *
   * @return The {@link List} of {@link SzRelationCounts} instances for
   *         each requested match key and principle combination
   *         describing the disclosed-relation entity, record and
   *         relationship counts for this instance.
   */
  List<SzRelationCounts> getDisclosedRelations();

  /**
   * Sets the {@link SzRelationCounts} instances describing the
   * disclosed-relation counts for one or more match-key/principle
   * combination using the specified {@link Collection} of {@link
   * SzRelationCounts}. Any current {@link SzRelationCounts} are removed
   * and replaced with the specified instances. If any of the {@link
   * SzRelationCounts} instances have the same match-key/principle pairs
   * then the last one wins out replacing any previously added ones.
   * 
   * @param relationCounts The {@link Collection} of {@link SzRelationCounts}
   *                       instances to set.
   */
  void setDisclosedRelations(Collection<SzRelationCounts> relationCounts);

  /**
   * Adds the specified {@link SzRelationCounts} instance to the list of
   * {@link SzRelationCounts} instances describing the disclosed-relation
   * relationship counts for this instance, replacing any existing instance
   * with the same match key and principle combination.
   * 
   * @param relationCounts The {@link SzRelationCounts} instance to add.
   */
  void addDisclosedRelations(SzRelationCounts relationCounts);

  /**
   * Removes the {@link SzRelationCounts} describing the disclosed
   * relation statistics associated with the optionally specified
   * match key and principle.
   * 
   * @param matchKey  The match key for the disclosed relations {@link
   *                  SzRelationCounts} to remove, or <code>null</code>
   *                  if removing the statistics associated with any
   *                  match key.
   * @param principle The principle for the disclosed relations {@link
   *                  SzRelationCounts} to remove, or <code>null<code>
   *                  if removing the statistics associated with any
   *                  principle.
   */
  void removeDisclosedRelations(String matchKey, String principle);

  /**
   * Removes all the {@link SzRelationCounts} describing all the
   * disclosed relation statistics associated with every combination
   * of match key and principle.
   */
  void removeAllDisclosedRelations();

  /**
   * A {@link ModelProvider} for instances of {@link SzCrossSourceSummary}.
   */
  interface Provider extends ModelProvider<SzCrossSourceSummary> {
    /**
     * Creates a new instance of {@link SzCrossSourceSummary}.
     * 
     * @return The new instance of {@link SzCrossSourceSummary}
     */
    SzCrossSourceSummary create();

    /**
     * Creates a new instance of {@link SzCrossSourceSummary} with the
     * specified data source code.
     * 
     * @param dataSourceCode   The non-null data source code identifying the
     *                         data source to which the statistics are associated.
     *
     * @param vsDataSourceCode The non-null "versus" data source code identifying
     *                         the "versus" data source to which the associated
     *                         are associated.
     * 
     * @return The new instance of {@link SzCrossSourceSummary}
     */
    SzCrossSourceSummary create(String dataSourceCode, String vsDataSource);
  }

  /**
   * Provides a default {@link Provider} implementation for
   * {@link SzCrossSourceSummary} that produces instances of
   * {@link SzCrossSourceSummaryImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzCrossSourceSummary>
      implements Provider {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzCrossSourceSummary.class, SzCrossSourceSummaryImpl.class);
    }

    @Override
    public SzCrossSourceSummary create() {
      return new SzCrossSourceSummaryImpl();
    }

    @Override
    public SzCrossSourceSummary create(String dataSourceCode,
        String vsDataSourceCode) {
      return new SzCrossSourceSummaryImpl(dataSourceCode, vsDataSourceCode);
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for
   * {@link SzCrossSourceSummary}.
   */
  class Factory extends ModelFactory<SzCrossSourceSummary, Provider> {
    /**
     * Default constructor. This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzCrossSourceSummary.class);
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
     * Creates a new instance of {@link SzCrossSourceSummary} with the
     * specified data source codes.
     * 
     * @param dataSourceCode   The non-null data source code identifying the
     *                         data source to which the statistics are associated.
     *
     * @param vsDataSourceCode The non-null "versus" data source code identifying
     *                         the "versus" data source to which the associated
     *                         are associated.
     *
     * @return The new instance of {@link SzCrossSourceSummary}
     * 
     * @throws NullPointerException If the specified data source code is
     *                              <code>null</code>.
     */
    public SzCrossSourceSummary create(String dataSourceCode,
        String vsDataSourceCode) {
      return this.getProvider().create(dataSourceCode, vsDataSourceCode);
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
