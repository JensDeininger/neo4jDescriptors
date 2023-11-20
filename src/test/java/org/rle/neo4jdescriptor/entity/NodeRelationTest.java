package org.rle.neo4jdescriptor.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.Cardinality;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;
import org.rle.neo4jdescriptor.testutils.TestBase;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NodeRelationTest extends TestBase {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(NodeRelationTestProcedure.class);
  }

  @Override
  protected String initialCypher() {
    return null;
  }

  private NodeRelation createSample(
    RelationshipDescriptor relDesc,
    Direction dir,
    Cardinality cardinality
  ) {
    NodeRelation res;
    switch (cardinality) {
      case ONE:
        res = new NodeRelationOneOne(relDesc, dir);
        break;
      case ONE_MANY:
        res = new NodeRelationOneMany(relDesc, dir);
        break;
      case ZERO_MANY:
        res = new NodeRelationZeroMany(relDesc, dir);
        break;
      case ZERO_ONE:
        res = new NodeRelationZeroOne(relDesc, dir);
        break;
      default:
        throw new IllegalArgumentException();
    }
    return res;
  }

  private NodeRelation[] createSamples(
    RelationshipDescriptor relDesc,
    Direction dir
  ) {
    return new NodeRelation[] {
      new NodeRelationOneMany(relDesc, dir),
      new NodeRelationOneOne(relDesc, dir),
      new NodeRelationZeroMany(relDesc, dir),
      new NodeRelationZeroOne(relDesc, dir),
    };
  }

  @Test
  public void ctorTest() {
    Direction[] dirs = new Direction[] {
      Direction.INCOMING,
      Direction.OUTGOING,
    };
    RelationshipDescriptor relDesc = SampleRelationshipRep.FooBar;
    for (Direction dir : dirs) {
      NodeRelation[] samples = createSamples(relDesc, dir);
      for (NodeRelation sample : samples) {
        assertEquals(relDesc, sample.relationshipDescriptor(), "fail 1");
        assertEquals(dir, sample.direction(), "fail 2");
        assertNotEquals(Cardinality.UNDEFINED, sample.cardinality(), "fail 3");
      }
    }
  }

  @Test
  public void nullArgsTest() {
    for (Cardinality card : Cardinality.class.getEnumConstants()) {
      if (card == Cardinality.UNDEFINED) {
        continue;
      }
      Exception exc = null;
      try {
        createSample(null, Direction.INCOMING, card);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalArgumentException.class,
        NodeRelation.ARGUMENT_MUST_NOT_BE_NULL
      );
    }
  }

  @Test
  public void wrongArgTest_direction() {
    for (Cardinality card : Cardinality.class.getEnumConstants()) {
      if (card == Cardinality.UNDEFINED) {
        continue;
      }
      Exception exc = null;
      try {
        createSample(SampleRelationshipRep.FooBar, Direction.BOTH, card);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalArgumentException.class,
        NodeRelation.DIRECTION_MUST_BE_INCOMING_OR_OUTGOING
      );
    }
  }

  @Test
  public void equalityTest() {
    Direction[] dirs = new Direction[] {
      Direction.INCOMING,
      Direction.OUTGOING,
    };
    for (Direction dir : dirs) {
      NodeRelation[] samples1 = createSamples(
        SampleRelationshipRep.FooBar,
        dir
      );
      NodeRelation[] samples2 = createSamples(
        SampleRelationshipRep.FooBar,
        dir
      );
      for (int i = 0; i < samples1.length; i++) {
        NodeRelation nr1 = samples1[i];
        NodeRelation nr2 = samples2[i];
        assertNotSame(nr1, nr2);
        assertEquals(nr1, nr2, "equals fail @index " + i);
        assertEquals(nr1.hashCode(), nr2.hashCode(), "hash fail @index " + i);
        assertEquals(0, nr1.compareTo(nr2), "compare fail @index " + i);
      }
    }
  }

  @Test
  public void negativeEqualityTest_relationship() {
    Direction[] dirs = new Direction[] {
      Direction.INCOMING,
      Direction.OUTGOING,
    };
    for (Direction dir : dirs) {
      NodeRelation[] samples1 = createSamples(
        SampleRelationshipRep.FooBar,
        dir
      );
      NodeRelation[] samples2 = createSamples(
        SampleRelationshipRep.BarFoo,
        dir
      );
      for (int i = 0; i < samples1.length; i++) {
        NodeRelation nr1 = samples1[i];
        NodeRelation nr2 = samples2[i];
        assertNotEquals(nr1, nr2, "equals fail @index " + i);
        // strictly speaking, the hash code may be the same, but its so goddamn unlikely that I choose to test this anyway
        assertNotEquals(
          nr1.hashCode(),
          nr2.hashCode(),
          "hash fail @index " + i
        );
        assertNotEquals(0, nr1.compareTo(nr2), "compare fail @index " + i);
      }
    }
  }

  @Test
  public void negativeEqualityTest_direction() {
    NodeRelation[] samples1 = createSamples(
      SampleRelationshipRep.FooBar,
      Direction.INCOMING
    );
    NodeRelation[] samples2 = createSamples(
      SampleRelationshipRep.FooBar,
      Direction.OUTGOING
    );
    for (int i = 0; i < samples1.length; i++) {
      NodeRelation nr1 = samples1[i];
      NodeRelation nr2 = samples2[i];
      assertNotEquals(nr1, nr2, "equals fail @index " + i);
      // strictly speaking, the hash code may be the same, but its so goddamn unlikely that I choose to test this anyway
      assertNotEquals(nr1.hashCode(), nr2.hashCode(), "hash fail @index " + i);
      assertNotEquals(0, nr1.compareTo(nr2), "compare fail @index " + i);
    }
  }

  @Test
  public void negativeEqualityTest_cardinality() {
    Direction[] dirs = new Direction[] {
      Direction.INCOMING,
      Direction.OUTGOING,
    };
    for (Direction dir : dirs) {
      NodeRelation[] samples1 = createSamples(
        SampleRelationshipRep.FooBar,
        dir
      );
      NodeRelation[] samples2 = createSamples(
        SampleRelationshipRep.FooBar,
        dir
      );
      for (int i = 0; i < samples1.length; i++) {
        for (int j = 0; j < samples2.length; j++) {
          if (i == j) {
            continue;
          }
          NodeRelation nr1 = samples1[i];
          NodeRelation nr2 = samples2[j];
          assertNotEquals(
            nr1,
            nr2,
            String.format("equals fail @index %d/%d", i, j)
          );
          // strictly speaking, the hash code may be the same, but its so goddamn unlikely that I choose to test this anyway
          assertNotEquals(
            nr1.hashCode(),
            nr2.hashCode(),
            String.format("hash fail @index %d/%d", i, j)
          );
          assertNotEquals(
            0,
            nr1.compareTo(nr2),
            String.format("compare fail @index %d/%d", i, j)
          );
        }
      }
    }
  }

  @Test
  public void testAllProcedures() {
    List<String> failingProcs = super.runAllTestProcedures(
      NodeRelationTestProcedure.TestProcedureName.class
    );
    String fails = String.join(
      System.lineSeparator(),
      failingProcs.stream().toArray(String[]::new)
    );
    assertTrue(failingProcs.isEmpty(), "failed procs: " + fails);
  }
}
