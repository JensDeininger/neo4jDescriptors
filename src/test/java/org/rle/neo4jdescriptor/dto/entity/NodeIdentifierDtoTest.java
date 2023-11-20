package org.rle.neo4jdescriptor.dto.entity;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

@java.lang.SuppressWarnings("java:S2699")
public class NodeIdentifierDtoTest
  extends EntityIdentifierDtoTest<NodeIdentifierDto> {

  protected List<LabelDescriptorDto[]> labelDtoSamples() {
    String[] values = new String[] { null, "", "foo", "bar" };
    ArrayList<LabelDescriptorDto> candidates = new ArrayList<>();
    for (String v1 : values) {
      for (String v2 : values) {
        candidates.add(new LabelDescriptorDto(v1, v2));
      }
    }
    List<LabelDescriptorDto[]> result = new ArrayList<LabelDescriptorDto[]>();
    for (int i = 0; i < 32; i++) {
      ArrayList<LabelDescriptorDto> list = new ArrayList<>();
      if ((i & 1) == 1) {
        list.add(null);
      }
      if ((i & 2) == 2) {
        list.add(candidates.get(0));
      }
      if ((i & 4) == 4) {
        list.add(candidates.get(1));
      }
      if ((i & 8) == 8) {
        list.add(candidates.get(2));
      }
      if ((i & 16) == 16) {
        list.add(candidates.get(3));
      }
      result.add(list.toArray(LabelDescriptorDto[]::new));
    }
    result.add(null);
    return result;
  }

  @Override
  protected NodeIdentifierDto[] samples() {
    ArrayList<NodeIdentifierDto> sampleList = new ArrayList<>();
    for (PropertyDescriptorDto[] props : propDtoSamples()) {
      for (LabelDescriptorDto[] labels : labelDtoSamples()) {
        NodeIdentifierDto nodeIdDto = new NodeIdentifierDto();
        nodeIdDto.setLabels(labels);
        nodeIdDto.setProperties(props);
        sampleList.add(nodeIdDto);
      }
    }
    sampleList.add(null);
    return sampleList.toArray(NodeIdentifierDto[]::new);
  }

  @Override
  protected Class<NodeIdentifierDto> clazz() {
    return NodeIdentifierDto.class;
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
