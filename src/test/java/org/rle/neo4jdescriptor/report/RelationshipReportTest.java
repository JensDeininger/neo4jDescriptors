package org.rle.neo4jdescriptor.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.dto.report.RelationshipReportDto;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

@java.lang.SuppressWarnings("java:S2699") // complains about missing asserts in the methods that simply call the super method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RelationshipReportTest
  extends EntityReportTest<RelationshipReport> {

  @Override
  protected RelationshipReport createSampleReport(Random ran) {
    RelationshipReport[] relReps = createEmptyReports();
    RelationshipReport rep = relReps[ran.nextInt(relReps.length)];
    randomiseReport(ran, rep);
    return rep;
  }

  @Override
  protected RelationshipReport[] createEmptyReports() {
    SampleRelationshipRep relRep = new SampleRelationshipRep();
    return relRep
      .relationshipDescriptors()
      .map(o -> new RelationshipReport(o, String.valueOf(23l)))
      .toArray(RelationshipReport[]::new);
  }

  @Override
  protected void randomiseReport(Random ran, RelationshipReport relReport) {
    super.randomiseReport(ran, relReport);
    RelationshipDescriptor relDesc = relReport.relationshipDescriptor();
    if (relDesc.startNodeDescriptor() != null) {
      relReport.setStartNodeCheck(ran.nextDouble() < 0.5);
    }
    if (relDesc.endNodeDescriptor() != null) {
      relReport.setEndNodeCheck(ran.nextDouble() < 0.5);
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
  @Override
  public void addPropertyReportTest_yay() {
    super.addPropertyReportTest_yay();
  }

  @Test
  @Override
  public void addPropertyReportTest_wrong() {
    super.addPropertyReportTest_wrong();
  }

  @Test
  @Override
  public void addPropertyReportTest_duplicate() {
    super.addPropertyReportTest_duplicate();
  }

  @Test
  @Override
  public void addPropertyReportTest_openReport() {
    super.addPropertyReportTest_openReport();
  }

  @Test
  public void setStartCheckTest_yay() {
    RelationshipReport[] reports = createEmptyReports();
    boolean didSomething = false;
    for (int i = 0; i < reports.length; i++) {
      RelationshipReport report = reports[i];
      if (report.relationshipDescriptor().startNodeDescriptor() == null) {
        continue;
      }
      didSomething = true;
      report.setStartNodeCheck(true);
      assertTrue(report.startNodeCheck(), "fail true @index " + i);
      report.setStartNodeCheck(false);
      assertFalse(report.startNodeCheck(), "fail false @index " + i);
    }
    assertTrue(didSomething, "failed to test anything");
  }

  @Test
  public void setStartCheckTest_noSuchNode() {
    RelationshipReport[] reports = createEmptyReports();
    boolean didSomething = false;
    for (int i = 0; i < reports.length; i++) {
      RelationshipReport report = reports[i];
      if (report.relationshipDescriptor().startNodeDescriptor() != null) {
        continue;
      }
      didSomething = true;
      Exception exc = null;
      try {
        report.setStartNodeCheck(false);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalStateException.class,
        RelationshipReport.NO_SUCH_NODEDESCRIPTOR_SPECIFIED,
        i
      );
    }
    assertTrue(didSomething, "failed to test anything");
  }

  @Test
  public void setEndCheckTest_yay() {
    RelationshipReport[] reports = createEmptyReports();
    boolean didSomething = false;
    for (int i = 0; i < reports.length; i++) {
      RelationshipReport report = reports[i];
      if (report.relationshipDescriptor().endNodeDescriptor() == null) {
        continue;
      }
      didSomething = true;
      report.setEndNodeCheck(true);
      assertTrue(report.endNodeCheck(), "fail true @index " + i);
      report.setEndNodeCheck(false);
      assertFalse(report.endNodeCheck(), "fail false @index " + i);
    }
    assertTrue(didSomething, "failed to test anything");
  }

  @Test
  public void setEndCheckTest_noSuchNode() {
    RelationshipReport[] reports = createEmptyReports();
    boolean didSomething = false;
    for (int i = 0; i < reports.length; i++) {
      RelationshipReport report = reports[i];
      if (report.relationshipDescriptor().endNodeDescriptor() != null) {
        continue;
      }
      didSomething = true;
      Exception exc = null;
      try {
        report.setEndNodeCheck(false);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalStateException.class,
        RelationshipReport.NO_SUCH_NODEDESCRIPTOR_SPECIFIED,
        i
      );
    }
    assertTrue(didSomething, "failed to test anything");
  }

  // endregion

  // region calling setters on a closed report test

  @Test
  @Override
  public void closedTest_exception() {
    super.closedTest_exception();
  }

  @Test
  @Override
  public void closedTest_properties() {
    super.closedTest_properties();
  }

  @Test
  public void closedTest_startNode() {
    RelationshipReport[] reports = createEmptyReports();
    boolean didSomething = false;
    for (int i = 0; i < reports.length; i++) {
      RelationshipReport report = reports[i].closeReport();
      if (report.relationshipDescriptor().startNodeDescriptor() == null) {
        continue;
      }
      didSomething = true;
      Exception exc = null;
      try {
        report.setStartNodeCheck(false);
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
    assertTrue(didSomething, "failed to test anything");
  }

  @Test
  public void closedTest_endNode() {
    RelationshipReport[] reports = createEmptyReports();
    boolean didSomething = false;
    for (int i = 0; i < reports.length; i++) {
      RelationshipReport report = reports[i].closeReport();
      if (report.relationshipDescriptor().endNodeDescriptor() == null) {
        continue;
      }
      didSomething = true;
      Exception exc = null;
      try {
        report.setEndNodeCheck(false);
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
    assertTrue(didSomething, "failed to test anything");
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
  @Override
  public void inequalityTest_properties() {
    super.inequalityTest_properties();
  }

  @Test
  public void inequalityTest_relationshipDescriptor() {
    RelationshipReport r1 = new RelationshipReport(
      SampleRelationshipRep.BarFoo,
      String.valueOf(0)
    );
    RelationshipReport r2 = new RelationshipReport(
      SampleRelationshipRep.FooBar,
      String.valueOf(0)
    );
    assertNotEquals(r1, r2);
    // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
    assertNotEquals(r1.hashCode(), r2.hashCode());
  }

  @Test
  public void inequalityTest_relationshipId() {
    RelationshipReport r1 = new RelationshipReport(
      SampleRelationshipRep.BarFoo,
      String.valueOf(0)
    );
    RelationshipReport r2 = new RelationshipReport(
      SampleRelationshipRep.BarFoo,
      String.valueOf(1)
    );
    assertNotEquals(r1, r2);
    // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
    assertNotEquals(r1.hashCode(), r2.hashCode());
  }

  @Test
  public void inequalityTest_startNode() {
    RelationshipReport r1 = new RelationshipReport(
      SampleRelationshipRep.BarFoo,
      String.valueOf(0)
    );
    RelationshipReport r2 = new RelationshipReport(
      SampleRelationshipRep.BarFoo,
      String.valueOf(0)
    );
    assertEquals(r1, r2, "fail expected equality");
    r1.setStartNodeCheck(true);
    r2.setStartNodeCheck(false);
    assertNotEquals(r1, r2, "fail expected inequality");
  }

  @Test
  public void inequalityTest_endNode() {
    RelationshipReport r1 = new RelationshipReport(
      SampleRelationshipRep.BarFoo,
      String.valueOf(0)
    );
    RelationshipReport r2 = new RelationshipReport(
      SampleRelationshipRep.BarFoo,
      String.valueOf(0)
    );
    assertEquals(r1, r2, "fail expected equality");
    r1.setEndNodeCheck(true);
    r2.setEndNodeCheck(false);
    assertNotEquals(r1, r2, "fail expected inequality");
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
  @Override
  public void hashResetTest_properties() {
    super.equalityTest();
  }

  @Test
  public void hashResetTest_startNode() {
    RelationshipReport rep = new RelationshipReport(
      SampleRelationshipRep.BarFoo,
      String.valueOf(0)
    );
    rep.setStartNodeCheck(false);
    int origHash = rep.hashCode();
    rep.setStartNodeCheck(true);
    // Strictly speaking, the hash doesnt HAVE to change. But it'd be really weird if it did not
    assertNotEquals(origHash, rep.hashCode());
  }

  @Test
  public void hashResetTest_endNode() {
    RelationshipReport rep = new RelationshipReport(
      SampleRelationshipRep.BarFoo,
      String.valueOf(0)
    );
    rep.setEndNodeCheck(false);
    int origHash = rep.hashCode();
    rep.setEndNodeCheck(true);
    // Strictly speaking, the hash doesnt HAVE to change. But it'd be really weird if it did not
    assertNotEquals(origHash, rep.hashCode());
  }

  // endregion

  @Test
  @Override
  public void printExceptionsTest() {
    super.printExceptionsTest();
  }

  @Test
  public void printTest() {
    RelationshipReport[] relReports = createEmptyReports();
    for (int i = 0; i < relReports.length; i++) {
      RelationshipReport relReport = relReports[i];
      PropertyDescriptor[] props = relReport
        .relationshipDescriptor()
        .properties(Modality.CONTINGENT)
        .toArray(PropertyDescriptor[]::new);
      for (PropertyDescriptor p : props) {
        PropertyReport propRep = new PropertyReport(p);
        propRep.setKeyCheck(false).closeReport();
        relReport.addPropertyReport(propRep);
      }
      if (relReport.relationshipDescriptor().startNodeDescriptor() != null) {
        relReport.setStartNodeCheck(false);
      }
      if (relReport.relationshipDescriptor().endNodeDescriptor() != null) {
        relReport.setEndNodeCheck(false);
      }
      String gI = "---";
      String sI = "..";
      String str = relReport.print(gI, sI);
      int expectedLineCount = 1; // header
      if (relReport.propertyReportCount() > 0) {
        expectedLineCount++;
      }
      expectedLineCount += relReport.errorCount();
      assertEquals(
        expectedLineCount,
        str.split(System.lineSeparator()).length,
        "fail @ index " + i
      );
    }
  }

  @Test
  public void report2DtoRoundRobinTest() {
    SampleRelationshipRep relRepo = new SampleRelationshipRep();
    for (long seed = 0; seed < 20l; seed++) {
      Random ran = new Random(seed);
      Iterator<RelationshipDescriptor> relIter = relRepo
        .relationshipDescriptors()
        .iterator();
      while (relIter.hasNext()) {
        RelationshipDescriptor relDesc = relIter.next();
        RelationshipReport rep1 = new RelationshipReport(
          relDesc,
          String.valueOf(0)
        );
        randomiseReport(ran, rep1);
        RelationshipReportDto dto = rep1.getDto();
        RelationshipReport rep2 = new RelationshipReport(relRepo, dto);
        assertEquals(
          rep1,
          rep2,
          "Fail equality " + relDesc.getClass().getSimpleName()
        );
        assertEquals(
          rep1.hashCode(),
          rep2.hashCode(),
          "Fail hash " + relDesc.getClass().getSimpleName()
        );
      }
    }
  }
}
