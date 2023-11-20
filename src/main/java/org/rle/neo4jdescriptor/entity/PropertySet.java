package org.rle.neo4jdescriptor.entity;

import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.neo4j.graphdb.Entity;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;

/**
 * A {@link PropertySpecification2} holds information about required {@link PropertyDescriptor}s.
 * <p>
 * As such, it is part of {@link EntityIdentifier}s (i.e. {@link RelationshipIdentifier} and {@link NodeIdentifier}),
 * as well as {@link RelationshipDescriptor}s and {@link NodeDescriptor}s
 */
@SuppressWarnings("java:S2160") // override equals in derived classes that have new fields. Not necessary here
public final class PropertySet extends DefinitionBase {

  // region static string

  protected static final String M_KEY_IS_ALREADY_USED_BY_A_DIFFERENT_DESCRIPTOR =
    "a different PropertyDescriptor is already listed under this";

  private static final String M_IS_NOT_NULL = "IS NOT NULL";

  // endregion

  // region fields

  private TreeSet<PropertyDescriptor> mProperties = new TreeSet<>();

  // endregion

  // region ctor

  private PropertySet() {}

  public static PropertySet empty() {
    return new PropertySet();
  }

  // endregion

  // region getters

  public int propertiesCount() {
    return mProperties.size();
  }

  public Stream<PropertyDescriptor> properties() {
    return mProperties.stream();
  }

  public boolean contains(PropertyDescriptor prop) {
    return mProperties.contains(prop);
  }

  public boolean isEmpty() {
    return mProperties.isEmpty();
  }

  public PropertySet openCopy() {
    PropertySet copy = new PropertySet();
    for (PropertyDescriptor reqProp : mProperties) {
      copy.mProperties.add(reqProp.copy());
    }
    return copy;
  }

  public String print(String mainIndent, String subIndent) {
    if (mProperties.isEmpty()) {
      return "";
    }
    String[] propStr = mProperties
      .stream()
      .map(o -> o.print(""))
      .toArray(String[]::new);
    String mI = mainIndent == null ? "" : mainIndent;
    String sI = subIndent == null ? "" : subIndent;
    String sep = System.lineSeparator() + mI + sI;
    String res = String.join(sep, propStr);
    return mI + sI + res;
  }

  // endregion

  // region fluent like config methods

  /**
   * Adds the specified {@link PropertyDescriptor} if it is not already a member. <p>
   * Will throw an exception if its key is already registered with a different {@link PropertyDescriptor}
   * @param propertyDescriptor
   * @return
   */
  public PropertySet add(PropertyDescriptor propertyDescriptor) {
    if (propertyDescriptor == null) {
      throw new IllegalArgumentException(M_ARGUMENT_MUST_NOT_BE_NULL_MSG);
    }
    isOpenOrThrow();
    String key = propertyDescriptor.key();
    Optional<PropertyDescriptor> existing = mProperties
      .stream()
      .filter(o -> o.key().equals(key))
      .findFirst();
    if (existing.isPresent() && !existing.get().equals(propertyDescriptor)) {
      throw new IllegalArgumentException(
        M_KEY_IS_ALREADY_USED_BY_A_DIFFERENT_DESCRIPTOR
      );
    }

    mProperties.add(propertyDescriptor);
    resetHash();
    return this;
  }

  @Override
  public PropertySet closeDefinition() {
    super.closeDefinition();
    return this;
  }

  // endregion

  // region cypher, match and find

  /**
   * Returns something like "n.prop1 IS NOT NULL AND n.prop2 IS NOT NULL AND n.prop3 IS NOT NULL" <p>
   * Or an empty String if there are no properties
   */
  public String propertiesCypher(String alias) {
    if (mProperties.isEmpty()) {
      return "";
    }
    String[] propBits = mProperties
      .stream()
      .map(o -> String.format("%s.%s %s", alias, o.key(), M_IS_NOT_NULL))
      .toArray(String[]::new);
    String sep = String.format(" %s ", M_AND);
    return String.join(sep, propBits);
  }

  public boolean matches(Entity entity) {
    if (entity == null) {
      throw new IllegalArgumentException(M_ARGUMENT_MUST_NOT_BE_NULL_MSG);
    }
    return mProperties.stream().allMatch(o -> entity.hasProperty(o.key()));
  }

  public <T extends Entity> Stream<T> filter(Stream<T> entities) {
    return entities.filter(this::matches);
  }

  // endregion

  // region equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      EqualityUtils.sortedSetHash(mProperties)
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof PropertySet)) {
      return false;
    }
    PropertySet cast = PropertySet.class.cast(obj);
    return EqualityUtils.setEquals(mProperties, cast.mProperties);
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
    PropertySet cast = PropertySet.class.cast(other);
    return EqualityUtils.sortedSetCompare(mProperties, cast.mProperties);
  }
  //endregion
}
