package org.rle.neo4jdescriptor.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.annotation.RepositoryMember;
import org.rle.neo4jdescriptor.dto.entity.RelationshipTypeDescriptorDto;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RelationshipTypeRepositoryTest {

  private static final String[] mRelationshipTypeNames = new String[] {
    "RelationshipType_00",
    "RelationshipType_01",
    "RelationshipType_02",
    "RelationshipType_03",
    "RelationshipType_04",
    "RelationshipType_05",
    "RelationshipType_06",
    "RelationshipType_07",
  };

  public static class YayRelationshipTypeRep
    extends RelationshipTypeRepository {

    @RepositoryMember
    public static final RelationshipTypeDescriptor RelationshipType1 = new RelationshipTypeDescriptor(
      mRelationshipTypeNames[0]
    );

    @RepositoryMember
    public static final RelationshipTypeDescriptor RelationshipType2 = new RelationshipTypeDescriptor(
      mRelationshipTypeNames[1]
    );

    public static class Sub1 {

      @RepositoryMember
      public static final RelationshipTypeDescriptor Sub1RelationshipType1 = new RelationshipTypeDescriptor(
        mRelationshipTypeNames[2]
      );

      @RepositoryMember
      public static final RelationshipTypeDescriptor Sub1RelationshipType2 = new RelationshipTypeDescriptor(
        mRelationshipTypeNames[3]
      );

      public static class SubSub {

        @RepositoryMember
        public static final RelationshipTypeDescriptor SubSubRelationshipType1 = new RelationshipTypeDescriptor(
          mRelationshipTypeNames[4]
        );

        @RepositoryMember
        public static final RelationshipTypeDescriptor SubSubRelationshipType2 = new RelationshipTypeDescriptor(
          mRelationshipTypeNames[5]
        );
      }
    }

    public static class Sub2 {

      @RepositoryMember
      public static final RelationshipTypeDescriptor Sub2RelationshipType1 = new RelationshipTypeDescriptor(
        mRelationshipTypeNames[6]
      );

      @RepositoryMember
      public static final RelationshipTypeDescriptor Sub2RelationshipType2 = new RelationshipTypeDescriptor(
        mRelationshipTypeNames[7]
      );
    }
  }

  public static class FinalFailRelationshipTypeRep
    extends RelationshipTypeRepository {

    @RepositoryMember
    public static RelationshipTypeDescriptor RelationshipType = new RelationshipTypeDescriptor(
      "foo"
    );
  }

  public static class StaticFailRelationshipTypeRep
    extends RelationshipTypeRepository {

    @RepositoryMember
    public final RelationshipTypeDescriptor RelationshipType = new RelationshipTypeDescriptor(
      "foo"
    );
  }

  public static class NullFailRelationshipTypeRep
    extends RelationshipTypeRepository {

    @RepositoryMember
    public static final RelationshipTypeDescriptor RelationshipType = null;
  }

  public static class DuplicateFailRelationshipTypeRep
    extends RelationshipTypeRepository {

    @RepositoryMember
    public static RelationshipTypeDescriptor RelationshipType = new RelationshipTypeDescriptor(
      "foo"
    );

    public static class Sub {

      @RepositoryMember
      public static RelationshipTypeDescriptor RelationshipType = new RelationshipTypeDescriptor(
        "foo"
      );
    }
  }

  @Test
  public void scan4RelationshipTypesTest() {
    YayRelationshipTypeRep rep = new YayRelationshipTypeRep();
    Set<String> names1 = rep
      .relationshipTypeDescriptors()
      .map(o -> o.name())
      .collect(Collectors.toSet());
    Set<String> names2 = new HashSet<>(Arrays.asList(mRelationshipTypeNames));
    assertEquals(names1, names2);
  }

  @Test
  public void registerTest() {
    YayRelationshipTypeRep rep = new YayRelationshipTypeRep();
    RelationshipTypeDescriptor relDesc = new RelationshipTypeDescriptor(
      "jhkjgjhgg"
    );
    assertTrue(rep.register(relDesc), "fail 1");
    assertTrue(
      rep.relationshipTypeDescriptors().anyMatch(o -> o.equals(relDesc)),
      "fail 2"
    );
  }

  @Test
  public void duplicateRegisterTest() {
    YayRelationshipTypeRep rep = new YayRelationshipTypeRep();
    RelationshipTypeDescriptor relDesc = new RelationshipTypeDescriptor(
      mRelationshipTypeNames[0]
    );
    assertFalse(rep.register(relDesc), "fail 1");
    assertTrue(
      rep.relationshipTypeDescriptors().anyMatch(o -> o.equals(relDesc)),
      "fail 2"
    );
  }

  @Test
  public void finalFailTest() {
    assertThrows(
      IllegalStateException.class,
      () -> new FinalFailRelationshipTypeRep()
    );
  }

  @Test
  public void staticFailTest() {
    assertThrows(
      IllegalStateException.class,
      () -> new StaticFailRelationshipTypeRep()
    );
  }

  @Test
  public void nullFailTest() {
    assertThrows(
      IllegalStateException.class,
      () -> new NullFailRelationshipTypeRep()
    );
  }

  @Test
  public void duplicateFailTest() {
    assertThrows(
      IllegalStateException.class,
      () -> new DuplicateFailRelationshipTypeRep()
    );
  }

  @Test
  public void matchDtoTest() {
    YayRelationshipTypeRep rep = new YayRelationshipTypeRep();
    for (String relTypeName : mRelationshipTypeNames) {
      RelationshipTypeDescriptorDto dto = new RelationshipTypeDescriptorDto(
        relTypeName,
        relTypeName
      );
      RelationshipTypeDescriptor desc = rep.findMatch(dto);
      assertNotNull(desc);
    }
  }
}
