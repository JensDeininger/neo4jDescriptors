package org.rle.neo4jdescriptor.report;

import java.util.Comparator;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;

/**
 * This comparator works ONLY on the {@link NodeDescriptor}s.<p>
 * Thus, equal {@link NodeDescriptorReport}s will compare to 0, but some unequal ones may as well
 */
public class NodeDescriptorReportComparator
  implements Comparator<NodeDescriptorReport> {

  @Override
  public int compare(NodeDescriptorReport o1, NodeDescriptorReport o2) {
    NodeDescriptor nd1 = (o1 == null) ? null : o1.nodeDescriptor();
    NodeDescriptor nd2 = (o2 == null) ? null : o2.nodeDescriptor();
    return EqualityUtils.itemCompare(nd1, nd2);
  }
}
