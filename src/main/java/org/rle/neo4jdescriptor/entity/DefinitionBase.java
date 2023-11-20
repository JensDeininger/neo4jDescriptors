package org.rle.neo4jdescriptor.entity;

import java.util.Objects;

/**
 * Base class for all specifications, identifiers and definitions that are configured
 * during runtime and then closed to become immutable.
 */
public abstract class DefinitionBase implements Comparable<DefinitionBase> {

  // region static strings

  protected static final String M_ARGUMENT_MUST_NOT_BE_NULL_MSG =
    "argument must not be null";

  protected static final String M_CAN_NOT_MODIFY_CLOSED_MSG =
    "can not modify a closed Identifier any further";

  protected static final String M_AND = "AND";

  // endregion

  // region Private fields

  private Integer mHash;

  private boolean mIsClosed;

  //endregion

  public boolean isClosed() {
    return mIsClosed;
  }

  protected DefinitionBase closeDefinition() {
    if (!mIsClosed) {
      mHash = null;
      mIsClosed = true;
    }
    return this;
  }

  protected void isOpenOrThrow() {
    if (mIsClosed) {
      throw new IllegalStateException(M_CAN_NOT_MODIFY_CLOSED_MSG);
    }
  }

  protected void resetHash() {
    mHash = null;
  }

  // region equality, hash and compare overrides

  /**
   * returns the combo hash of class and the closed flag
   */
  protected int processHash() {
    return Objects.hash(this.getClass(), mIsClosed);
  }

  /**
   * returns the equality of class and the closed flag
   */
  protected boolean processEquals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!this.getClass().equals(obj.getClass())) {
      return false;
    }
    if (!(obj instanceof DefinitionBase)) {
      return false;
    }
    return mIsClosed == DefinitionBase.class.cast(obj).mIsClosed;
  }

  /**
   * returns the comparison of class and the closed flag
   */
  protected int processCompare(DefinitionBase other) {
    if (other == null) {
      return 1;
    }
    if (other == this) {
      return 0;
    }
    int typeComp =
      this.getClass().getName().compareTo(other.getClass().getName());
    if (typeComp != 0) {
      return typeComp;
    }
    return Boolean.compare(this.mIsClosed, other.mIsClosed);
  }

  @Override
  public int hashCode() {
    if (mHash == null) {
      mHash = processHash();
    }
    return mHash;
  }

  @Override
  public boolean equals(Object obj) {
    return processEquals(obj);
  }

  public int compareTo(DefinitionBase other) {
    return processCompare(other);
  }
  //endregion
}
