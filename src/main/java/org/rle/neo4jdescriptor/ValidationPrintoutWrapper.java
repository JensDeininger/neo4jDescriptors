package org.rle.neo4jdescriptor;

/**
 * These classes can only have public non-final fields, and the fields must
 * be one of the following types:
 *
 * <ul>
 *     <li>{@link String}</li>
 *     <li>{@link Long} or {@code long}</li>
 *     <li>{@link Double} or {@code double}</li>
 *     <li>{@link Number}</li>
 *     <li>{@link Boolean} or {@code boolean}</li>
 *     <li>{@link Node}</li>
 *     <li>{@link org.neo4j.graphdb.Relationship}</li>
 *     <li>{@link org.neo4j.graphdb.Path}</li>
 *     <li>{@link Map} with key {@link String} and value {@link Object}</li>
 *     <li>{@link List} of elements of any valid field type, including {@link List}</li>
 *     <li>{@link Object}, meaning any of the valid field types</li>
 * </ul>
 */
@SuppressWarnings("java:S1104") // complains about there being public non static non final fields and no accessors. But Neo4j needs those in its wrapper objects
public class ValidationPrintoutWrapper {

  public static final String CONTENT_NAME = "validationPrintout";

  public String validationPrintout;

  public ValidationPrintoutWrapper(String printout) {
    validationPrintout = printout;
  }
}
