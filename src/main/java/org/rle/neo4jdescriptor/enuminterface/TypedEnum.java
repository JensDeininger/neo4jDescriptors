package org.rle.neo4jdescriptor.enuminterface;

public interface TypedEnum<T> {
  T dbValue();

  boolean isValueEqualTo(T t);

  boolean isValueEqualToObject(Object obj);

  String cypherString();
}
