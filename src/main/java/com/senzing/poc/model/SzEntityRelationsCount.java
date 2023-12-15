package com.senzing.poc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzEntityRelationsCountImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes a the number of entities having a specific number of 
 * entity relations given by the {@linkplain #getRelationsCount()
 * relations count}.
 */
@JsonDeserialize(using = SzEntityRelationsCount.Factory.class)
public interface SzEntityRelationsCount {
  /**
   * Gets the number of entity relations for which the entity count
   * is provided.
   *
   * @return The number of entity relations for which the entity 
   *         count is provided.
   */
  int getRelationsCount();

  /**
   * Sets the number of entity relations for which the entity count 
   * is provided.
   *
   * @param relationsCount The number of entity relations for which
   *                       the entity count is provided.
   */
  void setRelationsCount(int relationsCount);

  /**
   * Gets number of entities in the entity repository having the associated
   * {@linkplain #getRelationsCount() number of entity relations}.
   *
   * @return The number of entities in the entity repository having the
   *         associated {@linkplain #getRelationsCount() number of entity
   *         relations}.
   */
  long getEntityCount();

  /**
   * Sets number of entities in the entity repository having the associated
   * {@linkplain #getRelationsCount() number of entity relations}.
   *
   * @param entityCount The number of entities in the entity repository
   *                    having the associated {@linkplain #getRelationsCount()
   *                    number of entity relations}.
   */
  void setEntityCount(long entityCount);

  /**
   * A {@link ModelProvider} for instances of {@link SzEntityRelationsCount}.
   */
  interface Provider extends ModelProvider<SzEntityRelationsCount> {
    /**
     * Creates a new instance of {@link SzEntityRelationsCount}.
     * 
     * @return The new instance of {@link SzEntityRelationsCount}
     */
    SzEntityRelationsCount create();
  }

  /**
   * Provides a default {@link Provider} implementation for {@link SzEntityRelationsCount}
   * that produces instances of {@link SzEntityRelationsCountImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzEntityRelationsCount>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzEntityRelationsCount.class, SzEntityRelationsCountImpl.class);
    }

    @Override
    public SzEntityRelationsCount create() {
      return new SzEntityRelationsCountImpl();
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzEntityRelationsCount}.
   */
  class Factory extends ModelFactory<SzEntityRelationsCount, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzEntityRelationsCount.class);
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
     * Creates a new instance of {@link SzEntityRelationsCount}.
     * 
     * @return The new instance of {@link SzEntityRelationsCount}
     */
    public SzEntityRelationsCount create()
    {
      return this.getProvider().create();
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
