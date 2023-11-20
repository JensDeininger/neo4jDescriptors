package org.rle.neo4jdescriptor.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;

public abstract class ReportBaseDto {

  @JsonProperty("exceptionMsgs")
  private String[] mExceptionMsgs;

  protected ReportBaseDto() {}

  public String[] getExceptionMsgs() {
    return mExceptionMsgs;
  }

  public void setExceptionMsgs(String[] exceptionMsgs) {
    mExceptionMsgs = exceptionMsgs;
  }

  //region hash, equality and compare overrides

  protected int processHash() {
    return Objects.hash(
      this.getClass(), // I dont want all completely empty Dots to have hash 0
      EqualityUtils.arrayHash(mExceptionMsgs)
    );
  }

  protected boolean processEquals(Object object) {
    if (!(object instanceof ReportBaseDto)) {
      return false;
    }
    if (object == this) {
      return true;
    }
    if (!(object instanceof ReportBaseDto)) {
      return false;
    }
    ReportBaseDto cast = ReportBaseDto.class.cast(object);
    return EqualityUtils.arrayEquals(this.mExceptionMsgs, cast.mExceptionMsgs);
  }

  @Override
  public int hashCode() {
    // cant cache the hash because I dont want to hide access to the arrays indexers
    return processHash();
  }

  @Override
  public boolean equals(Object obj) {
    return processEquals(obj);
  }
  //endregion
}
