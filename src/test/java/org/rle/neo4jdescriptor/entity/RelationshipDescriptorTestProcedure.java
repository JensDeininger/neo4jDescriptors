package org.rle.neo4jdescriptor.entity;

import java.util.stream.Stream;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.annotation.*;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.prop_basic.*;
import org.rle.neo4jdescriptor.report.RelationshipReport;
import org.rle.neo4jdescriptor.samples.SampleCreationUtils;
import org.rle.neo4jdescriptor.testutils.BoolMessageWrapper;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

public class RelationshipDescriptorTestProcedure {

  private static final String DELETE_ALL_CYPHER = "MATCH (n) DETACH DELETE n";

  @Context
  public Transaction tx;

  public static class TestProcedureName {

    public static final String Validation_Yay_TypeOnly =
      "validationYayTypeOnly";

    public static final String Validation_Yay_Props = "validationYayProps";

    public static final String Validation_Yay_Start = "validationYayStart";

    public static final String Validation_Yay_End = "validationYayEnd";

    public static final String Validation_Yay_Both = "validationYayBoth";

    public static final String Validation_Nay_Does_Not_Apply =
      "validationNayNoApply";

    public static final String Validation_Nay_PropFail =
      "validationNayPropFail";

    public static final String Validation_Nay_StartFail =
      "validationNayStartFail";

    public static final String Validation_Nay_EndFail = "validationNayEndFail";

    public static final String Validation_Nay_BothFail =
      "validationNayBothFail";
  }

  private static final StartNodeDesc smStartNodeDesc = new StartNodeDesc();

  private static final EndNodeDesc smEndNodeDesc = new EndNodeDesc();

  private static final TypeOnlyRelationship smTypeRel = new TypeOnlyRelationship();

  private static final PropsRelationship smPropsRel = new PropsRelationship();

  private static final StartNodeRelationship smStartRel = new StartNodeRelationship();

  private static final EndNodeRelationship smEndRel = new EndNodeRelationship();

  private static final BothNodeRelationship smBothRel = new BothNodeRelationship();

  private Relationship createTestSetup(
    NodeDescriptor startNodeDesc,
    RelationshipDescriptor relDesc,
    NodeDescriptor endNodeDesc
  ) {
    Node startNode = SampleCreationUtils.createSampleNode(tx, startNodeDesc);
    Node endNode = SampleCreationUtils.createSampleNode(tx, endNodeDesc);
    Relationship rel = SampleCreationUtils.createSampleRelationship(
      tx,
      startNode,
      relDesc,
      endNode
    );
    return rel;
  }

  // region Dummy Descriptor classes

  /* Test Classes:
      
                          TypeOnlyRelationship 
                                  ↑
                  ┌──────  PropsRelationship  ←──┐
                  |               ↑              |
     StartNodeRelationship EndNodeRelationship BothNodeRelationship
   */

  public static class StartNodeDesc extends NodeDescriptor {

    public StartNodeDesc() {
      super();
      initLabelsAndProperties(StartNodeDesc.class);
    }

    @Identifying
    public final LabelDescriptor label = new LabelDescriptor("Start");
  }

  public static class EndNodeDesc extends NodeDescriptor {

    public EndNodeDesc() {
      super();
      initLabelsAndProperties(EndNodeDesc.class);
    }

    @Identifying
    public final LabelDescriptor label = new LabelDescriptor("End");
  }

  public static class TypeOnlyRelationship extends RelationshipDescriptor {

    public TypeOnlyRelationship() {
      super();
      initTypeAndProperties(TypeOnlyRelationship.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "DUMMY_REL"
    );
  }

  public static class PropsRelationship extends TypeOnlyRelationship {

    public PropsRelationship() {
      super();
      initTypeAndProperties(PropsRelationship.class);
    }

    @Identifying
    public final StringProperty necessaryStringProp = new StringProperty(
      "stringProp",
      "log_stringProp"
    );

    @Validate
    public final LongProperty longProp = new LongProperty(
      "longsProp",
      "log_longsProp"
    );
  }

  public static class StartNodeRelationship extends PropsRelationship {

    private final StartNodeDesc mStartNodeDesc = new StartNodeDesc();

    public StartNodeRelationship() {
      super();
      initTypeAndProperties(StartNodeRelationship.class);
    }

    @StartNode
    public final StartNodeDesc startNode() {
      return mStartNodeDesc;
    }
  }

  public static class EndNodeRelationship extends PropsRelationship {

    private final EndNodeDesc mEndNodeDesc = new EndNodeDesc();

    public EndNodeRelationship() {
      super();
      initTypeAndProperties(EndNodeRelationship.class);
    }

