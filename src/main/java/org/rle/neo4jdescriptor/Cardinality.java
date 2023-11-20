package org.rle.neo4jdescriptor;

/**
 * Possible values: <p>
 * UNDEFINED<p>
 * ZERO_ONE<p>
 * ONE<p>
 * ZERO_MANY<p>
 * ONE_MANY<p>
 */
public enum Cardinality {
  UNDEFINED,
  ZERO_ONE,
  ONE,
  ZERO_MANY,
  ONE_MANY,
}
