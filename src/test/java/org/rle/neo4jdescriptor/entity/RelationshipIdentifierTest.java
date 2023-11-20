package org.rle.neo4jdescriptor.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.prop_basic.*;
import org.rle.neo4jdescriptor.testutils.TestBase;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RelationshipIdentifierTest extends TestBase {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(RelationshipIdentifierTestProcedure.class);
  }

  @Override
  protected String initialCypher() {
    return RelationshipIdentifierTestProcedure.cypherSetup();
  }

  private RelationshipIdentifier openSample() {
    RelationshipIdentifier relId = RelationshipIdentifier.empty();
    relId.setType(new RelationshipTypeDescriptor("typeFoo"));
    relId.add(new BooleanProperty("boolProp"));
    relId.add(new DoubleProperty("doubleProp"));
    relId.add(new LongProperty("longProp"));
    relId.add(new NumberProperty("numberProp"));
    relId.add(new ObjectProperty("objectProp"));
    relId.add(new StringProperty("stringProp"));
    return relId;
  }

  @Test
  public void nullArgsTest() {
    RelationshipIdentifier nodeId = RelationshipIdentifier.empty();
    assertThrows(
      IllegalArgumentException.class,
      () -> nodeId.add((PropertyDescriptor) null),
      "prop fail"
    );
  }

  @Test
  public void configTest() {
    RelationshipIdentifier relId = RelationshipIdentifier.empty();
    RelationshipTypeDescriptor type = new RelationshipTypeDescriptor("foo");
    relId.setType(type);
    assertEquals(type, relId.relationshipTypeDescriptor(), "fail type");

    PropertyDescriptor propDesc = new LongProperty("foo");
    relId.add(propDesc);
    assertTrue(
      relId.properties().anyMatch(o -> o.equals(propDesc)),
      "fail prop"
    );
  }

  @Test
  public void closedIsImmutableTest() {
    RelationshipIdentifier nodeId = RelationshipIdentifier
      .empty()
      .closeDefinition();
    RelationshipTypeDescriptor type = new RelationshipTypeDescriptor("typeFoo");
    assertThrows(
      IllegalStateException.class,
      () -> nodeId.setType(type),
      "type fail"
    );
    PropertyDescriptor propDesc = new LongProperty("foo");
    assertThrows(
      IllegalStateException.class,
      () -> nodeId.add(propDesc),
      "prop fail"
    );
  }

  @Test
  public void identicalDeclarationEqualityTest() {
    RelationshipIdentifier relId1 = openSample();
    RelationshipIdentifier relId2 = openSample();
    assertEquals(relId1, relId2, "open equals fail");
    assertEquals(relId1.hashCode(), relId2.hashCode(), "open hash fail");
    assertEquals(0, relId1.compareTo(relId2), "open compare fail");
    relId1.closeDefinition();
    relId2.closeDefinition();
    assertEquals(relId1, relId2, "closed equals fail");
    assertEquals(relId1.hashCode(), relId2.hashCode(), "closed hash fail");
    assertEquals(0, relId1.compareTo(relId2), "closed compare fail");
  }

  @Test
  public void copyEqualityTest() {
    RelationshipIdentifier orig = openSample();
    orig.closeDefinition();
    RelationshipIdentifier copy = orig.openCopy().closeDefinition();
    assertEquals(orig, copy, "closed equals fail");
    assertEquals(orig.hashCode(), copy.hashCode(), "closed hash fail");
    assertEquals(0, orig.compareTo(copy));
  }

  @Test
  public void negativeEqualityTest_closed() {
    RelationshipIdentifier relId1 = openSample();
    relId1.closeDefinition();
    RelationshipIdentifier relId2 = openSample();
    assertNotEquals(relId1, relId2, "equals fail");
    assertNotEquals(0, relId1.compareTo(relId2), "compare fail");
  }

  @Test
  public void negativeEqualityTest_type() {
    RelationshipIdentifier relId1 = openSample();
    relId1.setType(new RelationshipTypeDescriptor("foo"));
    RelationshipIdentifier relId2 = openSample();
    relId2.setType(new RelationshipTypeDescriptor("bar"));
    assertNotEquals(relId1, relId2, "equals fail");
    assertNotEquals(0, relId1.compareTo(relId2), "compare fail");
  }

  @Test
  public void negativeEqualityTest_property() {
    RelationshipIdentifier nodeId1 = openSample();
    nodeId1.add(new LongProperty("foo"));
    RelationshipIdentifier nodeId2 = openSample();
    assertNotEquals(nodeId1, nodeId2, "equals fail");
    assertNotEquals(0, nodeId1.compareTo(nodeId2), "compare fail");
  }

  @Test
  public void hashResetTest() {
    RelationshipIdentifier relId = openSample();
    int hash0 = relId.hashCode();
    relId.setType(new RelationshipTypeDescriptor("foo"));
    int hash1 = relId.hashCode();
    assertNotEquals(hash0, hash1, "fail 1");

    relId.add(new LongProperty("bar"));
    int hash2 = relId.hashCode();
    assertNotEquals(hash1, hash2, "fail 2");

    relId.closeDefinition();
    int hash3 = relId.hashCode();
    assertNotEquals(hash2, hash3, "fail 3");
  }

  @Test
  public void printTest() {
    String mI = "mmmm";
    String sI = "ss";
    RelationshipIdentifier sample = openSample();
    String str = sample.print(mI, sI);
    String[] bits = str.split(System.lineSeparator());
    int count = sample.propertiesCount() + 1;
    assertEquals(bits.length, count, "fail 1");
    assertTrue(
      Arrays.stream(bits).allMatch(o -> o.startsWith(mI + sI)),
      "fail 2"
    );
    assertEquals("", RelationshipIdentifier.empty().print(mI, sI), "fail 3");
  }

  @Test
  public void testAllProcedures() {
    List<String> failingProcs = super.runAllTestProcedures(
      RelationshipIdentifierTestProcedure.SimpleTestProcedureName.class
    );
    String fails = String.join(
      System.lineSeparator(),
      failingProcs.stream().toArray(String[]::new)
    );
    assertTrue(failingProcs.isEmpty(), "failed procs: " + fails);
  }
}
