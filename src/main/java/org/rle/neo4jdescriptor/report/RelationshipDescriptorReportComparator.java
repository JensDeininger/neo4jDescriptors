package org.rle.neo4jdescriptor.report;

import java.util.Comparator;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;

/**
 * This comparator works ONLY on the {@link RelationshipDescriptor}s.<p>
 * Thus, equal {@link RelationshipDescriptorReport}s will compare to 0, but some unequal ones may as well
 */
public class RelationshipDescriptorReportComparator
  implements Comparator<RelationshipDescriptorReport> {

  @Override
  public int compare(
    RelationshipDescriptorReport o1,
    RelationshipDescriptorReport o2
  ) {
    RelationshipDescriptor nd1 = (o1 == null)
      ? null
      : o1.relationshipDescriptor();
    RelationshipDescriptor nd2 = (o2 == null)
      ? null
      : o2.relationshipDescriptor();
    return EqualityUtils.itemCompare(nd1, nd2);
  }
}
