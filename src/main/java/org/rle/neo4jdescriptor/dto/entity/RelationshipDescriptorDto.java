package org.rle.neo4jdescriptor.dto.entity;

import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;

public class RelationshipDescriptorDto extends EntityDescriptorDto {

  /*
   * Why is this so empty?
   *
   * I only need this Dto in the validation reports Dtos. All I need is the class name
   * of the RelationshipDescriptor. I will then get the RelationshipRepository to give
   * me an instance of the RelationshipDescriptor.
   */

  public RelationshipDescriptorDto() {
    super();
  }

  public RelationshipDescriptorDto(
    RelationshipDescriptor relationshipDescriptor
  ) {
    super(relationshipDescriptor);
  }

  @Override
  @SuppressWarnings("java:S1185") // complains about useless overrides, but I want to keep that here as a reminder
  protected int processHash() {
    return super.processHash();
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    return obj instanceof RelationshipDescriptorDto;
  }
}
