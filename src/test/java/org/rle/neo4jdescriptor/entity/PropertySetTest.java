package org.rle.neo4jdescriptor.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.prop_basic.*;
import org.rle.neo4jdescriptor.testutils.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PropertySetTest extends TestBase {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(PropertySetTestProcedure.class);
  }

  @Override
  protected String initialCypher() {
    return PropertySetTestProcedure.cypherSetup();
  }

  private PropertySet openSample() {
    PropertySet propSpec = PropertySet.empty();
    propSpec.add(new BooleanProperty("boolProp"));
    propSpec.add(new DoubleProperty("doubleProp"));
    propSpec.add(new LongProperty("longProp"));
    propSpec.add(new NumberProperty("numberProp"));
    propSpec.add(new ObjectProperty("objectProp"));
    propSpec.add(new StringProperty("stringProp"));
    return propSpec;
  }

  @Test
  public void nullArgTest() {
    PropertySet propSpec = PropertySet.empty();
    Exception exc = null;
    try {
      propSpec.add(null);
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalArgumentException.class,
      DefinitionBase.M_ARGUMENT_MUST_NOT_BE_NULL_MSG
    );
  }

  @Test
  public void configTest() {
    PropertySet propSpec = openSample();
    LongProperty prop1 = new LongProperty("fooBar");
    propSpec.add(prop1);
    assertTrue(propSpec.properties().anyMatch(o -> o.equals(prop1)), "fail 1");
  }

  @Test
  public void closedIsImmutableTest() {
    PropertySet propSpec = PropertySet.empty();
    propSpec.closeDefinition();
    LongProperty prop = new LongProperty("foo");
    Exception exc = null;
    try {
      propSpec.add(prop);
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalStateException.class,
      DefinitionBase.M_CAN_NOT_MODIFY_CLOSED_MSG
    );
  }

  @Test
  public void idempotentAddTest() {
    PropertySet sample = openSample();
    int origCount = sample.propertiesCount();
    PropertyDescriptor prop = sample.properties().findFirst().get();
    sample.add(prop);
    assertEquals(origCount, sample.propertiesCount());
  }

  @Test
  public void sameKeyNewPropAddTest() {
    PropertySet sample = openSample();
    LongProperty prop1 = new LongProperty("fooBar");
    sample.add(prop1);
    StringProperty prop2 = new StringProperty("fooBar");
    Exception exc = null;
    try {
      sample.add(prop2);
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalArgumentException.class,
      PropertySet.M_KEY_IS_ALREADY_USED_BY_A_DIFFERENT_DESCRIPTOR
    );
  }

  @Test
  public void identicalDeclarationEqualityTest() {
    PropertySet propSpec1 = openSample();
    PropertySet propSpec2 = openSample();
    assertEquals(propSpec1, propSpec2, "open equals fail");
    assertEquals(propSpec1.hashCode(), propSpec2.hashCode(), "open hash fail");
    assertEquals(0, propSpec1.compareTo(propSpec2), "open compare fail");
    propSpec1.closeDefinition();
    propSpec2.closeDefinition();
    assertEquals(propSpec1, propSpec2, "closed equals fail");
    assertEquals(
      propSpec1.hashCode(),
      propSpec2.hashCode(),
      "closed hash fail"
    );
    assertEquals(0, propSpec1.compareTo(propSpec2), "closed compare fail");
  }

  @Test
  public void copyEqualityTest() {
    PropertySet orig = openSample();
    orig.closeDefinition();
    PropertySet copy = orig.openCopy().closeDefinition();
    assertEquals(orig, copy, "equals fail");
    assertEquals(orig.hashCode(), copy.hashCode(), "hash fail");
    assertEquals(0, orig.compareTo(copy), "compare fail");
  }

  @Test
  public void negativeEqualityTest_closed() {
    PropertySet propSpec1 = openSample();
    propSpec1.closeDefinition();
    PropertySet propSpec2 = openSample();
    assertNotEquals(propSpec1, propSpec2, "equals fail");
    assertNotEquals(0, propSpec1.compareTo(propSpec2), "compare fail");
  }

  @Test
  public void negativeEqualityTest_prop() {
    PropertySet propSpec1 = openSample();
    propSpec1.add(new BooleanProperty("hjkokujzh"));
    PropertySet propSpec2 = openSample();
    assertNotEquals(propSpec1, propSpec2, "equals fail");
    assertNotEquals(0, propSpec1.compareTo(propSpec2), "compare fail");
  }

  @Test
  public void hashResetTest() {
    BooleanProperty prop = new BooleanProperty("reqBoolProp");

    PropertySet propSpec = openSample();
    int hash0 = propSpec.hashCode();
    propSpec.add(prop);
    int hash1 = propSpec.hashCode();
    assertNotEquals(hash0, hash1, "fail 1");

    propSpec.closeDefinition();
    int hash2 = propSpec.hashCode();
    assertNotEquals(hash1, hash2, "fail 2");
  }

  @Test
  public void printTest() {
    String mI = "mmmm";
    String sI = "ss";
    PropertySet sample = openSample();
    String str = sample.print(mI, sI);
    String[] bits = str.split(System.lineSeparator());
    assertEquals(bits.length, sample.propertiesCount(), "fail 1");
    assertTrue(
      Arrays.stream(bits).allMatch(o -> o.startsWith(mI + sI)),
      "fail 2"
    );
    assertEquals("", PropertySet.empty().print(mI, sI), "fail 3");
  }

  @Test
  public void runDBTests() {
    List<String> procNames = getProcNames(
      PropertySetTestProcedure.SimpleTestProcedureName.class
    );
    List<String> failures = new ArrayList<>();
    for (String procName : procNames) {
      System.out.println("Running " + procName);
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
            "db test failure: ProcName: %s, Msg: %s",
            procName,
            msg
          );
          failures.add(failMsg);
        }
      }
    }
    String failMsg = String.join(System.lineSeparator(), failures);
    assertEquals(0, failures.size(), failMsg);
  }
}
