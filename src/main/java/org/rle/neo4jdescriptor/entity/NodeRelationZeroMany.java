package org.rle.neo4jdescriptor.entity;

import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.Cardinality;
import org.rle.neo4jdescriptor.report.NodeRelationReport;

public class NodeRelationZeroMany extends NodeRelation {

  public NodeRelationZeroMany(RelationshipDescriptor relDesc, Direction dir) {
    super(relDesc, dir);
  }

  @Override
  public Cardinality cardinality() {
    return Cardinality.ZERO_MANY;
  }

  protected void reportCardinality(NodeRelationReport report, int actualCount) {
    report.setCountCheck(true);
  }
}
