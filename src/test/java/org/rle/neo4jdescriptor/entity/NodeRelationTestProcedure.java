package org.rle.neo4jdescriptor.entity;

import java.util.Arrays;
import java.util.stream.Stream;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.annotation.Validate;
import org.rle.neo4jdescriptor.report.NodeRelationReport;
import org.rle.neo4jdescriptor.testutils.BoolMessageWrapper;

public class NodeRelationTestProcedure {

  private static final String DELETE_ALL_CYPHER = "MATCH (n) DETACH DELETE n";

  @Context
  public Transaction tx;

  public static class TestProcedureName {

    public static final String Validation_Yay_OneMany =
      "validation_Yay_OneMany";

    public static final String Validation_Yay_OneOne = "validation_Yay_OneOne";

    public static final String Validation_Yay_ZeroMany =
      "validation_Yay_ZeroMany";

    public static final String Validation_Yay_ZeroOne =
      "validation_Yay_ZeroOne";

    public static final String Validation_Fail_OneMany =
      "validation_Fail_OneMany";

    public static final String Validation_Fail_OneOne =
      "validation_Fail_OneOne";

    public static final String Validation_Fail_ZeroOne =
      "validation_Fail_ZeroOne";
  }

  private Node createTestSetup(RelationshipDescriptor relDesc, int count) {
    Node node = tx.createNode(smTestNodeDescriptor.label);
    for (int i = 0; i < count; i++) {
      Node target = tx.createNode(Label.label("Target_" + i));
      node.createRelationshipTo(target, relDesc.type());
    }
    return node;
  }

  public static class OneManyRelationship extends RelationshipDescriptor {

    public OneManyRelationship() {
      super();
      initTypeAndProperties(OneManyRelationship.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "ONE_MANY"
    );
  }

  public static class OneOneRelationship extends RelationshipDescriptor {

    public OneOneRelationship() {
      super();
      initTypeAndProperties(OneOneRelationship.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "ONE_ONE"
    );
  }

  public static class ZeroManyRelationship extends RelationshipDescriptor {

    public ZeroManyRelationship() {
      super();
      initTypeAndProperties(ZeroManyRelationship.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "ZERO_MANY"
    );
  }

  public static class ZeroOneRelationship extends RelationshipDescriptor {

    public ZeroOneRelationship() {
      super();
      initTypeAndProperties(ZeroOneRelationship.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "ZERO_ONE"
    );
  }

  public static class TestNode extends NodeDescriptor {

    private NodeRelationOneMany mOneMany;

    private NodeRelationOneOne mOneOne;

    private NodeRelationZeroMany mZeroMany;

    private NodeRelationZeroOne mZeroOne;

    public TestNode() {
      super();
      initLabelsAndProperties(TestNode.class);
    }

    @Identifying
    public final LabelDescriptor label = new LabelDescriptor("Test");

    @Validate
    public final NodeRelationOneMany oneManyNodeRelation() {
      if (mOneMany == null) {
        mOneMany =
          new NodeRelationOneMany(
            new OneManyRelationship(),
            Direction.OUTGOING
          );
      }
      return mOneMany;
    }

    @Validate
    public final NodeRelationOneOne oneOneNodeRelation() {
      if (mOneOne == null) {
        mOneOne =
          new NodeRelationOneOne(new OneOneRelationship(), Direction.OUTGOING);
      }
      return mOneOne;
    }

    @Validate
    public final NodeRelationZeroMany zeroManyNodeRelation() {
      if (mZeroMany == null) {
        mZeroMany =
          new NodeRelationZeroMany(
            new ZeroManyRelationship(),
            Direction.OUTGOING
          );
      }
      return mZeroMany;
    }

    @Validate
    public final NodeRelationZeroOne zeroOneNodeRelation() {
      if (mZeroOne == null) {
        mZeroOne =
          new NodeRelationZeroOne(
            new ZeroOneRelationship(),
            Direction.OUTGOING
          );
      }
      return mZeroOne;
    }
  }

  public static final TestNode smTestNodeDescriptor = new TestNode();

  // region yay validation

  private Stream<BoolMessageWrapper> condenseYayReports(
    NodeRelationReport... reports
  ) {
    boolean yay = Arrays.stream(reports).allMatch(o -> o.errorCount() == 0);
    return Stream.of(new BoolMessageWrapper(yay));
  }

  @Procedure(mode = Mode.WRITE, name = TestProcedureName.Validation_Yay_OneMany)
  public Stream<BoolMessageWrapper> validation_Yay_OneMany() {
    tx.execute(DELETE_ALL_CYPHER);
    NodeRelation nodeRel = smTestNodeDescriptor.oneManyNodeRelation();
    Node testNodeOne = createTestSetup(nodeRel.relationshipDescriptor(), 1);
    Node testNodeMany = createTestSetup(nodeRel.relationshipDescriptor(), 3);
    NodeRelationReport reportOne = nodeRel.validate(testNodeOne);
    NodeRelationReport reportMany = nodeRel.validate(testNodeMany);
    return condenseYayReports(reportOne, reportMany);
  }

