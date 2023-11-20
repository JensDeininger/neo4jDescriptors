package org.rle.neo4jdescriptor.property.prop_basic;

import org.rle.neo4jdescriptor.property.BasicProperty;

public class ObjectProperty extends BasicProperty<Object> {

  public ObjectProperty(String key) {
    super(key, Object.class);
  }

  public ObjectProperty(String key, String logKey) {
    super(key, logKey, Object.class);
  }

  @Override
  public ObjectProperty copy() {
    return new ObjectProperty(key(), logKey());
  }
}
