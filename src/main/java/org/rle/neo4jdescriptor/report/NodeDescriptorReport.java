package org.rle.neo4jdescriptor.report;

import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.report.NodeDescriptorReportDto;
import org.rle.neo4jdescriptor.dto.report.NodeReportDto;
import org.rle.neo4jdescriptor.dto.report.ReportBaseDto;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;
import org.rle.neo4jdescriptor.repository.NodeRepository;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class NodeDescriptorReport extends ReportBase {

  // region static strings

  private static final String VALIDATION_HEADER_S1 = "Validation Report for %s";

  protected static final String WRONG_NODEDESCRIPTOR =
    "NodeReport has wrong NodeDescriptor";

  protected static final String DUPLICATE_NODE_ID =
    "already contains a NodeReport for a node with the specified id";

  // endregion

  // region private fields

  private final NodeDescriptor mNodeDescriptor;

  private final TreeSet<NodeReport> mNodeReports = new TreeSet<>(
    new NodeReportComparator()
  );

  // endregion

  // region ctor

  public NodeDescriptorReport(NodeDescriptor nodeDescriptor) {
    mNodeDescriptor = nodeDescriptor;
  }

  public NodeDescriptorReport(
    NodeRepository nodeRepository,
    NodeDescriptorReportDto dto
  ) {
    super(dto);
    String c = dto.getNodeDescriptorDto().getClassName();
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
    for (NodeReportDto nodeRepDto : dto.getNodeReportDtos()) {
      NodeReport nodeRep = new NodeReport(nodeRepository, nodeRepDto);
      addNodeReport(nodeRep.closeReport());
    }
  }

  // endregion

  // region accessors

  public NodeDescriptor nodeDescriptor() {
    return mNodeDescriptor;
  }

  public Stream<NodeReport> nodeReports() {
    return mNodeReports.stream();
  }

  @Override
  public int errorCount() {
    int count = super.exceptionCount();
    count += mNodeReports.stream().mapToInt(NodeReport::errorCount).sum();
    return count;
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
        VALIDATION_HEADER_S1,
        mNodeDescriptor.getClass().getSimpleName()
      );
    String[] nodePrints = mNodeReports
      .stream()
      .filter(o -> o.errorCount() > 0)
      .map(o -> o.print(gI + sI, sI))
      .toArray(String[]::new);
    return (
      header +
      System.lineSeparator() +
      String.join(System.lineSeparator(), nodePrints)
    );
  }

  @Override
  protected ReportBaseDto getEmptyDto() {
    return new NodeDescriptorReportDto(mNodeDescriptor);
  }

  @Override
  public NodeDescriptorReportDto getDto() {
    NodeDescriptorReportDto dto = (NodeDescriptorReportDto) super.getDto();
    dto.setNodeReportDtos(
      mNodeReports
        .stream()
        .map(NodeReport::getDto)
        .toArray(NodeReportDto[]::new)
    );
    return dto;
  }

  // endregion

  // region setters

  public NodeDescriptorReport addNodeReport(NodeReport nodeReport) {
    if (nodeReport == null) {
      throw new IllegalArgumentException();
    }
    if (!mNodeDescriptor.equals(nodeReport.nodeDescriptor())) {
      throw new IllegalArgumentException(WRONG_NODEDESCRIPTOR);
    }
    if (!nodeReport.isClosed()) {
      throw new IllegalArgumentException(SUBREPORT_MUST_BE_CLOSED);
    }
    if (
      mNodeReports
        .stream()
        .anyMatch(o -> Objects.equals(o.dbId(), nodeReport.dbId()))
    ) {
      throw new IllegalArgumentException(DUPLICATE_NODE_ID);
    }
    super.closedCheck();
    mNodeReports.add(nodeReport);
    resetHash();
    return this;
  }

  @Override
  public NodeDescriptorReport closeReport() {
    return (NodeDescriptorReport) super.closeReport();
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mNodeDescriptor,
      EqualityUtils.sortedSetHash(mNodeReports)
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof NodeDescriptorReport)) {
      return false;
    }
    NodeDescriptorReport cast = NodeDescriptorReport.class.cast(obj);
    if (!EqualityUtils.itemEquals(this.mNodeDescriptor, cast.mNodeDescriptor)) {
      return false;
    }
    return EqualityUtils.setEquals(this.mNodeReports, cast.mNodeReports);
  }
  //endregion
}
