package org.rle.neo4jdescriptor.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.dto.report.NodeReportDto;
import org.rle.neo4jdescriptor.entity.*;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.samples.SampleNodeRep;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

@java.lang.SuppressWarnings("java:S2699") // complains about missing asserts in the methods that simply call the super method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NodeReportTest extends EntityReportTest<NodeReport> {

  private static final NodeRelationReportTest smNodeRelationReportTest = new NodeRelationReportTest();

  @Override
  protected NodeReport createSampleReport(Random ran) {
    NodeReport[] nodeReports = createEmptyReports();
    NodeReport report = nodeReports[ran.nextInt(nodeReports.length)];
    randomiseReport(ran, report);
    return report;
  }

  @Override
  protected NodeReport[] createEmptyReports() {
    NodeDescriptor[] nodeDescriptors = new NodeDescriptor[] {
      SampleNodeRep.Foo,
      SampleNodeRep.Bar,
      SampleNodeRep.Keks,
    };
    ArrayList<NodeReport> reportList = new ArrayList<>();
    for (NodeDescriptor nd : nodeDescriptors) {
      NodeReport nodeRep = new NodeReport(nd, String.valueOf(42l));
      reportList.add(nodeRep);
    }
    return reportList.toArray(NodeReport[]::new);
  }

  @Override
  protected void randomiseReport(Random ran, NodeReport nodeReport) {
    super.randomiseReport(ran, nodeReport);
    NodeDescriptor nodeDesc = nodeReport.nodeDescriptor();
    Iterator<NodeRelation> nodeRelIter = nodeDesc.nodeRelations().iterator();
    while (nodeRelIter.hasNext()) {
      NodeRelation nodeRel = nodeRelIter.next();
      NodeRelationReport nodeRelReport = new NodeRelationReport(nodeRel);
      smNodeRelationReportTest.randomiseReport(ran, nodeRelReport);
      nodeReport.addNodeRelationReport(nodeRelReport.closeReport());
    }
    Iterator<LabelDescriptor> labelIter = nodeDesc
      .labels(Modality.CONTINGENT)
      .iterator();
    while (labelIter.hasNext()) {
      if (ran.nextDouble() < 0.5) {
        nodeReport.addMissingLabel(labelIter.next());
      }
    }
  }

  private NodeRelationReport[] openNodeRelationReports(
    NodeDescriptor nodeDesc
  ) {
    return nodeDesc
      .nodeRelations()
      .map(o ->
        new NodeRelationReport(o).setCountCheck(false).setDeviantCount(23)
      )
      .toArray(NodeRelationReport[]::new);
  }

  private NodeRelationReport openEmptyNodeRelationReport(
    NodeDescriptor nodeDesc
  ) {
    Optional<NodeRelation> nodeRel = nodeDesc.nodeRelations().findFirst();
    if (nodeRel.isEmpty()) {
      return null;
    }
    return new NodeRelationReport(nodeRel.get());
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
  public void addMissingLabelsTest_yay() {
    NodeReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      NodeReport report = reports[i];
      assertEquals(0, report.missingLabels().count(), "fail 0a @index " + i);
      assertEquals(0, report.errorCount(), "fail 0b @index " + i);
      Optional<LabelDescriptor> lblDesc = report
        .nodeDescriptor()
        .labels(Modality.CONTINGENT)
        .findFirst();
      if (lblDesc.isEmpty()) {
        continue;
      }
      report.addMissingLabel(lblDesc.get());
      assertEquals(1, report.missingLabels().count(), "fail 1 @index " + i);
      assertEquals(1, report.errorCount(), "fail 1b @index " + i);
    }
  }

  @Test
  public void addMissingLabelsTest_wrong() {
    NodeReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      NodeReport report = reports[i];
      Exception exc = null;
      try {
        report.addMissingLabel(new LabelDescriptor("kjghjghfjr"));
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalArgumentException.class,
        NodeReport.NO_SUCH_LABEL
      );
    }
  }

  @Test
  public void addNodeRelationReportTest_yay() {
    NodeReport[] nodeReports = createEmptyReports();
    for (int i = 0; i < nodeReports.length; i++) {
      NodeReport nodeRep = nodeReports[i];
      NodeDescriptor nodeDesc = nodeRep.nodeDescriptor();
      NodeRelationReport[] nodeRelationReports = openNodeRelationReports(
        nodeDesc
      );
      for (NodeRelationReport nodeRelationReport : nodeRelationReports) {
        nodeRep.addNodeRelationReport(nodeRelationReport.closeReport());
      }
      assertEquals(
        nodeRelationReports.length,
        nodeRep.errorCount(),
        "fail 1 @ index " + i
      );
      assertEquals(
        nodeRelationReports.length,
        nodeRep.nodeRelationReports().count(),
        "fail 2 @ index " + i
      );
    }
  }

  @Test
  public void addNodeRelationReportTest_wrong() {
    RelationshipDescriptor[] relationships = new SampleRelationshipRep()
      .relationshipDescriptors()
      .toArray(RelationshipDescriptor[]::new);
    NodeReport[] nodeReports = createEmptyReports();
    for (int i = 0; i < nodeReports.length; i++) {
      // find a relationship that is NOT involved in the NodeDescriptor
      NodeReport nodeRep = nodeReports[i];
      NodeDescriptor nodeDesc = nodeRep.nodeDescriptor();
      Set<RelationshipDescriptor> declaredRelations = nodeDesc
        .nodeRelations()
        .map(o -> o.relationshipDescriptor())
        .collect(Collectors.toSet());
      RelationshipDescriptor relDesc = Arrays
        .stream(relationships)
        .filter(o -> !declaredRelations.contains(o))
        .findFirst()
        .get();
      NodeRelationReport nodeRelRep = new NodeRelationReport(
        new NodeRelationOneOne(relDesc, Direction.INCOMING)
      )
        .setCountCheck(false)
        .closeReport();
      Exception exc = null;
      try {
        nodeRep.addNodeRelationReport(nodeRelRep);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalArgumentException.class,
        NodeReport.NO_SUCH_NODE_RELATION,
        i
      );
    }
  }

  @Test
  public void addNodeRelationReportTest_duplicate() {
    NodeRelationReport nodeRelRep1 = openEmptyNodeRelationReport(
      SampleNodeRep.Foo
    )
      .setCountCheck(false)
      .closeReport();
    NodeRelationReport nodeRelRep2 = openEmptyNodeRelationReport(
      SampleNodeRep.Foo
    )
      .setCountCheck(false)
      .setDeviantCount(22)
      .closeReport();
    NodeReport nodeRep = new NodeReport(SampleNodeRep.Foo, String.valueOf(23l));
    nodeRep.addNodeRelationReport(nodeRelRep1);
    Exception exc = null;
    try {
      nodeRep.addNodeRelationReport(nodeRelRep2);
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalArgumentException.class,
      NodeReport.DUPLICATE_NODE_RELATION_REPORT
    );
  }

  @Test
  public void addNodeRelationReportTest_openReport() {
    NodeRelationReport nodeRelRep = openEmptyNodeRelationReport(
      SampleNodeRep.Foo
    );
    nodeRelRep.setCountCheck(false);
    NodeReport nodeRep = new NodeReport(SampleNodeRep.Foo, String.valueOf(23l));
    Exception exc = null;
    try {
      nodeRep.addNodeRelationReport(nodeRelRep);
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
  @Override
  public void closedTest_properties() {
    super.closedTest_properties();
  }

  @Test
  public void closedTest_missingLabels() {
    NodeReport nodeRep = createEmptyReports()[0].closeReport();
    LabelDescriptor lbl = nodeRep
      .nodeDescriptor()
      .labels(Modality.CONTINGENT)
      .findFirst()
      .get();
    Exception exc = null;
    try {
      nodeRep.addMissingLabel(lbl);
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
  public void closedTest_nodeRelation() {
    NodeReport nodeRep = createEmptyReports()[0].closeReport();
    NodeRelationReport nodeRelRep = openEmptyNodeRelationReport(
      nodeRep.nodeDescriptor()
    )
      .setCountCheck(false)
      .closeReport();
    Exception exc = null;
    try {
      nodeRep.addNodeRelationReport(nodeRelRep);
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
  @Override
  public void inequalityTest_properties() {
    super.inequalityTest_properties();
  }

  @Test
  public void inequalityTest_nodeDescriptor() {
    NodeReport rep1 = new NodeReport(SampleNodeRep.Foo, String.valueOf(0));
    NodeReport rep2 = new NodeReport(SampleNodeRep.Bar, String.valueOf(0));
    assertNotEquals(rep1, rep2);
    // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
    assertNotEquals(rep1.hashCode(), rep2.hashCode());
  }

  @Test
  public void inequalityTest_nodeId() {
    NodeReport rep1 = new NodeReport(SampleNodeRep.Foo, String.valueOf(1));
    NodeReport rep2 = new NodeReport(SampleNodeRep.Foo, String.valueOf(2));
    assertNotEquals(rep1, rep2);
    // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
    assertNotEquals(rep1.hashCode(), rep2.hashCode());
  }

  @Test
  public void inequalityTest_missingLabels() {
    NodeReport[] reports1 = createEmptyReports();
    NodeReport[] reports2 = createEmptyReports();
    for (int i = 0; i < reports1.length; i++) {
      NodeReport rep1 = reports1[i];
      NodeReport rep2 = reports2[i];
      Optional<LabelDescriptor> lbl = rep1
        .nodeDescriptor()
        .labels(Modality.CONTINGENT)
        .findFirst();
      if (lbl.isEmpty()) {
        continue;
      }
      rep1.addMissingLabel(lbl.get());
      assertNotEquals(rep1, rep2);
      // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
      assertNotEquals(rep1.hashCode(), rep2.hashCode());
    }
  }

  @Test
  public void inequalityTest_nodeRelation() {
    NodeReport[] reports1 = createEmptyReports();
    NodeReport[] reports2 = createEmptyReports();
    for (int i = 0; i < reports1.length; i++) {
      NodeReport rep1 = reports1[i];
      NodeReport rep2 = reports2[i];
      NodeRelationReport nodeRelRep = openEmptyNodeRelationReport(
        rep1.nodeDescriptor()
      );
      if (nodeRelRep == null) {
        continue;
      }
      nodeRelRep.setCountCheck(false).closeReport();
      rep1.addNodeRelationReport(nodeRelRep);
      assertNotEquals(rep1, rep2);
      // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
      assertNotEquals(rep1.hashCode(), rep2.hashCode());
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
  @Override
  public void hashResetTest_properties() {
    super.equalityTest();
  }

  @Test
  public void hashResetTest_missingLabels() {
    NodeReport[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      NodeReport rep = reports[i];
      Optional<LabelDescriptor> lbl = rep
        .nodeDescriptor()
        .labels(Modality.CONTINGENT)
        .findFirst();
      if (lbl.isEmpty()) {
        continue;
      }
      int origHash = rep.hashCode();
      rep.addMissingLabel(lbl.get());
      // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
      assertNotEquals(origHash, rep.hashCode());
    }
  }

  @Test
  public void hashResetTest_nodeRelation() {
    NodeReport report = createEmptyReports()[0];
    int origHash = report.hashCode();
    NodeRelationReport nodeRelRep = openEmptyNodeRelationReport(
      report.nodeDescriptor()
    )
      .closeReport();
    report.addNodeRelationReport(nodeRelRep);
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
  public void printTest() {
    NodeReport[] nodeReports = createEmptyReports();
    for (int i = 0; i < nodeReports.length; i++) {
      NodeReport nodeRep = nodeReports[i];
      nodeRep
        .nodeDescriptor()
        .labels(Modality.CONTINGENT)
        .forEach(o -> nodeRep.addMissingLabel(o));
      PropertyDescriptor[] props = nodeRep
        .nodeDescriptor()
        .properties(Modality.CONTINGENT)
        .toArray(PropertyDescriptor[]::new);
      for (PropertyDescriptor p : props) {
        PropertyReport propRep = new PropertyReport(p);
        propRep.setKeyCheck(false).closeReport();
        nodeRep.addPropertyReport(propRep);
      }
      NodeRelationReport[] nodeRelReps = openNodeRelationReports(
        nodeRep.nodeDescriptor()
      );
      for (NodeRelationReport nodeRelationReport : nodeRelReps) {
        nodeRelationReport.closeReport();
        nodeRep.addNodeRelationReport(nodeRelationReport);
      }
      String gI = "---";
      String sI = "..";
      String str = nodeRep.print(gI, sI);
      int expectedLineCount = 1; // header
      if (nodeRep.missingLabels().count() > 0) {
        expectedLineCount++;
      }
      if (nodeRep.propertyReportCount() > 0) {
        expectedLineCount++;
      }
      if (nodeRep.nodeRelationReports().count() > 0) {
        expectedLineCount++;
      }
      expectedLineCount += nodeRep.errorCount();
      assertEquals(
        expectedLineCount,
        str.split(System.lineSeparator()).length,
        "fail @ index " + i
      );
    }
  }

  @Test
  public void report2DtoRoundRobinTest() {
    SampleNodeRep nodeRepo = new SampleNodeRep();
    for (long seed = 0; seed < 20l; seed++) {
      Iterator<NodeDescriptor> nodeIter = nodeRepo.nodeDescriptors().iterator();
      Random ran = new Random(seed);
      while (nodeIter.hasNext()) {
        NodeDescriptor nodeDesc = nodeIter.next();
        NodeReport rep1 = new NodeReport(nodeDesc, String.valueOf(0));
        randomiseReport(ran, rep1);
        NodeReportDto dto = rep1.getDto();
        NodeReport rep2 = new NodeReport(nodeRepo, dto);
        assertEquals(
          rep1,
          rep2,
          "Fail equality " + nodeDesc.getClass().getSimpleName()
        );
        assertEquals(
          rep1.hashCode(),
          rep2.hashCode(),
          "Fail hash " + nodeDesc.getClass().getSimpleName()
        );
      }
    }
  }
}
