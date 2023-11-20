package org.rle.neo4jdescriptor.property.array_basic;

import org.neo4j.graphdb.Entity;
import org.rle.neo4jdescriptor.property.ArrayProperty;

public class BooleanArrayProperty extends ArrayProperty<Boolean> {

  public BooleanArrayProperty(String key) {
    super(key, Boolean.class);
  }

  public BooleanArrayProperty(String key, String logKey) {
    super(key, logKey, Boolean.class);
  }

  @Override
  public Class<Boolean[]> codeType() {
    return Boolean[].class;
  }

  @Override
  public Class<Boolean[]> dbType() {
    return Boolean[].class;
  }

  @Override
  public BooleanArrayProperty copy() {
    return new BooleanArrayProperty(key(), logKey());
  }

  @Override
  protected boolean isCorrectComponentType(Class<?> compType) {
    return compType == Boolean.class || compType == boolean.class;
  }

  @Override
  public Boolean[] getValueOn(Entity entity) {
    return getValueOnHelper(entity, Boolean[].class);
  }
}
