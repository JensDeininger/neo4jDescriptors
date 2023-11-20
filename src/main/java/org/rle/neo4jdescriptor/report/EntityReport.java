package org.rle.neo4jdescriptor.report;

import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.dto.report.EntityReportDto;
import org.rle.neo4jdescriptor.dto.report.PropertyReportDto;
import org.rle.neo4jdescriptor.entity.EntityDescriptor;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public abstract class EntityReport extends ReportBase {

  // region stric strings

  protected static final String FAILED_TO_MATCH = "Failed to match";

  private static final String FAULTY_PROPERTIES = "Faulty properties";

  protected static final String NO_SUCH_PROPERTY =
    "The EntityDescriptor has no such contingent Property";

  protected static final String DUPLICATE_PROPERTY_REPORT =
    "Duplicate report for Property";

  //endregion

  // region private fields

  private final String mDbId;

  private final TreeSet<PropertyReport> mPropertyReports = new TreeSet<>(
    new PropertyReportComparator()
  );

  // endregion

  // region ctor

  protected EntityReport(String dbId) {
    mDbId = dbId;
  }

  protected EntityReport(EntityReportDto entityReportDto) {
    super(entityReportDto);
    mDbId = entityReportDto.getDbId();
  }

  // endregion

  // region accessors

  protected abstract EntityDescriptor entityDescriptor();

  public String dbId() {
    return mDbId;
  }

  public int propertyReportCount() {
    return mPropertyReports.stream().mapToInt(PropertyReport::errorCount).sum();
  }

  public Stream<PropertyReport> propertyReports() {
    return mPropertyReports.stream();
  }

  protected String propertyReportsString(
    String globalIndent,
    String subItemIndent
  ) {
    if (
      mPropertyReports.stream().filter(o -> o.errorCount() > 0).count() == 0
    ) {
      return "";
    }
    String gI = globalIndent == null ? "" : globalIndent;
    String sI = subItemIndent == null ? "" : subItemIndent;
    String header = gI + FAULTY_PROPERTIES + ":";
    String[] propReps = mPropertyReports
      .stream()
      .map(o -> o.print(gI + sI, sI))
      .toArray(String[]::new);
    String propBit = String.join(System.lineSeparator(), propReps);
    return (header + System.lineSeparator() + propBit);
  }

  @Override
  public EntityReportDto getDto() {
    EntityReportDto dto = (EntityReportDto) super.getDto();
    PropertyReportDto[] propReportDtos = mPropertyReports
      .stream()
      .map(PropertyReport::getDto)
      .toArray(PropertyReportDto[]::new);
    dto.setPropertyReportDtos(propReportDtos);
    dto.setDbId(mDbId);
    return dto;
  }

  // endregion

  // region setters

  public EntityReport addPropertyReport(PropertyReport propReport) {
    if (propReport == null) {
      throw new IllegalArgumentException();
    }
    if (!propReport.isClosed()) {
      throw new IllegalArgumentException(SUBREPORT_MUST_BE_CLOSED);
    }
    if (
      entityDescriptor()
        .properties(Modality.CONTINGENT)
        .noneMatch(o -> o.equals(propReport.propertyDescriptor()))
    ) {
      throw new IllegalArgumentException(NO_SUCH_PROPERTY);
    }
    if (
      mPropertyReports
        .stream()
        .anyMatch(o ->
          o.propertyDescriptor().equals(propReport.propertyDescriptor())
        )
    ) {
      throw new IllegalArgumentException(DUPLICATE_PROPERTY_REPORT);
    }
    super.closedCheck();
    mPropertyReports.add(propReport);
    resetHash();
    return this;
  }

  @Override
  public EntityReport closeReport() {
    return (EntityReport) super.closeReport();
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mDbId,
      EqualityUtils.sortedSetHash(mPropertyReports)
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof EntityReport)) {
      return false;
    }
    EntityReport cast = EntityReport.class.cast(obj);
    if (!EqualityUtils.itemEquals(this.mDbId, cast.mDbId)) {
      return false;
    }
    return EqualityUtils.setEquals(
      this.mPropertyReports,
      cast.mPropertyReports
    );
  }
  //endregion
}
