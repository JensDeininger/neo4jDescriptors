package org.rle.neo4jdescriptor.dto.entity;

import org.junit.jupiter.api.Test;
import org.rle.neo4jdescriptor.samples.SampleNodeRep;

@java.lang.SuppressWarnings("java:S2699")
public class NodeDescriptorDtoTest
  extends EntityDescriptorDtoTest<NodeDescriptorDto> {

  @Override
  protected NodeDescriptorDto[] samples() {
    SampleNodeRep rep = new SampleNodeRep();
    NodeDescriptorDto[] res = rep
      .nodeDescriptors()
      .map(o -> new NodeDescriptorDto(o))
      .toArray(NodeDescriptorDto[]::new);
    return res;
  }

  @Override
  protected NodeDescriptorDto create(String clazzName) {
    NodeDescriptorDto res = new NodeDescriptorDto();
    res.setClassName(clazzName);
    return res;
  }

  @Override
  protected Class<NodeDescriptorDto> clazz() {
    return NodeDescriptorDto.class;
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
