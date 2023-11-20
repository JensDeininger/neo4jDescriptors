package org.rle.neo4jdescriptor.report;

import java.util.Comparator;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;

/**
 * This comparator works ONLY on the {@link RelationshipDescriptor}s and the dbIds.<p>
 * Thus, equal {@link RelationshipReport}s will compare to 0, but some unequal ones may as well
 */
public class RelationshipReportComparator
  implements Comparator<RelationshipReport> {

  @Override
  public int compare(RelationshipReport o1, RelationshipReport o2) {
    RelationshipDescriptor nd1 = (o1 == null)
      ? null
      : o1.relationshipDescriptor();
    RelationshipDescriptor nd2 = (o2 == null)
      ? null
      : o2.relationshipDescriptor();
    int relDescriptorComp = EqualityUtils.itemCompare(nd1, nd2);
    if (relDescriptorComp != 0) {
      return relDescriptorComp;
    }
    String db1 = (o1 == null) ? "" : o1.dbId();
    String db2 = (o2 == null) ? "" : o2.dbId();
    return db1.compareTo(db2);
  }
}
