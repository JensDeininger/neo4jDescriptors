package org.rle.neo4jdescriptor.entity;

import java.util.stream.Stream;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.annotation.*;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.prop_basic.LongProperty;
import org.rle.neo4jdescriptor.report.NodeReport;
import org.rle.neo4jdescriptor.samples.SampleCreationUtils;
import org.rle.neo4jdescriptor.testutils.*;

public class NodeDescriptorTestProcedure {

  private static final String DELETE_ALL_CYPHER = "MATCH (n) DETACH DELETE n";

  @Context
  public Transaction tx;

  public static class TestProcedureName {

    public static final String Validation_Yay = "validation_Yay";

    public static final String Validation_Nay_Does_Not_Apply_Label =
      "validation_Nay_Does_Not_Apply_Label";

    public static final String Validation_Nay_Does_Not_Apply_Property =
      "validation_Nay_Does_Not_Apply_Property";

    public static final String Validation_Nay_Missing_Label =
      "validation_Nay_Missing_Label";

    public static final String Validation_Nay_Missing_Property =
      "validation_Nay_Missing_Property";

    public static final String Validation_Nay_TooFewRelations =
      "validation_Nay_TooFewRelations";

    public static final String Validation_Nay_TooManyRelations =
      "validation_Nay_TooManyRelations";
  }

  private static class OneOneRelationship extends RelationshipDescriptor {

    public OneOneRelationship() {
      super();
      initTypeAndProperties(OneOneRelationship.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "ONE_ONE"
    );

    @EndNode
    public final TestNode endNode() {
      return smTestNodeDesc;
    }
  }

  private static class TestNode extends NodeDescriptor {

    private NodeRelationOneOne mOneOne;

    public TestNode() {
      super();
      initLabelsAndProperties(TestNode.class);
    }

    @Identifying
    public final LabelDescriptor necLabel = new LabelDescriptor("NecLabel");

    @Validate
    public final LabelDescriptor contLabel = new LabelDescriptor("ContLabel");

    @Identifying
    public final LongProperty necLongProp = new LongProperty(
      "necLongProp",
      "logNecLongProp"
    );

    @Validate
    public final LongProperty optLongProp = new LongProperty(
      "optLongProp",
      "logOptLongProp"
    );

    @Validate
    public final NodeRelationOneOne oneOneNodeRelation() {
      if (mOneOne == null) {
        mOneOne =
          new NodeRelationOneOne(new OneOneRelationship(), Direction.OUTGOING);
      }
      return mOneOne;
    }
  }

  private static TestNode smTestNodeDesc = new TestNode();

  private static RelationshipDescriptor smTestRelationshipDesc = new OneOneRelationship();

