package org.rle.neo4jdescriptor.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.rle.neo4jdescriptor.testutils.BoolMessageWrapper;
import org.rle.neo4jdescriptor.testutils.TestBase;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class PropertyTestBase extends TestBase {

  private final String key = PropertyTestProcedureBase.PropertyKey;

  private final String logKey = "log_" + PropertyTestProcedureBase.PropertyKey;

  protected abstract PropertyDescriptor createPropertyDescriptor(
    String key,
    String logKey
  );

  protected abstract PropertyDescriptor[] createDifferentTypedPropertyDescriptors(
    String key,
    String logKey
  );

  @Override
  protected String initialCypher() {
    return String.format(
      "CREATE (n1:%s)-[r:%s]->(n2:%s)",
      PropertyTestProcedureBase.DummyLabel.name(),
      PropertyTestProcedureBase.DummyRelType.name(),
      PropertyTestProcedureBase.DummyLabel.name() + "keks"
    );
  }

  /*****************************************************************/

  public void crapArgsTest() {
    String[] values = new String[] { null, "", "\t", System.lineSeparator() };
    for (String s1 : values) {
      assertThrows(
        IllegalArgumentException.class,
        () -> createPropertyDescriptor(s1, "foo")
      );
      assertThrows(
        IllegalArgumentException.class,
        () -> createPropertyDescriptor("foo", s1)
      );
      for (String s2 : values) {
        assertThrows(
          IllegalArgumentException.class,
          () -> createPropertyDescriptor(s1, s2)
        );
      }
    }
  }

  public void identicalDeclarationTest() {
    PropertyDescriptor prop1 = createPropertyDescriptor(key, logKey);
    PropertyDescriptor prop2 = createPropertyDescriptor(key, logKey);
    assertEquals(prop1.getClass(), prop2.getClass(), "fail 1");
    assertEquals(prop1, prop2, "fail 2");
    assertEquals(prop1.hashCode(), prop2.hashCode(), "fail 3");
    assertEquals(0, prop1.compareTo(prop2), "fail 4");
  }

  public void copyTest() {
    PropertyDescriptor orig = createPropertyDescriptor(key, logKey);
    PropertyDescriptor copy = orig.copy();
    assertEquals(copy.getClass(), orig.getClass(), "fail 1");
    assertEquals(copy, orig, "fail 2");
    assertEquals(copy.hashCode(), orig.hashCode(), "fail 3");
    assertEquals(0, copy.compareTo(orig), "fail 4");
  }

  public void unequalsTest() {
    PropertyDescriptor prop = createPropertyDescriptor(key, logKey);

    PropertyDescriptor diffKeyProp = createPropertyDescriptor(
      key + "foo",
      logKey
    );
    assertNotEquals(prop, diffKeyProp, "fail 1");
    assertNotEquals(0, prop.compareTo(diffKeyProp), "fail 2");

    PropertyDescriptor diffLogProp = createPropertyDescriptor(
      key,
      logKey + "foo"
    );
    assertNotEquals(prop, diffLogProp, "fail 3");
    assertNotEquals(0, prop.compareTo(diffLogProp), "fail 4");

    PropertyDescriptor[] diffProps = createDifferentTypedPropertyDescriptors(
      key,
      logKey
    );
    for (int i = 0; i < diffProps.length; i++) {
      PropertyDescriptor diff = diffProps[i];
      assertNotEquals(prop, diff, "fail 5 @index " + i);
      assertNotEquals(0, prop.compareTo(diff), "fail 6 @index " + i);
    }
  }

  public void unequalsTest_key() {
    PropertyDescriptor prop = createPropertyDescriptor(key, logKey);

    PropertyDescriptor diffKeyProp = createPropertyDescriptor(
      key + "foo",
      logKey
    );
    assertNotEquals(prop, diffKeyProp, "fail 1");
    assertNotEquals(0, prop.compareTo(diffKeyProp), "fail 2");
  }

  public void unequalsTest_logKey() {
    PropertyDescriptor prop = createPropertyDescriptor(key, logKey);

    PropertyDescriptor diffLogProp = createPropertyDescriptor(
      key,
      logKey + "foo"
    );
    assertNotEquals(prop, diffLogProp, "fail 1");
    assertNotEquals(0, prop.compareTo(diffLogProp), "fail 2");
  }

  public void unequalsTest_class() {
    PropertyDescriptor prop = createPropertyDescriptor(key, logKey);

    PropertyDescriptor[] diffProps = createDifferentTypedPropertyDescriptors(
      key,
      logKey
    );
    for (int i = 0; i < diffProps.length; i++) {
      PropertyDescriptor diff = diffProps[i];
      assertNotEquals(prop, diff, "fail 1 @index " + i);
      assertNotEquals(0, prop.compareTo(diff), "fail 2 @index " + i);
    }
  }

  public String[] runBasicPropertyTests(String className4FailMsg) {
    List<String> procNames = getProcNames(
      PropertyTestProcedureBase.SimpleTestProcedureName.class
    );
    List<String> failures = new ArrayList<>();
    for (String procName : procNames) {
      System.out.println("Running " + procName + " for " + className4FailMsg);
      try (Session session = driver().session()) {
        String cypher = String.format(
          "CALL %s() YIELD %s, %s RETURN %s, %s",
          procName,
          BoolMessageWrapper.BoolComponentName,
          BoolMessageWrapper.MessageComponentName,
          BoolMessageWrapper.BoolComponentName,
          BoolMessageWrapper.MessageComponentName
        );
        Record record = session.run(cypher).next();
        boolean success = record
          .get(BoolMessageWrapper.BoolComponentName)
          .asBoolean();
        if (!success) {
          String msg = record
            .get(BoolMessageWrapper.MessageComponentName)
            .asString();
          String failMsg = String.format(
            "basic property test failure: ProcName: %s, Class: %s, Msg: %s",
            procName,
            className4FailMsg,
            msg
          );
          failures.add(failMsg);
        }
      }
    }
    return failures.toArray(String[]::new);
  }
}
