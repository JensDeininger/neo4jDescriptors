package org.rle.neo4jdescriptor.property.prop_enum.dummyenums;

import org.rle.neo4jdescriptor.enuminterface.LongEnum;

public enum DummyLongEnum2 implements LongEnum {
  SMALL(1l),
  MEDIUM(2l),
  LARGE(3l);

  private final Long mDbValue;

  DummyLongEnum2(Long dbValue) {
    this.mDbValue = dbValue;
  }

  public Long dbValue() {
    return mDbValue;
  }
}
