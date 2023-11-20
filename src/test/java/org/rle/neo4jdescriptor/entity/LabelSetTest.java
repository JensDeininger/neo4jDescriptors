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
import org.rle.neo4jdescriptor.testutils.TestBase;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LabelSetTest extends TestBase {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(LabelSetTestProcedure.class);
  }

  @Override
  protected String initialCypher() {
    return LabelSetTestProcedure.cypherSetup();
  }

  private LabelSet openSample() {
    LabelSet lblSpec = LabelSet.empty();
    lblSpec.add(new LabelDescriptor("labelName1", "logLabelName1"));
    lblSpec.add(new LabelDescriptor("labelName2", "logLabelName2"));
    lblSpec.add(new LabelDescriptor("labelName3", "logLabelName3"));
    return lblSpec;
  }

  @Test
  public void nullArgTest() {
    LabelSet lblSpec = LabelSet.empty();
    assertThrows(IllegalArgumentException.class, () -> lblSpec.add(null));
  }

  @Test
  public void configTest() {
    LabelSet lblSpec = openSample();
    LabelDescriptor lblDesc = new LabelDescriptor("fooBar");
    lblSpec.add(lblDesc);
    assertTrue(lblSpec.labels().anyMatch(o -> o.equals(lblDesc)));
  }

  @Test
  public void closedIsImmutableTest() {
    LabelSet lblSpec = openSample();
    lblSpec.closeDefinition();
    LabelDescriptor lblDesc = new LabelDescriptor("fooBar");
    assertThrows(IllegalStateException.class, () -> lblSpec.add(lblDesc));
  }

  @Test
  public void identicalDeclarationEqualityTest() {
    LabelSet lblSpec1 = openSample();
    LabelSet lblSpec2 = openSample();
    assertEquals(lblSpec1, lblSpec2, "open equals fail");
    assertEquals(lblSpec1.hashCode(), lblSpec2.hashCode(), "open hash fail");
    assertEquals(0, lblSpec1.compareTo(lblSpec2), "open compare fail");
    lblSpec1.closeDefinition();
    lblSpec2.closeDefinition();
    assertEquals(lblSpec1, lblSpec2, "closed equals fail");
    assertEquals(lblSpec1.hashCode(), lblSpec2.hashCode(), "closed hash fail");
    assertEquals(0, lblSpec1.compareTo(lblSpec2), "closed compare fail");
  }

  @Test
  public void copyEqualityTest() {
    LabelSet orig = openSample();
    orig.closeDefinition();
    LabelSet copy = orig.openCopy().closeDefinition();
    assertEquals(orig, copy, "closed equals fail");
    assertEquals(orig.hashCode(), copy.hashCode(), "closed hash fail");
    assertEquals(0, orig.compareTo(copy));
  }

  @Test
  public void negativeEqualityTest_closed() {
    LabelSet lblSpec1 = openSample();
    lblSpec1.closeDefinition();
    LabelSet lblSpec2 = openSample();
    assertNotEquals(lblSpec1, lblSpec2, "equals fail");
    assertNotEquals(0, lblSpec1.compareTo(lblSpec2), "compare fail");
  }

  @Test
  public void negativeEqualityTest_label() {
    LabelSet lblSpec1 = openSample();
    lblSpec1.add(new LabelDescriptor("foobar"));
    LabelSet lblSpec2 = openSample();
    assertNotEquals(lblSpec1, lblSpec2, "equals fail");
    assertNotEquals(0, lblSpec1.compareTo(lblSpec2), "compare fail");
  }

  @Test
  public void hashResetTest() {
    LabelSet labelSpec = openSample();
    int hash0 = labelSpec.hashCode();
    labelSpec.add(new LabelDescriptor("foobar"));
    int hash1 = labelSpec.hashCode();
    assertNotEquals(hash0, hash1, "fail 1");

    labelSpec.closeDefinition();
    int hash2 = labelSpec.hashCode();
    assertNotEquals(hash1, hash2, "fail 2");
  }

  @Test
  public void printTest() {
    String mI = "mmmm";
    String sI = "ss";
    LabelSet sample = openSample();
    String str = sample.print(mI, sI);
    String[] bits = str.split(System.lineSeparator());
    assertEquals(bits.length, sample.labelCount(), "fail 1");
    assertTrue(
      Arrays.stream(bits).allMatch(o -> o.startsWith(mI + sI)),
      "fail 2"
    );
    assertEquals("", LabelSet.empty().print(mI, sI), "fail 3");
  }

  @Test
  public void testAllProcedures() {
    List<String> failingProcs = super.runAllTestProcedures(
      LabelSetTestProcedure.SimpleTestProcedureName.class
    );
    String fails = String.join(
      System.lineSeparator(),
      failingProcs.stream().toArray(String[]::new)
    );
    assertTrue(failingProcs.isEmpty(), "failed procs: " + fails);
  }
}
