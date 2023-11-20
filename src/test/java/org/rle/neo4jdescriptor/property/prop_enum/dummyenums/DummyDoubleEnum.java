package org.rle.neo4jdescriptor.property.prop_enum.dummyenums;

import org.rle.neo4jdescriptor.enuminterface.DoubleEnum;

public enum DummyDoubleEnum implements DoubleEnum {
  SMALL(1.0),
  MEDIUM(2.0),
  LARGE(3.0);

  private final Double mDbValue;

  DummyDoubleEnum(Double dbValue) {
    this.mDbValue = dbValue;
  }

  public Double dbValue() {
    return mDbValue;
  }
}
