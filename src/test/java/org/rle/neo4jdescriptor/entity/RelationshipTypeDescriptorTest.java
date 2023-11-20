package org.rle.neo4jdescriptor.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class RelationshipTypeDescriptorTest {

  @Test
  public void crapArgsTest() {
    String[] values = new String[] { null, "", "\t", System.lineSeparator() };
    for (String s1 : values) {
      assertThrows(
        IllegalArgumentException.class,
        () -> new RelationshipTypeDescriptor(s1, "foo")
      );
      assertThrows(
        IllegalArgumentException.class,
        () -> new RelationshipTypeDescriptor("foo", s1)
      );
      for (String s2 : values) {
        assertThrows(
          IllegalArgumentException.class,
          () -> new RelationshipTypeDescriptor(s1, s2)
        );
      }
    }
  }

  @Test
  public void inputValueTest() {
    String name = "foo";
    String logName = "bar";
    RelationshipTypeDescriptor lbl = new RelationshipTypeDescriptor(
      name,
      logName
    );
    assertEquals(lbl.name(), name);
    assertEquals(lbl.logName(), logName);
  }

  @Test
  public void identicalDeclarationEqualsTest() {
    String name = "foo";
    String logName = "bar";
    RelationshipTypeDescriptor rel1 = new RelationshipTypeDescriptor(
      name,
      logName
    );
    RelationshipTypeDescriptor rel2 = new RelationshipTypeDescriptor(
      name,
      logName
    );
    assertEquals(rel1.hashCode(), rel2.hashCode(), "hashCode failure");
    assertEquals(rel1, rel2, "Equality failure");
    assertEquals(0, rel1.compareTo(rel2), "Compare failure");
  }

  @Test
  public void copyEqualsTest() {
    RelationshipTypeDescriptor rel = new RelationshipTypeDescriptor(
      "foo",
      "bar"
    );
    RelationshipTypeDescriptor copy = rel.copy();
    assertEquals(rel.hashCode(), copy.hashCode(), "hashCode failure");
    assertEquals(rel, copy, "equality failure");
    assertEquals(0, rel.compareTo(copy), "Compare failure");
  }

  @Test
  public void unqualsTest_dbKey() {
    RelationshipTypeDescriptor rel1 = new RelationshipTypeDescriptor(
      "doobedoo1",
      "bar"
    );
    RelationshipTypeDescriptor rel2 = new RelationshipTypeDescriptor(
      "doobedoo2",
      "bar"
    );
    assertNotEquals(rel1, rel2, "unequality failure");
    assertNotEquals(0, rel1.compareTo(rel2), "compare failure");
  }

  @Test
  public void unqualsTest_logKey() {
    RelationshipTypeDescriptor rel1 = new RelationshipTypeDescriptor(
      "foo",
      "whoopsie1"
    );
    RelationshipTypeDescriptor rel2 = new RelationshipTypeDescriptor(
      "foo",
      "whoopsie2"
    );
    assertNotEquals(rel1, rel2, "unequality failure");
    assertNotEquals(0, rel1.compareTo(rel2), "compare failure");
  }
}
