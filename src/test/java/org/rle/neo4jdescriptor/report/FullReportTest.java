package org.rle.neo4jdescriptor.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.dto.report.FullReportDto;
import org.rle.neo4jdescriptor.samples.SampleNodeRep;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

@java.lang.SuppressWarnings("java:S2699") // complains about missing asserts in the methods that simply call the super method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FullReportTest extends ReportBaseTest<FullReport> {

  private static final NodeDescriptorReportTest mNodeDescriptorReportTest = new NodeDescriptorReportTest();

  private static final RelationshipDescriptorReportTest mRelationshipDescriptorReportTest = new RelationshipDescriptorReportTest();

  @Override
  protected FullReport createSampleReport(Random ran) {
    FullReport fullReport = new FullReport();
    NodeDescriptorReport ndRep = mNodeDescriptorReportTest.createSampleReport(
      ran
    );
    RelationshipDescriptorReport rdRep = mRelationshipDescriptorReportTest.createSampleReport(
      ran
    );
    fullReport.addNodeDescriptorReport(ndRep.closeReport());
    fullReport.addRelationshipDescriptorReport(rdRep.closeReport());
    super.randomiseReport(ran, fullReport);
    return fullReport;
  }

  @Override
  protected FullReport[] createEmptyReports() {
    NodeDescriptorReport[] nodeDescReports = mNodeDescriptorReportTest.createEmptyReports();
    RelationshipDescriptorReport[] relDescReports = mRelationshipDescriptorReportTest.createEmptyReports();
    ArrayList<FullReport> fullReports = new ArrayList<>();
    FullReport allInReport = new FullReport();
    Arrays
      .stream(nodeDescReports)
      .forEach(o -> allInReport.addNodeDescriptorReport(o.closeReport()));
    Arrays
      .stream(relDescReports)
      .forEach(o -> allInReport.addRelationshipDescriptorReport(o.closeReport())
      );
    fullReports.add(allInReport);
    for (int n = 0; n < nodeDescReports.length; n++) {
      for (int r = 0; r < relDescReports.length; r++) {
        FullReport fullReport = new FullReport();
        fullReport.addNodeDescriptorReport(nodeDescReports[n].closeReport());
        fullReport.addRelationshipDescriptorReport(
          relDescReports[r].closeReport()
        );
        fullReports.add(fullReport);
      }
    }
    return fullReports.toArray(FullReport[]::new);
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
  public void addNodeDescReportTest_yay() {
    FullReport fullReport = new FullReport();
    NodeDescriptorReport ndRep = new NodeDescriptorReport(SampleNodeRep.Foo);
    ndRep.addException(new RuntimeException("bla"));
    fullReport.addNodeDescriptorReport(ndRep.closeReport());
    assertTrue(
      fullReport.nodeDescriptorReports().anyMatch(o -> o.equals(ndRep)),
      "fail 1"
    );
    assertEquals(1, fullReport.errorCount(), "fail 2");
  }

  @Test
  public void addNodeDescReportTest_duplicate() {
    FullReport fullReport = new FullReport();
    NodeDescriptorReport ndRep = new NodeDescriptorReport(SampleNodeRep.Foo);
    ndRep.addException(new RuntimeException("bla"));
    fullReport.addNodeDescriptorReport(ndRep.closeReport());
    Exception exc = null;
    try {
      NodeDescriptorReport ndRep2 = new NodeDescriptorReport(SampleNodeRep.Foo);
      ndRep2.addException(new RuntimeException("bla"));
      fullReport.addNodeDescriptorReport(ndRep.closeReport());
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalArgumentException.class,
      FullReport.SM_DUPLICATE_NODEDESCRIPTOR
    );
  }

  @Test
  public void addNodeDescReportTest_openSubreport() {
    FullReport fullReport = new FullReport();
    Exception exc = null;
    try {
      NodeDescriptorReport ndRep = new NodeDescriptorReport(SampleNodeRep.Foo);
      ndRep.addException(new RuntimeException("bla"));
      fullReport.addNodeDescriptorReport(ndRep);
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalArgumentException.class,
      ReportBase.SUBREPORT_MUST_BE_CLOSED
    );
  }

  @Test
  public void addRelationshipDescReportTest_yay() {
    FullReport fullReport = new FullReport();
    RelationshipDescriptorReport rdRep = new RelationshipDescriptorReport(
      SampleRelationshipRep.FooBar
    );
    rdRep.addException(new RuntimeException("bla"));
    fullReport.addRelationshipDescriptorReport(rdRep.closeReport());
    assertTrue(
      fullReport.relationshipDescriptorReports().anyMatch(o -> o.equals(rdRep)),
      "fail 1"
    );
    assertEquals(1, fullReport.errorCount(), "fail 2");
  }

  @Test
  public void addRelationshipDescReportTest_duplicate() {
    FullReport fullReport = new FullReport();
    RelationshipDescriptorReport ndRep = new RelationshipDescriptorReport(
      SampleRelationshipRep.FooBar
    );
    ndRep.addException(new RuntimeException("bla"));
    fullReport.addRelationshipDescriptorReport(ndRep.closeReport());
    Exception exc = null;
    try {
      RelationshipDescriptorReport ndRep2 = new RelationshipDescriptorReport(
        SampleRelationshipRep.FooBar
      );
      ndRep2.addException(new RuntimeException("bla"));
      fullReport.addRelationshipDescriptorReport(ndRep2.closeReport());
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalArgumentException.class,
      FullReport.SM_DUPLICATE_RELATIONSHIPDESCRIPTOR
    );
  }

  @Test
  public void addRelationshipDescReportTest_openSubreport() {
    FullReport fullReport = new FullReport();
    Exception exc = null;
    try {
      RelationshipDescriptorReport rdRep = new RelationshipDescriptorReport(
        SampleRelationshipRep.FooBar
      );
      rdRep.addException(new RuntimeException("bla"));
      fullReport.addRelationshipDescriptorReport(rdRep);
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalArgumentException.class,
      ReportBase.SUBREPORT_MUST_BE_CLOSED
    );
  }

  // endregion

  // region calling setters on a closed report test

  @Test
  @Override
  public void closedTest_exception() {
    super.closedTest_exception();
  }

  @Test
  public void closedTest_addNodeDescriptorReport() {
    FullReport fullReport = (new FullReport()).closeReport();
    Exception exc = null;
    try {
      NodeDescriptorReport ndRep = new NodeDescriptorReport(SampleNodeRep.Foo);
      ndRep.addException(new RuntimeException("bla"));
      fullReport.addNodeDescriptorReport(ndRep.closeReport());
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalStateException.class,
      ReportBase.CAN_NOT_MODIFY_CLOSED_REPORT
    );
  }

  @Test
  public void closedTest_addRelationshipDescriptorReport() {
    FullReport fullReport = (new FullReport()).closeReport();
    Exception exc = null;
    try {
      RelationshipDescriptorReport rdRep = new RelationshipDescriptorReport(
        SampleRelationshipRep.FooBar
      );
      rdRep.addException(new RuntimeException("bla"));
      fullReport.addRelationshipDescriptorReport(rdRep.closeReport());
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalStateException.class,
      ReportBase.CAN_NOT_MODIFY_CLOSED_REPORT
    );
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
  public void inequalityTest_nodeDescriptorReports() {
    FullReport rep1 = new FullReport();
    rep1.addNodeDescriptorReport(
      (new NodeDescriptorReport(SampleNodeRep.Foo)).closeReport()
    );
    FullReport rep2 = new FullReport();
    rep2.addNodeDescriptorReport(
      (new NodeDescriptorReport(SampleNodeRep.Foo)).closeReport()
    );
    assertEquals(rep1, rep2, "Fail expected equality");
    rep2.addNodeDescriptorReport(
      (new NodeDescriptorReport(SampleNodeRep.Bar)).closeReport()
    );
    assertNotEquals(rep1, rep2, "fail inequality");
  }

  @Test
  public void inequalityTest_relationshipDescriptorReports() {
    FullReport rep1 = new FullReport();
    rep1.addRelationshipDescriptorReport(
      (
        new RelationshipDescriptorReport(SampleRelationshipRep.FooBar)
      ).closeReport()
    );
    FullReport rep2 = new FullReport();
    rep2.addRelationshipDescriptorReport(
      (
        new RelationshipDescriptorReport(SampleRelationshipRep.FooBar)
      ).closeReport()
    );
    assertEquals(rep1, rep2, "Fail expected equality");
    rep2.addRelationshipDescriptorReport(
      (
        new RelationshipDescriptorReport(SampleRelationshipRep.BarFoo)
      ).closeReport()
    );
    assertNotEquals(rep1, rep2, "fail inequality");
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
  public void hashResetTest_nodeDescriptorReports() {
    FullReport rep = new FullReport();
    rep.addNodeDescriptorReport(
      (new NodeDescriptorReport(SampleNodeRep.Foo)).closeReport()
    );
    int origHash = rep.hashCode();
    rep.addNodeDescriptorReport(
      (new NodeDescriptorReport(SampleNodeRep.Bar)).closeReport()
    );
    assertNotEquals(origHash, rep.hashCode());
  }

  @Test
  public void hashResetTest_relationshipDescriptorReports() {
    FullReport rep = new FullReport();
    rep.addRelationshipDescriptorReport(
      (
        new RelationshipDescriptorReport(SampleRelationshipRep.FooBar)
      ).closeReport()
    );
    int origHash = rep.hashCode();
    rep.addRelationshipDescriptorReport(
      (
        new RelationshipDescriptorReport(SampleRelationshipRep.BarFoo)
      ).closeReport()
    );
    assertNotEquals(origHash, rep.hashCode());
  }

  // endregion

  @Test
  @Override
  public void printExceptionsTest() {
    super.printExceptionsTest();
  }

  @Test
  public void report2DtoRoundRobinTest() {
    for (long seed = 0; seed < 20l; seed++) {
      Random ran = new Random(seed);
      FullReport rep1 = createSampleReport(ran);
      FullReportDto dto = rep1.getDto();
      FullReport rep2 = new FullReport(
        new SampleNodeRep(),
        new SampleRelationshipRep(),
        dto
      );
      assertEquals(rep1, rep2, "Fail equality @seed " + seed);
      assertEquals(rep1.hashCode(), rep2.hashCode(), "Fail hash @seed " + seed);
    }
  }
}
