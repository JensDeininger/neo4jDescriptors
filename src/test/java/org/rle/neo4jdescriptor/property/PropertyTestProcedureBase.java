package org.rle.neo4jdescriptor.property;

import java.util.List;
import java.util.stream.Stream;
import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.rle.neo4jdescriptor.property.PropertyTestProcedureBase;
import org.rle.neo4jdescriptor.report.PropertyReport;
import org.rle.neo4jdescriptor.testutils.BoolMessageWrapper;

public abstract class PropertyTestProcedureBase<TCode, TDb> {

  @Context
  public Transaction tx;

  public static final Label DummyLabel = Label.label("DummyLabel");

  public static final RelationshipType DummyRelType = RelationshipType.withName(
    "DummyRelType"
  );

  public static final String PropertyKey = "propKey";

  public class TestValueInfo {

    public final TCode codeValue;
    public final TDb dbValue;
    public final String cypherString;

    public TestValueInfo(TCode codeValue, TDb dbValue, String cypherString) {
      this.codeValue = codeValue;
      this.dbValue = dbValue;
      this.cypherString = cypherString;
    }
  }

  protected abstract List<TestValueInfo> testValueTriples();

  protected abstract Object[] wrongTypeValues();

  private String setValueCypher(Entity entity, String valueCypher) {
    String cypher;
    if (entity instanceof Node) {
      cypher =
        String.format(
          "MATCH (n:%s) SET n.%s = %s",
          DummyLabel.name(),
          PropertyKey,
          valueCypher
        );
    } else if (entity instanceof Relationship) {
      cypher =
        String.format(
          "MATCH ()-[r:%s]->() SET r.%s = %s",
          DummyRelType.name(),
          PropertyKey,
          valueCypher
        );
    } else {
      throw new IllegalStateException();
    }
    return cypher;
  }

  public abstract PropertyDescriptor prop();

  /**
   * Uses the PropertyDescriptor to retrieve the value on the specified Entity
   */
  protected abstract TCode getValueOn(Entity entity);

  /**
   * Uses the PropertyDescriptor to set the value on the specified Entity
   */
  protected abstract void setValueOn(Entity entity, TCode value);

  protected abstract boolean compareCodeValues(TCode o1, TCode o2);

  protected abstract boolean compareCode2GeneralDbValue(TCode o1, Object o2);

  public static class SimpleTestProcedureName {

    public static class AppliesTo {

      public static final String SetViaCypher_Node =
        "appliesTo_SetViaCypher_Node";
      public static final String SetViaCypher_Relationship =
        "appliesTo_SetViaCypher_Relationship";

      public static final String SetViaApi_Node = "appliesTo_SetViaApi_Node";
      public static final String SetViaApi_Relationship =
        "appliesTo_SetViaApi_Relationship";

      public static final String SetViaCode_Node = "appliesTo_SetViaCode_Node";
      public static final String SetViaCode_Relationship =
        "appliesTo_SetViaCode_Relationship";
    }

    public static class Validate {

      public static final String SetViaCypher_Node =
        "validate_SetViaCypher_Node";
      public static final String SetViaCypher_Relationship =
        "validate_SetViaCypher_Relationship";

      public static final String SetViaApi_Node = "validate_SetViaApi_Node";
      public static final String SetViaApi_Relationship =
        "validate_SetViaApi_Relationship";

      public static final String SetViaCode_Node = "validate_SetViaCode_Node";
      public static final String SetViaCode_Relationship =
        "validate_SetViaCode_Relationship";
    }

    public static class NegativeAppliesTo {

      public static final String PropMissing_Node =
        "negativeAppliesTo_PropMissing_Node";
      public static final String PropMissing_Relationship =
        "negativeAppliesTo_PropMissing_Relationship";

      public static final String WrongType_Node =
        "negativeAppliesTo_WrongType_Node";
      public static final String WrongType_Relationship =
        "negativeAppliesTo_WrongType_Relationship";
    }

    public static class NegativeValidation {

      public static final String PropMissing_Node =
        "negativeValidation_PropMissing_Node";
      public static final String PropMissing_Relationship =
        "negativeValidation_PropMissing_Relationship";

      public static final String WrongType_Node =
        "negativeValidation_WrongType_Node";
      public static final String WrongType_Relationship =
        "negativeValidation_WrongType_Relationship";
    }

    public static class GetValue {

      public static final String SetViaCypher_Node =
        "getValue_SetViaCypher_Node";
      public static final String SetViaCypher_Relationship =
        "getValue_SetViaCypher_Relationship";

      public static final String SetViaApi_Node = "getValue_SetViaApi_Node";
      public static final String SetViaApi_Relationship =
        "getValue_SetViaApi_Relationship";

      public static final String SetViaCode_Node = "getValue_SetViaCode_Node";
      public static final String SetViaCode_Relationship =
        "getValue_SetViaCode_Relationship";
    }

    public static class SetValue {

      public static final String Node = "setValue_Node";
      public static final String Relationship = "setValue_Relationship";
    }
  }

  // region appliesTo

