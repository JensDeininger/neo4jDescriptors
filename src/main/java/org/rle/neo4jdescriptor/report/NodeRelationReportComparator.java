package org.rle.neo4jdescriptor.report;

import java.util.Comparator;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.entity.NodeRelation;

/**
 * This comparator works ONLY on the {@link NodeRelation}s.<p>
 * Thus, equal {@link NodeRelationReport}s will compare to 0, but some unequal ones may as well
 */
public class NodeRelationReportComparator
  implements Comparator<NodeRelationReport> {

  @Override
  public int compare(NodeRelationReport o1, NodeRelationReport o2) {
    NodeRelation nr1 = (o1 == null) ? null : o1.nodeRelation();
    NodeRelation nr2 = (o2 == null) ? null : o2.nodeRelation();
    return EqualityUtils.itemCompare(nr1, nr2);
  }
}
