package org.rle.neo4jdescriptor.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.entity.NodeRelationDto;
import org.rle.neo4jdescriptor.entity.NodeRelation;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class NodeRelationReportDto extends ReportBaseDto {

  // region private fields

  @JsonProperty("nodeRelationDto")
  private NodeRelationDto mNodeRelationDto;

  @JsonProperty("countCheck")
  private Boolean mCountCheck;

  @JsonProperty("deviantCheck")
  private Integer mDeviantCount;

  // endregion

  // region ctor

  public NodeRelationReportDto() {}

  public NodeRelationReportDto(NodeRelationDto nodeRelationDto) {
    mNodeRelationDto = nodeRelationDto;
  }

  public NodeRelationReportDto(NodeRelation nodeRelation) {
    this(new NodeRelationDto(nodeRelation));
  }

  // endregion

  // region getters and setters

  public NodeRelationDto getNodeRelationDto() {
    return mNodeRelationDto;
  }

  public Boolean getCountCheck() {
    return mCountCheck;
  }

  public Integer getDeviantCount() {
    return mDeviantCount;
  }

  public void setNodeRelationDto(NodeRelationDto nodeRelationDto) {
    mNodeRelationDto = nodeRelationDto;
  }

  public void setCountCheck(Boolean countCheck) {
    mCountCheck = countCheck;
  }

  public void setDeviantCount(Integer deviantCount) {
    mDeviantCount = deviantCount;
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mNodeRelationDto,
      mCountCheck,
      mDeviantCount
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof NodeRelationReportDto)) {
      return false;
    }
    NodeRelationReportDto cast = NodeRelationReportDto.class.cast(obj);
    if (
      !EqualityUtils.itemEquals(this.mNodeRelationDto, cast.mNodeRelationDto)
    ) {
      return false;
    }
    if (!EqualityUtils.itemEquals(this.mCountCheck, cast.mCountCheck)) {
      return false;
    }
    return EqualityUtils.itemEquals(this.mDeviantCount, cast.mDeviantCount);
  }
  //endregion
}
