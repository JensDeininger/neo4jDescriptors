package org.rle.neo4jdescriptor.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.annotation.*;
import org.rle.neo4jdescriptor.property.prop_basic.*;
import org.rle.neo4jdescriptor.samples.*;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RelationshipDescriptorTest extends EntityDescriptorTest {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(RelationshipDescriptorTestProcedure.class);
  }

  @Override
  protected String initialCypher() {
    return null;
  }

  // region inherited ctor config test

  /* Tests here:
   * - it must be possible to have some more necessary and optional properties in derived RelationshipDescriptors
   * - it must NOT be possible to override properties already declared in the base class
   * - it must be possible to have a Start- or EndNode in the derived class where the base class has none
   * - it must NOT be possible to have a Start- or EndNode in the derived class where the base class already has that
   */

  public class StartNodeDesc extends NodeDescriptor {

    public StartNodeDesc() {
      super();
      initLabelsAndProperties(StartNodeDesc.class);
    }

    @Identifying
    public final LabelDescriptor label = new LabelDescriptor("Start");
  }

  public class EndNodeDesc extends NodeDescriptor {

    public EndNodeDesc() {
      super();
      initLabelsAndProperties(EndNodeDesc.class);
    }

    @Identifying
    public final LabelDescriptor label = new LabelDescriptor("End");
  }

  public class BaseRelationship extends RelationshipDescriptor {

    private final StartNodeDesc mStartNodeDesc = new StartNodeDesc();

    public BaseRelationship() {
      super();
      initTypeAndProperties(BaseRelationship.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "DUMMY_REL"
    );

    @Identifying
    public final StringProperty necessaryBaseProp = new StringProperty(
      "necBaseProp",
      "log_necBaseProp"
    );

    @Validate
    public final StringProperty optionalBaseProp = new StringProperty(
      "optBaseProp",
      "log_optBaseProp"
    );

    @StartNode
    public final StartNodeDesc startNode() {
      return mStartNodeDesc;
    }
  }

  public class SpecialRelationship extends BaseRelationship {

    private EndNodeDesc mEndNodeDesc = new EndNodeDesc();

    public SpecialRelationship() {
      super();
      initTypeAndProperties(SpecialRelationship.class);
    }

    @Identifying
    public final StringProperty necessarySpecialProp = new StringProperty(
      "necSpecialProp",
      "log_necSpecialProp"
    );

    @Validate
    public final StringProperty optionalSpecialProp = new StringProperty(
      "optSpecialProp",
      "log_optSpecialProp"
    );

    @EndNode
    public final EndNodeDesc endNode() {
      return mEndNodeDesc;
    }
  }

  public class OverwriteTypeRelationship extends BaseRelationship {

    public OverwriteTypeRelationship() {
      super();
      initTypeAndProperties(OverwriteTypeRelationship.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "DUMMY_REL"
    );
  }

  public class OverwriteStartNodeRelationship extends BaseRelationship {

    private StartNodeDesc mStartNodeDesc = new StartNodeDesc();

    public OverwriteStartNodeRelationship() {
      super();
      initTypeAndProperties(OverwriteStartNodeRelationship.class);
    }

    @StartNode
    public final StartNodeDesc startNode2() {
      return mStartNodeDesc;
    }
  }

  public class OverwriteEndNodeRelationship extends SpecialRelationship {

    private EndNodeDesc mEndNodeDesc = new EndNodeDesc();

    public OverwriteEndNodeRelationship() {
      super();
      initTypeAndProperties(OverwriteEndNodeRelationship.class);
    }

    @EndNode
    public final EndNodeDesc endNode2() {
      return mEndNodeDesc;
    }
  }

  public class OverwriteIdPropRelationship extends BaseRelationship {

    public OverwriteIdPropRelationship() {
      super();
      initTypeAndProperties(OverwriteIdPropRelationship.class);
    }

    @Identifying
    public final StringProperty prop1 = new StringProperty(
      "necBaseProp",
      "log_necBaseProp"
    );
  }

  public class OverwriteValPropRelationship extends BaseRelationship {

    public OverwriteValPropRelationship() {
      super();
      initTypeAndProperties(OverwriteValPropRelationship.class);
    }

    @Validate
    public final StringProperty prop2 = new StringProperty(
      "optBaseProp",
      "log_optBaseProp"
    );
  }

  @Test
  public void basicInheritanceTest() {
    SpecialRelationship specRel = new SpecialRelationship();
    assertEquals(4, specRel.properties(Modality.BOTH).count(), "fail props");
    assertEquals(new StartNodeDesc(), specRel.startNode(), "fail startnode");
    assertEquals(new EndNodeDesc(), specRel.endNode(), "fail endnode");
  }

  @Test
  public void overwriteTypeTest() {
    faultyDeclarationTest(
      this,
      OverwriteTypeRelationship.class,
      RelationshipDescriptor.M_MUST_DEFINE_UNIQUE_RELATIONTYPE
    );
  }

  @Test
  public void overwriteStartNodeTest() {
    // I can not use the faultyDeclarationTest like in other tests here because
    // the exception comes not in the ctor but by forcing the nodeInit
    // And I cannot do the nodeInit in the ctor, because that would call
    // init methods in the NodeDescriptors and they might call init methods
    // in RelationshipDescriptors and having a bunch of init methods calling each other
    // in a merry roundabound with possible loops and half of these instances
    // stuck in partial initialisation is just a nightmare.
    Exception exc = null;
    try {
      OverwriteStartNodeRelationship rel = new OverwriteStartNodeRelationship();
      rel.startNodeDescriptor();
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalStateException.class,
      RelationshipDescriptor.M_ONLY_ONE_NODE_EACH
    );
  }

  @Test
  public void overwriteEndNodeTest() {
    // I can not use the faultyDeclarationTest like in other tests here because
    // the exception comes not in the ctor but by forcing the nodeInit
    // And I cannot do the nodeInit in the ctor, because that would call
    // init methods in the NodeDescriptors and they might call init methods
    // in RelationshipDescriptors and having a bunch of init methods calling each other
    // in a merry roundabound with possible loops and half of these instances
    // stuck in partial initialisation is just a nightmare.
    Exception exc = null;
    try {
      OverwriteEndNodeRelationship rel = new OverwriteEndNodeRelationship();
      rel.endNodeDescriptor();
    } catch (Exception e) {
      exc = e;
    }
    TestUtilities.checkException(
      exc,
      IllegalStateException.class,
      RelationshipDescriptor.M_ONLY_ONE_NODE_EACH
    );
  }

  @Test
  public void overwriteIdPropTest() {
    faultyDeclarationTest(
      this,
      OverwriteIdPropRelationship.class,
      RelationshipDescriptor.M_DUPLICATE_PROPERTY_KEY
    );
  }

  @Test
  public void overwriteValPropTest() {
    faultyDeclarationTest(
      this,
      OverwriteValPropRelationship.class,
      RelationshipDescriptor.M_DUPLICATE_PROPERTY_KEY
    );
  }

  // endregion

  // region type checks

  @Test
  public void typeCheck() {
    FooHasBarRelationDesc fooBar = SampleRelationshipRep.FooBar;
    assertEquals(
      SampleRelationshipTypeRep.FOOBAR,
      fooBar.type(),
      "fail fooBar"
    );
    BarHasFooRelationDesc barFoo = SampleRelationshipRep.BarFoo;
    assertEquals(
      SampleRelationshipTypeRep.BARFOO,
      barFoo.type(),
      "fail barFoo"
    );
    HasKeksRelationDesc hasKeks = SampleRelationshipRep.HasKeks;
    assertEquals(
      SampleRelationshipTypeRep.HAS_KEKS,
      hasKeks.type(),
      "fail hasKeks"
    );
    HasFooRelationDesc hasFoo = SampleRelationshipRep.HasFoo;
    assertEquals(
      SampleRelationshipTypeRep.HAS_FOO,
      hasFoo.type(),
      "fail hasFoo"
    );
    HasBarRelationDesc hasBar = SampleRelationshipRep.HasBar;
    assertEquals(
      SampleRelationshipTypeRep.HAS_BAR,
      hasBar.type(),
      "fail hasBar"
    );
  }

  class NoTypeDesc extends RelationshipDescriptor {

    public NoTypeDesc() {
      super();
      initTypeAndProperties(NoTypeDesc.class);
    }
  }

  @Test
  public void noTypeTest() {
    faultyDeclarationTest(
      this,
      NoTypeDesc.class,
      RelationshipDescriptor.M_MUST_DEFINE_UNIQUE_RELATIONTYPE
    );
  }

  class ManyTypeDesc extends RelationshipDescriptor {

    public ManyTypeDesc() {
      super();
      initTypeAndProperties(ManyTypeDesc.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type1 = new RelationshipTypeDescriptor(
      "whatev1"
    );

    @Identifying
    public final RelationshipTypeDescriptor type2 = new RelationshipTypeDescriptor(
      "whatev2"
    );
  }

  @Test
  public void manyTypeTest() {
    faultyDeclarationTest(
      this,
      ManyTypeDesc.class,
      RelationshipDescriptor.M_MUST_DEFINE_UNIQUE_RELATIONTYPE
    );
  }

  class NonIdTypeDesc extends RelationshipDescriptor {

    public NonIdTypeDesc() {
      super();
      initTypeAndProperties(NonIdTypeDesc.class);
    }

    public final RelationshipTypeDescriptor type1 = new RelationshipTypeDescriptor(
      "whatev1"
    );
  }

  @Test
  public void nonIdTypeTest() {
    faultyDeclarationTest(
      this,
      NonIdTypeDesc.class,
      RelationshipDescriptor.M_RELATIONTYPE_MUST_BE_IDENTIFYING
    );
  }

  class NonPublicTypeDesc extends RelationshipDescriptor {

    public NonPublicTypeDesc() {
      super();
      initTypeAndProperties(NonPublicTypeDesc.class);
    }

    @Identifying
    protected final RelationshipTypeDescriptor type1 = new RelationshipTypeDescriptor(
      "whatev1"
    );
  }

  @Test
  public void nonPublicTypeTest() {
    faultyDeclarationTest(
      this,
      NonPublicTypeDesc.class,
      RelationshipDescriptor.M_RELATIONTYPE_MUST_BE_PUBLICFINAL
    );
  }

  class NonFinalTypeDesc extends RelationshipDescriptor {

    public NonFinalTypeDesc() {
      super();
      initTypeAndProperties(NonFinalTypeDesc.class);
    }

    @Identifying
    public RelationshipTypeDescriptor type1 = new RelationshipTypeDescriptor(
      "whatev1"
    );
  }

  @Test
  public void nonFinalTypeTest() {
    faultyDeclarationTest(
      this,
      NonFinalTypeDesc.class,
      RelationshipDescriptor.M_RELATIONTYPE_MUST_BE_PUBLICFINAL
    );
  }

  // endregion

  // region property checks

  @Test
  public void propertyCheckFooBar() {
    FooHasBarRelationDesc fooBar = SampleRelationshipRep.FooBar;
    expectedPropertyCheck(
      fooBar,
      new TreeSet<>(Arrays.asList(fooBar.longsProp)),
      new TreeSet<>(Arrays.asList(fooBar.necessaryStringProp))
    );
  }

  @Test
  public void propertyCheckBarFoo() {
    BarHasFooRelationDesc barFoo = SampleRelationshipRep.BarFoo;
    expectedPropertyCheck(
      barFoo,
      new TreeSet<>(Arrays.asList(barFoo.longsProp)),
      new TreeSet<>(Arrays.asList(barFoo.necessaryStringProp))
    );
  }

  @Test
  public void propertyCheckHasKeks() {
    HasKeksRelationDesc hasKeks = SampleRelationshipRep.HasKeks;
    expectedPropertyCheck(
      hasKeks,
      new TreeSet<>(Arrays.asList(hasKeks.longsProp1, hasKeks.longsProp2)),
      new TreeSet<>(
        Arrays.asList(
          hasKeks.necessaryStringProp1,
          hasKeks.necessaryStringProp2
        )
      )
    );
  }

  @Test
  public void matchDtoTest() {
    super.matchDtoTest(SampleRelationshipRep.FooBar);
    super.matchDtoTest(SampleRelationshipRep.BarFoo);
    super.matchDtoTest(SampleRelationshipRep.HasKeks);
  }

  class DoublePropkeyDesc extends RelationshipDescriptor {

    public DoublePropkeyDesc() {
      super();
      initTypeAndProperties(DoublePropkeyDesc.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "whatev"
    );

    @Identifying
    public final LongProperty prop1 = new LongProperty("prop", "log_prop1");

    @Validate
    public final LongProperty prop2 = new LongProperty("prop", "log_prop2");
  }

  @Test
  public void doublePropKeyTest() {
    faultyDeclarationTest(
      this,
      DoublePropkeyDesc.class,
      EntityDescriptor.M_DUPLICATE_PROPERTY_KEY
    );
  }

  class DoublePropLogKeyDesc extends RelationshipDescriptor {

    public DoublePropLogKeyDesc() {
      super();
      initTypeAndProperties(DoublePropLogKeyDesc.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "whatev"
    );

    @Identifying
    public final LongProperty prop1 = new LongProperty("prop1", "log_prop");

    @Validate
    public final LongProperty prop2 = new LongProperty("prop2", "log_prop");
  }

  @Test
  public void doublePropLogKeyTest() {
    faultyDeclarationTest(
      this,
      DoublePropLogKeyDesc.class,
      EntityDescriptor.M_DUPLICATE_PROPERTY_LOGKEY
    );
  }

  class NonPublicPropDesc extends RelationshipDescriptor {

    public NonPublicPropDesc() {
      super();
      initTypeAndProperties(NonPublicPropDesc.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "whatev"
    );

    @Identifying
    protected final LongProperty prop1 = new LongProperty("prop", "log_prop");
  }

  @Test
  public void nonPublicPropTest() {
    faultyDeclarationTest(
      this,
      NonPublicPropDesc.class,
      EntityDescriptor.M_PROPERTY_FIELDS_MUST_BE_PUBLIC_FINAL
    );
  }

  class NonFinalPropDesc extends RelationshipDescriptor {

    public NonFinalPropDesc() {
      super();
      initTypeAndProperties(NonFinalPropDesc.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "whatev"
    );

    @Identifying
    public LongProperty prop1 = new LongProperty("prop", "log_prop");
  }

  @Test
  public void nonFinalPropTest() {
    faultyDeclarationTest(
      this,
      NonFinalPropDesc.class,
      EntityDescriptor.M_PROPERTY_FIELDS_MUST_BE_PUBLIC_FINAL
    );
  }

  class NoAnnotationPropDesc extends RelationshipDescriptor {

    public NoAnnotationPropDesc() {
      super();
      initTypeAndProperties(NoAnnotationPropDesc.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "whatev"
    );

    public final LongProperty prop1 = new LongProperty("prop", "log_prop");
  }

  @Test
  public void noAnnotationPropTest() {
    faultyDeclarationTest(
      this,
      NoAnnotationPropDesc.class,
      EntityDescriptor.M_PROPERTY_FIELDS_MUST_BE_ANNOTATED
    );
  }

  class DoubleAnnotationPropDesc extends RelationshipDescriptor {

    public DoubleAnnotationPropDesc() {
      super();
      initTypeAndProperties(DoubleAnnotationPropDesc.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
      "whatev"
    );

    @Identifying
    @Validate
    public final LongProperty prop1 = new LongProperty("prop", "log_prop");
  }

  @Test
  public void doubleAnnotationPropTest() {
    faultyDeclarationTest(
      this,
      NoAnnotationPropDesc.class,
      EntityDescriptor.M_PROPERTY_FIELDS_MUST_BE_ANNOTATED
    );
  }

  // endregion

  // region nodes checks

  private void expectedStartEndNodeTest(
    RelationshipDescriptor relDesc,
    NodeDescriptor excpectedStart,
    NodeDescriptor excpectedEnd
  ) {
    String msg = String.format(
      "Failed expected node test @%s ",
      relDesc.getClass().getName()
    );
    assertEquals(excpectedStart, relDesc.startNodeDescriptor(), msg + " start");
    assertEquals(excpectedEnd, relDesc.endNodeDescriptor(), msg + " end");
  }

  @Test
  public void nodeTests() {
    expectedStartEndNodeTest(
      SampleRelationshipRep.FooBar,
      SampleNodeRep.Foo,
      SampleNodeRep.Bar
    );
    expectedStartEndNodeTest(
      SampleRelationshipRep.BarFoo,
      SampleNodeRep.Bar,
      SampleNodeRep.Foo
    );
    expectedStartEndNodeTest(
      SampleRelationshipRep.HasBar,
      null,
      SampleNodeRep.Bar
    );
    expectedStartEndNodeTest(
      SampleRelationshipRep.HasFoo,
      null,
      SampleNodeRep.Foo
    );
    expectedStartEndNodeTest(
      SampleRelationshipRep.HasKeks,
      null,
      SampleNodeRep.Keks
    );
    expectedStartEndNodeTest(
      SampleRelationshipRep.IsSubKeksOf,
      SampleNodeRep.Keks,
      SampleNodeRep.Keks
    );
  }

  class UndeclaredNodeDesc extends RelationshipDescriptor {

    public UndeclaredNodeDesc() {
      super();
      initTypeAndProperties(UndeclaredNodeDesc.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type1 = new RelationshipTypeDescriptor(
      "whatev1"
    );

    public final KeksNodeDesc keksNode() {
      return SampleNodeRep.Keks;
    }
  }

  @Test
  public void undeclaredNodeTest() {
    Exception exc = null;
    UndeclaredNodeDesc rel = new UndeclaredNodeDesc();
    try {
      rel.startNodeDescriptor();
    } catch (IllegalStateException e) {
      exc = e;
    }
    assertNotNull(exc, "fail undeclared node 1");
    assertEquals(
      RelationshipDescriptor.M_NODEDESCRIPTORS_MUST_BE_START_OR_END,
      exc.getMessage(),
      "failmany undeclared node 2"
    );
  }

  class DoubleStartDesc extends RelationshipDescriptor {

    public DoubleStartDesc() {
      super();
      initTypeAndProperties(DoubleStartDesc.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type1 = new RelationshipTypeDescriptor(
      "whatev1"
    );

    @StartNode
    public final KeksNodeDesc keksNode1() {
      return SampleNodeRep.Keks;
    }

    @StartNode
    public final KeksNodeDesc keksNode2() {
      return SampleNodeRep.Keks;
    }
  }

  @Test
  public void doubleStartNodeTest() {
    Exception exc = null;
    DoubleStartDesc rel = new DoubleStartDesc();
    try {
      rel.startNodeDescriptor();
    } catch (IllegalStateException e) {
      exc = e;
    }
    assertNotNull(exc, "fail double start node 1");
    assertEquals(
      RelationshipDescriptor.M_ONLY_ONE_NODE_EACH,
      exc.getMessage(),
      "failmany double start node 2"
    );
  }

  class DoubleEndDesc extends RelationshipDescriptor {

    public DoubleEndDesc() {
      super();
      initTypeAndProperties(DoubleEndDesc.class);
    }

    @Identifying
    public final RelationshipTypeDescriptor type1 = new RelationshipTypeDescriptor(
      "whatev1"
    );

    @EndNode
    public final KeksNodeDesc keksNode1() {
      return SampleNodeRep.Keks;
    }

    @EndNode
    public final KeksNodeDesc keksNode2() {
      return SampleNodeRep.Keks;
    }
  }

  @Test
  public void doubleEndNodeTest() {
    Exception exc = null;
    DoubleEndDesc rel = new DoubleEndDesc();
    try {
      rel.startNodeDescriptor();
    } catch (IllegalStateException e) {
      exc = e;
    }
    assertNotNull(exc, "fail double end node 1");
    assertEquals(
      RelationshipDescriptor.M_ONLY_ONE_NODE_EACH,
      exc.getMessage(),
      "failmany double end node 2"
    );
  }

  // endregion

  // region hash, equal, compare checks

  @Test
  public void hashTest() {
    FooHasBarRelationDesc foobar1 = new FooHasBarRelationDesc();
    FooHasBarRelationDesc foobar2 = new FooHasBarRelationDesc();
    assertEquals(foobar1.hashCode(), foobar2.hashCode(), "fail foobar");
    HasKeksRelationDesc keks1 = new HasKeksRelationDesc();
    HasKeksRelationDesc keks2 = new HasKeksRelationDesc();
    assertEquals(keks1.hashCode(), keks2.hashCode(), "fail hasKeks");
    IsSubKeksOfRelationDesc subKeks1 = new IsSubKeksOfRelationDesc();
    IsSubKeksOfRelationDesc subKeks2 = new IsSubKeksOfRelationDesc();
    assertEquals(subKeks1.hashCode(), subKeks2.hashCode(), "fail subKeks");
  }

  @Test
  public void equalTest() {
    FooHasBarRelationDesc foobar1 = new FooHasBarRelationDesc();
    FooHasBarRelationDesc foobar2 = new FooHasBarRelationDesc();
    assertEquals(foobar1, foobar2, "fail foobar");
    HasKeksRelationDesc keks1 = new HasKeksRelationDesc();
    HasKeksRelationDesc keks2 = new HasKeksRelationDesc();
    assertEquals(keks1, keks2, "fail keks");
    IsSubKeksOfRelationDesc subKeks1 = new IsSubKeksOfRelationDesc();
    IsSubKeksOfRelationDesc subKeks2 = new IsSubKeksOfRelationDesc();
    assertEquals(subKeks1, subKeks2, "fail subKeks");
    assertNotEquals(foobar1, keks1, "fail foobar == keks");
    assertNotEquals(foobar1, subKeks1, "fail foobar == subKeks");
    assertNotEquals(subKeks1, keks1, "fail subKeks == keks");
  }

  @Test
  public void compareTest() {
    FooHasBarRelationDesc foobar1 = new FooHasBarRelationDesc();
    FooHasBarRelationDesc foobar2 = new FooHasBarRelationDesc();
    assertEquals(0, foobar1.compareTo(foobar1), "fail foobar 1");
    assertEquals(0, foobar1.compareTo(foobar2), "fail foobar 2");
    HasKeksRelationDesc keks1 = new HasKeksRelationDesc();
    HasKeksRelationDesc keks2 = new HasKeksRelationDesc();
    assertEquals(0, keks1.compareTo(keks1), "fail keks 1");
    assertEquals(0, keks1.compareTo(keks2), "fail keks 2");
    IsSubKeksOfRelationDesc subKeks1 = new IsSubKeksOfRelationDesc();
    IsSubKeksOfRelationDesc subKeks2 = new IsSubKeksOfRelationDesc();
    assertEquals(0, subKeks1.compareTo(subKeks1), "fail subKeks 1");
    assertEquals(0, subKeks1.compareTo(subKeks2), "fail subKeks 2");
    int fooBar2Keks = foobar1.compareTo(keks1);
    int keks2FooBar = keks1.compareTo(foobar1);
    int fooBar2subKeks = foobar1.compareTo(subKeks1);
    int subKeks2FooBar = subKeks1.compareTo(foobar1);
    int keks2subKeks = keks1.compareTo(subKeks1);
    int subKeks2Keks = subKeks1.compareTo(keks1);
    assertNotEquals(0, fooBar2Keks, "fail comp 1");
    assertNotEquals(0, keks2FooBar, "fail comp 2");
    assertNotEquals(0, fooBar2subKeks, "fail comp 3");
    assertNotEquals(0, subKeks2FooBar, "fail comp 4");
    assertNotEquals(0, keks2subKeks, "fail comp 5");
    assertNotEquals(0, subKeks2Keks, "fail comp 6");
    assertEquals(fooBar2Keks, -keks2FooBar, "fail symmetry 1");
    assertEquals(fooBar2subKeks, -subKeks2FooBar, "fail symmetry 2");
    assertEquals(keks2subKeks, -subKeks2Keks, "fail symmetry 3");
  }

  // endregion

  private void printTest(RelationshipDescriptor relDesc) {
    String mI = "...";
    String sI = "^^";
    String print = relDesc.print(mI, sI);
    String[] splits = print.split(System.lineSeparator());
    int expectedCount = 3; // header line, Identifier line, properties line
    RelationshipIdentifier id = relDesc.identifier();
    if (id.relationshipTypeDescriptor() != null) {
      expectedCount++;
    }
    if (relDesc.startNodeDescriptor() != null) {
      expectedCount++;
    }
    if (relDesc.endNodeDescriptor() != null) {
      expectedCount++;
    }
    expectedCount += relDesc.properties(Modality.BOTH).count();
    assertEquals(
      expectedCount,
      splits.length,
      "Fail " + relDesc.getClass().getSimpleName()
    );
    assertTrue(Arrays.stream(splits).allMatch(o -> o.startsWith(mI)), "fail 1");
    assertTrue(
      Arrays.stream(splits).skip(1).allMatch(o -> o.startsWith(mI + sI)),
      "fail 2"
    );
  }

  @Test
  public void printTest() {
    RelationshipDescriptor[] samples = new RelationshipDescriptor[] {
      SampleRelationshipRep.FooBar,
      SampleRelationshipRep.BarFoo,
      SampleRelationshipRep.HasKeks,
      SampleRelationshipRep.HasFoo,
      SampleRelationshipRep.HasBar,
    };
    for (RelationshipDescriptor rd : samples) {
      printTest(rd);
    }
  }

  @Test
  public void testAllProcedures() {
    List<String> failingProcs = super.runAllTestProcedures(
      RelationshipDescriptorTestProcedure.TestProcedureName.class
    );
    String fails = String.join(
      System.lineSeparator(),
      failingProcs.stream().toArray(String[]::new)
    );
    assertTrue(failingProcs.isEmpty(), "failed procs: " + fails);
  }
}
