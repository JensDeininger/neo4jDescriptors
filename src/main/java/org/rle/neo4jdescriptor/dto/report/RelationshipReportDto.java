package org.rle.neo4jdescriptor.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.entity.RelationshipDescriptorDto;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class RelationshipReportDto extends EntityReportDto {

  // region private fields

  @JsonProperty("relationshipDescriptorDto")
  private RelationshipDescriptorDto mRelationshipDescriptorDto;

  @JsonProperty("startNodeCheck")
  private Boolean mStartNodeCheck;

  @JsonProperty("endNodeCheck")
  private Boolean mEndNodeCheck;

  // endregion

  // region ctor

  public RelationshipReportDto() {}

  public RelationshipReportDto(
    RelationshipDescriptorDto relationshipDescriptorDto
  ) {
    mRelationshipDescriptorDto = relationshipDescriptorDto;
  }

  public RelationshipReportDto(RelationshipDescriptor relationshipDescriptor) {
    this(new RelationshipDescriptorDto(relationshipDescriptor));
  }

  // endregion

  // region getters and setters

  public RelationshipDescriptorDto getRelationshipDescriptorDto() {
    return mRelationshipDescriptorDto;
  }

  public Boolean getStartNodeCheck() {
    return mStartNodeCheck;
  }

  public Boolean getEndNodeCheck() {
    return mEndNodeCheck;
  }

  public void setRelationshipDescriptorDto(
    RelationshipDescriptorDto relationshipDescriptorDto
  ) {
    mRelationshipDescriptorDto = relationshipDescriptorDto;
  }

  public void setStartNodeCheck(Boolean startNodeCheck) {
    mStartNodeCheck = startNodeCheck;
  }

  public void setEndNodeCheck(Boolean endNodeCheck) {
    mEndNodeCheck = endNodeCheck;
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mRelationshipDescriptorDto,
      mStartNodeCheck,
      mEndNodeCheck
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof RelationshipReportDto)) {
      return false;
    }
    RelationshipReportDto cast = RelationshipReportDto.class.cast(obj);
    if (!EqualityUtils.itemEquals(this.mStartNodeCheck, cast.mStartNodeCheck)) {
      return false;
    }
    if (!EqualityUtils.itemEquals(this.mEndNodeCheck, cast.mEndNodeCheck)) {
      return false;
    }
    return EqualityUtils.itemEquals(
      this.mRelationshipDescriptorDto,
      cast.mRelationshipDescriptorDto
    );
  }
  //endregion
}
