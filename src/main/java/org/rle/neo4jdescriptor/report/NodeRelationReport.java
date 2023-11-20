package org.rle.neo4jdescriptor.report;

import java.util.Objects;
import java.util.Optional;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.entity.NodeRelationDto;
import org.rle.neo4jdescriptor.dto.report.NodeRelationReportDto;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;
import org.rle.neo4jdescriptor.entity.NodeRelation;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class NodeRelationReport extends ReportBase {

  // region private fields

  protected static final String EXPECTED_RELATIONS_FAIL_MSG =
    "Failed to find expected relation";

  protected static final String NODEDESCRIPOR_CONTAINS_NO_SUCH_NODERELATION =
    "NodeDescriptor contains no such NodeRelation";

  private NodeRelation mNodeRelation;

  private Boolean mCountCheck;

  private Integer mDeviantCount;

  // endregion

  // region ctor

  public NodeRelationReport(NodeRelation nodeRel) {
    if (nodeRel == null) {
      throw new IllegalArgumentException();
    }
    mNodeRelation = nodeRel;
  }

  public NodeRelationReport(
    NodeDescriptor nodeDescriptor,
    NodeRelationReportDto nodeRelationReportDto
  ) {
    super(nodeRelationReportDto);
    NodeRelationDto nodeRelDto = nodeRelationReportDto.getNodeRelationDto();
    Optional<NodeRelation> nodeRel = nodeDescriptor
      .nodeRelations()
      .filter(o -> nodeRelDto.equals(new NodeRelationDto(o)))
      .findFirst();
    if (nodeRel.isEmpty()) {
      throw new IllegalArgumentException(
        NODEDESCRIPOR_CONTAINS_NO_SUCH_NODERELATION
      );
    }
    mNodeRelation = nodeRel.get();
    mCountCheck = nodeRelationReportDto.getCountCheck();
    mDeviantCount = nodeRelationReportDto.getDeviantCount();
  }

  // endregion

  // region getters

  public NodeRelation nodeRelation() {
    return mNodeRelation;
  }

  public Boolean checkResultCount() {
    return mCountCheck;
  }

  public Integer deviantCount() {
    return mDeviantCount;
  }

  @Override
  public int errorCount() {
    int count = super.exceptionCount();
    if (Boolean.FALSE.equals(mCountCheck)) {
      count++;
    }
    return count;
  }

  private String printNodeRelErrors(String globalIndent) {
    if (Boolean.FALSE.equals(mCountCheck)) {
      String str = String.format(
        "%s: %s. Found: %d",
        EXPECTED_RELATIONS_FAIL_MSG,
        mNodeRelation.print(""),
        mDeviantCount
      );
      return globalIndent + str;
    }
    return "";
  }

  @Override
  public String print(String globalIndent, String subItemIndent) {
    String gI = globalIndent == null ? "" : globalIndent;
    String sI = subItemIndent == null ? "" : subItemIndent;
    String propProbs = printNodeRelErrors(gI);
    String excProbs = printExceptions(gI, sI);
    String filling = "";
    if (propProbs.length() > 0 && excProbs.length() > 0) {
      filling = System.lineSeparator();
    }
    return propProbs + filling + excProbs;
  }

  @Override
  protected NodeRelationReportDto getEmptyDto() {
    return new NodeRelationReportDto();
  }

  @Override
  public NodeRelationReportDto getDto() {
    NodeRelationReportDto dto = (NodeRelationReportDto) super.getDto();
    dto.setNodeRelationDto(new NodeRelationDto(mNodeRelation));
    dto.setCountCheck(mCountCheck);
    dto.setDeviantCount(mDeviantCount);
    return dto;
  }

  // endregion

  // region setters

  public NodeRelationReport setCountCheck(boolean value) {
    super.closedCheck();
    mCountCheck = value;
    resetHash();
    return this;
  }

  public NodeRelationReport setDeviantCount(int count) {
    super.closedCheck();
    mDeviantCount = count;
    resetHash();
    return this;
  }

  @Override
  public NodeRelationReport closeReport() {
    return (NodeRelationReport) super.closeReport();
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mNodeRelation,
      mCountCheck,
      mDeviantCount
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof NodeRelationReport)) {
      return false;
    }
    NodeRelationReport cast = NodeRelationReport.class.cast(obj);
    if (!EqualityUtils.itemEquals(this.mNodeRelation, cast.mNodeRelation)) {
      return false;
    }
    if (!EqualityUtils.itemEquals(this.mCountCheck, cast.mCountCheck)) {
      return false;
    }
    return EqualityUtils.itemEquals(this.mDeviantCount, cast.mDeviantCount);
  }
  //endregion
}
