package org.rle.neo4jdescriptor.property.prop_basic;

import org.rle.neo4jdescriptor.property.BasicProperty;

public class LongProperty extends BasicProperty<Long> {

  public LongProperty(String key) {
    super(key, Long.class);
  }

  public LongProperty(String key, String logKey) {
    super(key, logKey, Long.class);
  }

  @Override
  public LongProperty copy() {
    return new LongProperty(key(), logKey());
  }
}
