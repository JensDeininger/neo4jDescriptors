package org.rle.neo4jdescriptor.testutils;

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
public class BoolMessageWrapper {

  public static final String BoolComponentName = "bool";

  public static final String MessageComponentName = "message";

  /* The name of that public field must always be the same as the static String ComponentName.
   * This literal string is used on Map<String, Object> that gets returned from the neo4j database. */

  public boolean bool;

  public String message;

  public BoolMessageWrapper(boolean bool) {
    this.bool = bool;
    this.message = "";
  }

  public BoolMessageWrapper(boolean bool, String message) {
    this.bool = bool;
    this.message = message;
  }
}
