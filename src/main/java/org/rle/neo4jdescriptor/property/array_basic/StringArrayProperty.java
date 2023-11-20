package org.rle.neo4jdescriptor.property.array_basic;

import org.neo4j.graphdb.Entity;
import org.rle.neo4jdescriptor.property.ArrayProperty;

public class StringArrayProperty extends ArrayProperty<String> {

  public StringArrayProperty(String key) {
    super(key, String.class);
  }

  public StringArrayProperty(String key, String logKey) {
    super(key, logKey, String.class);
  }

  @Override
  public Class<String[]> codeType() {
    return String[].class;
  }

  @Override
  public Class<String[]> dbType() {
    return String[].class;
  }

  @Override
  public StringArrayProperty copy() {
    return new StringArrayProperty(key(), logKey());
  }

  @Override
  protected boolean isCorrectComponentType(Class<?> compType) {
    return compType == String.class;
  }

  @Override
  public String[] getValueOn(Entity entity) {
    return getValueOnHelper(entity, String[].class);
  }
}
