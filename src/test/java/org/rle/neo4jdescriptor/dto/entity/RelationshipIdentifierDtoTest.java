package org.rle.neo4jdescriptor.dto.entity;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

@java.lang.SuppressWarnings("java:S2699")
public class RelationshipIdentifierDtoTest
  extends EntityIdentifierDtoTest<RelationshipIdentifierDto> {

  protected RelationshipTypeDescriptorDto[] typeSamples() {
    String[] values = new String[] { null, "", "foo", "bar" };
    ArrayList<RelationshipTypeDescriptorDto> res = new ArrayList<>();
    for (String v1 : values) {
      for (String v2 : values) {
        res.add(new RelationshipTypeDescriptorDto(v1, v2));
      }
    }
    res.add(null);
    return res.stream().toArray(RelationshipTypeDescriptorDto[]::new);
  }

  @Override
  protected RelationshipIdentifierDto[] samples() {
    RelationshipTypeDescriptorDto[] typeSamples = typeSamples();
    List<PropertyDescriptorDto[]> propSamples = propDtoSamples();
    ArrayList<RelationshipIdentifierDto> result = new ArrayList<>();
    for (PropertyDescriptorDto[] props : propSamples) {
      for (RelationshipTypeDescriptorDto type : typeSamples) {
        RelationshipIdentifierDto relIdDto = new RelationshipIdentifierDto();
        relIdDto.setProperties(props);
        relIdDto.setRelationshipType(type);
        result.add(relIdDto);
      }
    }
    result.add(null);
    return result.toArray(RelationshipIdentifierDto[]::new);
  }

  @Override
  protected Class<RelationshipIdentifierDto> clazz() {
    return RelationshipIdentifierDto.class;
  }

  @Test
  public void equalityTest() {
    super.equalityTest();
  }

  @Test
  public void unequalityTest_props() {
    super.unequalityTest_props();
  }

  @Test
  public void dynamicHashTest() {
    super.dynamicHashTest();
  }

  @Test
  public void roundRobinJson() {
    super.roundRobinJson();
  }
}
