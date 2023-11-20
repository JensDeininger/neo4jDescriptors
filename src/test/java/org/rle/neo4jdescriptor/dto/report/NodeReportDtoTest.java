package org.rle.neo4jdescriptor.dto.report;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.dto.entity.LabelDescriptorDto;
import org.rle.neo4jdescriptor.dto.entity.NodeDescriptorDto;
import org.rle.neo4jdescriptor.entity.*;
import org.rle.neo4jdescriptor.samples.SampleNodeRep;

public class NodeReportDtoTest extends EntityReportDtoTest {

  @Override
  protected NodeReportDto createSampleDto(long seed) {
    NodeDescriptor[] nodeDesc = new NodeDescriptor[] {
      SampleNodeRep.Foo,
      SampleNodeRep.Bar,
      SampleNodeRep.Keks,
    };
    return sample(nodeDesc[new Random(seed).nextInt(nodeDesc.length)], seed);
  }

  protected static NodeReportDto sample(
    NodeDescriptor nodeDescriptor,
    long seed
  ) {
    Random ran = new Random(seed);
    NodeReportDto dto = new NodeReportDto(nodeDescriptor);
    dto.setExceptionMsgs(ranExcMessages(ran, 0));

    dto.setDbId(String.valueOf(ran.nextLong()));

    NodeRelationReportDto[] nodeRelationReports = nodeDescriptor
      .nodeRelations()
      .filter(o -> ran.nextDouble() < 0.8)
      .map(o -> NodeRelationReportDtoTest.sample(o, ran.nextLong()))
      .toArray(NodeRelationReportDto[]::new);

    dto.setNodeRelationReportDtos(nodeRelationReports);

    LabelDescriptorDto[] missingLabels = nodeDescriptor
      .labels(Modality.CONTINGENT)
      .filter(o -> ran.nextDouble() < 0.8)
      .map(o -> new LabelDescriptorDto(o))
      .toArray(LabelDescriptorDto[]::new);
    dto.setMissingLabelDtos(missingLabels);

    PropertyReportDto[] propertyReports = nodeDescriptor
      .properties(Modality.CONTINGENT)
      .filter(o -> ran.nextDouble() < 0.8)
      .map(o -> new PropertyReportDto(o))
      .toArray(PropertyReportDto[]::new);
    dto.setPropertyReportDtos(propertyReports);

    return dto;
  }

  @Test
  public void equalityTest() {
    List<Long> seeds = createSeeds(0, 20);
    for (Long seed : seeds) {
      NodeReportDto dto1 = createSampleDto(seed);
      NodeReportDto dto2 = createSampleDto(seed);
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

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions here
  public void inequalityTest_dbId() {
    super.inequalityTest_dbId();
  }

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions here
  public void inequalityTest_properties() {
    super.inequalityTest_properties();
  }

  @Test
  public void inequalityTest_nodeDescriptor() {
    NodeReportDto dto1 = sample(SampleNodeRep.Foo, 0);
    NodeReportDto dto2 = sample(SampleNodeRep.Foo, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setNodeDescriptorDto(new NodeDescriptorDto(SampleNodeRep.Bar));
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_missingLabels() {
    NodeReportDto dto1 = sample(SampleNodeRep.Foo, 0);
    NodeReportDto dto2 = sample(SampleNodeRep.Foo, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setMissingLabelDtos(
      new LabelDescriptorDto[dto1.getMissingLabelDtos().length + 1]
    );
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_nodeRelations() {
    NodeReportDto dto1 = sample(SampleNodeRep.Foo, 0);
    NodeReportDto dto2 = sample(SampleNodeRep.Foo, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setNodeRelationReportDtos(
      new NodeRelationReportDto[dto1.getNodeRelationReportDtos().length + 1]
    );
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

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions in the methods that just call super
  public void hashResetCheck_dbId() {
    super.hashResetCheck_dbId();
  }

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions in the methods that just call super
  public void hashResetCheck_properties() {
    super.hashResetCheck_properties();
  }

  @Test
  public void hashResetCheck_nodeDescriptor() {
    NodeReportDto dto = sample(SampleNodeRep.Foo, 0);
    int origHash = dto.hashCode();
    dto.setNodeDescriptorDto(new NodeDescriptorDto(SampleNodeRep.Bar));
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode(), "hash diff fail");
  }

  @Test
  public void hashResetCheck_missingLabels() {
    NodeReportDto dto = sample(SampleNodeRep.Foo, 0);
    int origHash = dto.hashCode();
    dto.setMissingLabelDtos(
      new LabelDescriptorDto[dto.getMissingLabelDtos().length + 1]
    );
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode(), "hash diff fail");
  }

  @Test
  public void hashResetCheck_nodeRelations() {
    NodeReportDto dto1 = sample(SampleNodeRep.Foo, 0);
    NodeReportDto dto2 = sample(SampleNodeRep.Foo, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setNodeRelationReportDtos(
      new NodeRelationReportDto[dto1.getNodeRelationReportDtos().length + 1]
    );
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  // endregion

  @Test
  public void roundRobinJson() {
    List<Long> seeds = createSeeds(0, 20);
    ObjectMapper objectMapper = new ObjectMapper();
    for (Long seed : seeds) {
      NodeReportDto dto = createSampleDto(seed);
      String jsonString = assertDoesNotThrow(() ->
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto)
      );
      NodeReportDto revDto = assertDoesNotThrow(() ->
        objectMapper.readValue(jsonString, NodeReportDto.class)
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
