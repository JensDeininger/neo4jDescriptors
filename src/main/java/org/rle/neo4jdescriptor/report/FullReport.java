package org.rle.neo4jdescriptor.report;

import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.report.FullReportDto;
import org.rle.neo4jdescriptor.dto.report.NodeDescriptorReportDto;
import org.rle.neo4jdescriptor.dto.report.RelationshipDescriptorReportDto;
import org.rle.neo4jdescriptor.repository.NodeRepository;
import org.rle.neo4jdescriptor.repository.RelationshipRepository;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class FullReport extends ReportBase {

  // region static strings

  protected static final String SM_GLOBAL_HEADER =
    "Validation report: Found %s problems";

  protected static final String SM_DASH_LINE = "------------------------------";

  protected static final String SM_NODE_HEADER = "NodeDescriptor reports: ";

  protected static final String SM_RELATIONSHIP_HEADER =
    "RelationshipDescriptor reports: ";

  public static final String SM_NO_PROBLEMS = "no problems found";

  protected static final String SM_FOUND_X_PROBLEMS = "found %d problem(s)";

  protected static final String SM_DUPLICATE_NODEDESCRIPTOR =
    "can not add a second report for the same NodeDescriptor";

  protected static final String SM_DUPLICATE_RELATIONSHIPDESCRIPTOR =
    "can not add a second report for the same RelationshipDescriptor";

  // endregion

  // region private fields

  private final TreeSet<NodeDescriptorReport> mNodeDescriptorReports = new TreeSet<>(
    new NodeDescriptorReportComparator()
  );

  private final TreeSet<RelationshipDescriptorReport> mRelationshipDescriptorReports = new TreeSet<>(
    new RelationshipDescriptorReportComparator()
  );

  // endregion

  //region ctor

  public FullReport() {}

  public FullReport(
    NodeRepository nodeRepo,
    RelationshipRepository relRepo,
    FullReportDto dto
  ) {
    super(dto);
    NodeDescriptorReportDto[] nodeDescriptorReportDtos = dto.getNodeDescriptorReportDtos();
    for (NodeDescriptorReportDto nDto : nodeDescriptorReportDtos) {
      NodeDescriptorReport nodeDescriptorReport = new NodeDescriptorReport(
        nodeRepo,
        nDto
      )
        .closeReport();
      addNodeDescriptorReport(nodeDescriptorReport);
    }
    RelationshipDescriptorReportDto[] relDescriptorReportDtos = dto.getRelationshipDescriptorReportDtos();
    for (RelationshipDescriptorReportDto rDto : relDescriptorReportDtos) {
      RelationshipDescriptorReport relRep = new RelationshipDescriptorReport(
        relRepo,
        rDto
      )
        .closeReport();
      addRelationshipDescriptorReport(relRep);
    }
  }

  // endregion

  // region accessors

  public int nodeReportCount() {
    return mNodeDescriptorReports.size();
  }

  public int relationshipReportCount() {
    return mRelationshipDescriptorReports.size();
  }

  public Stream<NodeDescriptorReport> nodeDescriptorReports() {
    return mNodeDescriptorReports.stream();
  }

  public Stream<RelationshipDescriptorReport> relationshipDescriptorReports() {
    return mRelationshipDescriptorReports.stream();
  }

  @Override
  public int errorCount() {
    int excCount = super.exceptionCount();
    int nCount = mNodeDescriptorReports
      .stream()
      .mapToInt(NodeDescriptorReport::errorCount)
      .sum();
    int rCount = mRelationshipDescriptorReports
      .stream()
      .mapToInt(RelationshipDescriptorReport::errorCount)
      .sum();
    return excCount + nCount + rCount;
  }

  private String nodeSection(String[] nodeRepBits, int errorCount) {
    String sectionSummary = errorCount == 0
      ? SM_NO_PROBLEMS
      : String.format(SM_FOUND_X_PROBLEMS, errorCount);
    String result = SM_NODE_HEADER + sectionSummary;
    if (errorCount != 0) {
      result =
        result +
        System.lineSeparator() +
        String.join(System.lineSeparator(), nodeRepBits);
    }
    return result;
  }

  private String relationshipSection(String[] relRepBits, int errorCount) {
    String sectionSummary = errorCount == 0
      ? SM_NO_PROBLEMS
      : String.format(SM_FOUND_X_PROBLEMS, errorCount);
    String result = SM_RELATIONSHIP_HEADER + sectionSummary;
    if (errorCount != 0) {
      result =
        result +
        System.lineSeparator() +
        String.join(System.lineSeparator(), relRepBits);
    }
    return result;
  }

  @Override
  public String print(String globalIndent, String subItemIndent) {
    if (errorCount() == 0) {
      return SM_NO_PROBLEMS;
    }
    String gI = globalIndent == null ? "" : globalIndent;
    String sI = subItemIndent == null ? "" : subItemIndent;
    int nodeErrorCount = mNodeDescriptorReports
      .stream()
      .mapToInt(NodeDescriptorReport::errorCount)
      .sum();
    String[] nodeRepBits = mNodeDescriptorReports
      .stream()
      .map(o -> o.print(gI + sI, sI))
      .filter(o -> o.length() > 0)
      .toArray(String[]::new);
    int relErrorCount = mRelationshipDescriptorReports
      .stream()
      .mapToInt(RelationshipDescriptorReport::errorCount)
      .sum();
    String[] relRepBits = mRelationshipDescriptorReports
      .stream()
      .map(o -> o.print(gI + sI, sI))
      .filter(o -> o.length() > 0)
      .toArray(String[]::new);
    String globalHeader = gI + String.format(SM_GLOBAL_HEADER, errorCount());
    String nodeSection = gI + nodeSection(nodeRepBits, nodeErrorCount);
    String relSection = gI + relationshipSection(relRepBits, relErrorCount);
    String sep =
      System.lineSeparator() + gI + SM_DASH_LINE + System.lineSeparator();
    return String.join(sep, globalHeader, nodeSection, relSection);
  }

  @Override
  protected FullReportDto getEmptyDto() {
    return new FullReportDto();
  }

  @Override
  public FullReportDto getDto() {
    FullReportDto dto = (FullReportDto) super.getDto();
    NodeDescriptorReportDto[] nodeReportDtos = mNodeDescriptorReports
      .stream()
      .map(NodeDescriptorReport::getDto)
      .toArray(NodeDescriptorReportDto[]::new);
    RelationshipDescriptorReportDto[] relationshipReportDtos = mRelationshipDescriptorReports
      .stream()
      .map(RelationshipDescriptorReport::getDto)
      .toArray(RelationshipDescriptorReportDto[]::new);
    dto.setNodeDescriptorReportDtos(nodeReportDtos);
    dto.setRelationshipDescriptorReportDtos(relationshipReportDtos);
    return dto;
  }

  // endregion

  // region setters

  public FullReport addNodeDescriptorReport(
    NodeDescriptorReport nodeDescriptorReport
  ) {
    if (nodeDescriptorReport == null) {
      throw new IllegalArgumentException();
    }
    if (!nodeDescriptorReport.isClosed()) {
      throw new IllegalArgumentException(SUBREPORT_MUST_BE_CLOSED);
    }
    super.closedCheck();
    if (
      mNodeDescriptorReports
        .stream()
        .anyMatch(o ->
          o.nodeDescriptor().equals(nodeDescriptorReport.nodeDescriptor())
        )
    ) {
      throw new IllegalArgumentException(SM_DUPLICATE_NODEDESCRIPTOR);
    }
    mNodeDescriptorReports.add(nodeDescriptorReport);
    resetHash();
    return this;
  }

  public FullReport addRelationshipDescriptorReport(
    RelationshipDescriptorReport relationshipDescriptorReport
  ) {
    if (relationshipDescriptorReport == null) {
      throw new IllegalArgumentException();
    }
    if (!relationshipDescriptorReport.isClosed()) {
      throw new IllegalArgumentException(SUBREPORT_MUST_BE_CLOSED);
    }
    super.closedCheck();
    if (
      mRelationshipDescriptorReports
        .stream()
        .anyMatch(o ->
          o
            .relationshipDescriptor()
            .equals(relationshipDescriptorReport.relationshipDescriptor())
        )
    ) {
      throw new IllegalArgumentException(SM_DUPLICATE_RELATIONSHIPDESCRIPTOR);
    }
    mRelationshipDescriptorReports.add(relationshipDescriptorReport);
    resetHash();
    return this;
  }

  @Override
  public FullReport closeReport() {
    return (FullReport) super.closeReport();
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      EqualityUtils.sortedSetHash(mNodeDescriptorReports),
      EqualityUtils.sortedSetHash(mRelationshipDescriptorReports)
    );
  }

  @Override
  protected boolean processEquals(Object object) {
    if (!super.processEquals(object)) {
      return false;
    }
    if (!(object instanceof FullReport)) {
      return false;
    }
    FullReport cast = FullReport.class.cast(object);
    if (
      !EqualityUtils.setEquals(
        this.mNodeDescriptorReports,
        cast.mNodeDescriptorReports
      )
    ) {
      return false;
    }
    return EqualityUtils.setEquals(
      this.mRelationshipDescriptorReports,
      cast.mRelationshipDescriptorReports
    );
  }
  //endregion
}
