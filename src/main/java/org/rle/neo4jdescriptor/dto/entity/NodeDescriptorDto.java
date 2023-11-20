package org.rle.neo4jdescriptor.dto.entity;

import org.rle.neo4jdescriptor.entity.NodeDescriptor;

public class NodeDescriptorDto extends EntityDescriptorDto {

  /*
   * Why is this so empty?
   *
   * I only need this Dto in the validation reports Dtos. All I need is the class name
   * of the NodeDescriptor. I will then get the NodeRepository to give
   * me an instance of the NodeDescriptor.
   */

  public NodeDescriptorDto() {
    super();
  }

  public NodeDescriptorDto(NodeDescriptor nodeDescriptor) {
    super(nodeDescriptor);
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
    return obj instanceof NodeDescriptorDto;
  }
}
