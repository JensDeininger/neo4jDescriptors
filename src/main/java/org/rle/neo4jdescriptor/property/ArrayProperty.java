package org.rle.neo4jdescriptor.property;

import java.lang.reflect.Array;
import org.neo4j.graphdb.Entity;
import org.rle.neo4jdescriptor.report.PropertyReport;

public abstract class ArrayProperty<T> extends PropertyDescriptor {

  private final Class<T> mComponentType;

  protected ArrayProperty(String key, Class<T> componentType) {
    super(key);
    if (componentType == null) {
      throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
    }
    mComponentType = componentType;
  }

  protected ArrayProperty(String key, String logKey, Class<T> componentType) {
    super(key, logKey);
    if (componentType == null) {
      throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
    }
    mComponentType = componentType;
  }

  protected Class<T> componentType() {
    return mComponentType;
  }

  @Override
  public abstract ArrayProperty<T> copy();

  protected abstract boolean isCorrectComponentType(Class<?> compType);

  @Override
  public boolean appliesTo(Entity entity) {
    if (!super.appliesTo(entity)) {
      return false;
    }
    Object nodeValue = entity.getProperty(key());
    if (nodeValue == null) {
      return false;
    }
    if (!nodeValue.getClass().isArray()) {
      return false;
    }
    // empty arrays created by a cypher statement are always string typed. In cypher, there
    // is no way to specify the data type other than by specifying values.
    if (Array.getLength(nodeValue) == 0) {
      return true;
    }
    Class<?> componentType = nodeValue.getClass().getComponentType();
    return isCorrectComponentType(componentType);
  }

  protected T[] getValueOnHelper(Entity entity, Class<T[]> clazz) {
    super.hasPropOrThrow(entity);
    Object dbValue = entity.getProperty(this.key());
    Class<?> dbType = dbValue.getClass();
    if (!dbType.isArray()) {
      String msg = wrongTypeMessage(entity, dbType, clazz);
      throw new ClassCastException(msg);
    }
    // the reason for this special case is this: if I set the value of a property to an empty
    // array in cypher, it will be a string array. But I want to get the correct type here
    if (Array.getLength(dbValue) == 0) {
      return clazz.cast(Array.newInstance(clazz.getComponentType(), 0));
    }
    Class<?> dbComponentType = dbValue.getClass().getComponentType();
    if (!isCorrectComponentType(dbComponentType)) {
      String msg = wrongTypeMessage(entity, dbComponentType, mComponentType);
      throw new ClassCastException(msg);
    }
    int arrayLength = Array.getLength(dbValue);
    T[] result = clazz.cast(
      Array.newInstance(clazz.getComponentType(), arrayLength)
    );
    for (int i = 0; i < arrayLength; i++) {
      result[i] = mComponentType.cast(Array.get(dbValue, i));
    }
    return result;
  }

  public abstract T[] getValueOn(Entity entity);

  public void setValueOn(Entity entity, T[] value) {
    if (entity == null) {
      throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
    }
    entity.setProperty(this.key(), value);
  }

  @Override
  protected String typedPrint() {
    return String.format("ArrayProperty<%s>", dbType().getSimpleName());
  }

  @Override
  protected void validatePropertyValue(Entity entity, PropertyReport report) {
    // As this method is only called in one spot, I can assume that
    // the property key does exist on the entity AND that the
    // report is still open
    Object nodeValue = entity.getProperty(this.key());
    Class<?> nodeValueType = nodeValue.getClass();
    if (
      !nodeValueType.isArray() ||
      (
        // the reason for treating empty arrays differently is this:
        // Setting the value of a property to an empty array *using cypher*, neo4j will make it a string array.
        // But I still want to accept that as having the correct type
        Array.getLength(nodeValue) > 0 &&
        !isCorrectComponentType(nodeValue.getClass().getComponentType())
      )
    ) {
      report.setTypeCheck(false).setDeviantType(nodeValueType.getSimpleName());
    } else {
      report.setTypeCheck(true);
    }
    report.closeReport();
  }

  // region Equals, Hash and Compare Overrides

  @Override
  public boolean equals(Object obj) {
    // The super.equals method already incorporates class and DbType
    // Only reason this method is here is to stop SonarLint from complaining.
    // Well, a reminder that semantic equality rules this class is nice, I guess
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    // This works because super.hashCode incorporates the class and the Db Type
    // via abstract methods.
    return super.hashCode();
  }
}
