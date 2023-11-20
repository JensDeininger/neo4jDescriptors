package org.rle.neo4jdescriptor.dto.entity;

import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class RelationshipTypeDescriptorDto extends NamedDescriptorDto {

  //region ctor

  public RelationshipTypeDescriptorDto() {}

  public RelationshipTypeDescriptorDto(RelationshipTypeDescriptor relTypeDesc) {
    this(relTypeDesc.name(), relTypeDesc.logName());
  }

  public RelationshipTypeDescriptorDto(String name, String logName) {
    super(name, logName);
  }

  //endregion

  //region hash and equality overrides

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
    return obj instanceof RelationshipTypeDescriptorDto;
  }
  //endregion
}
