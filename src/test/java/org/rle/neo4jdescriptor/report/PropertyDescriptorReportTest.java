package org.rle.neo4jdescriptor.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.dto.report.PropertyReportDto;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;
import org.rle.neo4jdescriptor.property.EnumProperty;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.array_basic.*;
import org.rle.neo4jdescriptor.property.prop_basic.*;
import org.rle.neo4jdescriptor.property.prop_enum.dummyenums.*;
import org.rle.neo4jdescriptor.samples.SampleNodeRep;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

@java.lang.SuppressWarnings("java:S2699") // complains about missing asserts in the methods that simply call the super method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PropertyDescriptorReportTest
  extends ReportBaseTest<PropertyReport> {

  private static final String key = "testKey";

  private static final String log = "testLogKey";

  private static final PropertyDescriptor[] props = new PropertyDescriptor[] {
    new BooleanProperty(key, log),
    new DoubleProperty(key, log),
    new LongProperty(key, log),
    new NumberProperty(key, log),
    new ObjectProperty(key, log),
    new StringProperty(key, log),
    new BooleanArrayProperty(key, log),
    new DoubleArrayProperty(key, log),
    new LongArrayProperty(key, log),
    new StringArrayProperty(key, log),
    new EnumProperty<>(key, log, String.class, DummyStringEnum.class),
    new EnumProperty<>(key, log, Long.class, DummyLongEnum.class),
    new EnumProperty<>(key, log, Double.class, DummyDoubleEnum.class),
  };

  @Override
  protected PropertyReport[] createEmptyReports() {
    return Arrays
      .stream(props)
      .map(o -> new PropertyReport(o))
      .toArray(PropertyReport[]::new);
  }

  @Override
  protected PropertyReport createSampleReport(Random ran) {
    PropertyDescriptor propDesc = props[ran.nextInt(props.length)];
    PropertyReport propReport = new PropertyReport(propDesc);
    randomiseReport(ran, propReport);
    return propReport;
  }

  @Override
  protected void randomiseReport(Random ran, PropertyReport propReport) {
    super.randomiseReport(ran, propReport);
    Boolean b = randomBool(ran);
    if (b != null) {
      propReport.setKeyCheck(b);
    }
    b = randomBool(ran);
    if (b != null) {
      propReport.setTypeCheck(b);
    }
    b = randomBool(ran);
    if (b != null) {
      propReport.setEnumCheck(b);
    }
    if (ran.nextDouble() < 0.5) {
      propReport.setDeviantType("deviantTypeName");
    }
  }

  @Test
  @Override
  public void countTestsTest() {
    super.countTestsTest();
  }

  @Test
  @Override
  public void equalityTest() {
    super.equalityTest();
  }

  // region calling setters tests

  @Test
  @Override
  public void addExceptionTest() {
    super.addExceptionTest();
  }

  @Test
  public void keyCheckTest() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport propRep = reports[i];
      propRep.setKeyCheck(true);
      assertTrue(propRep.checkResultKeyExists(), "fail true @index " + i);
      propRep.setKeyCheck(false);
      assertFalse(propRep.checkResultKeyExists(), "fail false @index " + i);
    }
  }

  @Test
  public void typeCheckTest() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport propRep = reports[i];
      propRep.setTypeCheck(true);
      assertTrue(propRep.checkResultType(), "fail true @index " + i);
      propRep.setTypeCheck(false);
      assertFalse(propRep.checkResultType(), "fail false @index " + i);
    }
  }

  @Test
  public void enumCheckTest() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport propRep = reports[i];
      propRep.setEnumCheck(true);
      assertTrue(propRep.checkResultEnumValue(), "fail true @index " + i);
      propRep.setEnumCheck(false);
      assertFalse(propRep.checkResultEnumValue(), "fail false @index " + i);
    }
  }

  @Test
  public void deviantTypeTest() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport propRep = reports[i];
      String devTypeName = "deviantTypeName";
      propRep.setDeviantType(devTypeName);
      assertEquals(propRep.deviantTypeName(), devTypeName, "fail @index " + i);
    }
  }

  // endregion

  // region calling setters on a closed report test

  @Test
  @Override
  public void closedTest_exception() {
    super.closedTest_exception();
  }

  @Test
  public void closedTest_keyCheck() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport propRep = reports[i];
      propRep.closeReport();
      Exception exc = null;
      try {
        propRep.setKeyCheck(true);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalStateException.class,
        ReportBase.CAN_NOT_MODIFY_CLOSED_REPORT,
        i
      );
    }
  }

  @Test
  public void closedTest_typeCheck() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport propRep = reports[i];
      propRep.closeReport();
      Exception exc = null;
      try {
        propRep.setTypeCheck(true);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalStateException.class,
        ReportBase.CAN_NOT_MODIFY_CLOSED_REPORT,
        i
      );
    }
  }

  @Test
  public void closedTest_enumCheck() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport propRep = reports[i];
      propRep.closeReport();
      Exception exc = null;
      try {
        propRep.setEnumCheck(true);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalStateException.class,
        ReportBase.CAN_NOT_MODIFY_CLOSED_REPORT,
        i
      );
    }
  }

  @Test
  public void closedTest_deviantType() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport propRep = reports[i];
      propRep.closeReport();
      Exception exc = null;
      try {
        propRep.setDeviantType("foo");
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalStateException.class,
        ReportBase.CAN_NOT_MODIFY_CLOSED_REPORT,
        i
      );
    }
  }

  // endregion

  // region inequality tests

  @Test
  @Override
  public void inequalityTest_closed() {
    super.equalityTest();
  }

  @Test
  @Override
  public void inequalityTest_exceptions() {
    super.equalityTest();
  }

  @Test
  public void inequalityTest_keyCheck() {
    PropertyReport[] reports1 = createEmptyReports();
    PropertyReport[] reports2 = createEmptyReports();
    for (int i = 0; i < reports1.length; i++) {
      PropertyReport rep1 = reports1[i];
      PropertyReport rep2 = reports2[i];
      assertEquals(rep1, rep2, "fail expected equality");
      rep1.setKeyCheck(true);
      rep2.setKeyCheck(false);
      assertNotEquals(rep1, rep2, "fail expected inequality");
      // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
      assertNotEquals(
        rep1.hashCode(),
        rep2.hashCode(),
        "fail expected hash diff"
      );
    }
  }

  @Test
  public void inequalityTest_typeCheck() {
    PropertyReport[] reports1 = createEmptyReports();
    PropertyReport[] reports2 = createEmptyReports();
    for (int i = 0; i < reports1.length; i++) {
      PropertyReport rep1 = reports1[i];
      PropertyReport rep2 = reports2[i];
      assertEquals(rep1, rep2, "fail expected equality");
      rep1.setTypeCheck(true);
      rep2.setTypeCheck(false);
      assertNotEquals(rep1, rep2, "fail expected inequality");
      // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
      assertNotEquals(
        rep1.hashCode(),
        rep2.hashCode(),
        "fail expected hash diff"
      );
    }
  }

  @Test
  public void inequalityTest_enumCheck() {
    PropertyReport[] reports1 = createEmptyReports();
    PropertyReport[] reports2 = createEmptyReports();
    for (int i = 0; i < reports1.length; i++) {
      PropertyReport rep1 = reports1[i];
      PropertyReport rep2 = reports2[i];
      assertEquals(rep1, rep2, "fail expected equality");
      rep1.setEnumCheck(true);
      rep2.setEnumCheck(false);
      assertNotEquals(rep1, rep2, "fail expected inequality");
      // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
      assertNotEquals(
        rep1.hashCode(),
        rep2.hashCode(),
        "fail expected hash diff"
      );
    }
  }

  @Test
  public void inequalityTest_deviantType() {
    PropertyReport[] reports1 = createEmptyReports();
    PropertyReport[] reports2 = createEmptyReports();
    for (int i = 0; i < reports1.length; i++) {
      PropertyReport rep1 = reports1[i];
      PropertyReport rep2 = reports2[i];
      assertEquals(rep1, rep2, "fail expected equality");
      rep1.setDeviantType("bla");
      rep2.setDeviantType("blub");
      assertNotEquals(rep1, rep2, "fail expected inequality");
      // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
      assertNotEquals(
        rep1.hashCode(),
        rep2.hashCode(),
        "fail expected hash diff"
      );
    }
  }

  // endregion

  // region hash reset tests

  @Test
  @Override
  public void hashResetTest_closed() {
    super.equalityTest();
  }

  @Test
  @Override
  public void hashResetTest_exception() {
    super.equalityTest();
  }

  @Test
  public void hashResetTest_keyCheck() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport rep = reports[i];
      rep.setKeyCheck(true);
      int origHash = rep.hashCode();
      rep.setKeyCheck(false);
      // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
      assertNotEquals(origHash, rep.hashCode());
    }
  }

  @Test
  public void hashResetTest_typeCheck() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport rep = reports[i];
      rep.setTypeCheck(true);
      int origHash = rep.hashCode();
      rep.setTypeCheck(false);
      // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
      assertNotEquals(origHash, rep.hashCode());
    }
  }

  @Test
  public void hashResetTest_enumCheck() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport rep = reports[i];
      rep.setEnumCheck(true);
      int origHash = rep.hashCode();
      rep.setEnumCheck(false);
      // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
      assertNotEquals(origHash, rep.hashCode());
    }
  }

  @Test
  public void hashResetTest_deviantType() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport rep = reports[i];
      rep.setDeviantType("foooo");
      int origHash = rep.hashCode();
      rep.setDeviantType("baaaaaaar");
      // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
      assertNotEquals(origHash, rep.hashCode());
    }
  }

  // endregion

  @Test
  @Override
  public void printExceptionsTest() {
    super.printExceptionsTest();
  }

  @Test
  public void printProblemsTest_missingKey() {
    PropertyReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      PropertyReport propRep = reports[i];
      propRep.setKeyCheck(false);
      String printMsg = propRep.print("ggg", "ss");
      String expMsg = "gggtestKey: no such key found";
      assertEquals(printMsg, expMsg, "fail @index " + i);
    }
  }

  @Test
  public void printProblemsTest_wrongType() {
    LongArrayProperty longProp = new LongArrayProperty("testKey");
    PropertyReport propRep = new PropertyReport(longProp);
    propRep.setTypeCheck(false);
    propRep.setDeviantType("fuubar");
    String printMsg = propRep.print("ggg", "ss");
    String expMsg = "gggtestKey: type error. Expected: Long[], actual: fuubar";
    assertEquals(printMsg, expMsg);
  }

  @Test
  public void printProblemsTest_enumValue() {
    EnumProperty<Double, DummyDoubleEnum> prop = new EnumProperty<Double, DummyDoubleEnum>(
      "testKey",
      Double.class,
      DummyDoubleEnum.class
    );
    PropertyReport propRep = new PropertyReport(prop);
    propRep.setEnumCheck(false);
    String printMsg = propRep.print("ggg", "ss");
    String expMsg =
      "gggtestKey: invalid enum value. Valid values are: 1.0, 2.0, 3.0";
    assertEquals(printMsg, expMsg);
  }

  @Test
  public void report2DtoRoundRobinTest() {
    SampleNodeRep nodeRepo = new SampleNodeRep();
    Iterator<NodeDescriptor> nodeDescIter = nodeRepo
      .nodeDescriptors()
      .iterator();
    while (nodeDescIter.hasNext()) {
      NodeDescriptor nodeDesc = nodeDescIter.next();
      Iterator<PropertyDescriptor> propDescIter = nodeDesc
        .properties(Modality.CONTINGENT)
        .iterator();
      Random ran = new Random(42l);
      while (propDescIter.hasNext()) {
        PropertyDescriptor propDesc = propDescIter.next();
        PropertyReport rep1 = new PropertyReport(propDesc);
        randomiseReport(ran, rep1);
        PropertyReportDto dto = rep1.getDto();
        PropertyReport rep2 = new PropertyReport(nodeDesc, dto);
        assertEquals(
          rep1,
          rep2,
          String.format(
            "equality fail @ %s.%s",
            nodeDesc.getClass().getSimpleName(),
            propDesc.key()
          )
        );
        assertEquals(
          rep1.hashCode(),
          rep2.hashCode(),
          String.format(
            "hash fail @ %s.%s",
            nodeDesc.getClass().getSimpleName(),
            propDesc.key()
          )
        );
      }
    }
  }
}
