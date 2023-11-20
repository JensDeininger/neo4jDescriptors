package org.rle.neo4jdescriptor.report;

public class MessageWrapper {

  public static final String MESSAGE_COMPONANT_NAME = "message";

  /* The name of that public field must always be the same as the static String ComponentName.
   * This literal string is used on Map<String, Object> that gets returned from the neo4j database. */

  public final String message;

  public MessageWrapper(String error) {
    this.message = error;
  }

  @Override
  public String toString() {
    return message;
  }
}
