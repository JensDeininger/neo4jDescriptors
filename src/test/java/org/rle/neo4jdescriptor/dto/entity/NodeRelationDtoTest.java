package org.rle.neo4jdescriptor.dto.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.Cardinality;
import org.rle.neo4jdescriptor.entity.NodeRelation;
import org.rle.neo4jdescriptor.entity.NodeRelationOneMany;
import org.rle.neo4jdescriptor.entity.NodeRelationOneOne;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;

public class NodeRelationDtoTest {

  @Test
  public void equalityTest() {
    NodeRelation nodeRel = new NodeRelationOneOne(
      SampleRelationshipRep.FooBar,
      Direction.INCOMING
    );
    NodeRelationDto dto1 = new NodeRelationDto(nodeRel);
    NodeRelationDto dto2 = new NodeRelationDto(nodeRel);
    assertEquals(dto1, dto2, "fail equality");
    assertEquals(dto1.hashCode(), dto2.hashCode(), "fail equality");
  }

  @Test
  public void inequalityTest_RelationDescriptor() {
    NodeRelation nodeRel1 = new NodeRelationOneOne(
      SampleRelationshipRep.FooBar,
      Direction.INCOMING
    );
    NodeRelationDto dto1 = new NodeRelationDto(nodeRel1);
    NodeRelation nodeRel2 = new NodeRelationOneOne(
      SampleRelationshipRep.BarFoo,
      Direction.INCOMING
    );
    NodeRelationDto dto2 = new NodeRelationDto(nodeRel2);
    assertNotEquals(dto1, dto2, "fail equality");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "fail equality");
  }

  @Test
  public void inequalityTest_Direction() {
    NodeRelation nodeRel1 = new NodeRelationOneOne(
      SampleRelationshipRep.FooBar,
      Direction.INCOMING
    );
    NodeRelationDto dto1 = new NodeRelationDto(nodeRel1);
    NodeRelation nodeRel2 = new NodeRelationOneOne(
      SampleRelationshipRep.FooBar,
      Direction.OUTGOING
    );
    NodeRelationDto dto2 = new NodeRelationDto(nodeRel2);
    assertNotEquals(dto1, dto2, "fail equality");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "fail equality");
  }

  @Test
  public void inequalityTest_Cardinality() {
    NodeRelation nodeRel1 = new NodeRelationOneOne(
      SampleRelationshipRep.FooBar,
      Direction.INCOMING
    );
    NodeRelationDto dto1 = new NodeRelationDto(nodeRel1);
    NodeRelation nodeRel2 = new NodeRelationOneMany(
      SampleRelationshipRep.FooBar,
      Direction.INCOMING
    );
    NodeRelationDto dto2 = new NodeRelationDto(nodeRel2);
    assertNotEquals(dto1, dto2, "fail equality");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "fail equality");
  }

  @Test
  public void hashResetTest_RelationDescriptor() {
    NodeRelation nodeRel = new NodeRelationOneOne(
      SampleRelationshipRep.FooBar,
      Direction.INCOMING
    );
    NodeRelationDto dto = new NodeRelationDto(nodeRel);
    int origHash = dto.hashCode();
    dto.setRelationshipDescriptorDto(
      new RelationshipDescriptorDto(SampleRelationshipRep.BarFoo)
    );
    assertNotEquals(origHash, dto.hashCode());
  }

  @Test
  public void hashResetTest_Direction() {
    NodeRelation nodeRel = new NodeRelationOneOne(
      SampleRelationshipRep.FooBar,
      Direction.INCOMING
    );
    NodeRelationDto dto = new NodeRelationDto(nodeRel);
    int origHash = dto.hashCode();
    dto.setDirection(Direction.OUTGOING);
    assertNotEquals(origHash, dto.hashCode());
  }

  @Test
  public void hashResetTest_Cardinality() {
    NodeRelation nodeRel = new NodeRelationOneOne(
      SampleRelationshipRep.FooBar,
      Direction.INCOMING
    );
    NodeRelationDto dto = new NodeRelationDto(nodeRel);
    int origHash = dto.hashCode();
    dto.setCardinality(Cardinality.ONE_MANY);
    assertNotEquals(origHash, dto.hashCode());
  }

  @Test
  public void roundRobinJson() {
    NodeRelation nodeRel = new NodeRelationOneOne(
      SampleRelationshipRep.FooBar,
      Direction.INCOMING
    );
    NodeRelationDto dto = new NodeRelationDto(nodeRel);
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = assertDoesNotThrow(() ->
      objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto)
    );
    NodeRelationDto revDto = assertDoesNotThrow(() ->
      objectMapper.readValue(jsonString, NodeRelationDto.class)
    );
    assertEquals(dto, revDto, "eq fail");
    assertEquals(dto.hashCode(), revDto.hashCode(), "hash fail");
  }
}
