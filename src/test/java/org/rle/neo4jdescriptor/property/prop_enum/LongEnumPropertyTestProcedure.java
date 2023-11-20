package org.rle.neo4jdescriptor.property.prop_enum;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.neo4j.graphdb.Entity;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;
import org.rle.neo4jdescriptor.CypherUtils;
import org.rle.neo4jdescriptor.property.EnumProperty;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.prop_enum.dummyenums.DummyLongEnum;
import org.rle.neo4jdescriptor.testutils.BoolMessageWrapper;

public class LongEnumPropertyTestProcedure
  extends EnumPropertyTestProcedureBase<DummyLongEnum, Long> {

  private static final EnumProperty<Long, DummyLongEnum> prop = new EnumProperty<>(
    PropertyKey,
    Long.class,
    DummyLongEnum.class
  );

  private static final DummyLongEnum[] correctValues = new DummyLongEnum[] {
    DummyLongEnum.SMALL,
    DummyLongEnum.MEDIUM,
    DummyLongEnum.LARGE,
  };

  private static final Object[] wrongTypeValues = new Object[] {
    "foo",
    23.0,
    true,
    new Long[0],
    new Long[] { DummyLongEnum.SMALL.dbValue() },
  };

  private static final Long[] correctTypeWrongEnumValues = new Long[] {
    DummyLongEnum.SMALL.dbValue() + 100l,
    DummyLongEnum.MEDIUM.dbValue() + 100l,
    DummyLongEnum.LARGE.dbValue() + 100l,
  };

  // region base test overrides

  @Override
  protected List<TestValueInfo> testValueTriples() {
    return Arrays
      .stream(correctValues)
      .map(o ->
        new TestValueInfo(o, o.dbValue(), CypherUtils.long2Cypher(o.dbValue()))
      )
      .collect(Collectors.toList());
  }

  @Override
  protected Object[] wrongTypeValues() {
    return wrongTypeValues;
  }

  @Override
  protected Long[] correctTypeWrongEnumValues() {
    return correctTypeWrongEnumValues;
  }

  @Override
  public PropertyDescriptor prop() {
    return prop;
  }

  @Override
  protected DummyLongEnum getValueOn(Entity entity) {
    return prop.getValueOn(entity);
  }

  @Override
  protected void setValueOn(Entity entity, DummyLongEnum value) {
    prop.setValueOn(entity, value);
  }

  @Override
  protected boolean compareCodeValues(DummyLongEnum o1, DummyLongEnum o2) {
    return Objects.equals(o1, o2);
  }

  @Override
  protected boolean compareCode2GeneralDbValue(DummyLongEnum o1, Object o2) {
    return o1.isValueEqualToObject(o2);
  }

  // endregion

  // region appliesTo

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.AppliesTo.SetViaCypher_Node
  )
  public Stream<BoolMessageWrapper> appliesTo_SetViaCypher_Node() {
    return super.appliesTo_SetViaCypher_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.AppliesTo.SetViaCypher_Relationship
  )
  public Stream<BoolMessageWrapper> appliesTo_SetViaCypher_Relationship() {
    return super.appliesTo_SetViaCypher_Relationship();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.AppliesTo.SetViaApi_Node
  )
  public Stream<BoolMessageWrapper> appliesTo_SetViaApi_Node() {
    return super.appliesTo_SetViaApi_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.AppliesTo.SetViaApi_Relationship
  )
  public Stream<BoolMessageWrapper> appliesTo_SetViaApi_Relationship() {
    return super.appliesTo_SetViaApi_Relationship();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.AppliesTo.SetViaCode_Node
  )
  public Stream<BoolMessageWrapper> appliesTo_SetViaCode_Node() {
    return super.appliesTo_SetViaCode_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.AppliesTo.SetViaCode_Relationship
  )
  public Stream<BoolMessageWrapper> appliesTo_SetViaCode_Relationship() {
    return super.appliesTo_SetViaCode_Relationship();
  }

  // endregion

  // region validate

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.Validate.SetViaCypher_Node
  )
  public Stream<BoolMessageWrapper> validate_SetViaCypher_Node() {
    return super.validate_SetViaCypher_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.Validate.SetViaCypher_Relationship
  )
  public Stream<BoolMessageWrapper> validate_SetViaCypher_Relationship() {
    return super.validate_SetViaCypher_Relationship();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.Validate.SetViaApi_Node
  )
  public Stream<BoolMessageWrapper> validate_SetViaApi_Node() {
    return super.validate_SetViaApi_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.Validate.SetViaApi_Relationship
  )
  public Stream<BoolMessageWrapper> validate_SetViaApi_Relationship() {
    return super.validate_SetViaApi_Relationship();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.Validate.SetViaCode_Node
  )
  public Stream<BoolMessageWrapper> validate_SetViaCode_Node() {
    return super.validate_SetViaCode_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.Validate.SetViaCode_Relationship
  )
  public Stream<BoolMessageWrapper> validate_SetViaCode_Relationship() {
    return super.validate_SetViaCode_Relationship();
  }

  // endregion

  // region Negative AppliesTo

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.NegativeAppliesTo.PropMissing_Node
  )
  public Stream<BoolMessageWrapper> negativeAppliesTo_PropMissing_Node() {
    return super.negativeAppliesTo_PropMissing_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.NegativeAppliesTo.PropMissing_Relationship
  )
  public Stream<BoolMessageWrapper> negativeAppliesTo_PropMissing_Relationship() {
    return super.negativeAppliesTo_PropMissing_Relationship();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.NegativeAppliesTo.WrongType_Node
  )
  public Stream<BoolMessageWrapper> negativeAppliesTo_WrongType_Node() {
    return super.negativeAppliesTo_WrongType_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.NegativeAppliesTo.WrongType_Relationship
  )
  public Stream<BoolMessageWrapper> negativeAppliesTo_WrongType_Relationship() {
    return super.negativeAppliesTo_WrongType_Relationship();
  }

  // endregion

  // region negative validation

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.NegativeValidation.PropMissing_Node
  )
  public Stream<BoolMessageWrapper> negativeValidation_PropMissing_Node() {
    return super.negativeValidation_PropMissing_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.NegativeValidation.PropMissing_Relationship
  )
  public Stream<BoolMessageWrapper> negativeValidation_PropMissing_Relationship() {
    return super.negativeValidation_PropMissing_Relationship();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.NegativeValidation.WrongType_Node
  )
  public Stream<BoolMessageWrapper> negativeValidation_WrongType_Node() {
    return super.negativeValidation_WrongType_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.NegativeValidation.WrongType_Relationship
  )
  public Stream<BoolMessageWrapper> negativeValidation_WrongType_Relationship() {
    return super.negativeValidation_WrongType_Relationship();
  }

  // endregion

  // region Get Value

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.GetValue.SetViaCypher_Node
  )
  public Stream<BoolMessageWrapper> getValue_SetViaCypher_Node() {
    return super.getValue_SetViaCypher_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.GetValue.SetViaCypher_Relationship
  )
  public Stream<BoolMessageWrapper> getValue_SetViaCypher_Relationship() {
    return super.getValue_SetViaCypher_Relationship();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.GetValue.SetViaApi_Node
  )
  public Stream<BoolMessageWrapper> getValue_SetViaApi_Node() {
    return super.getValue_SetViaApi_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.GetValue.SetViaApi_Relationship
  )
  public Stream<BoolMessageWrapper> getValue_SetViaApi_Relationship() {
    return super.getValue_SetViaApi_Relationship();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.GetValue.SetViaCode_Node
  )
  public Stream<BoolMessageWrapper> getValue_SetViaCode_Node() {
    return super.getValue_SetViaCode_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.GetValue.SetViaCode_Relationship
  )
  public Stream<BoolMessageWrapper> getValue_SetViaCode_Relationship() {
    return super.getValue_SetViaCode_Relationship();
  }

  // endregion

  // region Set Value

  @Override
  @Procedure(mode = Mode.WRITE, name = SimpleTestProcedureName.SetValue.Node)
  public Stream<BoolMessageWrapper> setValue_Node() {
    return super.setValue_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.SetValue.Relationship
  )
  public Stream<BoolMessageWrapper> setValue_Relationship() {
    return super.setValue_Relationship();
  }

  // endregion

  // region enum specific procedures

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = EnumSimpleTestProcedureName.NegativeAppliesTo.CorrectTypeWrongEnumValue_Node
  )
  public Stream<BoolMessageWrapper> negativeAppliesTo_correctTypeWrongEnumValue_Node() {
    return super.negativeAppliesTo_correctTypeWrongEnumValue_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = EnumSimpleTestProcedureName.NegativeAppliesTo.CorrectTypeWrongEnumValue_Relationship
  )
  public Stream<BoolMessageWrapper> negativeAppliesTo_correctTypeWrongEnumValue_Relationship() {
    return super.negativeAppliesTo_correctTypeWrongEnumValue_Relationship();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = EnumSimpleTestProcedureName.NegativeValidation.CorrectTypeWrongEnumValue_Node
  )
  public Stream<BoolMessageWrapper> negativeValidation_correctTypeWrongEnumValue_Node() {
    return super.negativeValidation_correctTypeWrongEnumValue_Node();
  }

  @Override
  @Procedure(
    mode = Mode.WRITE,
    name = EnumSimpleTestProcedureName.NegativeValidation.CorrectTypeWrongEnumValue_Relationship
  )
  public Stream<BoolMessageWrapper> negativeValidation_correctTypeWrongEnumValue_Relationship() {
    return super.negativeValidation_correctTypeWrongEnumValue_Relationship();
  }
  // endregion
}
