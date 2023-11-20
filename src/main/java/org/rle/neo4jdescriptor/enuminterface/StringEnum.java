package org.rle.neo4jdescriptor.enuminterface;

import org.rle.neo4jdescriptor.CypherUtils;

public interface StringEnum extends TypedEnum<String> {
  default boolean isValueEqualTo(String str) {
    if (str == null) {
      return false;
    }
    return dbValue().equals(str);
  }

  default boolean isValueEqualToObject(Object obj) {
    if (!(obj instanceof String)) {
      return false;
    }
    return isValueEqualTo(String.class.cast(obj));
  }

  default String cypherString() {
    return CypherUtils.string2Cypher(dbValue());
  }
}
