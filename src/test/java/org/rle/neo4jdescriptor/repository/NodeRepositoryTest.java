package org.rle.neo4jdescriptor.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.annotation.RepositoryMember;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NodeRepositoryTest {

  public static final String[] NodeNames = new String[] {
    "Node_00",
    "Node_01",
    "Node_02",
  };

  public static class YayNodeRep extends NodeRepository {

    @RepositoryMember
    public static final Node0 Node = new Node0();

    public static class Sub1 {

      @RepositoryMember
      public static final Node1 Node = new Node1();

      public static class SubSub {

        @RepositoryMember
        public static final Node2 Node = new Node2();
      }
    }
  }

  public static class FinalFailNodeRep extends NodeRepository {

    @RepositoryMember
    public static NodeDescriptor Node = new Node0();
  }

  public static class StaticFailNodeRep extends NodeRepository {

    @RepositoryMember
    public final NodeDescriptor Node = new Node0();
  }

  public static class NullFailNodeRep extends NodeRepository {

    @RepositoryMember
    public static final NodeDescriptor Node = null;
  }

  public static class DuplicateFailNodeRep extends NodeRepository {

    @RepositoryMember
    public final NodeDescriptor Node = new Node0();

    public static class Sub {

      @RepositoryMember
      public final NodeDescriptor Node = new Node0();
    }
  }

  @Test
  public void scan4NodesTest() {
    YayNodeRep rep = new YayNodeRep();
    Set<String> names1 = rep
      .nodeDescriptors()
      .map(o -> o.labels(Modality.NECESSARY).findFirst().get().name())
      .collect(Collectors.toSet());
    Set<String> names2 = new HashSet<>(Arrays.asList(NodeNames));
    assertEquals(names1, names2);
  }

  @Test
  public void registerTest() {
    YayNodeRep rep = new YayNodeRep();
    NodeDescriptor nodeDesc = new NodeBonus();
    assertTrue(rep.register(nodeDesc), "fail 1");
    assertTrue(
      rep.nodeDescriptors().anyMatch(o -> o.equals(nodeDesc)),
      "fail 2"
    );
  }

  @Test
  public void duplicateRegisterTest() {
    YayNodeRep rep = new YayNodeRep();
    NodeDescriptor nodeDesc = new Node0();
    assertFalse(rep.register(nodeDesc), "fail 1");
    assertTrue(
      rep.nodeDescriptors().anyMatch(o -> o.equals(nodeDesc)),
      "fail 2"
    );
  }

  @Test
  public void finalFailTest() {
    assertThrows(IllegalStateException.class, () -> new FinalFailNodeRep());
  }

  @Test
  public void staticFailTest() {
    assertThrows(IllegalStateException.class, () -> new StaticFailNodeRep());
  }

  @Test
  public void nullFailTest() {
    assertThrows(IllegalStateException.class, () -> new NullFailNodeRep());
  }

  @Test
  public void duplicateFailTest() {
    assertThrows(IllegalStateException.class, () -> new DuplicateFailNodeRep());
  }
}
