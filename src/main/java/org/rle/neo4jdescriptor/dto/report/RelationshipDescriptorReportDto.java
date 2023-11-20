package org.rle.neo4jdescriptor.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.entity.RelationshipDescriptorDto;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class RelationshipDescriptorReportDto extends ReportBaseDto {

  // region private fields

  @JsonProperty("relationshipDescriptorDto")
  private RelationshipDescriptorDto mRelationshipDescriptorDto;

  @JsonProperty("relationshipReportDtos")
  private RelationshipReportDto[] mRelationshipReportDtos;

  // endregion

  // region ctor

  public RelationshipDescriptorReportDto() {}

  public RelationshipDescriptorReportDto(
    RelationshipDescriptorDto relationshipDescriptorDto
  ) {
    mRelationshipDescriptorDto = relationshipDescriptorDto;
  }

  public RelationshipDescriptorReportDto(
    RelationshipDescriptor relationshipDescriptor
  ) {
    this(new RelationshipDescriptorDto(relationshipDescriptor));
  }

  //endregion

  // region getters and setters

  public RelationshipDescriptorDto getRelationshipDescriptorDto() {
    return mRelationshipDescriptorDto;
  }

  public RelationshipReportDto[] getRelationshipReportDtos() {
    return mRelationshipReportDtos;
  }

  public void setRelationshipDescriptorDto(
    RelationshipDescriptorDto relationshipDescriptorDto
  ) {
    mRelationshipDescriptorDto = relationshipDescriptorDto;
  }

  public void setRelationshipReportDtos(
    RelationshipReportDto[] relationshipReportDtos
  ) {
    mRelationshipReportDtos = relationshipReportDtos;
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mRelationshipDescriptorDto,
      EqualityUtils.arrayHash(mRelationshipReportDtos)
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof RelationshipDescriptorReportDto)) {
      return false;
    }
    RelationshipDescriptorReportDto cast =
      RelationshipDescriptorReportDto.class.cast(obj);
    if (
      !EqualityUtils.itemEquals(
        this.mRelationshipDescriptorDto,
        cast.mRelationshipDescriptorDto
      )
    ) {
      return false;
    }
    return EqualityUtils.arrayEquals(
      this.mRelationshipReportDtos,
      cast.mRelationshipReportDtos
    );
  }
  //endregion
}
