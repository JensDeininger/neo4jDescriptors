package org.rle.neo4jdescriptor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.rle.neo4jdescriptor.dto.report.FullReportDto;
import org.rle.neo4jdescriptor.property.*;
import org.rle.neo4jdescriptor.property.array_basic.*;
import org.rle.neo4jdescriptor.property.prop_basic.*;
import org.rle.neo4jdescriptor.report.FullReport;
import org.rle.neo4jdescriptor.samples.*;
import org.rle.neo4jdescriptor.testutils.TestBase;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValidationTest extends TestBase {

  private static final String MISSING_PROPERTY_CLAUSE =
    "forgot to add clause for the PropertyDescriptor";

  private static final String DELETE_ALL_CYPHER = "MATCH (n) DETACH DELETE n";

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(ValidationProcedures.class);
  }

  @Override
  protected String initialCypher() {
    return null;
  }

  private void setupSampleDatabase(Session session) {
    session.run(DELETE_ALL_CYPHER);
    session.run(SampleCreationUtils.createFullSampleCypher());
  }

  @Test
  public void validationTest_yay_returnPrint() {
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      String cypher = String.format(
        "CALL %s() YIELD %s RETURN %s",
        ValidationProcedures.ProcedureName.PrintValidation,
        ValidationPrintoutWrapper.CONTENT_NAME,
        ValidationPrintoutWrapper.CONTENT_NAME
      );
      Record record = session.run(cypher).next();
      String msg = record
        .get(ValidationPrintoutWrapper.CONTENT_NAME)
        .asString();
      assertEquals(FullReport.SM_NO_PROBLEMS, msg);
    }
  }

  private FullReport callFullValidation(Session session) {
    String cypher = String.format(
      "CALL %s() YIELD %s, %s RETURN %s, %s",
      ValidationProcedures.ProcedureName.GetValidationReport,
      ValidationReportWrapper.JSON_DTO_COMPONENT_NAME,
      ValidationReportWrapper.JSON_EXC_COMPONENT_NAME,
      ValidationReportWrapper.JSON_DTO_COMPONENT_NAME,
      ValidationReportWrapper.JSON_EXC_COMPONENT_NAME
    );
    Record record = session.run(cypher).next();
    String jsonDto = record
      .get(ValidationReportWrapper.JSON_DTO_COMPONENT_NAME)
      .asString();
    String jsonExc = record
      .get(ValidationReportWrapper.JSON_EXC_COMPONENT_NAME)
      .asString();
    assertEquals("null", jsonExc, "fail 1");
    ObjectMapper objectMapper = new ObjectMapper();
    FullReportDto revDto = assertDoesNotThrow(() ->
      objectMapper.readValue(jsonDto, FullReportDto.class)
    );
    FullReport fullReport = new FullReport(
      ValidationProcedures.smNodeRepo,
      ValidationProcedures.smRelationshipRepo,
      revDto
    );
    return fullReport;
  }

  @Test
  public void validationTest_yay_returnReport() {
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      FullReport fullReport = callFullValidation(session);
      assertEquals(0, fullReport.errorCount(), "fail 2");
    }
  }

  // region screw with properties

  @Test
  public void propertyTest_keyExistence_node() {
    String nodeAlias = "n";
    String[] allPropBits = SampleNodeRep.AllProps
      .properties(Modality.CONTINGENT)
      .map(o -> String.format("%s.%s", nodeAlias, o.key()))
      .toArray(String[]::new);
    String killPropsCypher = String.format(
      "MATCH (%s:%s) REMOVE %s",
      nodeAlias,
      SampleNodeRep.AllProps.allPropsLabel.name(),
      String.join(", ", allPropBits)
    );
    int propCount = allPropBits.length;
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(killPropsCypher);
      FullReport fullReport = callFullValidation(session);

      long totalErrorCount = fullReport.errorCount();
      assertEquals(propCount, totalErrorCount, "fail totalErrorCount");

      long faultyNodeDescriptorCount = fullReport
        .nodeDescriptorReports()
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        1,
        faultyNodeDescriptorCount,
        "fail: more than one faulty NodeDescriptor"
      );

      long faultyNodeCount = fullReport
        .nodeDescriptorReports()
        .flatMap(o -> o.nodeReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(1, faultyNodeCount, "fail: more than one faulty Node");

      long faultyPropReports = fullReport
        .nodeDescriptorReports()
        .flatMap(o -> o.nodeReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        totalErrorCount,
        faultyPropReports,
        "fail: faultyPropReports"
      );

      long keyMissingErrors = fullReport
        .nodeDescriptorReports()
        .flatMap(o -> o.nodeReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> Boolean.FALSE.equals(o.checkResultKeyExists()))
        .count();
      assertEquals(totalErrorCount, keyMissingErrors, "fail: keyMissingErrors");
    }
  }

  @Test
  public void propertyTest_keyExistence_relation() {
    String relAlias = "r";
    String[] allPropBits = SampleRelationshipRep.AllPropsRel
      .properties(Modality.CONTINGENT)
      .map(o -> String.format("%s.%s", relAlias, o.key()))
      .toArray(String[]::new);
    String killPropsCypher = String.format(
      "MATCH ()-[%s:%s]-() REMOVE %s",
      relAlias,
      SampleRelationshipRep.AllPropsRel.type.name(),
      String.join(", ", allPropBits)
    );
    int propCount = allPropBits.length;
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(killPropsCypher);
      FullReport fullReport = callFullValidation(session);

      long totalErrorCount = fullReport.errorCount();
      assertEquals(propCount, totalErrorCount, "fail totalErrorCount");

      long faultyRelDescriptorCount = fullReport
        .relationshipDescriptorReports()
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        1,
        faultyRelDescriptorCount,
        "fail: more than one faulty RelationshipDescriptor"
      );

      long faultyRelCount = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(1, faultyRelCount, "fail: more than one faulty relation");

      long faultyPropReports = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        totalErrorCount,
        faultyPropReports,
        "fail: faultyPropReports"
      );

      long keyMissingErrors = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> Boolean.FALSE.equals(o.checkResultKeyExists()))
        .count();
      assertEquals(totalErrorCount, keyMissingErrors, "fail: keyMissingErrors");
    }
  }

  private String fuckWithPropertyTypeCypher(
    ArrayProperty<?> arrayPropertyDescriptor
  ) {
    if (arrayPropertyDescriptor instanceof BooleanArrayProperty) {
      return CypherUtils.bool2Cypher(true);
    }
    if (arrayPropertyDescriptor instanceof DoubleArrayProperty) {
      return CypherUtils.double2Cypher(23.0);
    }
    if (arrayPropertyDescriptor instanceof LongArrayProperty) {
      return CypherUtils.long2Cypher(23l);
    }
    if (arrayPropertyDescriptor instanceof StringArrayProperty) {
      return CypherUtils.string2Cypher("moo");
    } else {
      throw new IllegalArgumentException(MISSING_PROPERTY_CLAUSE);
    }
  }

  private String fuckWithPropertyTypeCypher(
    BasicProperty<?> basicPropertyDescriptor
  ) {
    if (basicPropertyDescriptor instanceof BooleanProperty) {
      return CypherUtils.string2Cypher("bullerbue");
    }
    if (basicPropertyDescriptor instanceof DoubleProperty) {
      return CypherUtils.bool2Cypher(true);
    }
    if (basicPropertyDescriptor instanceof LongProperty) {
      return CypherUtils.double2Cypher(3.14);
    }
    if (basicPropertyDescriptor instanceof NumberProperty) {
      return CypherUtils.string2Cypher("bullerbue");
    }
    if (basicPropertyDescriptor instanceof ObjectProperty) {
      return CypherUtils.bool2Cypher(true);
    }
    if (basicPropertyDescriptor instanceof StringProperty) {
      return CypherUtils.long2Cypher(3l);
    } else {
      throw new IllegalArgumentException(MISSING_PROPERTY_CLAUSE);
    }
  }

  private String fuckWithPropertyTypeCypher(
    EnumProperty<?, ?> enumPropertyDescriptor
  ) {
    return CypherUtils.bool2Cypher(true);
  }

  private String fuckWithPropertyTypeCypher(PropertyDescriptor propDesc) {
    String valueCypher;
    if (propDesc instanceof ArrayProperty<?>) {
      valueCypher =
        fuckWithPropertyTypeCypher(ArrayProperty.class.cast(propDesc));
    } else if (propDesc instanceof BasicProperty<?>) {
      valueCypher =
        fuckWithPropertyTypeCypher(BasicProperty.class.cast(propDesc));
    } else if (propDesc instanceof EnumProperty<?, ?>) {
      valueCypher =
        fuckWithPropertyTypeCypher(EnumProperty.class.cast(propDesc));
    } else {
      throw new IllegalArgumentException(MISSING_PROPERTY_CLAUSE);
    }
    return valueCypher;
    //return String.format("%s.%s=%s", entityAlias, propDesc.key(), valueCypher);
  }

  @Test
  public void propertyTest_wrongType_node() {
    String nodeAlias = "n";
    String[] allPropBits = SampleNodeRep.AllProps
      .properties(Modality.CONTINGENT)
      .map(o ->
        String.format(
          "%s.%s=%s",
          nodeAlias,
          o.key(),
          fuckWithPropertyTypeCypher(o)
        )
      )
      .toArray(String[]::new);

    String fuckWithPropsCypher = String.format(
      "MATCH (%s:%s) SET %s",
      nodeAlias,
      SampleNodeRep.AllProps.allPropsLabel.name(),
      String.join(", ", allPropBits)
    );
    int expErrorCount = allPropBits.length - 1; // fucking with the ObjectProperty does nuffin
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(fuckWithPropsCypher);
      FullReport fullReport = callFullValidation(session);

      long totalErrorCount = fullReport.errorCount();
      assertEquals(expErrorCount, totalErrorCount, "fail totalErrorCount");

      long faultyNodeDescriptorCount = fullReport
        .nodeDescriptorReports()
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        1,
        faultyNodeDescriptorCount,
        "fail: more than one faulty NodeDescriptor"
      );

      long faultyNodeCount = fullReport
        .nodeDescriptorReports()
        .flatMap(o -> o.nodeReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(1, faultyNodeCount, "fail: more than one faulty Node");

      long faultyPropReports = fullReport
        .nodeDescriptorReports()
        .flatMap(o -> o.nodeReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        totalErrorCount,
        faultyPropReports,
        "fail: faultyPropReports"
      );

      long wrongTypeErrors = fullReport
        .nodeDescriptorReports()
        .flatMap(o -> o.nodeReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> Boolean.FALSE.equals(o.checkResultType()))
        .count();
      assertEquals(totalErrorCount, wrongTypeErrors, "fail: type check");
    }
  }

  @Test
  public void propertyTest_wrongType_relation() {
    String relAlias = "r";
    String[] allPropBits = SampleRelationshipRep.AllPropsRel
      .properties(Modality.CONTINGENT)
      .map(o ->
        String.format(
          "%s.%s=%s",
          relAlias,
          o.key(),
          fuckWithPropertyTypeCypher(o)
        )
      )
      .toArray(String[]::new);

    String fuckWithPropsCypher = String.format(
      "MATCH ()-[%s:%s]-() SET %s",
      relAlias,
      SampleRelationshipRep.AllPropsRel.type.name(),
      String.join(", ", allPropBits)
    );
    int expErrorCount = allPropBits.length - 1; // fucking with the ObjectProperty does nuffin
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(fuckWithPropsCypher);
      FullReport fullReport = callFullValidation(session);

      long totalErrorCount = fullReport.errorCount();
      assertEquals(expErrorCount, totalErrorCount, "fail totalErrorCount");

      long faultyRelDescriptorCount = fullReport
        .relationshipDescriptorReports()
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        1,
        faultyRelDescriptorCount,
        "fail: more than one faulty RelationshipDescriptor"
      );

      long faultyNodeCount = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(1, faultyNodeCount, "fail: more than one faulty relation");

      long faultyPropReports = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        totalErrorCount,
        faultyPropReports,
        "fail: faultyPropReports"
      );

      long wrongTypeErrors = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> Boolean.FALSE.equals(o.checkResultType()))
        .count();
      assertEquals(totalErrorCount, wrongTypeErrors, "fail: type check");
    }
  }

  @Test
  public void propertyTest_wrongEnumValue_node() {
    String nodeAlias = "n";
    AllPropsNodeDesc nd = SampleNodeRep.AllProps;
    String doubleFuck = String.format(
      "%s.%s=%s",
      nodeAlias,
      nd.doubleEnumProp.key(),
      CypherUtils.double2Cypher(1234567.89)
    );
    String longFuck = String.format(
      "%s.%s=%s",
      nodeAlias,
      nd.longEnumProp.key(),
      CypherUtils.long2Cypher(1234567l)
    );
    String stringFuck = String.format(
      "%s.%s=%s",
      nodeAlias,
      nd.stringEnumProp.key(),
      CypherUtils.string2Cypher("whatever man")
    );
    String[] allPropBits = new String[] { doubleFuck, longFuck, stringFuck };
    String fuckWithPropsCypher = String.format(
      "MATCH (%s:%s) SET %s",
      nodeAlias,
      SampleNodeRep.AllProps.allPropsLabel.name(),
      String.join(", ", allPropBits)
    );
    int expErrorCount = 3;
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(fuckWithPropsCypher);
      FullReport fullReport = callFullValidation(session);

      long totalErrorCount = fullReport.errorCount();
      assertEquals(expErrorCount, totalErrorCount, "fail totalErrorCount");

      long faultyNodeDescriptorCount = fullReport
        .nodeDescriptorReports()
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        1,
        faultyNodeDescriptorCount,
        "fail: more than one faulty NodeDescriptor"
      );

      long faultyNodeCount = fullReport
        .nodeDescriptorReports()
        .flatMap(o -> o.nodeReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(1, faultyNodeCount, "fail: more than one faulty Node");

      long faultyPropReports = fullReport
        .nodeDescriptorReports()
        .flatMap(o -> o.nodeReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        totalErrorCount,
        faultyPropReports,
        "fail: faultyPropReports"
      );

      long enumValueErrors = fullReport
        .nodeDescriptorReports()
        .flatMap(o -> o.nodeReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> o.errorCount() > 0)
        .filter(o -> Boolean.FALSE.equals(o.checkResultEnumValue()))
        .count();
      assertEquals(totalErrorCount, enumValueErrors, "fail: enum check");
    }
  }

  @Test
  public void propertyTest_wrongEnumValue_relation() {
    String relAlias = "n";
    AllPropsRelationDesc rd = SampleRelationshipRep.AllPropsRel;
    String doubleFuck = String.format(
      "%s.%s=%s",
      relAlias,
      rd.doubleEnumProp.key(),
      CypherUtils.double2Cypher(1234567.89)
    );
    String longFuck = String.format(
      "%s.%s=%s",
      relAlias,
      rd.longEnumProp.key(),
      CypherUtils.long2Cypher(1234567l)
    );
    String stringFuck = String.format(
      "%s.%s=%s",
      relAlias,
      rd.stringEnumProp.key(),
      CypherUtils.string2Cypher("whatever man")
    );
    String[] allPropBits = new String[] { doubleFuck, longFuck, stringFuck };
    String fuckWithPropsCypher = String.format(
      "MATCH ()-[%s:%s]-() SET %s",
      relAlias,
      SampleRelationshipRep.AllPropsRel.type.name(),
      String.join(", ", allPropBits)
    );
    int expErrorCount = 3;
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(fuckWithPropsCypher);
      FullReport fullReport = callFullValidation(session);

      long totalErrorCount = fullReport.errorCount();
      assertEquals(expErrorCount, totalErrorCount, "fail totalErrorCount");

      long faultyRelDescriptorCount = fullReport
        .relationshipDescriptorReports()
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        1,
        faultyRelDescriptorCount,
        "fail: more than one faulty NodeDescriptor"
      );

      long faultyRelCount = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(1, faultyRelCount, "fail: more than one faulty Node");

      long faultyPropReports = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        totalErrorCount,
        faultyPropReports,
        "fail: faultyPropReports"
      );

      long enumValueErrors = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .flatMap(o -> o.propertyReports())
        .filter(o -> o.errorCount() > 0)
        .filter(o -> Boolean.FALSE.equals(o.checkResultEnumValue()))
        .count();
      assertEquals(totalErrorCount, enumValueErrors, "fail: enum check");
    }
  }

  // endregion

  // region screw with NodeRelations

  private void checkNodeRelationErrorCounts(
    FullReport fullReport,
    int expTotalErrorCount,
    int expNodeDescCount,
    int expNodeCount,
    int expNodeRelCount,
    int expCardErrorCount
  ) {
    long totalErrorCount = fullReport.errorCount();
    assertEquals(expTotalErrorCount, totalErrorCount, "fail totalErrorCount");

    long faultyNodeDescriptorCount = fullReport
      .nodeDescriptorReports()
      .filter(o -> o.errorCount() > 0)
      .count();
    assertEquals(
      expNodeDescCount,
      faultyNodeDescriptorCount,
      "fail: faulty NodeDescriptors"
    );

    long faultyNodeCount = fullReport
      .nodeDescriptorReports()
      .flatMap(o -> o.nodeReports())
      .filter(o -> o.errorCount() > 0)
      .count();
    assertEquals(expNodeCount, faultyNodeCount, "fail: faulty Nodes");

    long faultyNodeRelReports = fullReport
      .nodeDescriptorReports()
      .flatMap(o -> o.nodeReports())
      .flatMap(o -> o.nodeRelationReports())
      .filter(o -> o.errorCount() > 0)
      .count();
    assertEquals(
      expNodeRelCount,
      faultyNodeRelReports,
      "fail: faulty NodeRelations"
    );

    long cardErrors = fullReport
      .nodeDescriptorReports()
      .flatMap(o -> o.nodeReports())
      .flatMap(o -> o.nodeRelationReports())
      .filter(o -> Boolean.FALSE.equals(o.checkResultCount()))
      .count();
    assertEquals(expCardErrorCount, cardErrors, "fail: cardinality errors");
  }

  @Test
  public void nodeRelationTest_OneOne_Zero() {
    String killHasFooCypher = String.format(
      "MATCH ()-[r:%s]-() DELETE r",
      SampleRelationshipRep.FooBar.type.name()
    );
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(killHasFooCypher);
      FullReport fullReport = callFullValidation(session);
      checkNodeRelationErrorCounts(fullReport, 2, 2, 2, 2, 2);
    }
  }

  @Test
  public void nodeRelationTest_OneOne_Many() {
    String fooAlias = "f";
    String barAlias = "b";
    String cypher = String.format(
      "MATCH (%s:%s), (%s:%s) CREATE %s",
      fooAlias,
      SampleNodeRep.Foo.fooLabel.name(),
      barAlias,
      SampleNodeRep.Bar.barLabel.name(),
      SampleCreationUtils.relationShipCypher(
        SampleRelationshipRep.FooBar,
        fooAlias,
        barAlias
      )
    );
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(cypher);
      FullReport fullReport = callFullValidation(session);
      checkNodeRelationErrorCounts(fullReport, 2, 2, 2, 2, 2);
    }
  }

  @Test
  public void nodeRelationTest_OneMany_Zero() {
    String killHasFooCypher = String.format(
      "MATCH ()-[r:%s]-() DELETE r",
      SampleRelationshipRep.HasFoo.type.name()
    );
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(killHasFooCypher);
      FullReport fullReport = callFullValidation(session);
      // only Keks complains, the incoming HasFoo on the Foo Node is ZeroMany
      checkNodeRelationErrorCounts(fullReport, 1, 1, 1, 1, 1);
    }
  }

  @Test
  public void nodeRelationTest_ZeroOne_Many() {
    String keksAlias = "k";
    String cypher = String.format(
      "MATCH (%s:%s) CREATE %s",
      keksAlias,
      SampleNodeRep.Keks.keksLabel.name(),
      SampleCreationUtils.relationShipCypher(
        SampleRelationshipRep.IsSubKeksOf,
        keksAlias,
        keksAlias
      )
    );
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(cypher);
      FullReport fullReport = callFullValidation(session);
      checkNodeRelationErrorCounts(fullReport, 1, 1, 1, 1, 1);
    }
  }

  // endregion

  // region screw with labels
  @Test
  public void labelTest() {
    String nodeAlias = "n";
    String killLabelCypher = String.format(
      "MATCH (%s:%s) REMOVE %s:%s",
      nodeAlias,
      SampleNodeRep.Foo.fooLabel.name(),
      nodeAlias,
      SampleNodeRep.Foo.mooLabel.name()
    );
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(killLabelCypher);
      FullReport fullReport = callFullValidation(session);

      long totalErrorCount = fullReport.errorCount();
      assertEquals(1, totalErrorCount, "fail totalErrorCount");

      long faultyNodeDescriptorCount = fullReport
        .nodeDescriptorReports()
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(
        1,
        faultyNodeDescriptorCount,
        "fail: more than one faulty NodeDescriptor"
      );

      long faultyNodeCount = fullReport
        .nodeDescriptorReports()
        .flatMap(o -> o.nodeReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(1, faultyNodeCount, "fail: more than one faulty Node");

      long missingLabels = fullReport
        .nodeDescriptorReports()
        .flatMap(o -> o.nodeReports())
        .filter(o -> o.missingLabels().count() == 1)
        .count();
      assertEquals(totalErrorCount, missingLabels, "fail: missingLabels");
    }
  }

  // endregion

  // region screw with Start and End Nodes

  @Test
  public void startNodeTest() {
    String keksAlias = "k";
    String fooAlias = "f";
    String cypher = String.format(
      "MATCH (%s:%s), (%s:%s) CREATE %s",
      keksAlias,
      SampleNodeRep.Keks.keksLabel.name(),
      fooAlias,
      SampleNodeRep.Foo.fooLabel.name(),
      SampleCreationUtils.relationShipCypher(
        SampleRelationshipRep.BarFoo,
        keksAlias,
        fooAlias
      )
    );
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(cypher);
      FullReport fullReport = callFullValidation(session);

      // there will be two complaints:
      // - FooNode does not like a second incoming BarFoo relation
      // - the BarFoo relation does not like starting on a KeksNode
      long totalErrorCount = fullReport.errorCount();
      assertEquals(2, totalErrorCount, "fail totalErrorCount");

      long relDescErrorCount = fullReport
        .relationshipDescriptorReports()
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(1, relDescErrorCount, "fail relDescErrorCount");

      long relErrorCount = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(1, relErrorCount, "fail relErrorCount");

      long startNodeErrorCount = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .filter(o -> Boolean.FALSE.equals(o.startNodeCheck()))
        .count();
      assertEquals(1, startNodeErrorCount, "fail startNodeErrorCount");
    }
  }

  @Test
  public void endNodeTest() {
    String fooAlias = "f";
    String keksAlias = "k";
    String cypher = String.format(
      "MATCH (%s:%s), (%s:%s) CREATE %s",
      fooAlias,
      SampleNodeRep.Foo.fooLabel.name(),
      keksAlias,
      SampleNodeRep.Keks.keksLabel.name(),
      SampleCreationUtils.relationShipCypher(
        SampleRelationshipRep.FooBar,
        fooAlias,
        keksAlias
      )
    );
    try (Session session = driver().session()) {
      setupSampleDatabase(session);
      session.run(cypher);
      FullReport fullReport = callFullValidation(session);

      // there will be two complaints:
      // - the FooNode does not like there being two outgoing FooBar relations
      // - the FooBar relation does not like ending on a KeksNode
      long totalErrorCount = fullReport.errorCount();
      assertEquals(2, totalErrorCount, "fail totalErrorCount");

      long relDescErrorCount = fullReport
        .relationshipDescriptorReports()
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(1, relDescErrorCount, "fail relDescErrorCount");

      long relErrorCount = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .filter(o -> o.errorCount() > 0)
        .count();
      assertEquals(1, relErrorCount, "fail relErrorCount");

      long startNodeErrorCount = fullReport
        .relationshipDescriptorReports()
        .flatMap(o -> o.relationshipReports())
        .filter(o -> Boolean.FALSE.equals(o.endNodeCheck()))
        .count();
      assertEquals(1, startNodeErrorCount, "fail startNodeErrorCount");
    }
  }
  // endregion
}
