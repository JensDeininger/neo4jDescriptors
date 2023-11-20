package org.rle.neo4jdescriptor.dto.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;

@java.lang.SuppressWarnings("java:S2699")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LabelDescriptorDtoTest
  extends NamedDescriptorDtoTest<LabelDescriptorDto> {

  protected LabelDescriptorDto[] samples() {
    String[] values = new String[] { null, "", "foo", "bar" };
    ArrayList<LabelDescriptorDto> res = new ArrayList<>();
    for (String v1 : values) {
      for (String v2 : values) {
        res.add(new LabelDescriptorDto(v1, v2));
      }
    }
    return res.stream().toArray(LabelDescriptorDto[]::new);
  }

  protected LabelDescriptorDto create(String name, String logName) {
    return new LabelDescriptorDto(name, logName);
  }

  protected Class<LabelDescriptorDto> clazz() {
    return LabelDescriptorDto.class;
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
    LabelDescriptorDto[] dtos = samples();
    for (int i = 0; i < dtos.length; i++) {
      LabelDescriptorDto dto = dtos[i];
      if (
        dto.getName() == null ||
        dto.getLogName() == null ||
        dto.getName() == "" ||
        dto.getLogName() == ""
      ) {
        continue;
      }
      LabelDescriptor desc = new LabelDescriptor(dto);
      LabelDescriptorDto dtoRev = desc.dto();
      assertEquals(dtoRev, dto);
      assertEquals(dtoRev.hashCode(), dto.hashCode());
    }
  }
}
