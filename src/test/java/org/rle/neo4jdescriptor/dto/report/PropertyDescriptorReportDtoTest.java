package org.rle.neo4jdescriptor.dto.report;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.rle.neo4jdescriptor.dto.entity.PropertyDescriptorDto;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.prop_basic.LongProperty;

public class PropertyDescriptorReportDtoTest extends ReportBaseDtoTest {

  @Override
  protected ReportBaseDto createSampleDto(long seed) {
    return sample(new LongProperty("longProp1"), seed);
  }

  protected static PropertyReportDto sample(
    PropertyDescriptor propDesc,
    long seed
  ) {
    Random ran = new Random(seed);
    PropertyReportDto dto = new PropertyReportDto();
    dto.setPropertyDescriptorDto(new PropertyDescriptorDto(propDesc));
    dto.setKeyExistsCheck(ranBool(ran));
    dto.setTypeCheck(ranBool(ran));
    dto.setEnumValueCheck(ranBool(ran));
    dto.setDeviantPropertyTypeName(ranString(ran));
    dto.setExceptionMsgs(ranExcMessages(ran, 0));
    return dto;
  }

  @Test
  public void equalityTest() {
    List<Long> seeds = createSeeds(0, 20);
    PropertyDescriptor propDesc = new LongProperty("longProp");
    for (Long seed : seeds) {
      PropertyReportDto dto1 = sample(propDesc, seed);
      PropertyReportDto dto2 = sample(propDesc, seed);
      assertEquals(dto1, dto2, "fail @ seed " + seed);
      assertEquals(dto1.hashCode(), dto2.hashCode(), "fail @ seed " + seed);
    }
  }

  // region inequality tests

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions here
  public void inequalityTest_exceptions() {
    super.inequalityTest_exceptions();
  }

  @Test
  public void inequalityTest_propDesc() {
    PropertyDescriptor propDesc1 = new LongProperty("longProp1");
    PropertyDescriptor propDesc2 = new LongProperty("longProp2");
    PropertyReportDto dto1 = sample(propDesc1, 0);
    PropertyReportDto dto2 = sample(propDesc1, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setPropertyDescriptorDto(new PropertyDescriptorDto(propDesc2));
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_existsCheck() {
    PropertyDescriptor propDesc = new LongProperty("longProp");
    PropertyReportDto dto1 = sample(propDesc, 0);
    PropertyReportDto dto2 = sample(propDesc, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto1.setKeyExistsCheck(true);
    dto2.setKeyExistsCheck(null);
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_typeCheck() {
    PropertyDescriptor propDesc = new LongProperty("longProp");
    PropertyReportDto dto1 = sample(propDesc, 0);
    PropertyReportDto dto2 = sample(propDesc, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto1.setTypeCheck(true);
    dto2.setTypeCheck(null);
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_enumValueCheck() {
    PropertyDescriptor propDesc = new LongProperty("longProp");
    PropertyReportDto dto1 = sample(propDesc, 0);
    PropertyReportDto dto2 = sample(propDesc, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto1.setEnumValueCheck(true);
    dto2.setEnumValueCheck(null);
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_deviantPropertyType() {
    PropertyDescriptor propDesc = new LongProperty("longProp");
    PropertyReportDto dto1 = sample(propDesc, 0);
    PropertyReportDto dto2 = sample(propDesc, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto1.setDeviantPropertyTypeName("bla");
    dto2.setDeviantPropertyTypeName(null);
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  // endregion

  // region hash reset tests

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions in the methods that just call super
  public void hashResetCheck_exceptions() {
    super.hashResetCheck_exceptions();
  }

  @Test
  public void hashResetCheck_propDesc() {
    PropertyDescriptor propDesc = new LongProperty("longProp");
    PropertyReportDto dto = sample(propDesc, 0);
    int origHash = dto.hashCode();
    dto.setPropertyDescriptorDto(
      new PropertyDescriptorDto(new LongProperty("longProp2"))
    );
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  @Test
  public void hashResetCheck_existsCheck() {
    PropertyDescriptor propDesc = new LongProperty("longProp");
    PropertyReportDto dto = sample(propDesc, 0);
    int origHash = dto.hashCode();
    Boolean check = dto.getKeyExistsCheck() == null ? true : null;
    dto.setKeyExistsCheck(check);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  @Test
  public void hashResetCheck_typeCheck() {
    PropertyDescriptor propDesc = new LongProperty("longProp");
    PropertyReportDto dto = sample(propDesc, 0);
    int origHash = dto.hashCode();
    Boolean check = dto.getTypeCheck() == null ? true : null;
    dto.setTypeCheck(check);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  @Test
  public void hashResetCheck_enumValueCheck() {
    PropertyDescriptor propDesc = new LongProperty("longProp");
    PropertyReportDto dto = sample(propDesc, 0);
    int origHash = dto.hashCode();
    Boolean check = dto.getEnumValueCheck() == null ? true : null;
    dto.setEnumValueCheck(check);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  @Test
  public void hashResetCheck_deviantPropTypeName() {
    PropertyDescriptor propDesc = new LongProperty("longProp");
    PropertyReportDto dto = sample(propDesc, 0);
    int origHash = dto.hashCode();
    dto.setDeviantPropertyTypeName(dto.getDeviantPropertyTypeName() + "...");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  // endregion

  @Test
  public void roundRobinJson() {
    List<Long> seeds = createSeeds(0, 20);
    PropertyDescriptor propDesc = new LongProperty("longProp");
    ObjectMapper objectMapper = new ObjectMapper();
    for (Long seed : seeds) {
      PropertyReportDto dto = sample(propDesc, seed);
      String jsonString = assertDoesNotThrow(() ->
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto)
      );
      PropertyReportDto revDto = assertDoesNotThrow(() ->
        objectMapper.readValue(jsonString, PropertyReportDto.class)
      );
      assertEquals(dto, revDto, "eq fail @ seed " + seed);
      assertEquals(
        dto.hashCode(),
        revDto.hashCode(),
        "hash fail @ seed " + seed
      );
    }
  }
}
