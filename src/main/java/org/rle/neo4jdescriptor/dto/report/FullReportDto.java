package org.rle.neo4jdescriptor.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class FullReportDto extends ReportBaseDto {

  @JsonProperty("nodeDescriptorReportDtos")
  private NodeDescriptorReportDto[] mNodeDescriptorReportDtos;

  @JsonProperty("relationshipDescriptorReportDtos")
  private RelationshipDescriptorReportDto[] mRelationshipDescriptorReportDtos;

  public FullReportDto() {
    // I dont like it when I can not see any ctor in a class
  }

  // region getters and setters

  public NodeDescriptorReportDto[] getNodeDescriptorReportDtos() {
    return mNodeDescriptorReportDtos;
  }

  public RelationshipDescriptorReportDto[] getRelationshipDescriptorReportDtos() {
    return mRelationshipDescriptorReportDtos;
  }

  public void setNodeDescriptorReportDtos(
    NodeDescriptorReportDto[] nodeDescriptorReportDtos
  ) {
    mNodeDescriptorReportDtos = nodeDescriptorReportDtos;
  }

  public void setRelationshipDescriptorReportDtos(
    RelationshipDescriptorReportDto[] relationshipDescriptorReportDtos
  ) {
    mRelationshipDescriptorReportDtos = relationshipDescriptorReportDtos;
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      EqualityUtils.arrayHash(mNodeDescriptorReportDtos),
      EqualityUtils.arrayHash(mRelationshipDescriptorReportDtos)
    );
  }

  @Override
  protected boolean processEquals(Object object) {
    if (!super.processEquals(object)) {
      return false;
    }
    if (!(object instanceof FullReportDto)) {
      return false;
    }
    FullReportDto cast = FullReportDto.class.cast(object);
    if (
      !EqualityUtils.arrayEquals(
        this.mNodeDescriptorReportDtos,
        cast.mNodeDescriptorReportDtos
      )
    ) {
      return false;
    }
    return EqualityUtils.arrayEquals(
      this.mRelationshipDescriptorReportDtos,
      cast.mRelationshipDescriptorReportDtos
    );
  }
  //endregion
}
