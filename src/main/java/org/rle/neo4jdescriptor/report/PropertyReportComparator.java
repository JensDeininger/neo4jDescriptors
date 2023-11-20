package org.rle.neo4jdescriptor.report;

import java.util.Comparator;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;

/**
 * This comparator works ONLY on the {@link PropertyDescriptor}s.<p>
 * Thus, equal {@link PropertyReport}s will compare to 0, but some unequal ones may as well
 */
public class PropertyReportComparator implements Comparator<PropertyReport> {

  @Override
  public int compare(PropertyReport o1, PropertyReport o2) {
    PropertyDescriptor p1 = (o1 == null) ? null : o1.propertyDescriptor();
    PropertyDescriptor p2 = (o2 == null) ? null : o2.propertyDescriptor();
    return EqualityUtils.itemCompare(p1, p2);
  }
}
