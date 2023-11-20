package org.rle.neo4jdescriptor.property.prop_basic;

import org.rle.neo4jdescriptor.property.BasicProperty;

public class NumberProperty extends BasicProperty<Number> {

  public NumberProperty(String key) {
    super(key, Number.class);
  }

  public NumberProperty(String key, String logKey) {
    super(key, logKey, Number.class);
  }

  @Override
  public NumberProperty copy() {
    return new NumberProperty(key(), logKey());
  }
}
