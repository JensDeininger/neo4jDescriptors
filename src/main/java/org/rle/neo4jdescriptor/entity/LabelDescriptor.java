package org.rle.neo4jdescriptor.entity;

import org.neo4j.graphdb.Label;
import org.rle.neo4jdescriptor.dto.entity.LabelDescriptorDto;

/**
 * A {@link LabelDescriptor} describes a label in the neo4j database.
 * <p>
 * It is fully defined by two {@link String}s, the name and the logName, both of
 * which must be specified in the constructor.
 * <p>
 * The name must be the name of the label in the database.
 * <p>
 * The logName is optional and will be the same as the name if not specified in
 * the constructor. It can be used to record database operations in logs or archives
 * so that old records are not affected by changes to the label name in the database.
 * <p>
 * {@link LabelDescriptor} overrides {@link #equals() equals()} and
 * {@link #hashCode() hashCode()} so that equality is determined only by the name(s).
 * <p>
 * A {@link LabelDescriptor} is immutable, so it can safely be used in {@link HashSet},
 * {@link HashMap} and the like.
 */
public class LabelDescriptor implements Label, Comparable<LabelDescriptor> {

  // region private fields

  private final String mName;

  private final String mLogName;

  private final int mHash;

  // endregion

  // region Ctor

  public LabelDescriptor(String name) {
    this(name, name);
  }

  public LabelDescriptor(LabelDescriptorDto dto) {
    this(dto.getName(), dto.getLogName());
  }

  public LabelDescriptor(String name, String logName) {
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

  public LabelDescriptor copy() {
    return new LabelDescriptor(mName, mLogName);
  }

  public LabelDescriptorDto dto() {
    return new LabelDescriptorDto(mName, mLogName);
  }

  public String print(String indent) {
    String ind = indent == null ? "" : indent;
    String res;
    if (mName.equals(mLogName)) {
      res = String.format("Label(%s)", mName);
    } else {
      res = String.format("Label(%s,%s)", mName, mLogName);
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
    if (!(obj instanceof LabelDescriptor)) {
      return false;
    }
    return equals(LabelDescriptor.class.cast(obj));
  }

  public boolean equals(LabelDescriptor other) {
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
  public int compareTo(LabelDescriptor other) {
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
