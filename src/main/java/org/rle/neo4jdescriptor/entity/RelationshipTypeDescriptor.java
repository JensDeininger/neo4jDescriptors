package org.rle.neo4jdescriptor.entity;

import org.neo4j.graphdb.RelationshipType;
import org.rle.neo4jdescriptor.dto.entity.RelationshipTypeDescriptorDto;

public class RelationshipTypeDescriptor
  implements RelationshipType, Comparable<RelationshipTypeDescriptor> {

  //region private fields

  private final String mName;

  private final String mLogName;

  private final int mHash;

  //endregion

  // region Ctor

  public RelationshipTypeDescriptor(String name) {
    this(name, name);
  }

  public RelationshipTypeDescriptor(RelationshipTypeDescriptorDto dto) {
    this(dto.getName(), dto.getLogName());
  }

  public RelationshipTypeDescriptor(String name, String logName) {
    if (
      name == null ||
      name.trim().isEmpty() ||
      logName == null ||
      logName.trim().isEmpty()
    ) {
      throw new IllegalArgumentException(
        "argument must not be null, empty or blank"
      );
    }
    mName = name;
    mLogName = logName;
    mHash = (mName.hashCode() * 397) ^ mLogName.hashCode();
  }

  // endregion

  // region accessors

  @Override
  public String name() {
    return mName;
  }

  public String logName() {
    return mLogName;
  }

  public RelationshipTypeDescriptor copy() {
    return new RelationshipTypeDescriptor(mName, mLogName);
  }

  public RelationshipTypeDescriptorDto dto() {
    return new RelationshipTypeDescriptorDto(mName, mLogName);
  }

  public String print(String indent) {
    String ind = indent == null ? "" : indent;
    String res;
    if (mName.equals(mLogName)) {
      res = String.format("RelationshipType(%s)", mName);
    } else {
      res = String.format("RelationshipType(%s,%s)", mName, mLogName);
    }
    return ind + res;
  }

  // endregion

  // region equals, hash and compare overrides

  @Override
  public int hashCode() {
    return mHash;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof RelationshipTypeDescriptor)) {
      return false;
    }
    return equals(RelationshipTypeDescriptor.class.cast(obj));
  }

  public boolean equals(RelationshipTypeDescriptor other) {
    if (other == null) {
      return false;
    }
    if (other == this) {
      return true;
    }
    return (
      this.mName.equals(other.mName) && this.mLogName.equals(other.mLogName)
    );
  }

  @Override
  public int compareTo(RelationshipTypeDescriptor other) {
    if (other == null) {
      return 1;
    }
    int nameComp = this.mName.compareTo(other.mName);
    if (nameComp != 0) {
      return nameComp;
    }
    return this.mLogName.compareTo(other.mLogName);
  }
  // endregion
}
