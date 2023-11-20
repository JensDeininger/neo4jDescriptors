package org.rle.neo4jdescriptor.enuminterface;

import org.rle.neo4jdescriptor.CypherUtils;
import org.rle.neo4jdescriptor.EqualityUtils;

public interface DoubleEnum extends TypedEnum<Double> {
  default boolean isValueEqualTo(Double dub) {
    return EqualityUtils.doubleEquals(dbValue(), dub);
  }

  default boolean isValueEqualToObject(Object obj) {
    if (!(obj instanceof Double)) {
      return false;
    }
    return EqualityUtils.doubleEquals(dbValue(), Double.class.cast(obj));
  }

  default String cypherString() {
    return CypherUtils.double2Cypher(dbValue());
  }
}
