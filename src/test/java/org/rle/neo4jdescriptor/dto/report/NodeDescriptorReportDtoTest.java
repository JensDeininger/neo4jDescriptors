package org.rle.neo4jdescriptor.dto.report;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.rle.neo4jdescriptor.dto.entity.NodeDescriptorDto;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;
import org.rle.neo4jdescriptor.samples.SampleNodeRep;

public class NodeDescriptorReportDtoTest extends ReportBaseDtoTest {

  @Override
  protected NodeDescriptorReportDto createSampleDto(long seed) {
    NodeDescriptor[] nodeDescs = new NodeDescriptor[] {
      SampleNodeRep.Foo,
      SampleNodeRep.Bar,
      SampleNodeRep.Keks,
    };
    Random ran = new Random(seed);
    NodeDescriptor nd = nodeDescs[ran.nextInt(nodeDescs.length)];
    return sample(nd, ran.nextLong());
  }

  protected static NodeDescriptorReportDto sample(
    NodeDescriptor nodeDescriptor,
    long seed
  ) {
    NodeDescriptorReportDto dto = new NodeDescriptorReportDto(nodeDescriptor);
    NodeReportDto[] nodeReportDtos = new NodeReportDto[10];

    Random ran = new Random(seed);
    for (int i = 0; i < 10; i++) {
      nodeReportDtos[i] =
        NodeReportDtoTest.sample(nodeDescriptor, ran.nextLong());
    }
    dto.setNodeReportDtos(nodeReportDtos);
    dto.setExceptionMsgs(ranExcMessages(ran, 0));
    return dto;
  }

  @Test
  public void equalityTest() {
    List<Long> seeds = createSeeds(0, 20);
    for (Long seed : seeds) {
      NodeDescriptorReportDto dto1 = createSampleDto(seed);
      NodeDescriptorReportDto dto2 = createSampleDto(seed);
      assertEquals(dto1, dto2, "equality fail @ seed " + seed);
      assertEquals(
        dto1.hashCode(),
        dto2.hashCode(),
        "hash equality fail @ seed " + seed
      );
    }
  }

  // region inequality tests

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions here
  public void inequalityTest_exceptions() {
    super.inequalityTest_exceptions();
  }

  @Test
  public void inequalityTest_nodeDescriptor() {
    NodeDescriptorReportDto dto1 = sample(SampleNodeRep.Foo, 0);
    NodeDescriptorReportDto dto2 = sample(SampleNodeRep.Foo, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setNodeDescriptorDto(new NodeDescriptorDto(SampleNodeRep.Bar));
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_nodeReports() {
    NodeDescriptorReportDto dto1 = createSampleDto(0);
    NodeDescriptorReportDto dto2 = createSampleDto(0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setNodeReportDtos(null);
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  // endregion

  // region hash reset tests

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions in the methods that just call super
  public void hashResetCheck_exceptions() {
    super.hashResetCheck_exceptions();
  }

  @Test
  public void hashResetCheck_nodeDescriptor() {
    NodeDescriptorReportDto dto = sample(SampleNodeRep.Foo, 0);
    int origHash = dto.hashCode();
    dto.setNodeDescriptorDto(new NodeDescriptorDto(SampleNodeRep.Bar));
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode(), "hash diff fail");
  }

  @Test
  public void hashResetCheck_nodeReports() {
    NodeDescriptorReportDto dto = createSampleDto(0);
    int origHash = dto.hashCode();
    dto.setNodeReportDtos(null);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  // endregion

  @Test
  public void roundRobinJson() {
    List<Long> seeds = createSeeds(0, 20);
    ObjectMapper objectMapper = new ObjectMapper();
    for (Long seed : seeds) {
      NodeDescriptorReportDto dto = createSampleDto(seed);
      String jsonString = assertDoesNotThrow(() ->
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto)
      );
      NodeDescriptorReportDto revDto = assertDoesNotThrow(() ->
        objectMapper.readValue(jsonString, NodeDescriptorReportDto.class)
      );
      assertEquals(dto, revDto, "eq fail @ seed " + seed);
      assertEquals(
        dto.hashCode(),
        revDto.hashCode(),
        "hash fail @ seed " + seed
      );
    }
  }
}
