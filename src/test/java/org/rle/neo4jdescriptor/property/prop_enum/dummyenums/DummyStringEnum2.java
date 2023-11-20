package org.rle.neo4jdescriptor.property.prop_enum.dummyenums;

import org.rle.neo4jdescriptor.enuminterface.StringEnum;

public enum DummyStringEnum2 implements StringEnum {
  SMALL("weeee"),
  MEDIUM("meh"),
  LARGE("woah");

  private final String mDbValue;

  DummyStringEnum2(String dbValue) {
    this.mDbValue = dbValue;
  }

  public String dbValue() {
    return mDbValue;
  }
}