  private Stream<BoolMessageWrapper> appliesTo_SetViaCypher(Entity entity) {
    List<TestValueInfo> testValues = testValueTriples();
    for (int i = 0; i < testValues.size(); i++) {
      String cypherValue = testValues.get(i).cypherString;
      String cypher2Execute = setValueCypher(entity, cypherValue);
      tx.execute(cypher2Execute);
      if (!prop().appliesTo(entity)) {
        String msg = String.format(
          "Failed appliesTo_SetViaCypher_%s test @index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> appliesTo_SetViaCypher_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return appliesTo_SetViaCypher(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> appliesTo_SetViaCypher_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return appliesTo_SetViaCypher(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  //---------------------

  private Stream<BoolMessageWrapper> appliesTo_SetViaApi(Entity entity) {
    List<TestValueInfo> testValues = testValueTriples();
    for (int i = 0; i < testValues.size(); i++) {
      TDb dbVal = testValues.get(i).dbValue;
      entity.setProperty(PropertyKey, dbVal);
      if (!prop().appliesTo(entity)) {
        String msg = String.format(
          "Failed appliesTo_SetViaApi%s test @index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> appliesTo_SetViaApi_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return appliesTo_SetViaApi(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> appliesTo_SetViaApi_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return appliesTo_SetViaApi(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  //---------------------

  private Stream<BoolMessageWrapper> appliesTo_SetViaCode(Entity entity) {
    List<TestValueInfo> testValues = testValueTriples();
    for (int i = 0; i < testValues.size(); i++) {
      TCode codeVal = testValues.get(i).codeValue;
      setValueOn(entity, codeVal);
      if (!prop().appliesTo(entity)) {
        String msg = String.format(
          "Failed appliesTo_SetViaCode%s test @index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> appliesTo_SetViaCode_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return appliesTo_SetViaCode(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> appliesTo_SetViaCode_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return appliesTo_SetViaCode(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  // endregion

  // region validate

  private Stream<BoolMessageWrapper> validate_SetViaCypher(Entity entity) {
    List<TestValueInfo> testValues = testValueTriples();
    for (int i = 0; i < testValues.size(); i++) {
      String cypherValue = testValues.get(i).cypherString;
      String cypher2Execute = setValueCypher(entity, cypherValue);
      tx.execute(cypher2Execute);

      PropertyReport report = prop().validate(entity);
      if (!report.isClosed() || report.errorCount() != 0) {
        String msg = String.format(
          "Failed validate_SetViaCypher_%s test @index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> validate_SetViaCypher_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return validate_SetViaCypher(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> validate_SetViaCypher_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return validate_SetViaCypher(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  //-------------------------

  private Stream<BoolMessageWrapper> validate_SetViaApi(Entity entity) {
    List<TestValueInfo> testValues = testValueTriples();
    for (int i = 0; i < testValues.size(); i++) {
      TDb dbVal = testValues.get(i).dbValue;
      entity.setProperty(PropertyKey, dbVal);
      PropertyReport report = prop().validate(entity);
      if (!report.isClosed() || report.errorCount() != 0) {
        String msg = String.format(
          "Failed validate_SetViaApi_%s test @index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> validate_SetViaApi_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return validate_SetViaApi(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> validate_SetViaApi_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return validate_SetViaApi(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  //-------------------------

  private Stream<BoolMessageWrapper> validate_SetViaCode(Entity entity) {
    List<TestValueInfo> testValues = testValueTriples();
    for (int i = 0; i < testValues.size(); i++) {
      TCode codeVal = testValues.get(i).codeValue;
      setValueOn(entity, codeVal);
      PropertyReport report = prop().validate(entity);
      if (!report.isClosed() || report.errorCount() != 0) {
        String msg = String.format(
          "Failed validate_SetViaApi_%s test @index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> validate_SetViaCode_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return validate_SetViaCode(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> validate_SetViaCode_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return validate_SetViaCode(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  // endregion

  // region negative appliesTo

  private Stream<BoolMessageWrapper> negativeAppliesTo_PropMissing(
    Entity entity
  ) {
    entity.removeProperty(PropertyKey);
    if (prop().appliesTo(entity)) {
      String msg = String.format(
        "Failed negativeAppliesTo_PropMissing_%s test",
        entity.getClass().getSimpleName()
      );
      return Stream.of(new BoolMessageWrapper(false, msg));
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> negativeAppliesTo_PropMissing_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return negativeAppliesTo_PropMissing(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> negativeAppliesTo_PropMissing_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return negativeAppliesTo_PropMissing(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  //--------------------------

  private Stream<BoolMessageWrapper> negativeAppliesTo_WrongType(
    Entity entity
  ) {
    Object[] wrongTypeValues = wrongTypeValues();
    for (int i = 0; i < wrongTypeValues.length; i++) {
      entity.setProperty(PropertyKey, wrongTypeValues[i]);
      if (prop().appliesTo(entity)) {
        String msg = String.format(
          "Failed negativeAppliesTo_WrongType_%s test @index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> negativeAppliesTo_WrongType_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return negativeAppliesTo_WrongType(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> negativeAppliesTo_WrongType_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return negativeAppliesTo_WrongType(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  // endregion

  // region negative validation

  private Stream<BoolMessageWrapper> negativeValidation_PropMissing(
    Entity entity
  ) {
    entity.removeProperty(PropertyKey);
    PropertyReport report = prop().validate(entity);
    if (
      !report.isClosed() ||
      report.errorCount() != 1 ||
      report.checkResultKeyExists() != false ||
      report.checkResultType() != null
    ) {
      String msg = String.format(
        "Failed negativeValidation_PropMissing_%s test",
        entity.getClass().getSimpleName()
      );
      return Stream.of(new BoolMessageWrapper(false, msg));
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> negativeValidation_PropMissing_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return negativeValidation_PropMissing(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> negativeValidation_PropMissing_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return negativeValidation_PropMissing(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  //------------------

  private Stream<BoolMessageWrapper> negativeValidation_WrongType(
    Entity entity
  ) {
    Object[] wrongTypeValues = wrongTypeValues();
    for (int i = 0; i < wrongTypeValues.length; i++) {
      entity.setProperty(PropertyKey, wrongTypeValues[i]);
      PropertyReport report = prop().validate(entity);
      if (
        !report.isClosed() ||
        report.errorCount() != 1 ||
        report.checkResultKeyExists() != true ||
        report.checkResultType() != false ||
        report.deviantTypeName() == null ||
        report.deviantTypeName().isEmpty() ||
        report.deviantTypeName().isBlank()
      ) {
        String msg = String.format(
          "Failed negativeValidation_WrongType_%s test",
          entity.getClass().getSimpleName()
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> negativeValidation_WrongType_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return negativeValidation_WrongType(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> negativeValidation_WrongType_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return negativeValidation_WrongType(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  // endregion

  // region getValue

  private Stream<BoolMessageWrapper> getValue_SetViaCypher(Entity entity) {
    List<TestValueInfo> testValues = testValueTriples();
    for (int i = 0; i < testValues.size(); i++) {
      TestValueInfo testVal = testValues.get(i);
      String cpher2Execute = setValueCypher(entity, testVal.cypherString);
      tx.execute(cpher2Execute);
      TCode value = getValueOn(entity);
      if (!compareCodeValues(testVal.codeValue, value)) {
        String msg = String.format(
          "Failed getValue_SetViaCypher_%s test @ index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> getValue_SetViaCypher_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return getValue_SetViaCypher(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> getValue_SetViaCypher_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return getValue_SetViaCypher(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  //-------------------------

  private Stream<BoolMessageWrapper> getValue_SetViaApi(Entity entity) {
    List<TestValueInfo> testValues = testValueTriples();
    for (int i = 0; i < testValues.size(); i++) {
      TestValueInfo val = testValues.get(i);
      entity.setProperty(PropertyKey, val.dbValue);
      TCode dbValue = getValueOn(entity);
      if (!compareCodeValues(val.codeValue, dbValue)) {
        String msg = String.format(
          "Failed getValue_SetViaApi_%s test @ index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> getValue_SetViaApi_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return getValue_SetViaApi(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> getValue_SetViaApi_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return getValue_SetViaApi(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  //-------------------------

  private Stream<BoolMessageWrapper> getValue_SetViaCode(Entity entity) {
    List<TestValueInfo> testValues = testValueTriples();
    for (int i = 0; i < testValues.size(); i++) {
      TCode codeVal = testValues.get(i).codeValue;
      setValueOn(entity, codeVal);
      TCode value = getValueOn(entity);
      if (!compareCodeValues(codeVal, value)) {
        String msg = String.format(
          "Failed getSetValueRoundRobin_%s test @ index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> getValue_SetViaCode_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return getValue_SetViaCode(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> getValue_SetViaCode_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return getValue_SetViaCode(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  // endregion

  // region setValue

  private Stream<BoolMessageWrapper> setValue(Entity entity) {
    List<TestValueInfo> testValues = testValueTriples();
    for (int i = 0; i < testValues.size(); i++) {
      TCode codeVal = testValues.get(i).codeValue;
      setValueOn(entity, codeVal);
      Object dbValue = entity.getProperty(PropertyKey);
      if (!compareCode2GeneralDbValue(codeVal, dbValue)) {
        String msg = String.format(
          "Failed setValue_%s test @ index %d",
          entity.getClass().getSimpleName(),
          i
        );
        return Stream.of(new BoolMessageWrapper(false, msg));
      }
    }
    return Stream.of(new BoolMessageWrapper(true, "Yay"));
  }

  public Stream<BoolMessageWrapper> setValue_Node() {
    try {
      Node node = tx.findNodes(DummyLabel).next();
      return setValue(node);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }

  public Stream<BoolMessageWrapper> setValue_Relationship() {
    try {
      Relationship rel = tx.getAllRelationships().iterator().next();
      return setValue(rel);
    } catch (Exception exc) {
      return Stream.of(new BoolMessageWrapper(false, exc.toString()));
    }
  }
  // endregion
}
