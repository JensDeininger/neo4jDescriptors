package org.rle.neo4jdescriptor.property.prop_basic;

import org.rle.neo4jdescriptor.property.BasicProperty;

public class StringProperty extends BasicProperty<String> {

  public StringProperty(String key) {
    this(key, key);
  }

  public StringProperty(String key, String logKey) {
    super(key, logKey, String.class);
  }

  @Override
  public StringProperty copy() {
    return new StringProperty(key(), logKey());
  }
}
