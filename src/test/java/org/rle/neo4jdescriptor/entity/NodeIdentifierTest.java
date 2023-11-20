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
import org.rle.neo4jdescriptor.property.prop_basic.BooleanProperty;
import org.rle.neo4jdescriptor.property.prop_basic.DoubleProperty;
import org.rle.neo4jdescriptor.property.prop_basic.LongProperty;
import org.rle.neo4jdescriptor.property.prop_basic.NumberProperty;
import org.rle.neo4jdescriptor.property.prop_basic.ObjectProperty;
import org.rle.neo4jdescriptor.property.prop_basic.StringProperty;
import org.rle.neo4jdescriptor.testutils.TestBase;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NodeIdentifierTest extends TestBase {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(NodeIdentifierTestProcedure.class);
  }

  @Override
  protected String initialCypher() {
    return NodeIdentifierTestProcedure.cypherSetup();
  }

  private NodeIdentifier openSample() {
    NodeIdentifier nodeId = NodeIdentifier.empty();
    nodeId.add(new LabelDescriptor("labelName1", "logLabelName1"));
    nodeId.add(new LabelDescriptor("labelName2", "logLabelName2"));
    nodeId.add(new LabelDescriptor("labelName3", "logLabelName3"));
    nodeId.add(new BooleanProperty("boolProp"));
    nodeId.add(new DoubleProperty("doubleProp"));
    nodeId.add(new LongProperty("longProp"));
    nodeId.add(new NumberProperty("numberProp"));
    nodeId.add(new ObjectProperty("objectProp"));
    nodeId.add(new StringProperty("stringProp"));
    return nodeId;
  }

  @Test
  public void nullArgsTest() {
    NodeIdentifier nodeId = NodeIdentifier.empty();
    assertThrows(
      IllegalArgumentException.class,
      () -> nodeId.add((LabelDescriptor) null),
      "fail label"
    );
    assertThrows(
      IllegalArgumentException.class,
      () -> nodeId.add((PropertyDescriptor) null),
      "fail prop"
    );
  }

  @Test
  public void configTest() {
    NodeIdentifier nodeId = NodeIdentifier.empty();
    LabelDescriptor lbl = new LabelDescriptor("foo", "bar");
    nodeId.add(lbl);
    assertTrue(nodeId.labels().anyMatch(o -> o.equals(lbl)), "fail label");
    PropertyDescriptor propDesc = new LongProperty("foo");
    nodeId.add(propDesc);
    assertTrue(
      nodeId.properties().anyMatch(o -> o.equals(propDesc)),
      "fail prop"
    );
  }

  @Test
  public void closedIsImmutableTest() {
    NodeIdentifier nodeId = NodeIdentifier.empty();
    LabelDescriptor lbl = new LabelDescriptor("foo", "bar");
    nodeId.add(lbl).closeDefinition();
    PropertyDescriptor propDesc = new LongProperty("foo");
    assertThrows(
      IllegalStateException.class,
      () -> nodeId.add(lbl),
      "label fail"
    );
    assertThrows(
      IllegalStateException.class,
      () -> nodeId.add(propDesc),
      "prop fail"
    );
  }

  @Test
  public void identicalDeclarationEqualityTest() {
    NodeIdentifier nodeId1 = openSample();
    NodeIdentifier nodeId2 = openSample();
    assertEquals(nodeId1, nodeId2, "open equals fail");
    assertEquals(nodeId1.hashCode(), nodeId2.hashCode(), "open hash fail");
    assertEquals(0, nodeId1.compareTo(nodeId2), "open compare fail");
    nodeId1.closeDefinition();
    nodeId2.closeDefinition();
    assertEquals(nodeId1, nodeId2, "closed equals fail");
    assertEquals(nodeId1.hashCode(), nodeId2.hashCode(), "closed hash fail");
    assertEquals(0, nodeId1.compareTo(nodeId2), "closed compare fail");
  }

  @Test
  public void copyEqualityTest() {
    NodeIdentifier orig = openSample();
    orig.closeDefinition();
    NodeIdentifier copy = orig.openCopy().closeDefinition();
    assertEquals(orig, copy, "closed equals fail");
    assertEquals(orig.hashCode(), copy.hashCode(), "closed hash fail");
    assertEquals(0, orig.compareTo(copy));
  }

  @Test
  public void negativeEqualityTest_closed() {
    NodeIdentifier nodeId1 = openSample();
    nodeId1.closeDefinition();
    NodeIdentifier nodeId2 = openSample();
    assertNotEquals(nodeId1, nodeId2, "equals fail");
    assertNotEquals(0, nodeId1.compareTo(nodeId2), "compare fail");
  }

  @Test
  public void negativeEqualityTest_label() {
    NodeIdentifier nodeId1 = openSample();
    nodeId1.add(new LabelDescriptor("hjkokujzh"));
    NodeIdentifier nodeId2 = openSample();
    assertNotEquals(nodeId1, nodeId2, "equals fail");
    assertNotEquals(0, nodeId1.compareTo(nodeId2), "compare fail");
  }

  @Test
  public void negativeEqualityTest_property() {
    NodeIdentifier nodeId1 = openSample();
    nodeId1.add(new LongProperty("hjkokujzh"));
    NodeIdentifier nodeId2 = openSample();
    assertNotEquals(nodeId1, nodeId2, "equals fail");
    assertNotEquals(0, nodeId1.compareTo(nodeId2), "compare fail");
  }

  @Test
  public void hashResetTest() {
    NodeIdentifier nodeId = openSample();
    int hash0 = nodeId.hashCode();
    nodeId.add(new LabelDescriptor("foo"));
    int hash1 = nodeId.hashCode();
    assertNotEquals(hash0, hash1, "fail 1");

    nodeId.add(new LongProperty("bar"));
    int hash2 = nodeId.hashCode();
    assertNotEquals(hash1, hash2, "fail 2");

    nodeId.closeDefinition();
    int hash3 = nodeId.hashCode();
    assertNotEquals(hash2, hash3, "fail 2");
  }

  @Test
  public void printTest() {
    String mI = "mmmm";
    String sI = "ss";
    NodeIdentifier sample = openSample();
    String str = sample.print(mI, sI);
    String[] bits = str.split(System.lineSeparator());
    int count = sample.labelCount() + sample.propertiesCount();
    assertEquals(bits.length, count, "fail 1");
    assertTrue(
      Arrays.stream(bits).allMatch(o -> o.startsWith(mI + sI)),
      "fail 2"
    );
    assertEquals("", NodeIdentifier.empty().print(mI, sI), "fail 3");
  }

  @Test
  public void testAllProcedures() {
    List<String> failingProcs = super.runAllTestProcedures(
      NodeIdentifierTestProcedure.SimpleTestProcedureName.class
    );
    String fails = String.join(
      System.lineSeparator(),
      failingProcs.stream().toArray(String[]::new)
    );
    assertTrue(failingProcs.isEmpty(), "failed procs: " + fails);
  }
}
