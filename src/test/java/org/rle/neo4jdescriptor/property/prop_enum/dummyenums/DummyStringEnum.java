package org.rle.neo4jdescriptor.property.prop_enum.dummyenums;

import org.rle.neo4jdescriptor.enuminterface.StringEnum;

public enum DummyStringEnum implements StringEnum {
  SMALL("weeee"),
  MEDIUM("meh"),
  LARGE("woah");

  private final String mDbValue;

  DummyStringEnum(String dbValue) {
    this.mDbValue = dbValue;
  }

  public String dbValue() {
    return mDbValue;
  }
}
