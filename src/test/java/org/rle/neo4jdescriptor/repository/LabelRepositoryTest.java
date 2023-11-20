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
import org.rle.neo4jdescriptor.dto.entity.LabelDescriptorDto;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LabelRepositoryTest {

  private static final String[] mlabelNames = new String[] {
    "Label_00",
    "Label_01",
    "Label_02",
    "Label_03",
    "Label_04",
    "Label_05",
    "Label_06",
    "Label_07",
  };

  public static class YayLabelRep extends LabelRepository {

    @RepositoryMember
    public static final LabelDescriptor Label1 = new LabelDescriptor(
      mlabelNames[0]
    );

    @RepositoryMember
    public static final LabelDescriptor Label2 = new LabelDescriptor(
      mlabelNames[1]
    );

    public static class Sub1 {

      @RepositoryMember
      public static final LabelDescriptor Sub1Label1 = new LabelDescriptor(
        mlabelNames[2]
      );

      @RepositoryMember
      public static final LabelDescriptor Sub1Label2 = new LabelDescriptor(
        mlabelNames[3]
      );

      public static class SubSub {

        @RepositoryMember
        public static final LabelDescriptor SubSubLabel1 = new LabelDescriptor(
          mlabelNames[4]
        );

        @RepositoryMember
        public static final LabelDescriptor SubSubLabel2 = new LabelDescriptor(
          mlabelNames[5]
        );
      }
    }

    public static class Sub2 {

      @RepositoryMember
      public static final LabelDescriptor Sub2Label1 = new LabelDescriptor(
        mlabelNames[6]
      );

      @RepositoryMember
      public static final LabelDescriptor Sub2Label2 = new LabelDescriptor(
        mlabelNames[7]
      );
    }
  }

  public static class FinalFailLabelRep extends LabelRepository {

    @RepositoryMember
    public static LabelDescriptor Label = new LabelDescriptor("foo");
  }

  public static class StaticFailLabelRep extends LabelRepository {

    @RepositoryMember
    public final LabelDescriptor Label = new LabelDescriptor("foo");
  }

  public static class NullFailLabelRep extends LabelRepository {

    @RepositoryMember
    public static final LabelDescriptor Label = null;
  }

  public static class DuplicateFailLabelRep extends LabelRepository {

    @RepositoryMember
    public static final LabelDescriptor Label = new LabelDescriptor(
      mlabelNames[0]
    );

    public static class Sub {

      @RepositoryMember
      public static final LabelDescriptor Label = new LabelDescriptor(
        mlabelNames[0]
      );
    }
  }

  @Test
  public void scan4LabelsTest() {
    YayLabelRep rep = new YayLabelRep();
    Set<String> names1 = rep
      .labelDescriptors()
      .map(o -> o.name())
      .collect(Collectors.toSet());
    Set<String> names2 = new HashSet<>(Arrays.asList(mlabelNames));
    assertEquals(names1, names2);
  }

  @Test
  public void registerTest() {
    YayLabelRep rep = new YayLabelRep();
    LabelDescriptor lblDesc = new LabelDescriptor("uihoiuiuhui");
    assertTrue(rep.register(lblDesc), "fail 1");
    assertTrue(
      rep.labelDescriptors().anyMatch(o -> o.equals(lblDesc)),
      "fail 2"
    );
  }

  @Test
  public void duplicateRegisterTest() {
    YayLabelRep rep = new YayLabelRep();
    LabelDescriptor lblDesc = new LabelDescriptor(mlabelNames[0]);
    assertFalse(rep.register(lblDesc), "fail 1");
    assertTrue(
      rep.labelDescriptors().anyMatch(o -> o.equals(lblDesc)),
      "fail 2"
    );
  }

  @Test
  public void finalFailTest() {
    assertThrows(IllegalStateException.class, () -> new FinalFailLabelRep());
  }

  @Test
  public void staticFailTest() {
    assertThrows(IllegalStateException.class, () -> new StaticFailLabelRep());
  }

  @Test
  public void nullFailTest() {
    assertThrows(IllegalStateException.class, () -> new NullFailLabelRep());
  }

  @Test
  public void duplicateFailTest() {
    assertThrows(
      IllegalStateException.class,
      () -> new DuplicateFailLabelRep()
    );
  }

  @Test
  public void matchDtoTest() {
    YayLabelRep rep = new YayLabelRep();
    for (String lblName : mlabelNames) {
      LabelDescriptorDto dto = new LabelDescriptorDto(lblName, lblName);
      LabelDescriptor lblDsc = rep.findMatch(dto);
      assertNotNull(lblDsc);
    }
  }
}
