package org.rle.neo4jdescriptor.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.entity.PropertyDescriptorDto;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class PropertyReportDto extends ReportBaseDto {

  // region private fields

  @JsonProperty("propertyDescriptorDto")
  private PropertyDescriptorDto mPropDescDto;

  @JsonProperty("keyExistsCheck")
  private Boolean mKeyExistsCheck;

  @JsonProperty("typeCheck")
  private Boolean mTypeCheck;

  @JsonProperty("enumValueCheck")
  private Boolean mEnumValueCheck;

  @JsonProperty("deviantPropertyTypeName")
  private String mDeviantPropertyTypeName;

  // endregion

  // region ctor

  public PropertyReportDto() {}

  public PropertyReportDto(PropertyDescriptorDto propDescDto) {
    mPropDescDto = propDescDto;
  }

  public PropertyReportDto(PropertyDescriptor propDesc) {
    this(new PropertyDescriptorDto(propDesc));
  }

  // endregion

  // region getters and setters

  public PropertyDescriptorDto getPropertyDescriptorDto() {
    return mPropDescDto;
  }

  public Boolean getKeyExistsCheck() {
    return mKeyExistsCheck;
  }

  public Boolean getTypeCheck() {
    return mTypeCheck;
  }

  public Boolean getEnumValueCheck() {
    return mEnumValueCheck;
  }

  public String getDeviantPropertyTypeName() {
    return mDeviantPropertyTypeName;
  }

  public void setPropertyDescriptorDto(PropertyDescriptorDto propDescDto) {
    mPropDescDto = propDescDto;
  }

  public void setKeyExistsCheck(Boolean existsCheck) {
    mKeyExistsCheck = existsCheck;
  }

  public void setTypeCheck(Boolean typeCheck) {
    mTypeCheck = typeCheck;
  }

  public void setEnumValueCheck(Boolean enumValueCheck) {
    mEnumValueCheck = enumValueCheck;
  }

  public void setDeviantPropertyTypeName(String deviantPropertyTypeName) {
    mDeviantPropertyTypeName = deviantPropertyTypeName;
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mPropDescDto,
      mKeyExistsCheck,
      mTypeCheck,
      mEnumValueCheck,
      mDeviantPropertyTypeName
    );
  }

  @Override
  protected boolean processEquals(Object object) {
    if (!super.processEquals(object)) {
      return false;
    }
    if (!(object instanceof PropertyReportDto)) {
      return false;
    }
    PropertyReportDto cast = PropertyReportDto.class.cast(object);
    if (!EqualityUtils.itemEquals(this.mPropDescDto, cast.mPropDescDto)) {
      return false;
    }
    if (!EqualityUtils.itemEquals(this.mKeyExistsCheck, cast.mKeyExistsCheck)) {
      return false;
    }
    if (!EqualityUtils.itemEquals(this.mTypeCheck, cast.mTypeCheck)) {
      return false;
    }
    if (!EqualityUtils.itemEquals(this.mEnumValueCheck, cast.mEnumValueCheck)) {
      return false;
    }
    return EqualityUtils.itemEquals(
      this.mDeviantPropertyTypeName,
      cast.mDeviantPropertyTypeName
    );
  }
  //endregion
}
