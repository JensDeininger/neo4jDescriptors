package org.rle.neo4jdescriptor.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.dto.report.NodeDescriptorReportDto;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;
import org.rle.neo4jdescriptor.samples.SampleNodeRep;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

@java.lang.SuppressWarnings("java:S2699") // complains about missing asserts in the methods that simply call the super method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NodeDescriptorReportTest
  extends ReportBaseTest<NodeDescriptorReport> {

  private static final NodeReportTest smNodeReportTest = new NodeReportTest();

  @Override
  protected NodeDescriptorReport createSampleReport(Random ran) {
    NodeDescriptorReport[] reps = createEmptyReports();
    NodeDescriptorReport rep = reps[ran.nextInt(reps.length)];
    randomiseReport(ran, rep);
    return rep;
  }

  @Override
  protected NodeDescriptorReport[] createEmptyReports() {
    NodeDescriptor[] nodeDescriptors = new NodeDescriptor[] {
      SampleNodeRep.Foo,
      SampleNodeRep.Bar,
      SampleNodeRep.Keks,
    };
    ArrayList<NodeDescriptorReport> reportList = new ArrayList<>();
    for (NodeDescriptor nd : nodeDescriptors) {
      NodeDescriptorReport nodeRep = new NodeDescriptorReport(nd);
      reportList.add(nodeRep);
    }
    return reportList.toArray(NodeDescriptorReport[]::new);
  }

  @Override
  protected void randomiseReport(
    Random ran,
    NodeDescriptorReport nodeDescriptorReport
  ) {
    super.randomiseReport(ran, nodeDescriptorReport);
    for (int i = 0; i < 10; i++) {
      NodeReport nodeRep = new NodeReport(
        nodeDescriptorReport.nodeDescriptor(),
        String.valueOf(i)
      );
      smNodeReportTest.randomiseReport(ran, nodeRep);
      nodeDescriptorReport.addNodeReport(nodeRep.closeReport());
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
  public void addNodeReportTest_yay() {
    NodeDescriptorReport nodeDescReport = new NodeDescriptorReport(
      SampleNodeRep.Foo
    );
    NodeReport nodeReport = new NodeReport(
      nodeDescReport.nodeDescriptor(),
      String.valueOf(0)
    );
    nodeReport.addException(new RuntimeException("bla")).closeReport();
    nodeDescReport.addNodeReport(nodeReport);
    assertTrue(
      nodeDescReport.nodeReports().anyMatch(o -> o.equals(nodeReport)),
      "fail contains"
    );
    assertEquals(1, nodeDescReport.errorCount(), "error count fail");
  }

  @Test
  public void addNodeReportTest_openReport() {
    NodeDescriptorReport nodeDescReport = new NodeDescriptorReport(
      SampleNodeRep.Foo
    );
    Exception exc = null;
    NodeReport nodeReport = new NodeReport(
      nodeDescReport.nodeDescriptor(),
      String.valueOf(0)
    );
    try {
      nodeDescReport.addNodeReport(nodeReport);
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
  public void addNodeReportTest_wrongNodeDescriptor() {
    NodeDescriptorReport nodeDescReport = new NodeDescriptorReport(
      SampleNodeRep.Foo
    );
    Exception exc = null;
    NodeReport nodeReport = new NodeReport(
      SampleNodeRep.Bar,
      String.valueOf(0)
    );
    try {
      nodeDescReport.addNodeReport(nodeReport.closeReport());
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalArgumentException.class,
      NodeDescriptorReport.WRONG_NODEDESCRIPTOR
    );
  }

  @Test
  public void addNodeReportTest_duplicateId() {
    NodeDescriptorReport nodeDescReport = new NodeDescriptorReport(
      SampleNodeRep.Foo
    );
    Exception exc = null;
    NodeReport nodeReport1 = new NodeReport(
      SampleNodeRep.Foo,
      String.valueOf(0)
    );
    NodeReport nodeReport2 = new NodeReport(
      SampleNodeRep.Foo,
      String.valueOf(0)
    );
    nodeDescReport.addNodeReport(nodeReport1.closeReport());
    try {
      nodeDescReport.addNodeReport(nodeReport2.closeReport());
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalArgumentException.class,
      NodeDescriptorReport.DUPLICATE_NODE_ID
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
  public void closedTest_addNodeReportCheck() {
    NodeDescriptorReport nodeDescReport = new NodeDescriptorReport(
      SampleNodeRep.Foo
    );
    nodeDescReport.closeReport();
    NodeReport nodeReport = new NodeReport(
      nodeDescReport.nodeDescriptor(),
      String.valueOf(0)
    )
      .closeReport();
    Exception exc = null;
    try {
      nodeDescReport.addNodeReport(nodeReport);
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
    NodeDescriptorReport rep1 = new NodeDescriptorReport(SampleNodeRep.Foo);
    NodeDescriptorReport rep2 = new NodeDescriptorReport(SampleNodeRep.Bar);
    assertNotEquals(rep1, rep2, "fail expected inequality");
    // strictly speaking, hashes dont HAVE to differ, but if they dont, its more likely to be an error than chance
    assertNotEquals(
      rep1.hashCode(),
      rep2.hashCode(),
      "fail expected hash diff"
    );
  }

  @Test
  public void inequalityTest_nodeReports() {
    NodeDescriptorReport rep1 = new NodeDescriptorReport(SampleNodeRep.Foo);
    NodeDescriptorReport rep2 = new NodeDescriptorReport(SampleNodeRep.Foo);
    rep1.addNodeReport(
      new NodeReport(SampleNodeRep.Foo, String.valueOf(1)).closeReport()
    );
    rep2.addNodeReport(
      new NodeReport(SampleNodeRep.Foo, String.valueOf(1)).closeReport()
    );
    assertEquals(rep1, rep2, "fail expected equality");
    rep2.addNodeReport(
      new NodeReport(SampleNodeRep.Foo, String.valueOf(2)).closeReport()
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
  public void hashResetTest_nodeReports() {
    NodeDescriptorReport rep = new NodeDescriptorReport(SampleNodeRep.Foo);
    rep.addNodeReport(
      new NodeReport(SampleNodeRep.Foo, String.valueOf(1)).closeReport()
    );
    int origHash = rep.hashCode();
    rep.addNodeReport(
      new NodeReport(SampleNodeRep.Foo, String.valueOf(2)).closeReport()
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
    SampleNodeRep nodeRepo = new SampleNodeRep();
    for (long seed = 0; seed < 20l; seed++) {
      Iterator<NodeDescriptor> nodeDescIter = nodeRepo
        .nodeDescriptors()
        .iterator();
      Random ran = new Random(seed);
      while (nodeDescIter.hasNext()) {
        NodeDescriptor nodeDesc = nodeDescIter.next();
        NodeDescriptorReport rep1 = new NodeDescriptorReport(nodeDesc);
        for (long id = 0; id < 10; id++) {
          NodeReport nodeReport = new NodeReport(nodeDesc, String.valueOf(id));
          smNodeReportTest.randomiseReport(ran, nodeReport);
          rep1.addNodeReport(nodeReport.closeReport());
        }
        NodeDescriptorReportDto dto = rep1.getDto();
        NodeDescriptorReport rep2 = new NodeDescriptorReport(nodeRepo, dto);
        assertEquals(
          rep1,
          rep2,
          String.format(
            "equality fail @ %s, @seed %d",
            nodeDesc.getClass().getSimpleName(),
            seed
          )
        );
        assertEquals(
          rep1.hashCode(),
          rep2.hashCode(),
          String.format(
            "hash fail @ %s, @seed %d",
            nodeDesc.getClass().getSimpleName(),
            seed
          )
        );
      }
    }
  }
}
