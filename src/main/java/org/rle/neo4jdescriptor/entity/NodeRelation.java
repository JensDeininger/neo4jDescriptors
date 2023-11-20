package org.rle.neo4jdescriptor.entity;

import java.util.Iterator;
import java.util.Objects;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.rle.neo4jdescriptor.Cardinality;
import org.rle.neo4jdescriptor.report.NodeRelationReport;

public abstract class NodeRelation implements Comparable<NodeRelation> {

  protected static final String ARGUMENT_MUST_NOT_BE_NULL =
    "argument must not be null";

  protected static final String DIRECTION_MUST_BE_INCOMING_OR_OUTGOING =
    "direction must be either INCOMING or OUTGOING";

  // region private fields

  private final RelationshipDescriptor mRelationshipDescriptor;

  private final Direction mDirection;

  private Integer mHash;

  // endregion

  // region ctor

  protected NodeRelation(RelationshipDescriptor relDesc, Direction dir) {
    if (relDesc == null) {
      throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
    }
    if (dir != Direction.INCOMING && dir != Direction.OUTGOING) {
      throw new IllegalArgumentException(
        DIRECTION_MUST_BE_INCOMING_OR_OUTGOING
      );
    }
    mRelationshipDescriptor = relDesc;
    mDirection = dir;
  }

  // endregion

  // region accessors

  public RelationshipDescriptor relationshipDescriptor() {
    return mRelationshipDescriptor;
  }

  public Direction direction() {
    return mDirection;
  }

  public abstract Cardinality cardinality();

  public String print(String indent) {
    String str = String.format(
      "%s, %s, %s",
      mRelationshipDescriptor.getClass().getSimpleName(),
      mDirection.toString(),
      cardinality().toString()
    );
    return indent + str;
  }

  // endregion

  protected abstract void reportCardinality(
    NodeRelationReport report,
    int actualCount
  );

  public NodeRelationReport validate(Node node) {
    NodeRelationReport report = new NodeRelationReport(this);
    try {
      if (node == null) {
        throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
      }
      Iterator<Relationship> relIter = node
        .getRelationships(mDirection, mRelationshipDescriptor.type())
        .iterator();
      int yayCount = 0;
      while (relIter.hasNext()) {
        Relationship rel = relIter.next();
        if (mRelationshipDescriptor.identifier().matches(rel)) {
          yayCount++;
        }
      }
      reportCardinality(report, yayCount);
    } catch (Exception exc) {
      report.addException(exc);
    }
    return report.closeReport();
  }

  // region hash, equals and compare

  /**
   * returns the combo hash of class, the RelationshipDescriptor and the direction
   */
  protected int processHash() {
    return Objects.hash(this.getClass(), mRelationshipDescriptor, mDirection);
  }

  /**
   * returns the equality of of class, the RelationshipDescriptor and the direction
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
    NodeRelation cast = NodeRelation.class.cast(obj);
    if (!this.mRelationshipDescriptor.equals(cast.mRelationshipDescriptor)) {
      return false;
    }
    return mDirection == cast.mDirection;
  }

  /**
   * returns the comparison of of class, the RelationshipDescriptor and the direction
   */
  protected int processCompare(NodeRelation other) {
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
    int relDescComp =
      this.relationshipDescriptor().compareTo(other.mRelationshipDescriptor);
    if (relDescComp != 0) {
      return relDescComp;
    }
    return this.mDirection.compareTo(other.mDirection);
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

  public int compareTo(NodeRelation other) {
    return processCompare(other);
  }
  // endregion
}
