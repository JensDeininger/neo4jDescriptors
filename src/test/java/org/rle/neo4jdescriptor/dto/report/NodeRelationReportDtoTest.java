package org.rle.neo4jdescriptor.dto.report;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.dto.entity.NodeRelationDto;
import org.rle.neo4jdescriptor.entity.NodeRelation;
import org.rle.neo4jdescriptor.entity.NodeRelationOneMany;
import org.rle.neo4jdescriptor.entity.NodeRelationOneOne;
import org.rle.neo4jdescriptor.entity.NodeRelationZeroMany;
import org.rle.neo4jdescriptor.entity.NodeRelationZeroOne;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;

public class NodeRelationReportDtoTest extends ReportBaseDtoTest {

  protected ReportBaseDto createSampleDto(long seed) {
    Random ran = new Random(seed);
    int d = ran.nextInt(4);
    NodeRelation nodeRel = null;
    switch (d) {
      case 0:
        nodeRel =
          new NodeRelationOneMany(
            SampleRelationshipRep.BarFoo,
            Direction.INCOMING
          );
        break;
      case 1:
        nodeRel =
          new NodeRelationOneOne(
            SampleRelationshipRep.BarFoo,
            Direction.INCOMING
          );
        break;
      case 2:
        nodeRel =
          new NodeRelationZeroMany(
            SampleRelationshipRep.BarFoo,
            Direction.INCOMING
          );
        break;
      case 3:
        nodeRel =
          new NodeRelationZeroOne(
            SampleRelationshipRep.BarFoo,
            Direction.INCOMING
          );
        break;
    }
    return sample(nodeRel, seed);
  }

  protected static NodeRelationReportDto sample(
    NodeRelation nodeRel,
    long seed
  ) {
    Random ran = new Random(seed);
    NodeRelationReportDto dto = new NodeRelationReportDto(nodeRel);
    dto.setCountCheck(ranBool(ran));
    dto.setDeviantCount(ranInteger(ran));
    dto.setExceptionMsgs(ranExcMessages(ran, 0));
    return dto;
  }

  @Test
  public void equalityTest() {
    List<Long> seeds = createSeeds(0, 20);
    NodeRelation nodeRel = new NodeRelationOneOne(
      SampleRelationshipRep.BarFoo,
      Direction.INCOMING
    );
    for (Long seed : seeds) {
      NodeRelationReportDto dto1 = sample(nodeRel, seed);
      NodeRelationReportDto dto2 = sample(nodeRel, seed);
      assertEquals(dto1, dto2, "fail @ seed " + seed);
      assertEquals(dto1.hashCode(), dto2.hashCode(), "fail @ seed " + seed);
    }
  }

  // region inequality tests

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions in the methods that just call super
  public void inequalityTest_exceptions() {
    super.inequalityTest_exceptions();
  }

  @Test
  public void inequalityTest_nodeRelation() {
    NodeRelation nodeRel1 = new NodeRelationOneOne(
      SampleRelationshipRep.BarFoo,
      Direction.INCOMING
    );
    NodeRelation nodeRel2 = new NodeRelationOneMany(
      SampleRelationshipRep.BarFoo,
      Direction.INCOMING
    );
    NodeRelationReportDto dto1 = sample(nodeRel1, 0);
    NodeRelationReportDto dto2 = sample(nodeRel2, 0);
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_countCheck() {
    NodeRelation nodeRel = new NodeRelationOneOne(
      SampleRelationshipRep.BarFoo,
      Direction.INCOMING
    );
    NodeRelationReportDto dto1 = sample(nodeRel, 0);
    NodeRelationReportDto dto2 = sample(nodeRel, 0);
    dto1.setCountCheck(true);
    dto2.setCountCheck(null);
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_typeCheck() {
    NodeRelation nodeRel = new NodeRelationOneOne(
      SampleRelationshipRep.BarFoo,
      Direction.INCOMING
    );
    NodeRelationReportDto dto1 = sample(nodeRel, 0);
    NodeRelationReportDto dto2 = sample(nodeRel, 0);
    dto1.setDeviantCount(23);
    dto2.setDeviantCount(33);
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
  public void hashResetCheck_NodeRelation() {
    NodeRelation nodeRel1 = new NodeRelationOneOne(
      SampleRelationshipRep.BarFoo,
      Direction.INCOMING
    );
    NodeRelationReportDto dto = sample(nodeRel1, 0);
    int origHash = dto.hashCode();
    NodeRelation nodeRel2 = new NodeRelationOneMany(
      SampleRelationshipRep.BarFoo,
      Direction.INCOMING
    );
    dto.setNodeRelationDto(new NodeRelationDto(nodeRel2));
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  @Test
  public void hashResetCheck_countCheck() {
    NodeRelation nodeRel1 = new NodeRelationOneOne(
      SampleRelationshipRep.BarFoo,
      Direction.INCOMING
    );
    NodeRelationReportDto dto = sample(nodeRel1, 0);
    int origHash = dto.hashCode();
    Boolean check = dto.getCountCheck() == null ? true : null;
    dto.setCountCheck(check);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  @Test
  public void hashResetCheck_deviantCount() {
    NodeRelation nodeRel1 = new NodeRelationOneOne(
      SampleRelationshipRep.BarFoo,
      Direction.INCOMING
    );
    NodeRelationReportDto dto = sample(nodeRel1, 0);
    int origHash = dto.hashCode();
    dto.setDeviantCount(dto.getDeviantCount() + 1);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  // endregion

  @Test
  public void roundRobinJson() {
    List<Long> seeds = createSeeds(0, 20);
    NodeRelation nodeRel1 = new NodeRelationOneOne(
      SampleRelationshipRep.BarFoo,
      Direction.INCOMING
    );

    ObjectMapper objectMapper = new ObjectMapper();
    for (Long seed : seeds) {
      NodeRelationReportDto dto = sample(nodeRel1, seed);
      String jsonString = assertDoesNotThrow(() ->
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto)
      );
      NodeRelationReportDto revDto = assertDoesNotThrow(() ->
        objectMapper.readValue(jsonString, NodeRelationReportDto.class)
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
