package org.rle.neo4jdescriptor.property.prop_basic;

import org.rle.neo4jdescriptor.property.BasicProperty;

public class DoubleProperty extends BasicProperty<Double> {

  public DoubleProperty(String key) {
    this(key, key);
  }

  public DoubleProperty(String key, String logKey) {
    super(key, logKey, Double.class);
  }

  @Override
  public DoubleProperty copy() {
    return new DoubleProperty(key(), logKey());
  }
}
