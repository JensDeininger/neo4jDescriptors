package org.rle.neo4jdescriptor.dto.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public abstract class EntityIdentifierDtoTest<T extends EntityIdentifierDto> {

  protected List<PropertyDescriptorDto[]> propDtoSamples() {
    String[] values = new String[] { null, "", "foo", "bar" };
    ArrayList<PropertyDescriptorDto> propDtoCandidates = new ArrayList<>();
    for (String v1 : values) {
      for (String v2 : values) {
        propDtoCandidates.add(new PropertyDescriptorDto(v1, v2));
      }
    }
    List<PropertyDescriptorDto[]> result = new ArrayList<PropertyDescriptorDto[]>();
    for (int i = 0; i < 32; i++) {
      ArrayList<PropertyDescriptorDto> list = new ArrayList<>();
      if ((i & 1) == 1) {
        list.add(null);
      }
      if ((i & 2) == 2) {
        list.add(propDtoCandidates.get(0));
      }
      if ((i & 4) == 4) {
        list.add(propDtoCandidates.get(1));
      }
      if ((i & 8) == 8) {
        list.add(propDtoCandidates.get(2));
      }
      if ((i & 16) == 16) {
        list.add(propDtoCandidates.get(3));
      }
      result.add(list.toArray(PropertyDescriptorDto[]::new));
    }
    result.add(null);
    return result;
  }

  protected abstract T[] samples();

  protected abstract Class<T> clazz();

  protected void equalityTest() {
    T[] dtos1 = samples();
    T[] dtos2 = samples();
    for (int i = 0; i < dtos1.length; i++) {
      T dto1 = dtos1[i];
      T dto2 = dtos2[i];
      assertEquals(dto1, dto2, "eq fail 2 @ index " + i);
      int h1 = dto1 == null ? 0 : dto1.hashCode();
      int h2 = dto2 == null ? 0 : dto1.hashCode();
      assertEquals(h1, h2, "hash fail 2 @ index " + i);
    }
  }

  protected void unequalityTest_props() {
    T[] origDtos = samples();
    T[] modDtos = samples();
    for (int i = 0; i < origDtos.length; i++) {
      T origDto = origDtos[i];
      T modDto = modDtos[i];
      assertEquals(origDto, modDto);
      if (modDto == null) {
        continue;
      }
      PropertyDescriptorDto[] newProps;
      if (modDto.getProperties() == null) {
        newProps = new PropertyDescriptorDto[0];
      } else {
        newProps =
          Stream
            .concat(
              Arrays.stream(modDto.getProperties()),
              Stream.of(new PropertyDescriptorDto())
            )
            .toArray(PropertyDescriptorDto[]::new);
      }
      modDto.setProperties(newProps);
      assertNotEquals(origDto, modDto, "fail @ index " + i);
    }
  }

  protected void dynamicHashTest() {
    T[] dtos = samples();
    for (int i = 0; i < dtos.length; i++) {
      T dto = dtos[i];
      if (dto == null) {
        continue;
      }
      int hash1 = dto.hashCode();
      PropertyDescriptorDto[] newProps;
      if (dto.getProperties() == null) {
        newProps = new PropertyDescriptorDto[0];
      } else {
        newProps =
          Stream
            .concat(
              Arrays.stream(dto.getProperties()),
              Stream.of(new PropertyDescriptorDto())
            )
            .toArray(PropertyDescriptorDto[]::new);
      }
      dto.setProperties(newProps);
      int hash2 = dto.hashCode();
      assertNotEquals(hash1, hash2, "fail @ index " + i);
    }
  }

  protected void roundRobinJson() {
    T[] samples = samples();
    for (int i = 0; i < samples.length; i++) {
      T dto = samples[i];
      if (dto == null) {
        continue;
      }
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
