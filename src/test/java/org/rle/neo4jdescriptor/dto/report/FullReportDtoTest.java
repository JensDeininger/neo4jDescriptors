package org.rle.neo4jdescriptor.dto.report;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.rle.neo4jdescriptor.samples.SampleNodeRep;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;

public class FullReportDtoTest extends ReportBaseDtoTest {

  @Override
  protected FullReportDto createSampleDto(long seed) {
    Random ran = new Random(seed);
    SampleNodeRep nodeRep = new SampleNodeRep();
    NodeDescriptorReportDto[] nodeReportDtos = nodeRep
      .nodeDescriptors()
      .map(o -> NodeDescriptorReportDtoTest.sample(o, ran.nextInt()))
      .toArray(NodeDescriptorReportDto[]::new);
    SampleRelationshipRep relRep = new SampleRelationshipRep();
    RelationshipDescriptorReportDto[] relReportDtos = relRep
      .relationshipDescriptors()
      .map(o -> RelationshipDescriptorReportDtoTest.sample(o, ran.nextInt()))
      .toArray(RelationshipDescriptorReportDto[]::new);
    FullReportDto result = new FullReportDto();
    result.setExceptionMsgs(ranExcMessages(ran, 0));
    result.setNodeDescriptorReportDtos(nodeReportDtos);
    result.setRelationshipDescriptorReportDtos(relReportDtos);
    return result;
  }

  @Test
  public void equalityTest() {
    List<Long> seeds = createSeeds(0, 20);
    for (Long seed : seeds) {
      FullReportDto dto1 = createSampleDto(seed);
      FullReportDto dto2 = createSampleDto(seed);
      assertEquals(dto1, dto2, "fail @ seed " + seed);
      assertEquals(dto1.hashCode(), dto2.hashCode(), "fail @ seed " + seed);
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
  public void inequalityTest_nodeReports() {
    FullReportDto dto1 = createSampleDto(0);
    FullReportDto dto2 = createSampleDto(0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setNodeDescriptorReportDtos(null);
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_relationshipReports() {
    FullReportDto dto1 = createSampleDto(0);
    FullReportDto dto2 = createSampleDto(0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setRelationshipDescriptorReportDtos(null);
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
  public void hashResetCheck_nodeReports() {
    FullReportDto dto = createSampleDto(0);
    int origHash = dto.hashCode();
    dto.setNodeDescriptorReportDtos(null);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  @Test
  public void hashResetCheck_relationshipReports() {
    FullReportDto dto = createSampleDto(0);
    int origHash = dto.hashCode();
    dto.setRelationshipDescriptorReportDtos(null);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  // endregion

  @Test
  public void roundRobinJson() {
    List<Long> seeds = createSeeds(0, 20);
    ObjectMapper objectMapper = new ObjectMapper();
    for (Long seed : seeds) {
      FullReportDto dto = createSampleDto(seed);
      String jsonString = assertDoesNotThrow(() ->
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto)
      );
      FullReportDto revDto = assertDoesNotThrow(() ->
        objectMapper.readValue(jsonString, FullReportDto.class)
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
