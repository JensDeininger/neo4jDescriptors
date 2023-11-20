package org.rle.neo4jdescriptor.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public abstract class EntityReportDto extends ReportBaseDto {

  // region private fields

  @JsonProperty("dbId")
  private String mDbId;

  @JsonProperty("propertyReportDtos")
  private PropertyReportDto[] mPropertyReportDtos;

  // endregion

  // region ctor

  protected EntityReportDto() {}

  protected EntityReportDto(String dbId) {
    mDbId = dbId;
  }

  // endregion

  // region getters and setters

  public String getDbId() {
    return mDbId;
  }

  public PropertyReportDto[] getPropertyReportDtos() {
    return mPropertyReportDtos;
  }

  public void setDbId(String dbId) {
    mDbId = dbId;
  }

  public void setPropertyReportDtos(PropertyReportDto[] propertyReportDtos) {
    mPropertyReportDtos = propertyReportDtos;
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mDbId,
      EqualityUtils.arrayHash(mPropertyReportDtos)
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof EntityReportDto)) {
      return false;
    }
    EntityReportDto cast = EntityReportDto.class.cast(obj);
    if (!EqualityUtils.itemEquals(this.mDbId, cast.mDbId)) {
      return false;
    }
    return EqualityUtils.arrayEquals(
      this.mPropertyReportDtos,
      cast.mPropertyReportDtos
    );
  }
  //endregion
}
