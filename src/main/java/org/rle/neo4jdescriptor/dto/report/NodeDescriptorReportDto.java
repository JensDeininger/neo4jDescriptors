package org.rle.neo4jdescriptor.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.entity.NodeDescriptorDto;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class NodeDescriptorReportDto extends ReportBaseDto {

  // region private fields

  @JsonProperty("nodeDescriptorDto")
  private NodeDescriptorDto mNodeDescriptorDto;

  @JsonProperty("nodeReportDtos")
  private NodeReportDto[] mNodeReportDtos;

  // endregion

  // region ctor

  public NodeDescriptorReportDto() {}

  public NodeDescriptorReportDto(NodeDescriptorDto nodeDescriptorDto) {
    mNodeDescriptorDto = nodeDescriptorDto;
  }

  public NodeDescriptorReportDto(NodeDescriptor nodeDescriptor) {
    this(new NodeDescriptorDto(nodeDescriptor));
  }

  //endregion

  // region getters and setters

  public NodeDescriptorDto getNodeDescriptorDto() {
    return mNodeDescriptorDto;
  }

  public NodeReportDto[] getNodeReportDtos() {
    return mNodeReportDtos;
  }

  public void setNodeDescriptorDto(NodeDescriptorDto nodeDescriptorDto) {
    mNodeDescriptorDto = nodeDescriptorDto;
  }

  public void setNodeReportDtos(NodeReportDto[] nodeReportDtos) {
    mNodeReportDtos = nodeReportDtos;
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mNodeDescriptorDto,
      EqualityUtils.arrayHash(mNodeReportDtos)
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof NodeDescriptorReportDto)) {
      return false;
    }
    NodeDescriptorReportDto cast = NodeDescriptorReportDto.class.cast(obj);
    if (
      !EqualityUtils.itemEquals(
        this.mNodeDescriptorDto,
        cast.mNodeDescriptorDto
      )
    ) {
      return false;
    }
    return EqualityUtils.arrayEquals(
      this.mNodeReportDtos,
      cast.mNodeReportDtos
    );
  }
  //endregion
}
