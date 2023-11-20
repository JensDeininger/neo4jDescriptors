package org.rle.neo4jdescriptor.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.report.ReportBaseDto;

public abstract class ReportBase {

  // region private fields

  protected static final String SUBREPORT_MUST_BE_CLOSED =
    "Can not add an open subreport";

  protected static final String CAN_NOT_MODIFY_CLOSED_REPORT =
    "Can not modify closed report";

  protected static final String REPOSITORY_CONTAINS_NO_SUCH_NODEDESCRIPTOR =
    "NodeRepository contains no such NodeDescriptor";

  protected static final String REPOSITORY_CONTAINS_NO_SUCH_RELATIONSHIPDESCRIPTOR =
    "NodeRepository contains no such RelationshipDescriptor";

  private boolean mIsClosed = false;

  private ArrayList<String> mExceptionMsgs = new ArrayList<>();

  private Integer mHash;

  // endregion

  protected ReportBase() {}

  protected ReportBase(ReportBaseDto reportBaseDto) {
    String[] excMsgs = reportBaseDto.getExceptionMsgs();
    if (excMsgs != null) {
      Arrays
        .stream(reportBaseDto.getExceptionMsgs())
        .forEach(o -> mExceptionMsgs.add(o));
    }
  }

  // region getters

  public boolean isClosed() {
    return mIsClosed;
  }

  public int exceptionCount() {
    return mExceptionMsgs.size();
  }

  public Stream<String> getExceptionMsg() {
    return mExceptionMsgs.stream();
  }

  public abstract int errorCount();

  protected String printExceptions(String globalIndent, String subItemIndent) {
    if (mExceptionMsgs.isEmpty()) {
      return "";
    }
    String[] excMsgArray = new String[mExceptionMsgs.size()];
    String line0Prefix = mExceptionMsgs.size() == 1 ? "" : "- ";
    String lineNPrefix = mExceptionMsgs.size() == 1 ? "" : "  ";

    for (int i = 0; i < mExceptionMsgs.size(); i++) {
      String[] splits = mExceptionMsgs.get(i).split(System.lineSeparator());
      String rejoin =
        globalIndent +
        subItemIndent +
        line0Prefix +
        String.join(
          System.lineSeparator() + globalIndent + subItemIndent + lineNPrefix,
          splits
        );
      excMsgArray[i] = rejoin;
    }
    String res = globalIndent + "Exception(s):" + System.lineSeparator();
    return res + (String.join(System.lineSeparator(), excMsgArray));
  }

  public abstract String print(String globalIndent, String subItemIndent);

  protected abstract ReportBaseDto getEmptyDto();

  public ReportBaseDto getDto() {
    ReportBaseDto dto = getEmptyDto();
    dto.setExceptionMsgs(mExceptionMsgs.toArray(String[]::new));
    return dto;
  }

  // endregion

  // region setters & config

  protected void resetHash() {
    mHash = null;
  }

  /**
   * Simply throws an IllegalStateException if this {@link ReportBase}
   * has already been closed
   */
  protected void closedCheck() {
    if (mIsClosed) {
      throw new IllegalStateException(CAN_NOT_MODIFY_CLOSED_REPORT);
    }
  }

  public ReportBase closeReport() {
    mHash = null;
    this.mIsClosed = true;
    return this;
  }

  public ReportBase addException(Exception exc) {
    if (exc == null) {
      throw new IllegalArgumentException();
    }
    closedCheck();
    String msg = exc.getMessage();
    // NullPointerExceptions dont have a message, apparently
    if (msg == null) {
      msg = exc.getClass().getSimpleName();
    }
    mExceptionMsgs.add(msg);
    return this;
  }

  // endregion

  //region hash, equality and compare overrides

  protected int processHash() {
    return Objects.hash(mIsClosed, EqualityUtils.listHash(mExceptionMsgs));
  }

  protected boolean processEquals(Object object) {
    if (!(object instanceof ReportBase)) {
      return false;
    }
    if (object == this) {
      return true;
    }
    ReportBase cast = ReportBase.class.cast(object);
    if (mIsClosed != cast.mIsClosed) {
      return false;
    }
    return EqualityUtils.listEquals(this.mExceptionMsgs, cast.mExceptionMsgs);
  }

  protected int processCompare(ReportBase other) {
    if (other == null) {
      return 1;
    }
    if (other == this) {
      return 0;
    }
    int typeComp =
      this.getClass().getName().compareTo(other.getClass().getName());
    if (typeComp != 0) {
      return typeComp;
    }
    return Boolean.compare(this.mIsClosed, other.mIsClosed);
  }

  @Override
  public int hashCode() {
    if (mHash == null) {
      mHash = processHash();
    }
    return mHash;
  }

  @Override
  public boolean equals(Object obj) {
    return processEquals(obj);
  }
  //endregion
}
