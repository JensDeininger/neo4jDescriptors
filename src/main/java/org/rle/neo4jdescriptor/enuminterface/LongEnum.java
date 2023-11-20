package org.rle.neo4jdescriptor.enuminterface;

import org.rle.neo4jdescriptor.CypherUtils;

public interface LongEnum extends TypedEnum<Long> {
  default boolean isValueEqualTo(Long l) {
    if (l == null) {
      return false;
    }
    return dbValue() == l.longValue();
  }

  default boolean isValueEqualToObject(Object obj) {
    if (!(obj instanceof Long)) {
      return false;
    }
    return isValueEqualTo(Long.class.cast(obj));
  }

  default String cypherString() {
    return CypherUtils.long2Cypher(dbValue());
  }
}
