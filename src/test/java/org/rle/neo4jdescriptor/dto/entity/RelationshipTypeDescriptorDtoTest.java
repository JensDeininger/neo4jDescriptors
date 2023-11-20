package org.rle.neo4jdescriptor.dto.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

@java.lang.SuppressWarnings("java:S2699")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RelationshipTypeDescriptorDtoTest
  extends NamedDescriptorDtoTest<RelationshipTypeDescriptorDto> {

  protected RelationshipTypeDescriptorDto[] samples() {
    String[] values = new String[] { null, "", "foo", "bar" };
    ArrayList<RelationshipTypeDescriptorDto> res = new ArrayList<>();
    for (String v1 : values) {
      for (String v2 : values) {
        res.add(new RelationshipTypeDescriptorDto(v1, v2));
      }
    }
    return res.stream().toArray(RelationshipTypeDescriptorDto[]::new);
  }

  protected RelationshipTypeDescriptorDto create(String name, String logName) {
    return new RelationshipTypeDescriptorDto(name, logName);
  }

  protected Class<RelationshipTypeDescriptorDto> clazz() {
    return RelationshipTypeDescriptorDto.class;
  }

  @Test
  public void equalityTest() {
    super.equalityTest();
  }

  @Test
  public void unequalTest_name() {
    super.unequalTest_name();
  }

  @Test
  public void unequalTest_logName() {
    super.unequalTest_logName();
  }

  @Test
  public void dymanicHashTest() {
    super.dymanicHashTest();
  }

  @Test
  public void roundRobinJson() {
    super.roundRobinJson();
  }

  @Test
  public void dto2ObjectTest() {
    RelationshipTypeDescriptorDto[] dtos = samples();
    for (int i = 0; i < dtos.length; i++) {
      RelationshipTypeDescriptorDto dto = dtos[i];
      if (
        dto.getName() == null ||
        dto.getLogName() == null ||
        dto.getName() == "" ||
        dto.getLogName() == ""
      ) {
        continue;
      }
      RelationshipTypeDescriptor desc = new RelationshipTypeDescriptor(dto);
      RelationshipTypeDescriptorDto dtoRev = desc.dto();
      assertEquals(dtoRev, dto);
      assertEquals(dtoRev.hashCode(), dto.hashCode());
    }
  }
}
