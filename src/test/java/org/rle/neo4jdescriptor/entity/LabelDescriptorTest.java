package org.rle.neo4jdescriptor.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LabelDescriptorTest {

  @Test
  public void crapArgsTest() {
    String[] values = new String[] { null, "", "\t", System.lineSeparator() };
    for (String s1 : values) {
      assertThrows(
        IllegalArgumentException.class,
        () -> new LabelDescriptor(s1, "foo")
      );
      assertThrows(
        IllegalArgumentException.class,
        () -> new LabelDescriptor("foo", s1)
      );
      for (String s2 : values) {
        assertThrows(
          IllegalArgumentException.class,
          () -> new LabelDescriptor(s1, s2)
        );
      }
    }
  }

  @Test
  public void inputValueTest() {
    String name = "foo";
    String logName = "bar";
    LabelDescriptor lbl = new LabelDescriptor(name, logName);
    assertEquals(lbl.name(), name);
    assertEquals(lbl.logName(), logName);
  }

  @Test
  public void identicalDeclarationEqualsTest() {
    String name = "foo";
    String logName = "bar";
    LabelDescriptor lbl1 = new LabelDescriptor(name, logName);
    LabelDescriptor lbl2 = new LabelDescriptor(name, logName);
    assertEquals(lbl1.hashCode(), lbl2.hashCode(), "hashCode failure");
    assertEquals(lbl1, lbl2, "Equality failure");
    assertEquals(0, lbl1.compareTo(lbl2), "Compare failure");
  }

  @Test
  public void copyEqualsTest() {
    LabelDescriptor lbl = new LabelDescriptor("foo", "bar");
    LabelDescriptor copy = lbl.copy();
    assertEquals(lbl.hashCode(), copy.hashCode(), "hashCode failure");
    assertEquals(lbl, copy, "equality failure");
    assertEquals(0, lbl.compareTo(copy), "Compare failure");
  }

  @Test
  public void unqualsTest_dbKey() {
    LabelDescriptor lbl1 = new LabelDescriptor("foo1", "bar");
    LabelDescriptor lbl2 = new LabelDescriptor("foo2", "bar");
    assertNotEquals(lbl1, lbl2, "unequality failure");
    assertNotEquals(0, lbl1.compareTo(lbl2), "compare failure");
  }

  @Test
  public void unqualsTest_logKey() {
    LabelDescriptor lbl1 = new LabelDescriptor("foo", "bar1");
    LabelDescriptor lbl2 = new LabelDescriptor("foo", "bar2");
    assertNotEquals(lbl1, lbl2, "unequality failure");
    assertNotEquals(0, lbl1.compareTo(lbl2), "compare failure");
  }
}
