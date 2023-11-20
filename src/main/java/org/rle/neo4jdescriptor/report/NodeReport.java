package org.rle.neo4jdescriptor.report;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.dto.entity.LabelDescriptorDto;
import org.rle.neo4jdescriptor.dto.entity.NodeDescriptorDto;
import org.rle.neo4jdescriptor.dto.report.NodeRelationReportDto;
import org.rle.neo4jdescriptor.dto.report.NodeReportDto;
import org.rle.neo4jdescriptor.dto.report.PropertyReportDto;
import org.rle.neo4jdescriptor.entity.EntityDescriptor;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;
import org.rle.neo4jdescriptor.entity.NodeIdentifier;
import org.rle.neo4jdescriptor.repository.NodeRepository;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class NodeReport extends EntityReport {

  // region static strings

  private static final String NODE = "Node";

  private static final String MISSING_LABELS = "Missing Labels";

  private static final String FAULTY_RELATIONS = "Faulty Relations";

  protected static final String NO_SUCH_LABEL =
    "NodeDescriptor contains no such Label as contingent";

  protected static final String NO_SUCH_NODE_RELATION =
    "NodeDescriptor contains no such NodeRelation";

  protected static final String DUPLICATE_NODE_RELATION_REPORT =
    "Duplicate report for NodeRelation";

  // endregion

  // region private fields

  private final NodeDescriptor mNodeDescriptor;

  private final TreeSet<LabelDescriptor> mMissingLabels = new TreeSet<>();

  private final TreeSet<NodeRelationReport> mNodeRelationReports = new TreeSet<>(
    new NodeRelationReportComparator()
  );

  // endregion

  // region ctor

  public NodeReport(NodeDescriptor nodeDesc, String nodeId) {
    super(nodeId);
    if (nodeDesc == null) {
      throw new IllegalArgumentException();
    }
    mNodeDescriptor = nodeDesc;
  }

  public NodeReport(
    NodeRepository nodeRepository,
    NodeReportDto nodeReportDto
  ) {
    super(nodeReportDto);
    String c = nodeReportDto.getNodeDescriptorDto().getClassName();
    Optional<NodeDescriptor> optNodeDesc = nodeRepository
      .nodeDescriptors()
      .filter(o -> o.getClass().getName().equals(c))
      .findFirst();
    if (optNodeDesc.isEmpty()) {
      throw new IllegalArgumentException(
        REPOSITORY_CONTAINS_NO_SUCH_NODEDESCRIPTOR
      );
    }
    mNodeDescriptor = optNodeDesc.get();
    for (PropertyReportDto p : nodeReportDto.getPropertyReportDtos()) {
      PropertyReport propRep = new PropertyReport(mNodeDescriptor, p);
      addPropertyReport(propRep.closeReport());
    }
    for (LabelDescriptorDto lblDto : nodeReportDto.getMissingLabelDtos()) {
      addMissingLabel(new LabelDescriptor(lblDto));
    }
    for (NodeRelationReportDto nodeRelDto : nodeReportDto.getNodeRelationReportDtos()) {
      NodeRelationReport nodeRelReport = new NodeRelationReport(
        mNodeDescriptor,
        nodeRelDto
      );
      addNodeRelationReport(nodeRelReport.closeReport());
    }
  }

  // endregion

  // region accessors

  @Override
  protected EntityDescriptor entityDescriptor() {
    return mNodeDescriptor;
  }

  public NodeIdentifier nodeIdentifier() {
    return mNodeDescriptor.identifier();
  }

  public NodeDescriptor nodeDescriptor() {
    return mNodeDescriptor;
  }

  @Override
  public int errorCount() {
    int count = super.exceptionCount();
    count += propertyReportCount();
    count += mMissingLabels.size();
    count +=
      mNodeRelationReports
        .stream()
        .mapToInt(NodeRelationReport::errorCount)
        .sum();
    return count;
  }

  public Stream<LabelDescriptor> missingLabels() {
    return mMissingLabels.stream();
  }

  public Stream<NodeRelationReport> nodeRelationReports() {
    return mNodeRelationReports.stream();
  }

  private String missingLabelsMsg(String gI, String sI) {
    if (mMissingLabels.isEmpty()) {
      return "";
    }
    String header = gI + String.format("%s:", MISSING_LABELS);
    String[] bits = mMissingLabels
      .stream()
      .map(o -> gI + sI + o.name())
      .toArray(String[]::new);
    String body = String.join(System.lineSeparator(), bits);
    return String.format("%s%s%s", header, System.lineSeparator(), body);
  }

  private String faultyRelationsMsg(String gI, String sI) {
    if (
      mNodeRelationReports
        .stream()
        .mapToInt(NodeRelationReport::errorCount)
        .sum() ==
      0
    ) {
      return "";
    }
    String header = gI + String.format("%s:", FAULTY_RELATIONS);
    String[] bits = mNodeRelationReports
      .stream()
      .filter(o -> o.errorCount() > 0)
      .map(o -> o.print(gI + sI, sI))
      .toArray(String[]::new);
    String body = String.join(System.lineSeparator(), bits);
    return String.format("%s%s%s", header, System.lineSeparator(), body);
  }

  @Override
  public String print(String globalIndent, String subItemIndent) {
    if (errorCount() == 0) {
      return "";
    }
    String gI = globalIndent == null ? "" : globalIndent;
    String sI = subItemIndent == null ? "" : subItemIndent;
    String header =
      gI +
      String.format(
        "%s<%s> %s %s",
        NODE,
        dbId(),
        FAILED_TO_MATCH,
        nodeDescriptor().getClass().getSimpleName()
      );
    String lblMsg = missingLabelsMsg(gI + sI, sI);
    String propMsg = propertyReportsString(gI + sI, sI);
    String relMsg = faultyRelationsMsg(gI + sI, sI);
    String excMsg = printExceptions(gI + sI, sI);
    String[] bits = new String[] { header, lblMsg, propMsg, relMsg, excMsg };
    String[] bits2 = Arrays
      .stream(bits)
      .filter(o -> o.length() > 0)
      .toArray(String[]::new);
    return String.join(System.lineSeparator(), bits2);
  }

  @Override
  protected NodeReportDto getEmptyDto() {
    return new NodeReportDto();
  }

  @Override
  public NodeReportDto getDto() {
    NodeReportDto dto = (NodeReportDto) super.getDto();
    LabelDescriptorDto[] missingLabelDtos = mMissingLabels
      .stream()
      .map(LabelDescriptorDto::new)
      .toArray(LabelDescriptorDto[]::new);
    NodeRelationReportDto[] nodeRelationReportDtos = mNodeRelationReports
      .stream()
      .map(NodeRelationReport::getDto)
      .toArray(NodeRelationReportDto[]::new);
    dto.setNodeDescriptorDto(new NodeDescriptorDto(mNodeDescriptor));
    dto.setMissingLabelDtos(missingLabelDtos);
    dto.setNodeRelationReportDtos(nodeRelationReportDtos);
    return dto;
  }

  // endregion

  // region setters

  public NodeReport addMissingLabel(LabelDescriptor missingLabel) {
    if (missingLabel == null) {
      throw new IllegalArgumentException();
    }
    if (
      mNodeDescriptor
        .labels(Modality.CONTINGENT)
        .noneMatch(o -> o.equals(missingLabel))
    ) {
      throw new IllegalArgumentException(NO_SUCH_LABEL);
    }
    super.closedCheck();
    mMissingLabels.add(missingLabel);
    resetHash();
    return this;
  }

  @Override
  public NodeReport addPropertyReport(PropertyReport propReport) {
    return (NodeReport) super.addPropertyReport(propReport);
  }

  public NodeReport addNodeRelationReport(NodeRelationReport nodeRelReport) {
    if (nodeRelReport == null) {
      throw new IllegalArgumentException();
    }
    if (!nodeRelReport.isClosed()) {
      throw new IllegalArgumentException(SUBREPORT_MUST_BE_CLOSED);
    }
    if (
      mNodeRelationReports
        .stream()
        .anyMatch(o -> o.nodeRelation().equals(nodeRelReport.nodeRelation()))
    ) {
      throw new IllegalArgumentException(DUPLICATE_NODE_RELATION_REPORT);
    }
    if (
      mNodeDescriptor
        .nodeRelations()
        .noneMatch(o -> nodeRelReport.nodeRelation().equals(o))
    ) {
      throw new IllegalArgumentException(NO_SUCH_NODE_RELATION);
    }
    super.closedCheck();
    mNodeRelationReports.add(nodeRelReport);
    resetHash();
    return this;
  }

  @Override
  public NodeReport closeReport() {
    return (NodeReport) super.closeReport();
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mNodeDescriptor,
      EqualityUtils.sortedSetHash(mMissingLabels),
      EqualityUtils.sortedSetHash(mNodeRelationReports)
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof NodeReport)) {
      return false;
    }
    NodeReport cast = NodeReport.class.cast(obj);
    if (!EqualityUtils.itemEquals(this.mNodeDescriptor, cast.mNodeDescriptor)) {
      return false;
    }
    if (!EqualityUtils.setEquals(this.mMissingLabels, cast.mMissingLabels)) {
      return false;
    }
    return EqualityUtils.setEquals(
      this.mNodeRelationReports,
      cast.mNodeRelationReports
    );
  }
  //endregion
}
