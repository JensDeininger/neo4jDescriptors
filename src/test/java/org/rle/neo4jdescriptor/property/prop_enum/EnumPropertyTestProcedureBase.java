package org.rle.neo4jdescriptor.property.prop_enum;

import java.util.stream.Stream;
import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.rle.neo4jdescriptor.property.PropertyTestProcedureBase;
import org.rle.neo4jdescriptor.report.PropertyReport;
import org.rle.neo4jdescriptor.testutils.BoolMessageWrapper;

public abstract class EnumPropertyTestProcedureBase<TCode, TDb>
  extends PropertyTestProcedureBase<TCode, TDb> {

  protected abstract TDb[] correctTypeWrongEnumValues();

  public static class EnumSimpleTestProcedureName {

    public static class NegativeAppliesTo {

      public static final String CorrectTypeWrongEnumValue_Node =
        "negativeAppliesTo_correctTypeWrongEnumValue_Node";

      public static final String CorrectTypeWrongEnumValue_Relationship =
        "negativeAppliesTo_correctTypeWrongEnumValue_Relationship";
    }

    public static class NegativeValidation {

      public static final String CorrectTypeWrongEnumValue_Node =
        "negativeValidation_correctTypeWrongEnumValue_Node";

      public static final String CorrectTypeWrongEnumValue_Relationship =
        "negativeValidation_correctTypeWrongEnumValue_Relationship";
    }
  }

  private Stream<BoolMessageWrapper> negativeAppliesTo_correctTypeWrongEnumValue(
    Entity entity
  ) {
    TDb[] wrongValues = correctTypeWrongEnumValues();
    for (int i = 0; i < wrongValues.length; i++) {
      entity.setProperty(PropertyKey, wrongValues[i]);
      if (prop().appliesTo(entity)) {
        String msg = String.format(
          "Failed negativeAppliesTo_correctTypeWrongEnumValue_%s test @index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> negativeAppliesTo_correctTypeWrongEnumValue_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return negativeAppliesTo_correctTypeWrongEnumValue(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> negativeAppliesTo_correctTypeWrongEnumValue_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return negativeAppliesTo_correctTypeWrongEnumValue(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  //------------------------

  private Stream<BoolMessageWrapper> negativeValidation_correctTypeWrongEnumValue(
    Entity entity
  ) {
    Object[] wrongValues = correctTypeWrongEnumValues();
    for (int i = 0; i < wrongValues.length; i++) {
      entity.setProperty(PropertyKey, wrongValues[i]);
      PropertyReport report = prop().validate(entity);
      if (
        !report.isClosed() ||
        report.errorCount() != 1 ||
        report.checkResultKeyExists() != true ||
        report.checkResultType() != true ||
        report.deviantTypeName() != null ||
        report.checkResultEnumValue() != false
      ) {
        String msg = String.format(
          "Failed negativeValidation_correctTypeWrongEnumValue_%s test",
          entity.getClass().getSimpleName()
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> negativeValidation_correctTypeWrongEnumValue_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return negativeValidation_correctTypeWrongEnumValue(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> negativeValidation_correctTypeWrongEnumValue_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return negativeValidation_correctTypeWrongEnumValue(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }
}
