package org.rle.neo4jdescriptor.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.dto.report.RelationshipDescriptorReportDto;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

@java.lang.SuppressWarnings("java:S2699") // complains about missing asserts in the methods that simply call the super method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RelationshipDescriptorReportTest
  extends ReportBaseTest<RelationshipDescriptorReport> {

  private static final RelationshipReportTest smRelationshipReportTest = new RelationshipReportTest();

  @Override
  protected RelationshipDescriptorReport createSampleReport(Random ran) {
    RelationshipDescriptorReport[] allReps = createEmptyReports();
    RelationshipDescriptorReport rep = allReps[ran.nextInt(allReps.length)];
    randomiseReport(ran, rep);
    return rep;
  }

  @Override
  protected RelationshipDescriptorReport[] createEmptyReports() {
    RelationshipDescriptor[] relDescriptors = new RelationshipDescriptor[] {
      SampleRelationshipRep.BarFoo,
      SampleRelationshipRep.FooBar,
      SampleRelationshipRep.HasBar,
      SampleRelationshipRep.HasFoo,
      SampleRelationshipRep.HasKeks,
      SampleRelationshipRep.IsSubKeksOf,
      SampleRelationshipRep.EndsAtKeks,
    };
    ArrayList<RelationshipDescriptorReport> reportList = new ArrayList<>();
    for (RelationshipDescriptor rd : relDescriptors) {
      RelationshipDescriptorReport relRep = new RelationshipDescriptorReport(
        rd
      );
      reportList.add(relRep);
    }
    return reportList.toArray(RelationshipDescriptorReport[]::new);
  }

  @Override
  public void randomiseReport(Random ran, RelationshipDescriptorReport rep) {
    super.randomiseReport(ran, rep);
    for (int i = 0; i < 10; i++) {
      RelationshipReport relRep = new RelationshipReport(
        rep.relationshipDescriptor(),
        String.valueOf(i)
      );
      smRelationshipReportTest.randomiseReport(ran, relRep);
      rep.addRelationshipReport(relRep.closeReport());
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
  public void addRelationshipReportTest_yay() {
    RelationshipDescriptorReport relDescReport = new RelationshipDescriptorReport(
      SampleRelationshipRep.FooBar
    );
    RelationshipReport relReport = new RelationshipReport(
      relDescReport.relationshipDescriptor(),
      String.valueOf(0)
    );
    relReport.addException(new RuntimeException("bla")).closeReport();
    relDescReport.addRelationshipReport(relReport);
    assertTrue(
      relDescReport.relationshipReports().anyMatch(o -> o.equals(relReport)),
      "fail contains"
    );
    assertEquals(1, relDescReport.errorCount(), "error count fail");
  }

  @Test
  public void addRelationshipReportTest_openReport() {
    RelationshipDescriptorReport relDescReport = new RelationshipDescriptorReport(
      SampleRelationshipRep.FooBar
    );
    Exception exc = null;
    RelationshipReport relReport = new RelationshipReport(
      relDescReport.relationshipDescriptor(),
      String.valueOf(0)
    );
    try {
      relDescReport.addRelationshipReport(relReport);
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
  public void addRelationshipReportTest_wrongRelationshipDescriptor() {
    RelationshipDescriptorReport relDescReport = new RelationshipDescriptorReport(
      SampleRelationshipRep.FooBar
    );
    Exception exc = null;
    RelationshipReport relReport = new RelationshipReport(
      SampleRelationshipRep.BarFoo,
      String.valueOf(0)
    );
    try {
      relDescReport.addRelationshipReport(relReport.closeReport());
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalArgumentException.class,
      RelationshipDescriptorReport.WRONG_RELATIONSHIPDESCRIPTOR
    );
  }

  @Test
  public void addRelationshipReportTest_duplicateId() {
    RelationshipDescriptorReport relDescReport = new RelationshipDescriptorReport(
      SampleRelationshipRep.FooBar
    );
    Exception exc = null;
    RelationshipReport relReport1 = new RelationshipReport(
      SampleRelationshipRep.FooBar,
      String.valueOf(0)
    );
    RelationshipReport relReport2 = new RelationshipReport(
      SampleRelationshipRep.FooBar,
      String.valueOf(0)
    );
    relDescReport.addRelationshipReport(relReport1.closeReport());
    try {
      relDescReport.addRelationshipReport(relReport2.closeReport());
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalArgumentException.class,
      RelationshipDescriptorReport.DUPLICATE_RELATIONSHIP_ID
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
  public void closedTest_addRelationshipReportCheck() {
    RelationshipDescriptorReport relDescReport = new RelationshipDescriptorReport(
      SampleRelationshipRep.FooBar
    );
    relDescReport.closeReport();
    RelationshipReport relReport = new RelationshipReport(
      relDescReport.relationshipDescriptor(),
      String.valueOf(0)
    );
    Exception exc = null;
    try {
      relDescReport.addRelationshipReport(relReport.closeReport());
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
  public void inequalityTest_nodeDescriptor() {
    RelationshipDescriptorReport rep1 = new RelationshipDescriptorReport(
      SampleRelationshipRep.FooBar
    );
    RelationshipDescriptorReport rep2 = new RelationshipDescriptorReport(
      SampleRelationshipRep.BarFoo
    );
    assertNotEquals(rep1, rep2, "fail expected inequality");
    // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
    assertNotEquals(
      rep1.hashCode(),
      rep2.hashCode(),
      "fail expected hash diff"
    );
  }

  @Test
  public void inequalityTest_relationshipReports() {
    RelationshipDescriptorReport rep1 = new RelationshipDescriptorReport(
      SampleRelationshipRep.FooBar
    );
    RelationshipDescriptorReport rep2 = new RelationshipDescriptorReport(
      SampleRelationshipRep.FooBar
    );
    rep1.addRelationshipReport(
      new RelationshipReport(SampleRelationshipRep.FooBar, String.valueOf(1))
        .closeReport()
    );
    rep2.addRelationshipReport(
      new RelationshipReport(SampleRelationshipRep.FooBar, String.valueOf(1))
        .closeReport()
    );
    assertEquals(rep1, rep2, "fail expected equality");
    rep2.addRelationshipReport(
      new RelationshipReport(SampleRelationshipRep.FooBar, String.valueOf(2))
        .closeReport()
    );
    assertNotEquals(rep1, rep2, "fail expected inequality");
    // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
    assertNotEquals(
      rep1.hashCode(),
      rep2.hashCode(),
      "fail expected hash diff"
    );
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
  public void hashResetTest_relationshipReports() {
    RelationshipDescriptorReport rep = new RelationshipDescriptorReport(
      SampleRelationshipRep.FooBar
    );
    rep.addRelationshipReport(
      new RelationshipReport(SampleRelationshipRep.FooBar, String.valueOf(1))
        .closeReport()
    );
    int origHash = rep.hashCode();
    rep.addRelationshipReport(
      new RelationshipReport(SampleRelationshipRep.FooBar, String.valueOf(2))
        .closeReport()
    );
    // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
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
    SampleRelationshipRep relRepo = new SampleRelationshipRep();
    for (long seed = 0; seed < 20l; seed++) {
      Iterator<RelationshipDescriptor> relIter = relRepo
        .relationshipDescriptors()
        .iterator();
      Random ran = new Random(seed);
      while (relIter.hasNext()) {
        RelationshipDescriptor relDesc = relIter.next();
        RelationshipDescriptorReport rep1 = new RelationshipDescriptorReport(
          relDesc
        );
        for (long id = 0; id < 10; id++) {
          RelationshipReport relRep = new RelationshipReport(
            relDesc,
            String.valueOf(id)
          );
          smRelationshipReportTest.randomiseReport(ran, relRep);
          rep1.addRelationshipReport(relRep.closeReport());
        }
        RelationshipDescriptorReportDto dto = rep1.getDto();
        RelationshipDescriptorReport rep2 = new RelationshipDescriptorReport(
          relRepo,
          dto
        );
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
