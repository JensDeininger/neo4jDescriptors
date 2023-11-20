package org.rle.neo4jdescriptor.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.Cardinality;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.entity.NodeRelation;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class NodeRelationDto extends DescriptorBaseDto {

  // region private fields

  @JsonProperty("relationshipDescriptorDto")
  private RelationshipDescriptorDto mRelationshipDescriptorDto;

  @JsonProperty("direction")
  private Direction mDirection;

  @JsonProperty("cardinality")
  private Cardinality mCardinality;

  // endregion

  // region ctor

  public NodeRelationDto() {}

  public NodeRelationDto(NodeRelation nodeRelation) {
    mRelationshipDescriptorDto =
      nodeRelation == null
        ? null
        : new RelationshipDescriptorDto(nodeRelation.relationshipDescriptor());
    mDirection = nodeRelation == null ? null : nodeRelation.direction();
    mCardinality = nodeRelation == null ? null : nodeRelation.cardinality();
  }

  // endregion

  // region getters and setters

  public RelationshipDescriptorDto getRelationshipDescriptorDto() {
    return mRelationshipDescriptorDto;
  }

  public Direction getDirection() {
    return mDirection;
  }

  public Cardinality getCardinality() {
    return mCardinality;
  }

  public void setRelationshipDescriptorDto(
    RelationshipDescriptorDto relDescDto
  ) {
    mRelationshipDescriptorDto = relDescDto;
  }

  public void setDirection(Direction dir) {
    mDirection = dir;
  }

  public void setCardinality(Cardinality card) {
    mCardinality = card;
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mRelationshipDescriptorDto,
      mDirection,
      mCardinality
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof NodeRelationDto)) {
      return false;
    }
    NodeRelationDto cast = NodeRelationDto.class.cast(obj);
    if (
      !EqualityUtils.itemEquals(
        this.mRelationshipDescriptorDto,
        cast.mRelationshipDescriptorDto
      )
    ) {
      return false;
    }
    if (!EqualityUtils.itemEquals(this.mDirection, cast.mDirection)) {
      return false;
    }
    return EqualityUtils.itemEquals(this.mCardinality, cast.mCardinality);
  }
  //endregion

}
