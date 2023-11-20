package org.rle.neo4jdescriptor.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.dto.report.NodeRelationReportDto;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;
import org.rle.neo4jdescriptor.entity.NodeRelation;
import org.rle.neo4jdescriptor.entity.NodeRelationOneMany;
import org.rle.neo4jdescriptor.entity.NodeRelationOneOne;
import org.rle.neo4jdescriptor.entity.NodeRelationZeroMany;
import org.rle.neo4jdescriptor.entity.NodeRelationZeroOne;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.samples.SampleNodeRep;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

@java.lang.SuppressWarnings("java:S2699")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NodeRelationReportTest extends ReportBaseTest<NodeRelationReport> {

  @Override
  protected NodeRelationReport createSampleReport(Random ran) {
    NodeRelationReport[] samples = createEmptyReports();
    NodeRelationReport rep = samples[ran.nextInt(samples.length)];
    randomiseReport(ran, rep);
    return rep;
  }

  @Override
  protected NodeRelationReport[] createEmptyReports() {
    RelationshipDescriptor[] relDescs = new SampleRelationshipRep()
      .relationshipDescriptors()
      .toArray(RelationshipDescriptor[]::new);
    Direction[] dirs = new Direction[] {
      Direction.INCOMING,
      Direction.OUTGOING,
    };
    ArrayList<NodeRelationReport> reps = new ArrayList<>();
    for (RelationshipDescriptor rd : relDescs) {
      for (Direction dir : dirs) {
        reps.add(new NodeRelationReport(new NodeRelationOneOne(rd, dir)));
        reps.add(new NodeRelationReport(new NodeRelationOneMany(rd, dir)));
        reps.add(new NodeRelationReport(new NodeRelationZeroOne(rd, dir)));
        reps.add(new NodeRelationReport(new NodeRelationZeroMany(rd, dir)));
      }
    }
    return reps.toArray(NodeRelationReport[]::new);
  }

  protected void randomiseReport(Random ran, NodeRelationReport nodeRelReport) {
    super.randomiseReport(ran, nodeRelReport);
    Boolean b = randomBool(ran);
    if (b != null) {
      nodeRelReport.setCountCheck(b);
    }
    if (ran.nextDouble() < 0.5) {
      nodeRelReport.setDeviantCount(ran.nextInt(123));
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
  public void countCheckTest() {
    NodeRelationReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      NodeRelationReport propRep = reports[i];
      propRep.setCountCheck(true);
      assertTrue(propRep.checkResultCount(), "fail true @index " + i);
      propRep.setCountCheck(false);
      assertFalse(propRep.checkResultCount(), "fail false @index " + i);
    }
  }

  @Test
  public void setDeviantCountTest() {
    NodeRelationReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      NodeRelationReport propRep = reports[i];
      propRep.setDeviantCount(23);
      assertEquals(23, propRep.deviantCount(), "fail @index " + i);
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
  public void closedTest_setCountCheck() {
    NodeRelationReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      NodeRelationReport propRep = reports[i];
      propRep.closeReport();
      Exception exc = null;
      try {
        propRep.setCountCheck(true);
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
  public void closedTest_setDeviantCount() {
    NodeRelationReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      NodeRelationReport propRep = reports[i];
      propRep.closeReport();
      Exception exc = null;
      try {
        propRep.setDeviantCount(23);
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
  public void inequalityTest_countCheck() {
    NodeRelationReport[] reports1 = createEmptyReports();
    NodeRelationReport[] reports2 = createEmptyReports();
    for (int i = 0; i < reports1.length; i++) {
      NodeRelationReport rep1 = reports1[i];
      NodeRelationReport rep2 = reports2[i];
      rep1.setCountCheck(true);
      rep2.setCountCheck(false);
      assertNotEquals(rep1, rep2);
    }
  }

  @Test
  public void inequalityTest_deviantCount() {
    NodeRelationReport[] reports1 = createEmptyReports();
    NodeRelationReport[] reports2 = createEmptyReports();
    for (int i = 0; i < reports1.length; i++) {
      NodeRelationReport rep1 = reports1[i];
      NodeRelationReport rep2 = reports2[i];
      rep1.setDeviantCount(23);
      rep2.setDeviantCount(42);
      assertNotEquals(rep1, rep2);
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
  public void hashResetTest_countCheck() {
    NodeRelationReport report = createEmptyReports()[0];
    report.setCountCheck(false);
    int origHash = report.hashCode();
    report.setCountCheck(true);
    // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
    assertNotEquals(origHash, report.hashCode());
  }

  @Test
  public void hashResetTest_deviantCount() {
    NodeRelationReport report = createEmptyReports()[0];
    report.setDeviantCount(23);
    int origHash = report.hashCode();
    report.setDeviantCount(232);
    // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
    assertNotEquals(origHash, report.hashCode());
  }

  // endregion

  @Test
  @Override
  public void printExceptionsTest() {
    super.printExceptionsTest();
  }

  @Test
  public void printProblemsTest() {
    NodeRelationReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      NodeRelationReport rep = reports[i];
      rep.setCountCheck(false);
      rep.setDeviantCount(23);
      String gI = "ggg";
      String printMsg = rep.print(gI, "ss");
      String expectedStr = String.format(
        "%s%s: %s. Found: 23",
        gI,
        NodeRelationReport.EXPECTED_RELATIONS_FAIL_MSG,
        rep.nodeRelation().print("")
      );
      assertEquals(printMsg, expectedStr, "fail @index " + i);
    }
  }

  @Test
  public void report2DtoRoundRobinTest() {
    SampleNodeRep nodeRepo = new SampleNodeRep();
    Iterator<NodeDescriptor> nodeDescIter = nodeRepo
      .nodeDescriptors()
      .iterator();
    while (nodeDescIter.hasNext()) {
      NodeDescriptor nodeDesc = nodeDescIter.next();
      Iterator<NodeRelation> nodeRelIter = nodeDesc.nodeRelations().iterator();
      for (long seed = 0; seed < 20l; seed++) {
        Random ran = new Random(seed);
        while (nodeRelIter.hasNext()) {
          NodeRelation nodeRel = nodeRelIter.next();
          NodeRelationReport rep1 = new NodeRelationReport(nodeRel);
          randomiseReport(ran, rep1);
          NodeRelationReportDto dto = rep1.getDto();
          NodeRelationReport rep2 = new NodeRelationReport(nodeDesc, dto);
          assertEquals(
            rep1,
            rep2,
            String.format(
              "equality fail @ %s.%s @seed %d",
              nodeDesc.getClass().getSimpleName(),
              nodeRel.relationshipDescriptor().getClass().getSimpleName(),
              seed
            )
          );
          assertEquals(
            rep1.hashCode(),
            rep2.hashCode(),
            String.format(
              "hash fail @ %s.%s  @seed %d",
              nodeDesc.getClass().getSimpleName(),
              nodeRel.relationshipDescriptor().getClass().getSimpleName(),
              seed
            )
          );
        }
      }
    }
  }
}
