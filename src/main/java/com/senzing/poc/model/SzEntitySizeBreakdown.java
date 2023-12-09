package com.senzing.poc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzEntitySizeBreakdownImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes the number of entities in the entity repository at each 
 * count statistics for the repository.
 */
@JsonDeserialize(using = SzEntitySizeBreakdown.Factory.class)
public interface SzEntitySizeBreakdown {
  /**
   * Gets the {@link List} of {@link SzEntitySizeCount} describing the 
   * number of entities having each entity size indicated by the number
   * of constituent records for that entity.  The returned value list 
   * should contain only one element for each distinct entity size that
   * exists in the repository.  The {@link List} is returned in descending
   * order of entity size.
   * 
   * @return The {@link List} of {@link SzEntitySizeCount} describing the 
   *         number of entities having each entity size indicated by the
   *         number of constituent records for that entity.
   */
  List<SzEntitySizeCount> getEntitySizeCounts();

  /**
   * Sets the {@link List} of {@link SzEntitySizeCount} describing the 
   * number of entities having each entity size indicated by the number
   * of constituent records for that entity.  This clears any existing 
   * entity size counts before setting with those specified.   The specified
   * {@link List} should contain only one element for each entity size, but
   * if duplicates are encountered then later values in the {@link List}
   * take precedence, overwriting prior values from the {@link List}.
   * Specifying a <code>null</code> {@link List} is equivalent to specifying
   * an empty {@link List}.
   * 
   * @param sizeCountList The {@link List} of {@link SzEntitySizeCount} 
   *                      describing the number of entities having each
   *                      entity size indicated by the number of
   *                      constituent records for that entity.
   */
  void setEntitySizeCounts(List<SzEntitySizeCount> sizeCountList);

  /**
   * Adds the specified {@link SzEntitySizeCount} describing the number of
   * entities in the entity repository having a specific entity size.  If
   * the specified {@link SzEntitySizeCount} has the same entity size as
   * an existing {@link SzEntitySizeCount} instance then the specified
   * value replaces the existing one for that entity size.
   * 
   * @param sizeCount The {@link SzEntitySizeCount} describing the number
   *                  of entities in the entity repository having a
   *                  specific entity size.
   */
  void addEntitySizeCount(SzEntitySizeCount sizeCount);

  /**
   * A {@link ModelProvider} for instances of {@link SzEntitySizeBreakdown}.
   */
  interface Provider extends ModelProvider<SzEntitySizeBreakdown> {
    /**
     * Creates a new instance of {@link SzEntitySizeBreakdown}.
     * 
     * @return The new instance of {@link SzEntitySizeBreakdown}
     */
    SzEntitySizeBreakdown create();
  }

  /**
   * Provides a default {@link Provider} implementation for {@link SzEntitySizeBreakdown}
   * that produces instances of {@link SzEntitySizeBreakdownImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzEntitySizeBreakdown>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzEntitySizeBreakdown.class, SzEntitySizeBreakdownImpl.class);
    }

    @Override
    public SzEntitySizeBreakdown create() {
      return new SzEntitySizeBreakdownImpl();
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzEntitySizeBreakdown}.
   */
  class Factory extends ModelFactory<SzEntitySizeBreakdown, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzEntitySizeBreakdown.class);
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
     * Creates a new instance of {@link SzEntitySizeBreakdown}.
     * 
     * @return The new instance of {@link SzEntitySizeBreakdown}
     */
    public SzEntitySizeBreakdown create()
    {
      return this.getProvider().create();
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
