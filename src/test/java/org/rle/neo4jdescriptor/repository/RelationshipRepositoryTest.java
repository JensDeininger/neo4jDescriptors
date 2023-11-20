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
import org.rle.neo4jdescriptor.annotation.RepositoryMember;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RelationshipRepositoryTest {

  public static final String[] TypeNames = new String[] {
    "Relationship_00",
    "Relationship_01",
    "Relationship_02",
  };

  public static class YayRelationshipRep extends RelationshipRepository {

    @RepositoryMember
    public static final Relation0 Relation = new Relation0();

    public static class Sub1 {

      @RepositoryMember
      public static final Relation1 Relation = new Relation1();

      public static class SubSub {

        @RepositoryMember
        public static final Relation2 Relation = new Relation2();
      }
    }
  }

  public static class FinalFailRelationshipRep extends RelationshipRepository {

    @RepositoryMember
    public static RelationshipDescriptor Relation = new Relation0();
  }

  public static class StaticFailRelationshipRep extends RelationshipRepository {

    @RepositoryMember
    public final RelationshipDescriptor Relation = new Relation0();
  }

  public static class NullFailRelationshipRep extends RelationshipRepository {

    @RepositoryMember
    public static final RelationshipDescriptor Relation = null;
  }

  public static class DuplicateFailRelationshipRep
    extends RelationshipRepository {

    @RepositoryMember
    public final RelationshipDescriptor Relation = new Relation0();

    public static class Sub {

      @RepositoryMember
      public final RelationshipDescriptor Relation = new Relation0();
    }
  }

  @Test
  public void scan4RelationshipsTest() {
    YayRelationshipRep rep = new YayRelationshipRep();
    Set<String> names1 = rep
      .relationshipDescriptors()
      .map(o -> o.type().name())
      .collect(Collectors.toSet());
    Set<String> names2 = new HashSet<>(Arrays.asList(TypeNames));
    assertEquals(names1, names2);
  }

  @Test
  public void registerTest() {
    YayRelationshipRep rep = new YayRelationshipRep();
    RelationshipDescriptor RelationshipDesc = new RelationBonus();
    assertTrue(rep.register(RelationshipDesc), "fail 1");
    assertTrue(
      rep.relationshipDescriptors().anyMatch(o -> o.equals(RelationshipDesc)),
      "fail 2"
    );
  }

  @Test
  public void duplicateRegisterTest() {
    YayRelationshipRep rep = new YayRelationshipRep();
    RelationshipDescriptor RelationshipDesc = new Relation0();
    assertFalse(rep.register(RelationshipDesc), "fail 1");
    assertTrue(
      rep.relationshipDescriptors().anyMatch(o -> o.equals(RelationshipDesc)),
      "fail 2"
    );
  }

  @Test
  public void finalFailTest() {
    assertThrows(
      IllegalStateException.class,
      () -> new FinalFailRelationshipRep()
    );
  }

  @Test
  public void staticFailTest() {
    assertThrows(
      IllegalStateException.class,
      () -> new StaticFailRelationshipRep()
    );
  }

  @Test
  public void nullFailTest() {
    assertThrows(
      IllegalStateException.class,
      () -> new NullFailRelationshipRep()
    );
  }

  @Test
  public void duplicateFailTest() {
    assertThrows(
      IllegalStateException.class,
      () -> new DuplicateFailRelationshipRep()
    );
  }
}
