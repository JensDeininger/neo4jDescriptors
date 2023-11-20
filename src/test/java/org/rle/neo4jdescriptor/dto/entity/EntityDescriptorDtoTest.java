package org.rle.neo4jdescriptor.dto.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class EntityDescriptorDtoTest<T extends EntityDescriptorDto> {

  protected abstract T[] samples();

  protected abstract T create(String clazzName);

  protected abstract Class<T> clazz();

  protected void equalityTest() {
    T[] dtos1 = samples();
    T[] dtos2 = samples();
    for (int i = 0; i < dtos1.length; i++) {
      T dto1 = dtos1[i];
      T dto2 = dtos2[i];
      assertEquals(dto1, dto1, "eq fail 1 @ index " + i);
      assertEquals(
        dto1.hashCode(),
        dto1.hashCode(),
        "hash fail 1 @ index " + i
      );
      assertEquals(dto1, dto2, "eq fail 2 @ index " + i);
      assertEquals(
        dto1.hashCode(),
        dto2.hashCode(),
        "hash fail 2 @ index " + i
      );
    }
  }

  protected void unequalTest_name() {
    T dto1 = create("foo");
    T dto2 = create("moo");
    assertNotEquals(dto1, dto2);
  }

  protected void dymanicHashTest() {
    T dto = create("foo");
    int hash1 = dto.hashCode();
    dto.setClassName("keks");
    int hash2 = dto.hashCode();
    assertNotEquals(hash1, hash2, "fail 1");
    dto.setClassName("moo");
    int hash3 = dto.hashCode();
    assertNotEquals(hash2, hash3, "fail 1");
  }

  protected void roundRobinJson() {
    T[] samples = samples();
    for (int i = 0; i < samples.length; i++) {
      T dto = samples[i];
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = assertDoesNotThrow(() ->
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto)
      );
      T revDto = assertDoesNotThrow(() ->
        objectMapper.readValue(jsonString, clazz())
      );
      assertEquals(dto, revDto, "eq fail @ index " + i);
      assertEquals(dto.hashCode(), revDto.hashCode(), "hash fail @ index " + i);
    }
  }
}
