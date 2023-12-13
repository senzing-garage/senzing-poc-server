package com.senzing.poc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.senzing.api.model.AbstractModelProvider;
import com.senzing.api.model.ModelFactory;
import com.senzing.api.model.ModelProvider;
import com.senzing.poc.model.impl.SzEntitySizeCountImpl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Describes a the number of entities having a specific entity size
 * given by the {@linkplain #getEntitySize() record count}.
 */
@JsonDeserialize(using = SzEntitySizeCount.Factory.class)
public interface SzEntitySizeCount {
  /**
   * Gets the number of records indicating the size of the entities
   * for which the entity count is provided.
   *
   * @return The number of records indicating the size of the entities
   *         for which the entity count is provided.
   */
  int getEntitySize();

  /**
   * Sets the number of records indicating the size of the entities
   * for which the entity count is provided.
   *
   * @param recordCount The number of records indicating the size of the
   *                    entities for which the entity count is provided.
   */
  void setEntitySize(int recordCount);

  /**
   * Gets number of entities in the entity repository having the associated
   * {@linkplain #getEntitySize() entity size}.
   *
   * @return The number of entities in the entity repository having the
   *         associated {@linkplain #getEntitySize() entity size}.
   */
  long getEntityCount();

  /**
   * Sets number of entities in the entity repository having the associated
   * {@linkplain #getEntitySize() entity size}.
   *
   * @param entityCount The number of entities in the entity repository
   *                    having the associated {@linkplain #getEntitySize() 
   *                    entity size}.
   */
  void setEntityCount(long entityCount);

  /**
   * A {@link ModelProvider} for instances of {@link SzEntitySizeCount}.
   */
  interface Provider extends ModelProvider<SzEntitySizeCount> {
    /**
     * Creates a new instance of {@link SzEntitySizeCount}.
     * 
     * @return The new instance of {@link SzEntitySizeCount}
     */
    SzEntitySizeCount create();
  }

  /**
   * Provides a default {@link Provider} implementation for {@link SzEntitySizeCount}
   * that produces instances of {@link SzEntitySizeCountImpl}.
   */
  class DefaultProvider extends AbstractModelProvider<SzEntitySizeCount>
    implements Provider
  {
    /**
     * Default constructor.
     */
    public DefaultProvider() {
      super(SzEntitySizeCount.class, SzEntitySizeCountImpl.class);
    }

    @Override
    public SzEntitySizeCount create() {
      return new SzEntitySizeCountImpl();
    }
  }

  /**
   * Provides a {@link ModelFactory} implementation for {@link SzEntitySizeCount}.
   */
  class Factory extends ModelFactory<SzEntitySizeCount, Provider> {
    /**
     * Default constructor.  This is public and can only be called after the
     * singleton master instance is created as it inherits the same state from
     * the master instance.
     */
    public Factory() {
      super(SzEntitySizeCount.class);
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
     * Creates a new instance of {@link SzEntitySizeCount}.
     * 
     * @return The new instance of {@link SzEntitySizeCount}
     */
    public SzEntitySizeCount create()
    {
      return this.getProvider().create();
    }
  }

  /**
   * The {@link Factory} instance for this interface.
   */
  Factory FACTORY = new Factory(new DefaultProvider());
}
