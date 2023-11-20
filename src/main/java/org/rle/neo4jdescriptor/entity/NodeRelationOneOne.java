package org.rle.neo4jdescriptor.entity;

import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.Cardinality;
import org.rle.neo4jdescriptor.report.NodeRelationReport;

public class NodeRelationOneOne extends NodeRelation {

  public NodeRelationOneOne(RelationshipDescriptor relDesc, Direction dir) {
    super(relDesc, dir);
  }

  @Override
  public Cardinality cardinality() {
    return Cardinality.ONE;
  }

  protected void reportCardinality(NodeRelationReport report, int actualCount) {
    if (actualCount == 1) {
      report.setCountCheck(true);
    } else {
      report.setCountCheck(false).setDeviantCount(actualCount);
    }
  }
}
