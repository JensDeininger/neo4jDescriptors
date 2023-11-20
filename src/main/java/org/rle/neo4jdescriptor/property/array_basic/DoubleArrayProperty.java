package org.rle.neo4jdescriptor.property.array_basic;

import org.neo4j.graphdb.Entity;
import org.rle.neo4jdescriptor.property.ArrayProperty;

public class DoubleArrayProperty extends ArrayProperty<Double> {

  public DoubleArrayProperty(String key) {
    super(key, Double.class);
  }

  public DoubleArrayProperty(String key, String logKey) {
    super(key, logKey, Double.class);
  }

  @Override
  public Class<Double[]> codeType() {
    return Double[].class;
  }

  @Override
  public Class<Double[]> dbType() {
    return Double[].class;
  }

  @Override
  public DoubleArrayProperty copy() {
    return new DoubleArrayProperty(key(), logKey());
  }

  @Override
  protected boolean isCorrectComponentType(Class<?> compType) {
    return compType == Double.class || compType == double.class;
  }

  @Override
  public Double[] getValueOn(Entity entity) {
    return getValueOnHelper(entity, Double[].class);
  }
}
