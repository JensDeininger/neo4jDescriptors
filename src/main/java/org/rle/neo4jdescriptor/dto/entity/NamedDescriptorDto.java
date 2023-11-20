package org.rle.neo4jdescriptor.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public abstract class NamedDescriptorDto extends DescriptorBaseDto {

  //region private fields

  @JsonProperty("name")
  private String mName;

  @JsonProperty("logName")
  private String mLogName;

  //endregion

  //region ctor

  protected NamedDescriptorDto() {}

  protected NamedDescriptorDto(String name, String logName) {
    mName = name;
    mLogName = logName;
  }

  //endregion

  //region accessors

  public String getName() {
    return mName;
  }

  public String getLogName() {
    return mLogName;
  }

  public void setName(String name) {
    mName = name;
  }

  public void setLogName(String logName) {
    mLogName = logName;
  }

  //endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(super.processHash(), mName, mLogName);
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof NamedDescriptorDto)) {
      return false;
    }
    NamedDescriptorDto cast = NamedDescriptorDto.class.cast(obj);
    if (!EqualityUtils.itemEquals(this.mName, cast.mName)) {
      return false;
    }
    return EqualityUtils.itemEquals(this.mLogName, cast.mLogName);
  }
  //endregion

}
