package org.rle.neo4jdescriptor.dto.entity;

import org.junit.jupiter.api.Test;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;

@java.lang.SuppressWarnings("java:S2699")
public class RelationshipDescriptorDtoTest
  extends EntityDescriptorDtoTest<RelationshipDescriptorDto> {

  @Override
  protected RelationshipDescriptorDto[] samples() {
    SampleRelationshipRep rep = new SampleRelationshipRep();
    RelationshipDescriptorDto[] res = rep
      .relationshipDescriptors()
      .map(o -> new RelationshipDescriptorDto(o))
      .toArray(RelationshipDescriptorDto[]::new);
    return res;
  }

  @Override
  protected RelationshipDescriptorDto create(String clazzName) {
    RelationshipDescriptorDto res = new RelationshipDescriptorDto();
    res.setClassName(clazzName);
    return res;
  }

  @Override
  protected Class<RelationshipDescriptorDto> clazz() {
    return RelationshipDescriptorDto.class;
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
  public void dymanicHashTest() {
    super.dymanicHashTest();
  }

  @Test
  public void roundRobinJson() {
    super.roundRobinJson();
  }
}
