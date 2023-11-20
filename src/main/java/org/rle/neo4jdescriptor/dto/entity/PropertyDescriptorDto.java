package org.rle.neo4jdescriptor.dto.entity;

import org.rle.neo4jdescriptor.property.PropertyDescriptor;

public class PropertyDescriptorDto extends NamedDescriptorDto {

  /*
   * Why is the class or the property type not encoded here?
   *
   * I only need this Dto in the validation reports Dtos. As a subreport in Node- and RelationshipReportDtos.
   * These contain enough info to know the Node- and RelationshipDescriptor. And once I know that, the
   * property key is all I need to get the PropertyDecsriptor
   */

  //region ctor

  public PropertyDescriptorDto() {}

  public PropertyDescriptorDto(PropertyDescriptor propDesc) {
    this(propDesc.key(), propDesc.logKey());
  }

  public PropertyDescriptorDto(String key, String logKey) {
    super(key, logKey);
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
    return obj instanceof PropertyDescriptorDto;
  }
  //endregion
}
