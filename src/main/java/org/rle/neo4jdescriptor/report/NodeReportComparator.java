package org.rle.neo4jdescriptor.report;

import java.util.Comparator;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;

/**
 * This comparator works ONLY on the {@link NodeDescriptor}s and the dbIds.<p>
 * Thus, equal {@link NodeReport}s will compare to 0, but some unequal ones may as well
 */
public class NodeReportComparator implements Comparator<NodeReport> {

  @Override
  public int compare(NodeReport o1, NodeReport o2) {
    NodeDescriptor nd1 = (o1 == null) ? null : o1.nodeDescriptor();
    NodeDescriptor nd2 = (o2 == null) ? null : o2.nodeDescriptor();
    int nodeDescriptorComp = EqualityUtils.itemCompare(nd1, nd2);
    if (nodeDescriptorComp != 0) {
      return nodeDescriptorComp;
    }
    String db1 = (o1 == null) ? "" : o1.dbId();
    String db2 = (o2 == null) ? "" : o2.dbId();
    return db1.compareTo(db2);
  }
}
