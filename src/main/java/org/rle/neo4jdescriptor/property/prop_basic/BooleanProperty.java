package org.rle.neo4jdescriptor.property.prop_basic;

import org.rle.neo4jdescriptor.property.BasicProperty;

public class BooleanProperty extends BasicProperty<Boolean> {

  public BooleanProperty(String key) {
    this(key, key);
  }

  public BooleanProperty(String key, String logKey) {
    super(key, logKey, Boolean.class);
  }

  @Override
  public BooleanProperty copy() {
    return new BooleanProperty(key(), logKey());
  }
}
