package org.rle.neo4jdescriptor.dto.entity;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@java.lang.SuppressWarnings("java:S2699")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PropertyDescriptorDtoTest
  extends NamedDescriptorDtoTest<PropertyDescriptorDto> {

  protected PropertyDescriptorDto[] samples() {
    String[] values = new String[] { null, "", "foo", "bar" };
    ArrayList<PropertyDescriptorDto> res = new ArrayList<>();
    for (String v1 : values) {
      for (String v2 : values) {
        res.add(new PropertyDescriptorDto(v1, v2));
      }
    }
    return res.stream().toArray(PropertyDescriptorDto[]::new);
  }

  protected PropertyDescriptorDto create(String name, String logName) {
    return new PropertyDescriptorDto(name, logName);
  }

  protected Class<PropertyDescriptorDto> clazz() {
    return PropertyDescriptorDto.class;
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
}