    @EndNode
    public final EndNodeDesc endNode() {
      return mEndNodeDesc;
    }
  }

  public static class BothNodeRelationship extends PropsRelationship {

    private final StartNodeDesc mStartNodeDesc = new StartNodeDesc();

    private final EndNodeDesc mEndNodeDesc = new EndNodeDesc();

    public BothNodeRelationship() {
      super();
      initTypeAndProperties(BothNodeRelationship.class);
    }

    @StartNode
    public final StartNodeDesc startNode() {
      return mStartNodeDesc;
    }

    @EndNode
    public final EndNodeDesc endNode() {
      return mEndNodeDesc;
    }
  }

  // endregion

  // region yay validation

  private Stream<BoolMessageWrapper> validationYay(
    RelationshipDescriptor relDesc
  ) {
    tx.execute(DELETE_ALL_CYPHER);
    Relationship rel = createTestSetup(smStartNodeDesc, relDesc, smEndNodeDesc);
    RelationshipReport rep = relDesc.validate(rel);
    return Stream.of(new BoolMessageWrapper(rep.errorCount() == 0));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Yay_TypeOnly
  )
  public Stream<BoolMessageWrapper> validationYayTypeOnly() {
    return validationYay(smTypeRel);
  }

  @Procedure(mode = Mode.WRITE, name = TestProcedureName.Validation_Yay_Props)
  public Stream<BoolMessageWrapper> validationYayProps() {
    return validationYay(smPropsRel);
  }

  @Procedure(mode = Mode.WRITE, name = TestProcedureName.Validation_Yay_Start)
  public Stream<BoolMessageWrapper> validationYayStart() {
    return validationYay(smStartRel);
  }

  @Procedure(mode = Mode.WRITE, name = TestProcedureName.Validation_Yay_End)
  public Stream<BoolMessageWrapper> validationYayEnd() {
    return validationYay(smEndRel);
  }

  @Procedure(mode = Mode.WRITE, name = TestProcedureName.Validation_Yay_Both)
  public Stream<BoolMessageWrapper> validationYayBoth() {
    return validationYay(smBothRel);
  }

  // endregion

  // region expected validation fail tests

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Nay_Does_Not_Apply
  )
  public Stream<BoolMessageWrapper> validationNayNoApply() {
    Node node1 = tx.createNode();
    Node node2 = tx.createNode();
    Relationship rel = node1.createRelationshipTo(
      node2,
      new RelationshipTypeDescriptor("WRONG")
    );
    Exception exc = null;
    try {
      smTypeRel.validate(rel);
    } catch (Exception e) {
      exc = e;
    }
    return TestUtilities.checkExceptionReport(
      exc,
      IllegalArgumentException.class,
      RelationshipDescriptor.M_DESCRIPTOR_DOES_NOT_APPLY
    );
  }

  private Stream<BoolMessageWrapper> validationNay(
    RelationshipDescriptor relDesc,
    Relationship rel,
    int expectedErrorCount
  ) {
    RelationshipReport rep = relDesc.validate(rel);
    return Stream.of(
      new BoolMessageWrapper(rep.errorCount() == expectedErrorCount)
    );
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Nay_PropFail
  )
  public Stream<BoolMessageWrapper> validationNayPropFail() {
    Relationship rel = createTestSetup(
      smStartNodeDesc,
      smPropsRel,
      smEndNodeDesc
    );
    PropertyDescriptor contProp = smPropsRel
      .properties(Modality.CONTINGENT)
      .findFirst()
      .get();
    rel.removeProperty(contProp.key());
    return validationNay(smPropsRel, rel, 1);
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Nay_StartFail
  )
  public Stream<BoolMessageWrapper> validationNayStartFail() {
    Relationship rel = createTestSetup(
      smEndNodeDesc,
      smStartRel,
      smEndNodeDesc
    );
    return validationNay(smStartRel, rel, 1);
  }

  @Procedure(mode = Mode.WRITE, name = TestProcedureName.Validation_Nay_EndFail)
  public Stream<BoolMessageWrapper> validationNayEndFail() {
    Relationship rel = createTestSetup(
      smStartNodeDesc,
      smEndRel,
      smStartNodeDesc
    );
    return validationNay(smEndRel, rel, 1);
  }

  @Procedure(
    mode = Mode.WRITE,
    name = TestProcedureName.Validation_Nay_BothFail
  )
  public Stream<BoolMessageWrapper> validationNayBothFail() {
    Relationship rel = createTestSetup(
      smEndNodeDesc,
      smBothRel,
      smStartNodeDesc
    );
    return validationNay(smBothRel, rel, 2);
  }
  // endregion

}
