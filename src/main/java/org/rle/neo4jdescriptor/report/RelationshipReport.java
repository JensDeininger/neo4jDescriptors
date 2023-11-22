package org.rle.neo4jdescriptor.report;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.dto.entity.RelationshipDescriptorDto;
import org.rle.neo4jdescriptor.dto.report.PropertyReportDto;
import org.rle.neo4jdescriptor.dto.report.RelationshipReportDto;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.repository.RelationshipRepository;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class RelationshipReport extends EntityReport {

  // region static strings

  private static final String RELATIONSHIP = "Relationship";

  protected static final String NO_SUCH_NODEDESCRIPTOR_SPECIFIED =
    "no such node descriptor specified";

  private static final String CPYHER_STRING =
    "MATCH (n)-[s]-(m) WHERE elementId(s) = '%s' RETURN n,m,s";

  private static final String FAULTY_STARTNODE = "Start node does not match";

  private static final String FAULTY_ENDNODE = "End node does not match";

  // endregion

  // region private fields

  private final RelationshipDescriptor mRelationshipDescriptor;

  private Boolean mStartNodeCheck;

  private Boolean mEndNodeCheck;

  // endregion

  // region ctor

  public RelationshipReport(
    RelationshipDescriptor relDesc,
    String relationshipDbId
  ) {
    super(relationshipDbId);
    if (relDesc == null) {
      throw new IllegalArgumentException();
    }
    mRelationshipDescriptor = relDesc;
  }

  public RelationshipReport(
    RelationshipRepository relRepository,
    RelationshipReportDto relReportDto
  ) {
    super(relReportDto);
    String c = relReportDto.getRelationshipDescriptorDto().getClassName();
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
    for (PropertyReportDto p : relReportDto.getPropertyReportDtos()) {
      PropertyReport propRep = new PropertyReport(mRelationshipDescriptor, p);
      addPropertyReport(propRep.closeReport());
    }
    mStartNodeCheck = relReportDto.getStartNodeCheck();
    mEndNodeCheck = relReportDto.getEndNodeCheck();
  }

  // endregion

  // region accessors

  @Override
  protected RelationshipDescriptor entityDescriptor() {
    return mRelationshipDescriptor;
  }

  public RelationshipDescriptor relationshipDescriptor() {
    return mRelationshipDescriptor;
  }

  public Boolean startNodeCheck() {
    return mStartNodeCheck;
  }

  public Boolean endNodeCheck() {
    return mEndNodeCheck;
  }

  @Override
  public int errorCount() {
    int count = super.exceptionCount();
    count += propertyReportCount();
    if (Boolean.FALSE.equals(mStartNodeCheck)) {
      count++;
    }
    if (Boolean.FALSE.equals(mEndNodeCheck)) {
      count++;
    }
    return count;
  }

  private String startNodeMsg(String indent) {
    String res = "";
    if (Boolean.FALSE.equals(mStartNodeCheck)) {
      String ndStr = mRelationshipDescriptor
        .startNodeDescriptor()
        .getClass()
        .getSimpleName();
      res = indent + String.format("%s: %s", FAULTY_STARTNODE, ndStr);
    }
    return res;
  }

  private String endNodeMsg(String indent) {
    String res = "";
    if (Boolean.FALSE.equals(mEndNodeCheck)) {
      String ndStr = mRelationshipDescriptor
        .endNodeDescriptor()
        .getClass()
        .getSimpleName();
      res = indent + String.format("%s: %s", FAULTY_ENDNODE, ndStr);
    }
    return res;
  }

  @Override
  public String print(String globalIndent, String subItemIndent) {
    String gI = globalIndent == null ? "" : globalIndent;
    String sI = subItemIndent == null ? "" : subItemIndent;
    String header =
      gI +
      String.format(
        "%s(%s) %s: %s",
        RELATIONSHIP,
        String.format(CPYHER_STRING, dbId()),
        FAILED_TO_MATCH,
        mRelationshipDescriptor.getClass().getSimpleName()
      );
    String propMsg = propertyReportsString(gI + sI, sI);
    String excMsg = printExceptions(gI + sI, sI);
    String startNodeMsg = startNodeMsg(gI + sI);
    String endNodeMsg = endNodeMsg(gI + sI);
    String[] bits = new String[] {
      header,
      propMsg,
      startNodeMsg,
      endNodeMsg,
      excMsg,
    };
    String[] bits2 = Arrays
      .stream(bits)
      .filter(o -> o.length() > 0)
      .toArray(String[]::new);
    return String.join(System.lineSeparator(), bits2);
  }

  @Override
  protected RelationshipReportDto getEmptyDto() {
    return new RelationshipReportDto();
  }

  @Override
  public RelationshipReportDto getDto() {
    RelationshipReportDto dto = (RelationshipReportDto) super.getDto();
    dto.setRelationshipDescriptorDto(
      new RelationshipDescriptorDto(mRelationshipDescriptor)
    );
    dto.setStartNodeCheck(mStartNodeCheck);
    dto.setEndNodeCheck(mEndNodeCheck);
    return dto;
  }

  // endregion

  // region setters

  public RelationshipReport setStartNodeCheck(boolean value) {
    closedCheck();
    if (mRelationshipDescriptor.startNodeDescriptor() == null) {
      throw new IllegalStateException(NO_SUCH_NODEDESCRIPTOR_SPECIFIED);
    }
    mStartNodeCheck = value;
    resetHash();
    return this;
  }

  public RelationshipReport setEndNodeCheck(boolean value) {
    closedCheck();
    if (mRelationshipDescriptor.endNodeDescriptor() == null) {
      throw new IllegalStateException(NO_SUCH_NODEDESCRIPTOR_SPECIFIED);
    }
    mEndNodeCheck = value;
    resetHash();
    return this;
  }

  @Override
  public RelationshipReport addPropertyReport(PropertyReport propReport) {
    return (RelationshipReport) super.addPropertyReport(propReport);
  }

  @Override
  public RelationshipReport closeReport() {
    return (RelationshipReport) super.closeReport();
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mRelationshipDescriptor,
      mStartNodeCheck,
      mEndNodeCheck
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof RelationshipReport)) {
      return false;
    }
    RelationshipReport cast = RelationshipReport.class.cast(obj);
    if (!EqualityUtils.itemEquals(this.mStartNodeCheck, cast.mStartNodeCheck)) {
      return false;
    }
    if (!EqualityUtils.itemEquals(this.mEndNodeCheck, cast.mEndNodeCheck)) {
      return false;
    }
    return EqualityUtils.itemEquals(
      this.mRelationshipDescriptor,
      cast.mRelationshipDescriptor
    );
  }
  //endregion
}