  @Procedure(mode = Mode.WRITE, name = TestProcedureName.Validation_Yay)
  public Stream<BoolMessageWrapper> validation_Yay() {
    tx.execute(DELETE_ALL_CYPHER);
    Node node2Check = SampleCreationUtils.createSampleNode(tx, smTestNodeDesc);
    SampleCreationUtils.createSampleRelationship(
      tx,
      node2Check,
      smTestRelationshipDesc,
      SampleCreationUtils.createSampleNode(tx, smTestNodeDesc)
    );
    NodeReport rep = smTestNodeDesc.validate(node2Check);
    return Stream.of(new BoolMessageWrapper(rep.errorCount() == 0));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Nay_Does_Not_Apply_Label
  )
  public Stream<BoolMessageWrapper> validation_Nay_Does_Not_Apply_Label() {
    tx.execute(DELETE_ALL_CYPHER);
    Node node2Check = SampleCreationUtils.createSampleNode(tx, smTestNodeDesc);
    SampleCreationUtils.createSampleRelationship(
      tx,
      node2Check,
      smTestRelationshipDesc,
      SampleCreationUtils.createSampleNode(tx, smTestNodeDesc)
    );
    LabelDescriptor necLabel = smTestNodeDesc
      .labels(Modality.NECESSARY)
      .findFirst()
      .get();
    node2Check.removeLabel(necLabel);

    Exception exc = null;
    try {
      smTestNodeDesc.validate(node2Check);
    } catch (Exception e) {
      exc = e;
    }
    return TestUtilities.checkExceptionReport(
      exc,
      IllegalArgumentException.class,
      NodeDescriptor.M_DESCRIPTOR_DOES_NOT_APPLY
    );
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Nay_Does_Not_Apply_Property
  )
  public Stream<BoolMessageWrapper> validation_Nay_Does_Not_Apply_Property() {
    tx.execute(DELETE_ALL_CYPHER);
    Node node2Check = SampleCreationUtils.createSampleNode(tx, smTestNodeDesc);
    SampleCreationUtils.createSampleRelationship(
      tx,
      node2Check,
      smTestRelationshipDesc,
      SampleCreationUtils.createSampleNode(tx, smTestNodeDesc)
    );
    PropertyDescriptor necProp = smTestNodeDesc
      .properties(Modality.NECESSARY)
      .findFirst()
      .get();
    node2Check.removeProperty(necProp.key());

    Exception exc = null;
    try {
      smTestNodeDesc.validate(node2Check);
    } catch (Exception e) {
      exc = e;
    }
    return TestUtilities.checkExceptionReport(
      exc,
      IllegalArgumentException.class,
      NodeDescriptor.M_DESCRIPTOR_DOES_NOT_APPLY
    );
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Nay_Missing_Label
  )
  public Stream<BoolMessageWrapper> validation_Nay_Missing_Label() {
    tx.execute(DELETE_ALL_CYPHER);
    Node node2Check = SampleCreationUtils.createSampleNode(tx, smTestNodeDesc);
    SampleCreationUtils.createSampleRelationship(
      tx,
      node2Check,
      smTestRelationshipDesc,
      SampleCreationUtils.createSampleNode(tx, smTestNodeDesc)
    );
    LabelDescriptor label = smTestNodeDesc
      .labels(Modality.CONTINGENT)
      .findFirst()
      .get();
    node2Check.removeLabel(label);
    NodeReport report = smTestNodeDesc.validate(node2Check);
    boolean yay =
      report.errorCount() == 1 && report.missingLabels().count() == 1;
    return Stream.of(new BoolMessageWrapper(yay));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Nay_Missing_Property
  )
  public Stream<BoolMessageWrapper> validation_Nay_Missing_Property() {
    tx.execute(DELETE_ALL_CYPHER);
    Node node2Check = SampleCreationUtils.createSampleNode(tx, smTestNodeDesc);
    SampleCreationUtils.createSampleRelationship(
      tx,
      node2Check,
      smTestRelationshipDesc,
      SampleCreationUtils.createSampleNode(tx, smTestNodeDesc)
    );
    PropertyDescriptor prop = smTestNodeDesc
      .properties(Modality.CONTINGENT)
      .findFirst()
      .get();
    node2Check.removeProperty(prop.key());
    NodeReport report = smTestNodeDesc.validate(node2Check);
    boolean yay =
      report.errorCount() == 1 &&
      report.propertyReports().filter(o -> o.errorCount() == 1).count() == 1;
    return Stream.of(new BoolMessageWrapper(yay));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Nay_TooFewRelations
  )
  public Stream<BoolMessageWrapper> validation_Nay_TooFewRelations() {
    tx.execute(DELETE_ALL_CYPHER);
    Node node2Check = SampleCreationUtils.createSampleNode(tx, smTestNodeDesc);
    NodeReport report = smTestNodeDesc.validate(node2Check);
    boolean yay =
      report.errorCount() == 1 &&
      report.nodeRelationReports().filter(o -> o.errorCount() == 1).count() ==
      1;
    return Stream.of(new BoolMessageWrapper(yay));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Nay_TooManyRelations
  )
  public Stream<BoolMessageWrapper> validation_Nay_TooManyRelations() {
    tx.execute(DELETE_ALL_CYPHER);
    Node node2Check = SampleCreationUtils.createSampleNode(tx, smTestNodeDesc);
    SampleCreationUtils.createSampleRelationship(
      tx,
      node2Check,
      smTestRelationshipDesc,
      SampleCreationUtils.createSampleNode(tx, smTestNodeDesc)
    );
    SampleCreationUtils.createSampleRelationship(
      tx,
      node2Check,
      smTestRelationshipDesc,
      SampleCreationUtils.createSampleNode(tx, smTestNodeDesc)
    );
    NodeReport report = smTestNodeDesc.validate(node2Check);
    boolean yay =
      report.errorCount() == 1 &&
      report.nodeRelationReports().filter(o -> o.errorCount() == 1).count() ==
      1;
    return Stream.of(new BoolMessageWrapper(yay));
  }
}
