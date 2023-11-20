package org.rle.neo4jdescriptor.report;

import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.EnumProperty;
import org.rle.neo4jdescriptor.dto.report.PropertyReportDto;
import org.rle.neo4jdescriptor.dto.report.ReportBaseDto;
import java.util.Objects;
import java.util.Optional;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.dto.entity.PropertyDescriptorDto;
import org.rle.neo4jdescriptor.entity.EntityDescriptor;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public class PropertyReport extends ReportBase {

  // region static string

  private static final String MISSING_KEY_MSG = "no such key found";

  private static final String WRONG_TYPE_EXP_S1_ACTUAL_S2 =
    "type error. Expected: %s, actual: %s";

  private static final String INVALID_ENUM_VALUE_S1 =
    "invalid enum value. Valid values are: %s";

  private static final String ENTITY_CONTAINS_NO_SUCH_PROPERTY =
    "entity contains no such property";

  // endregion

  // region private fields

  private final PropertyDescriptor mPropertyDescriptor;

  private Boolean mKeyExistsCheck;

  private Boolean mTypeCheck;

  private Boolean mEnumValueCheck;

  private String mDeviantPropertyTypeName;

  // endregion

  // region ctor

  public PropertyReport(PropertyDescriptor propertyDescriptor) {
    if (propertyDescriptor == null) {
      throw new IllegalArgumentException();
    }
    mPropertyDescriptor = propertyDescriptor;
  }

  public PropertyReport(
    EntityDescriptor entityDescriptor,
    PropertyReportDto propReportDto
  ) {
    super(propReportDto);
    PropertyDescriptorDto propDescDto = propReportDto.getPropertyDescriptorDto();
    Optional<PropertyDescriptor> propDesc = entityDescriptor
      .properties(Modality.BOTH)
      .filter(o -> propDescDto.equals(new PropertyDescriptorDto(o)))
      .findFirst();
    if (propDesc.isEmpty()) {
      throw new IllegalArgumentException(ENTITY_CONTAINS_NO_SUCH_PROPERTY);
    }
    mPropertyDescriptor = propDesc.get();
    mKeyExistsCheck = propReportDto.getKeyExistsCheck();
    mTypeCheck = propReportDto.getTypeCheck();
    mEnumValueCheck = propReportDto.getEnumValueCheck();
    mDeviantPropertyTypeName = propReportDto.getDeviantPropertyTypeName();
  }

  // endregion

  // region getters

  public PropertyDescriptor propertyDescriptor() {
    return mPropertyDescriptor;
  }

  public Boolean checkResultKeyExists() {
    return mKeyExistsCheck;
  }

  public Boolean checkResultType() {
    return mTypeCheck;
  }

  public String deviantTypeName() {
    return mDeviantPropertyTypeName;
  }

  public Boolean checkResultEnumValue() {
    return mEnumValueCheck;
  }

  @Override
  public int errorCount() {
    // only one of them will ever be false though.
    int count = super.exceptionCount();
    if (Boolean.FALSE.equals(mKeyExistsCheck)) {
      count++;
    }
    if (Boolean.FALSE.equals(mTypeCheck)) {
      count++;
    }
    if (Boolean.FALSE.equals(mEnumValueCheck)) {
      count++;
    }
    return count;
  }

  private String printPropertyProblems(String globalIndent) {
    String res;
    if (Boolean.FALSE.equals(mKeyExistsCheck)) {
      res = String.format("%s: " + MISSING_KEY_MSG, mPropertyDescriptor.key());
    } else if (Boolean.FALSE.equals(mTypeCheck)) {
      res =
        String.format(
          "%s: " + WRONG_TYPE_EXP_S1_ACTUAL_S2,
          mPropertyDescriptor.key(),
          mPropertyDescriptor.dbType().getSimpleName(),
          mDeviantPropertyTypeName
        );
    } else if (
      (mPropertyDescriptor instanceof EnumProperty) &&
      Boolean.FALSE.equals(mEnumValueCheck)
    ) {
      var cast = EnumProperty.class.cast(mPropertyDescriptor);
      String[] validValues = cast.validDbValuesPrintVersion2();
      String validValuesString = String.join(", ", validValues);
      res =
        String.format(
          "%s: " + INVALID_ENUM_VALUE_S1,
          mPropertyDescriptor.key(),
          validValuesString
        );
    } else {
      return "";
    }
    return globalIndent + res;
  }

  @Override
  public String print(String globalIndent, String subItemIndent) {
    if (this.errorCount() == 0) {
      return "";
    }
    String gI = globalIndent == null ? "" : globalIndent;
    String sI = subItemIndent == null ? "" : subItemIndent;
    String propProbs = printPropertyProblems(gI);
    String excProbs = printExceptions(gI, sI);
    String filling = "";
    if (propProbs.length() > 0 && excProbs.length() > 0) {
      filling = System.lineSeparator();
    }
    return propProbs + filling + excProbs;
  }

  @Override
  protected ReportBaseDto getEmptyDto() {
    return new PropertyReportDto();
  }

  @Override
  public PropertyReportDto getDto() {
    PropertyReportDto dto = (PropertyReportDto) super.getDto();
    dto.setPropertyDescriptorDto(
      new PropertyDescriptorDto(mPropertyDescriptor)
    );
    dto.setKeyExistsCheck(mKeyExistsCheck);
    dto.setTypeCheck(mTypeCheck);
    dto.setEnumValueCheck(mEnumValueCheck);
    dto.setDeviantPropertyTypeName(mDeviantPropertyTypeName);
    return dto;
  }

  // endregion

  // region setters

  public PropertyReport setKeyCheck(boolean value) {
    super.closedCheck();
    mKeyExistsCheck = value;
    resetHash();
    return this;
  }

  public PropertyReport setTypeCheck(boolean value) {
    super.closedCheck();
    mTypeCheck = value;
    resetHash();
    return this;
  }

  public PropertyReport setDeviantType(String deviantTypeName) {
    super.closedCheck();
    mDeviantPropertyTypeName = deviantTypeName;
    resetHash();
    return this;
  }

  public PropertyReport setEnumCheck(boolean value) {
    super.closedCheck();
    mEnumValueCheck = value;
    resetHash();
    return this;
  }

  @Override
  public PropertyReport closeReport() {
    return (PropertyReport) super.closeReport();
  }

  // endregion

  //region hash, equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      mPropertyDescriptor,
      mKeyExistsCheck,
      mTypeCheck,
      mEnumValueCheck,
      mDeviantPropertyTypeName
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof PropertyReport)) {
      return false;
    }
    PropertyReport cast = PropertyReport.class.cast(obj);
    if (
      !EqualityUtils.itemEquals(
        this.mPropertyDescriptor,
        cast.mPropertyDescriptor
      )
    ) {
      return false;
    }
    if (!EqualityUtils.itemEquals(this.mKeyExistsCheck, cast.mKeyExistsCheck)) {
      return false;
    }
    if (!EqualityUtils.itemEquals(this.mTypeCheck, cast.mTypeCheck)) {
      return false;
    }
    if (!EqualityUtils.itemEquals(this.mEnumValueCheck, cast.mEnumValueCheck)) {
      return false;
    }
    return EqualityUtils.itemEquals(
      this.mDeviantPropertyTypeName,
      cast.mDeviantPropertyTypeName
    );
  }
  //endregion
}
