package org.rle.neo4jdescriptor.testutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

public class TestUtilities {

  private static final String NO_EXCEPTION = "failed to throw any exception";

  private static final String WRONG_EXCEPTION = "unexpected exception type";

  private static final String WRONG_EXCEPTION_MSG =
    "unexpected exception message";

  private static final String NO_EXCEPTION_AT =
    "failed to throw any exception @ %d";

  private static final String WRONG_EXCEPTION_AT =
    "unexpected exception type  @ %d";

  private static final String WRONG_EXCEPTION_MSG_AT =
    "unexpected exception message  @ %d";

  public static void checkException(
    Exception exc,
    Class<? extends Exception> expectedType,
    String expectedMsg
  ) {
    assertNotNull(exc, NO_EXCEPTION);
    assertTrue(expectedType.isAssignableFrom(exc.getClass()), WRONG_EXCEPTION);
    assertEquals(expectedMsg, exc.getMessage(), WRONG_EXCEPTION_MSG);
  }

  public static void checkException(
    Exception exc,
    Class<? extends Exception> expectedType,
    String expectedMsg,
    int index
  ) {
    assertNotNull(exc, String.format(NO_EXCEPTION_AT, index));
    assertTrue(
      expectedType.isAssignableFrom(exc.getClass()),
      String.format(WRONG_EXCEPTION_AT, index)
    );
    assertEquals(
      expectedMsg,
      exc.getMessage(),
      String.format(WRONG_EXCEPTION_MSG_AT, index)
    );
  }

  public static Stream<BoolMessageWrapper> checkExceptionReport(
    Exception exc,
    Class<? extends Exception> expectedType,
    String expectedMsg
  ) {
    if (
      exc == null ||
      !expectedType.isAssignableFrom(exc.getClass()) ||
      !exc.getMessage().equals(expectedMsg)
    ) {
      return Stream.of(
        new BoolMessageWrapper(false, "no fancy msg, gotta debug anyway")
      );
    }
    return Stream.of(new BoolMessageWrapper(true, ""));
  }
}
