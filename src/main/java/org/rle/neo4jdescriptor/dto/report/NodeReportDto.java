package org.rle.neo4jdescriptor.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.entity.LabelDescriptorDto;
import org.rle.neo4jdescriptor.dto.entity.NodeDescriptorDto;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class NodeReportDto extends EntityReportDto {

  // region private fields

  @JsonProperty("nodeDescriptorDto")
  private NodeDescriptorDto mNodeDescriptorDto;

  @JsonProperty("missingLabels")
  private LabelDescriptorDto[] mMissingLabelDtos;

  @JsonProperty("nodeRelationReports")
  private NodeRelationReportDto[] mNodeRelationReportDtos;

  // endregion

  // region ctor

  public NodeReportDto() {}

  public NodeReportDto(NodeDescriptorDto nodeDescriptorDto) {
    mNodeDescriptorDto = nodeDescriptorDto;
  }

  public NodeReportDto(NodeDescriptor nodeDescriptor) {
    this(new NodeDescriptorDto(nodeDescriptor));
  }

  // endregion

  // region getters and setters

  public NodeDescriptorDto getNodeDescriptorDto() {
    return mNodeDescriptorDto;
  }

  public LabelDescriptorDto[] getMissingLabelDtos() {
    return mMissingLabelDtos;
  }

  public NodeRelationReportDto[] getNodeRelationReportDtos() {
    return mNodeRelationReportDtos;
  }

  public void setNodeDescriptorDto(NodeDescriptorDto nodeDescriptorDto) {
    mNodeDescriptorDto = nodeDescriptorDto;
  }

  public void setMissingLabelDtos(LabelDescriptorDto[] missingLabelDtos) {
    mMissingLabelDtos = missingLabelDtos;
  }

  public void setNodeRelationReportDtos(
    NodeRelationReportDto[] nodeRelationReportDtos
  ) {
    mNodeRelationReportDtos = nodeRelationReportDtos;
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mNodeDescriptorDto,
      EqualityUtils.arrayHash(mMissingLabelDtos),
      EqualityUtils.arrayHash(mNodeRelationReportDtos)
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof NodeReportDto)) {
      return false;
    }
    NodeReportDto cast = NodeReportDto.class.cast(obj);
    if (
      !EqualityUtils.itemEquals(
        this.mNodeDescriptorDto,
        cast.mNodeDescriptorDto
      )
    ) {
      return false;
    }
    if (
      !EqualityUtils.arrayEquals(this.mMissingLabelDtos, cast.mMissingLabelDtos)
    ) {
      return false;
    }
    return EqualityUtils.arrayEquals(
      this.mNodeRelationReportDtos,
      cast.mNodeRelationReportDtos
    );
  }
  //endregion
}
