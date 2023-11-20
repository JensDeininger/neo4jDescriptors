package org.rle.neo4jdescriptor.entity;

import java.util.Objects;
import java.util.stream.Stream;
import org.neo4j.graphdb.Entity;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;

@SuppressWarnings("java:S2160") // override equals in derived classes that have new fields. Not necessary here
public abstract class EntityIdentifier extends DefinitionBase {

  // region fields

  private PropertySet mPropertySet = PropertySet.empty();

  // endregion

  // region ctor

  protected EntityIdentifier() {}

  // endregion

  // region accessor methods

  public int propertiesCount() {
    return mPropertySet.propertiesCount();
  }

  public Stream<PropertyDescriptor> properties() {
    return mPropertySet.properties();
  }

  protected void copyPropertiesOnto(EntityIdentifier target) {
    if (!target.mPropertySet.isEmpty()) {
      throw new IllegalStateException();
    }
    target.mPropertySet = this.mPropertySet.openCopy();
  }

  protected PropertySet propertySet() {
    return mPropertySet;
  }

  public abstract String print(String mainIndent, String subIndent);

  // endregion

  // region config methods

  /**
   * Adds the specified {@link PropertyDescriptor} if it is not already a member. <p>
   * Will throw an exception if its key is already registered with a different {@link PropertyDescriptor}
   * @param propertyDescriptor
   * @return
   */
  public EntityIdentifier add(PropertyDescriptor propertyDescriptor) {
    isOpenOrThrow();
    mPropertySet.add(propertyDescriptor);
    resetHash();
    return this;
  }

  @Override
  public EntityIdentifier closeDefinition() {
    super.closeDefinition();
    mPropertySet.closeDefinition();
    return this;
  }

  //endregion

  // region cypher and match methods

  protected boolean propMatches(Entity entity) {
    return mPropertySet.matches(entity);
  }

  protected <T extends Entity> Stream<T> filter(Stream<T> entities) {
    return mPropertySet.filter(entities);
  }

  /**
   * Returns something like "n.prop1 IS NOT NULL AND n.prop2 IS NOT NULL AND n.prop3 IS NOT NULL" <p>
   * Or an empty String if there are no properties
   */
  protected String propCypher(String alias) {
    return mPropertySet.propertiesCypher(alias);
  }

  //endregion

  //region equality overrides

  @Override
  protected int processHash() {
    return Objects.hash(super.processHash(), mPropertySet);
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof EntityIdentifier)) {
      return false;
    }
    EntityIdentifier cast = EntityIdentifier.class.cast(obj);
    return mPropertySet.equals(cast.mPropertySet);
  }

  @Override
  protected int processCompare(DefinitionBase other) {
    if (other == this) {
      return 0;
    }
    int superComp = super.processCompare(other);
    if (superComp != 0) {
      return superComp;
    }
    EntityIdentifier cast = EntityIdentifier.class.cast(other);
    return mPropertySet.compareTo(cast.mPropertySet);
  }
  //endregion
}
