package org.rle.neo4jdescriptor.report;

import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.entity.RelationshipDescriptorDto;
import org.rle.neo4jdescriptor.dto.report.RelationshipDescriptorReportDto;
import org.rle.neo4jdescriptor.dto.report.RelationshipReportDto;
import org.rle.neo4jdescriptor.dto.report.ReportBaseDto;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.repository.RelationshipRepository;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class RelationshipDescriptorReport extends ReportBase {

  // region static strings

  private static final String VALIDATION_HEADER_S1 = "Validation Report for %s";

  protected static final String WRONG_RELATIONSHIPDESCRIPTOR =
    "RelationshipReport has wrong RelationshipDescriptor";

  protected static final String DUPLICATE_RELATIONSHIP_ID =
    "already contains a RelationshipReport for a relationship with the specified id";

  // endregion

  // region private fields

  private final RelationshipDescriptor mRelationshipDescriptor;

  private final TreeSet<RelationshipReport> mRelationshipReports = new TreeSet<>(
    new RelationshipReportComparator()
  );

  // endregion

  // region ctor

  public RelationshipDescriptorReport(
    RelationshipDescriptor relationshipDescriptor
  ) {
    mRelationshipDescriptor = relationshipDescriptor;
  }

  public RelationshipDescriptorReport(
    RelationshipRepository relRepository,
    RelationshipDescriptorReportDto dto
  ) {
    super(dto);
    String c = dto.getRelationshipDescriptorDto().getClassName();
    Optional<RelationshipDescriptor> optRelDesc = relRepository
      .relationshipDescriptors()
      .filter(o -> o.getClass().getName().equals(c))
      .findFirst();
    if (optRelDesc.isEmpty()) {
      throw new IllegalArgumentException(
        REPOSITORY_CONTAINS_NO_SUCH_RELATIONSHIPDESCRIPTOR
      );
    }
    mRelationshipDescriptor = optRelDesc.get();
    for (RelationshipReportDto relRepDto : dto.getRelationshipReportDtos()) {
      RelationshipReport relRep = new RelationshipReport(
        relRepository,
        relRepDto
      );
      addRelationshipReport(relRep.closeReport());
    }
  }

  // endregion

  // region accessors

  public RelationshipDescriptor relationshipDescriptor() {
    return mRelationshipDescriptor;
  }

  public Stream<RelationshipReport> relationshipReports() {
    return mRelationshipReports.stream();
  }

  @Override
  public int errorCount() {
    int count = super.exceptionCount();
    count +=
      mRelationshipReports
        .stream()
        .mapToInt(RelationshipReport::errorCount)
        .sum();
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
        mRelationshipDescriptor.getClass().getSimpleName()
      );
    String[] nodePrints = mRelationshipReports
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
    return new RelationshipDescriptorReportDto();
  }

  @Override
  public RelationshipDescriptorReportDto getDto() {
    RelationshipDescriptorReportDto dto = (RelationshipDescriptorReportDto) super.getDto();
    dto.setRelationshipDescriptorDto(
      new RelationshipDescriptorDto(mRelationshipDescriptor)
    );
    RelationshipReportDto[] relRerpDtos = mRelationshipReports
      .stream()
      .map(RelationshipReport::getDto)
      .toArray(RelationshipReportDto[]::new);
    dto.setRelationshipReportDtos(relRerpDtos);
    return dto;
  }

  // endregion

  // region setters

  public RelationshipDescriptorReport addRelationshipReport(
    RelationshipReport relationshipReport
  ) {
    if (relationshipReport == null) {
      throw new IllegalArgumentException();
    }
    if (
      !mRelationshipDescriptor.equals(
        relationshipReport.relationshipDescriptor()
      )
    ) {
      throw new IllegalArgumentException(WRONG_RELATIONSHIPDESCRIPTOR);
    }
    if (!relationshipReport.isClosed()) {
      throw new IllegalArgumentException(SUBREPORT_MUST_BE_CLOSED);
    }
    if (
      mRelationshipReports
        .stream()
        .anyMatch(o -> Objects.equals(o.dbId(), relationshipReport.dbId()))
    ) {
      throw new IllegalArgumentException(DUPLICATE_RELATIONSHIP_ID);
    }
    super.closedCheck();
    mRelationshipReports.add(relationshipReport);
    resetHash();
    return this;
  }

  @Override
  public RelationshipDescriptorReport closeReport() {
    return (RelationshipDescriptorReport) super.closeReport();
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mRelationshipDescriptor,
      EqualityUtils.sortedSetHash(mRelationshipReports)
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof RelationshipDescriptorReport)) {
      return false;
    }
    RelationshipDescriptorReport cast =
      RelationshipDescriptorReport.class.cast(obj);
    if (
      !EqualityUtils.itemEquals(
        this.mRelationshipDescriptor,
        cast.mRelationshipDescriptor
      )
    ) {
      return false;
    }
    return EqualityUtils.setEquals(
      this.mRelationshipReports,
      cast.mRelationshipReports
    );
  }
  //endregion
}
