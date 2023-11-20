package org.rle.neo4jdescriptor.property.array_basic;

import org.neo4j.graphdb.Entity;
import org.rle.neo4jdescriptor.property.ArrayProperty;

public class LongArrayProperty extends ArrayProperty<Long> {

  public LongArrayProperty(String key) {
    super(key, Long.class);
  }

  public LongArrayProperty(String key, String logKey) {
    super(key, logKey, Long.class);
  }

  @Override
  public Class<Long[]> codeType() {
    return Long[].class;
  }

  @Override
  public Class<Long[]> dbType() {
    return Long[].class;
  }

  @Override
  public LongArrayProperty copy() {
    return new LongArrayProperty(key(), logKey());
  }

  @Override
  protected boolean isCorrectComponentType(Class<?> compType) {
    return compType == Long.class || compType == long.class;
  }

  @Override
  public Long[] getValueOn(Entity entity) {
    return getValueOnHelper(entity, Long[].class);
  }
}