  @Procedure(mode = Mode.WRITE, name = TestProcedureName.Validation_Yay_OneOne)
  public Stream<BoolMessageWrapper> validation_Yay_OneOne() {
    tx.execute(DELETE_ALL_CYPHER);
    NodeRelation nodeRel = smTestNodeDescriptor.oneOneNodeRelation();
    Node testNode = createTestSetup(nodeRel.relationshipDescriptor(), 1);
    NodeRelationReport reportOne = nodeRel.validate(testNode);
    return condenseYayReports(reportOne);
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Yay_ZeroMany
  )
  public Stream<BoolMessageWrapper> validation_Yay_ZeroMany() {
    tx.execute(DELETE_ALL_CYPHER);
    NodeRelation nodeRel = smTestNodeDescriptor.zeroManyNodeRelation();
    Node testNodeZero = createTestSetup(nodeRel.relationshipDescriptor(), 0);
    Node testNodeOne = createTestSetup(nodeRel.relationshipDescriptor(), 1);
    Node testNodeMany = createTestSetup(nodeRel.relationshipDescriptor(), 3);
    NodeRelationReport reportZero = nodeRel.validate(testNodeZero);
    NodeRelationReport reportOne = nodeRel.validate(testNodeOne);
    NodeRelationReport reportMany = nodeRel.validate(testNodeMany);
    return condenseYayReports(reportZero, reportOne, reportMany);
  }

  @Procedure(mode = Mode.WRITE, name = TestProcedureName.Validation_Yay_ZeroOne)
  public Stream<BoolMessageWrapper> validation_Yay_ZeroOne() {
    tx.execute(DELETE_ALL_CYPHER);
    NodeRelation nodeRel = smTestNodeDescriptor.zeroOneNodeRelation();
    Node testNodeZero = createTestSetup(nodeRel.relationshipDescriptor(), 0);
    Node testNodeOne = createTestSetup(nodeRel.relationshipDescriptor(), 1);
    NodeRelationReport reportZero = nodeRel.validate(testNodeZero);
    NodeRelationReport reportOne = nodeRel.validate(testNodeOne);
    return condenseYayReports(reportZero, reportOne);
  }

  // endregion

  // region expected fails tests

  private Stream<BoolMessageWrapper> condenseFailReports(
    NodeRelationReport... reports
  ) {
    boolean allHave1error = Arrays
      .stream(reports)
      .allMatch(o -> o.errorCount() == 1);
    boolean allHaveDeviantCount = Arrays
      .stream(reports)
      .allMatch(o -> o.deviantCount() != null);
    if (allHave1error && allHaveDeviantCount) {
      return Stream.of(new BoolMessageWrapper(true, ""));
    } else {
      return Stream.of(
        new BoolMessageWrapper(false, "no fancy msg, gotta debug anyway")
      );
    }
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Fail_OneMany
  )
  public Stream<BoolMessageWrapper> validation_Fail_OneMany() {
    tx.execute(DELETE_ALL_CYPHER);
    NodeRelation nodeRel = smTestNodeDescriptor.oneManyNodeRelation();
    Node testNodeZero = createTestSetup(nodeRel.relationshipDescriptor(), 0);
    NodeRelationReport reportZero = nodeRel.validate(testNodeZero);
    return condenseFailReports(reportZero);
  }

  @Procedure(mode = Mode.WRITE, name = TestProcedureName.Validation_Fail_OneOne)
  public Stream<BoolMessageWrapper> validation_Fail_OneOne() {
    tx.execute(DELETE_ALL_CYPHER);
    NodeRelation nodeRel = smTestNodeDescriptor.oneOneNodeRelation();
    Node testNodeZero = createTestSetup(nodeRel.relationshipDescriptor(), 0);
    Node testNodeMany = createTestSetup(nodeRel.relationshipDescriptor(), 2);
    NodeRelationReport reportZero = nodeRel.validate(testNodeZero);
    NodeRelationReport reportMany = nodeRel.validate(testNodeMany);
    return condenseFailReports(reportZero, reportMany);
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Fail_ZeroOne
  )
  public Stream<BoolMessageWrapper> validation_Fail_ZeroOne() {
    tx.execute(DELETE_ALL_CYPHER);
    NodeRelation nodeRel = smTestNodeDescriptor.zeroOneNodeRelation();
    Node testNodeMany = createTestSetup(nodeRel.relationshipDescriptor(), 2);
    NodeRelationReport reportMany = nodeRel.validate(testNodeMany);
    return condenseFailReports(reportMany);
  }
  //endregion
}
