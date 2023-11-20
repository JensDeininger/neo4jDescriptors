package org.rle.neo4jdescriptor.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.annotation.*;
import org.rle.neo4jdescriptor.property.prop_basic.LongProperty;
import org.rle.neo4jdescriptor.samples.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NodeDescriptorTest extends EntityDescriptorTest {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(NodeDescriptorTestProcedure.class);
  }

  @Override
  protected String initialCypher() {
    return null;
  }

  // region label checks

  private void expectedLabelCheck(
    NodeDescriptor nodeDesc,
    TreeSet<LabelDescriptor> necessarys,
    TreeSet<LabelDescriptor> contingents
  ) {
    TreeSet<LabelDescriptor> conts = nodeDesc
      .labels(Modality.CONTINGENT)
      .collect(Collectors.toCollection(TreeSet::new));

    TreeSet<LabelDescriptor> neccs = nodeDesc
      .labels(Modality.NECESSARY)
      .collect(Collectors.toCollection(TreeSet::new));

    TreeSet<LabelDescriptor> boths = nodeDesc
      .labels(Modality.BOTH)
      .collect(Collectors.toCollection(TreeSet::new));

    String msg = String.format("Fail %s ", nodeDesc.getClass().getName());
    assertEquals(contingents.size(), conts.size(), msg + "contingent size");
    assertEquals(necessarys.size(), neccs.size(), msg + "necessary size");
    assertEquals(
      contingents.size() + necessarys.size(),
      boths.size(),
      msg + "both size"
    );

    assertTrue(
      EqualityUtils.setEquals(contingents, conts),
      msg + "cont equals"
    );
    assertTrue(EqualityUtils.setEquals(necessarys, neccs), msg + "cont equals");
    TreeSet<LabelDescriptor> combo = new TreeSet<>();
    combo.addAll(contingents);
    combo.addAll(necessarys);
    assertTrue(EqualityUtils.setEquals(combo, boths), msg + "combo equals");
  }

  @Test
  public void labelCheckFoo() {
    FooNodeDesc foo = SampleNodeRep.Foo;
    expectedLabelCheck(
      foo,
      new TreeSet<>(Arrays.asList(foo.fooLabel)),
      new TreeSet<>(Arrays.asList(foo.mooLabel))
    );
  }

  @Test
  public void labelCheckBar() {
    BarNodeDesc bar = SampleNodeRep.Bar;
    expectedLabelCheck(
      bar,
      new TreeSet<>(Arrays.asList(bar.barLabel)),
      new TreeSet<>(Arrays.asList(bar.mooLabel))
    );
  }

  @Test
  public void labelCheckKeks() {
    KeksNodeDesc kek = SampleNodeRep.Keks;
    expectedLabelCheck(
      kek,
      new TreeSet<>(Arrays.asList(kek.keksLabel, kek.MooLabel)),
      new TreeSet<>()
    );
  }

  @Test
  public void labelCheckInheritanceTop() {
    NodeDescTop top = SampleNodeRep.Top;
    expectedLabelCheck(
      top,
      new TreeSet<>(Arrays.asList(top.topIdLabel)),
      new TreeSet<>(Arrays.asList(top.topBonusLabel))
    );
  }

  @Test
  public void labelCheckInheritanceMiddle() {
    NodeDescMiddle mid = SampleNodeRep.Middle;
    expectedLabelCheck(
      mid,
      new TreeSet<>(Arrays.asList(mid.topIdLabel, mid.midIdLabel)),
      new TreeSet<>(Arrays.asList(mid.topBonusLabel, mid.midBonusLabel))
    );
  }

  @Test
  public void labelCheckInheritanceBottom() {
    NodeDescBottom bot = SampleNodeRep.Bottom;
    expectedLabelCheck(
      bot,
      new TreeSet<>(
        Arrays.asList(bot.topIdLabel, bot.midIdLabel, bot.bottomIdLabel)
      ),
      new TreeSet<>(
        Arrays.asList(
          bot.topBonusLabel,
          bot.midBonusLabel,
          bot.bottomBonusLabel
        )
      )
    );
  }

  @Test
  public void doubleLabelNameTest_sameClass() {
    class DoubleLabelNameDesc extends NodeDescriptor {

      private DoubleLabelNameDesc() {
        super();
        initLabelsAndProperties(DoubleLabelNameDesc.class);
      }

      @Identifying
      public final LabelDescriptor lbl1 = new LabelDescriptor("name", "log1");

      @Validate
      public final LabelDescriptor lbl2 = new LabelDescriptor("name", "log2");
    }

    faultyDeclarationTest(
      this,
      DoubleLabelNameDesc.class,
      NodeDescriptor.M_DUPLICATE_LABEL_NAME
    );
  }

  @Test
  public void doubleLabelNameTest_inherited_id() {
    class DoubleLabelNameDesc extends FooNodeDesc {

      private DoubleLabelNameDesc() {
        super();
        initLabelsAndProperties(DoubleLabelNameDesc.class);
      }

      @Identifying
      public final LabelDescriptor lbl1 = new LabelDescriptor(
        SampleNodeRep.Foo.mooLabel.name(),
        SampleNodeRep.Foo.mooLabel.logName() + "bla"
      );
    }

    faultyDeclarationTest(
      this,
      DoubleLabelNameDesc.class,
      NodeDescriptor.M_DUPLICATE_LABEL_NAME
    );
  }

  @Test
  public void doubleLabelNameTest_inherited_val() {
    class DoubleLabelNameDesc extends FooNodeDesc {

      private DoubleLabelNameDesc() {
        super();
        initLabelsAndProperties(DoubleLabelNameDesc.class);
      }

      @Validate
      public final LabelDescriptor lbl1 = new LabelDescriptor(
        SampleNodeRep.Foo.fooLabel.name(),
        SampleNodeRep.Foo.fooLabel.logName() + "bla"
      );
    }

    faultyDeclarationTest(
      this,
      DoubleLabelNameDesc.class,
      NodeDescriptor.M_DUPLICATE_LABEL_NAME
    );
  }

  @Test
  public void doubleLabelLogNameTest_sameClass() {
    class DoubleLabelLogNameDesc extends NodeDescriptor {

      private DoubleLabelLogNameDesc() {
        super();
        initLabelsAndProperties(DoubleLabelLogNameDesc.class);
      }

      @Identifying
      public final LabelDescriptor lbl1 = new LabelDescriptor("name1", "log");

      @Validate
      public final LabelDescriptor lbl2 = new LabelDescriptor("name2", "log");
    }

    faultyDeclarationTest(
      this,
      DoubleLabelLogNameDesc.class,
      NodeDescriptor.M_DUPLICATE_LABEL_LOGNAME
    );
  }

  @Test
  public void doubleLabelLogNameTest_inherited_id() {
    class DoubleLabelLogNameDesc extends FooNodeDesc {

      private DoubleLabelLogNameDesc() {
        super();
        initLabelsAndProperties(DoubleLabelLogNameDesc.class);
      }

      @Identifying
      public final LabelDescriptor lbl1 = new LabelDescriptor(
        SampleNodeRep.Foo.mooLabel.name() + "bla",
        SampleNodeRep.Foo.mooLabel.logName()
      );
    }

    faultyDeclarationTest(
      this,
      DoubleLabelLogNameDesc.class,
      NodeDescriptor.M_DUPLICATE_LABEL_LOGNAME
    );
  }

  @Test
  public void doubleLabelLogNameTest_inherited_val() {
    class DoubleLabelLogNameDesc extends FooNodeDesc {

      private DoubleLabelLogNameDesc() {
        super();
        initLabelsAndProperties(DoubleLabelLogNameDesc.class);
      }

      @Validate
      public final LabelDescriptor lbl1 = new LabelDescriptor(
        SampleNodeRep.Foo.fooLabel.name() + "bla",
        SampleNodeRep.Foo.fooLabel.logName()
      );
    }

    faultyDeclarationTest(
      this,
      DoubleLabelLogNameDesc.class,
      NodeDescriptor.M_DUPLICATE_LABEL_LOGNAME
    );
  }

  @Test
  public void nonPublicLabelTest() {
    class NonPublicLabelDesc extends NodeDescriptor {

      private NonPublicLabelDesc() {
        super();
        initLabelsAndProperties(NonPublicLabelDesc.class);
      }

      @Identifying
      protected final LabelDescriptor lbl = new LabelDescriptor("name", "log");
    }

    faultyDeclarationTest(
      this,
      NonPublicLabelDesc.class,
      NodeDescriptor.M_LABEL_FIELDS_MUST_BE_PUBLIC_FINAL
    );
  }

  @Test
  public void nonFinalLabelTest() {
    class NonFinalLabelDesc extends NodeDescriptor {

      private NonFinalLabelDesc() {
        super();
        initLabelsAndProperties(NonFinalLabelDesc.class);
      }

      @Identifying
      public LabelDescriptor lbl = new LabelDescriptor("name", "log");
    }

    faultyDeclarationTest(
      this,
      NonFinalLabelDesc.class,
      NodeDescriptor.M_LABEL_FIELDS_MUST_BE_PUBLIC_FINAL
    );
  }

  @Test
  public void noAnnotationLabelTest() {
    @SuppressWarnings("unused")
    class NoAnnotationLabelDesc extends NodeDescriptor {

      private NoAnnotationLabelDesc() {
        super();
        initLabelsAndProperties(NoAnnotationLabelDesc.class);
      }

      public final LabelDescriptor lbl = new LabelDescriptor("name", "log");
    }
    faultyDeclarationTest(
      this,
      NoAnnotationLabelDesc.class,
      NodeDescriptor.M_LABEL_FIELDS_MUST_BE_ANNOTATED
    );
  }

  @Test
  public void doubleAnnotationLabelTest() {
    class doubleAnnotationLabelDesc extends NodeDescriptor {

      private doubleAnnotationLabelDesc() {
        super();
        initLabelsAndProperties(doubleAnnotationLabelDesc.class);
      }

      @Identifying
      @Validate
      public final LabelDescriptor lbl = new LabelDescriptor("name", "log");
    }

    faultyDeclarationTest(
      this,
      doubleAnnotationLabelDesc.class,
      NodeDescriptor.M_LABEL_FIELDS_MUST_BE_ANNOTATED
    );
  }

  // endregion

  // region property checks

  @Test
  public void propertyCheck_Foo() {
    FooNodeDesc foo = SampleNodeRep.Foo;
    expectedPropertyCheck(
      foo,
      new TreeSet<>(Arrays.asList(foo.stringsProp)),
      new TreeSet<>(Arrays.asList(foo.necessaryBoolProp))
    );
  }

  @Test
  public void propertyCheck_Bar() {
    BarNodeDesc bar = SampleNodeRep.Bar;
    expectedPropertyCheck(
      bar,
      new TreeSet<>(Arrays.asList(bar.longsProp)),
      new TreeSet<>(Arrays.asList(bar.necessaryDoubleProp))
    );
  }

  @Test
  public void propertyCheck_Keks() {
    KeksNodeDesc kek = SampleNodeRep.Keks;
    expectedPropertyCheck(
      kek,
      new TreeSet<>(Arrays.asList(kek.description, kek.name)),
      new TreeSet<>(Arrays.asList(kek.cookieContent, kek.nutContent))
    );
  }

  @Test
  public void propertyCheck_Top() {
    NodeDescTop top = SampleNodeRep.Top;
    expectedPropertyCheck(
      top,
      new TreeSet<>(Arrays.asList(top.valTopProp)),
      new TreeSet<>(Arrays.asList(top.idTopProp))
    );
  }

  @Test
  public void propertyCheck_Middle() {
    NodeDescMiddle mid = SampleNodeRep.Middle;
    expectedPropertyCheck(
      mid,
      new TreeSet<>(Arrays.asList(mid.valTopProp, mid.valMiddleProp)),
      new TreeSet<>(Arrays.asList(mid.idTopProp, mid.idMiddleProp))
    );
  }

  @Test
  public void propertyCheck_Bottom() {
    NodeDescBottom bot = SampleNodeRep.Bottom;
    expectedPropertyCheck(
      bot,
      new TreeSet<>(
        Arrays.asList(bot.valTopProp, bot.valMiddleProp, bot.valBottomProp)
      ),
      new TreeSet<>(
        Arrays.asList(bot.idTopProp, bot.idMiddleProp, bot.idBottomProp)
      )
    );
  }

  @Test
  public void matchDtoTest() {
    super.matchDtoTest(SampleNodeRep.Foo);
    super.matchDtoTest(SampleNodeRep.Bar);
    super.matchDtoTest(SampleNodeRep.Keks);
    super.matchDtoTest(SampleNodeRep.Top);
    super.matchDtoTest(SampleNodeRep.Middle);
    super.matchDtoTest(SampleNodeRep.Bottom);
  }

  @Test
  public void doublePropKeyTest_sameClass() {
    class DoublePropkeyDesc extends NodeDescriptor {

      private DoublePropkeyDesc() {
        super();
        initLabelsAndProperties(DoublePropkeyDesc.class);
      }

      @Identifying
      public final LongProperty prop1 = new LongProperty("prop", "log_prop1");

      @Validate
      public final LongProperty prop2 = new LongProperty("prop", "log_prop2");
    }

    faultyDeclarationTest(
      this,
      DoublePropkeyDesc.class,
      EntityDescriptor.M_DUPLICATE_PROPERTY_KEY
    );
  }

  @Test
  public void doublePropKeyTest_inherited_id() {
    class DoublePropkeyDesc extends FooNodeDesc {

      private DoublePropkeyDesc() {
        super();
        initLabelsAndProperties(DoublePropkeyDesc.class);
      }

      @Identifying
      public final LongProperty prop1 = new LongProperty(
        SampleNodeRep.Foo.stringsProp.key(),
        SampleNodeRep.Foo.stringsProp.logKey() + "bla"
      );
    }

    faultyDeclarationTest(
      this,
      DoublePropkeyDesc.class,
      EntityDescriptor.M_DUPLICATE_PROPERTY_KEY
    );
  }

  @Test
  public void doublePropKeyTest_inherited_val() {
    class DoublePropkeyDesc extends FooNodeDesc {

      private DoublePropkeyDesc() {
        super();
        initLabelsAndProperties(DoublePropkeyDesc.class);
      }

      @Validate
      public final LongProperty prop1 = new LongProperty(
        SampleNodeRep.Foo.necessaryBoolProp.key(),
        SampleNodeRep.Foo.necessaryBoolProp.logKey() + "bla"
      );
    }

    faultyDeclarationTest(
      this,
      DoublePropkeyDesc.class,
      EntityDescriptor.M_DUPLICATE_PROPERTY_KEY
    );
  }

  @Test
  public void doublePropLogKeyTest_sameClass() {
    class DoublePropLogKeyDesc extends NodeDescriptor {

      private DoublePropLogKeyDesc() {
        super();
        initLabelsAndProperties(DoublePropLogKeyDesc.class);
      }

      @Identifying
      public final LongProperty prop1 = new LongProperty("prop1", "log_prop");

      @Validate
      public final LongProperty prop2 = new LongProperty("prop2", "log_prop");
    }

    faultyDeclarationTest(
      this,
      DoublePropLogKeyDesc.class,
      EntityDescriptor.M_DUPLICATE_PROPERTY_LOGKEY
    );
  }

  @Test
  public void doublePropLogKeyTest_inherited_id() {
    class DoublePropkeyDesc extends FooNodeDesc {

      private DoublePropkeyDesc() {
        super();
        initLabelsAndProperties(DoublePropkeyDesc.class);
      }

      @Identifying
      public final LongProperty prop1 = new LongProperty(
        SampleNodeRep.Foo.stringsProp.key() + "bla",
        SampleNodeRep.Foo.stringsProp.logKey()
      );
    }

    faultyDeclarationTest(
      this,
      DoublePropkeyDesc.class,
      EntityDescriptor.M_DUPLICATE_PROPERTY_LOGKEY
    );
  }

  @Test
  public void doublePropLogKeyTest_inherited_val() {
    class DoublePropkeyDesc extends FooNodeDesc {

      private DoublePropkeyDesc() {
        super();
        initLabelsAndProperties(DoublePropkeyDesc.class);
      }

      @Validate
      public final LongProperty prop1 = new LongProperty(
        SampleNodeRep.Foo.necessaryBoolProp.key() + "bla",
        SampleNodeRep.Foo.necessaryBoolProp.logKey()
      );
    }

    faultyDeclarationTest(
      this,
      DoublePropkeyDesc.class,
      EntityDescriptor.M_DUPLICATE_PROPERTY_LOGKEY
    );
  }

  @Test
  public void nonPublicPropTest() {
    class NonPublicPropKeyDesc extends NodeDescriptor {

      private NonPublicPropKeyDesc() {
        super();
        initLabelsAndProperties(NonPublicPropKeyDesc.class);
      }

      @Identifying
      protected final LongProperty prop1 = new LongProperty(
        "prop1",
        "log_prop"
      );
    }

    faultyDeclarationTest(
      this,
      NonPublicPropKeyDesc.class,
      EntityDescriptor.M_PROPERTY_FIELDS_MUST_BE_PUBLIC_FINAL
    );
  }

  @Test
  public void nonFinalPropTest() {
    class NonFinalPropKeyDesc extends NodeDescriptor {

      private NonFinalPropKeyDesc() {
        super();
        initLabelsAndProperties(NonFinalPropKeyDesc.class);
      }

      @Identifying
      public LongProperty prop1 = new LongProperty("prop1", "log_prop");
    }

    faultyDeclarationTest(
      this,
      NonFinalPropKeyDesc.class,
      EntityDescriptor.M_PROPERTY_FIELDS_MUST_BE_PUBLIC_FINAL
    );
  }

  @Test
  public void noAnnotationPropTest() {
    @SuppressWarnings("unused")
    class NonAnnotationPropKeyDesc extends NodeDescriptor {

      private NonAnnotationPropKeyDesc() {
        super();
        initLabelsAndProperties(NonAnnotationPropKeyDesc.class);
      }

      public final LongProperty prop1 = new LongProperty("prop1", "log_prop");
    }

    faultyDeclarationTest(
      this,
      NonAnnotationPropKeyDesc.class,
      EntityDescriptor.M_PROPERTY_FIELDS_MUST_BE_ANNOTATED
    );
  }

  @Test
  public void doubleAnnotationPropTest() {
    class DoubleAnnotationPropKeyDesc extends NodeDescriptor {

      private DoubleAnnotationPropKeyDesc() {
        super();
        initLabelsAndProperties(DoubleAnnotationPropKeyDesc.class);
      }

      @Validate
      @Identifying
      public final LongProperty prop1 = new LongProperty("prop1", "log_prop");
    }

    faultyDeclarationTest(
      this,
      DoubleAnnotationPropKeyDesc.class,
      EntityDescriptor.M_PROPERTY_FIELDS_MUST_BE_ANNOTATED
    );
  }

  // endregion

  // region NodeRelationChecks

  private void expectedNodeRelationCheck(
    NodeDescriptor nodeDesc,
    Class<? extends NodeRelation> clazz,
    TreeSet<NodeRelation> expected
  ) {
    TreeSet<NodeRelation> found = nodeDesc
      .nodeRelations(clazz)
      .map(o -> NodeRelation.class.cast(o))
      .collect(Collectors.toCollection(TreeSet::new));
    String msg = String.format(
      "Fail %s %s ",
      nodeDesc.getClass().getName(),
      clazz.getName()
    );
    assertEquals(expected.size(), found.size(), msg + "count");
    assertTrue(EqualityUtils.setEquals(expected, found), msg + "check");
  }

  @Test
  public void nodeRelationsCheck_Foo() {
    FooNodeDesc foo = SampleNodeRep.Foo;
    expectedNodeRelationCheck(
      foo,
      NodeRelationOneOne.class,
      new TreeSet<>(Arrays.asList(foo.barFoo(), foo.fooBar()))
    );
    expectedNodeRelationCheck(
      foo,
      NodeRelationOneMany.class,
      new TreeSet<>(Arrays.asList(foo.hasKeks()))
    );
    expectedNodeRelationCheck(
      foo,
      NodeRelationZeroMany.class,
      new TreeSet<>(Arrays.asList(foo.hasFoo()))
    );
  }

  @Test
  public void nodeRelationsCheck_Bar() {
    BarNodeDesc bar = SampleNodeRep.Bar;
    expectedNodeRelationCheck(
      bar,
      NodeRelationOneOne.class,
      new TreeSet<>(Arrays.asList(bar.barFoo(), bar.fooBar()))
    );
    expectedNodeRelationCheck(
      bar,
      NodeRelationOneMany.class,
      new TreeSet<>(Arrays.asList(bar.hasKeks()))
    );
    expectedNodeRelationCheck(
      bar,
      NodeRelationZeroMany.class,
      new TreeSet<>(Arrays.asList(bar.hasBar()))
    );
  }

  @Test
  public void nodeRelationsCheck_Keks() {
    KeksNodeDesc kek = SampleNodeRep.Keks;
    expectedNodeRelationCheck(
      kek,
      NodeRelationOneMany.class,
      new TreeSet<>(Arrays.asList(kek.hasFoo(), kek.hasBar()))
    );
    expectedNodeRelationCheck(
      kek,
      NodeRelationZeroMany.class,
      new TreeSet<>(Arrays.asList(kek.hasKeks(), kek.hasSubKekse()))
    );
    expectedNodeRelationCheck(
      kek,
      NodeRelationZeroOne.class,
      new TreeSet<>(Arrays.asList(kek.isSubKeksOf()))
    );
  }

  @Test
  public void nodeRelationsCheck_Top() {
    NodeDescTop top = SampleNodeRep.Top;
    expectedNodeRelationCheck(
      top,
      NodeRelationZeroMany.class,
      new TreeSet<>(Arrays.asList(top.hasTops()))
    );
  }

  @Test
  public void nodeRelationsCheck_Middle() {
    NodeDescMiddle mid = SampleNodeRep.Middle;
    expectedNodeRelationCheck(
      mid,
      NodeRelationZeroMany.class,
      new TreeSet<>(Arrays.asList(mid.hasTops(), mid.hasMiddles()))
    );
  }

  @Test
  public void nodeRelationsCheck_Bottom() {
    NodeDescBottom bot = SampleNodeRep.Bottom;
    expectedNodeRelationCheck(
      bot,
      NodeRelationZeroMany.class,
      new TreeSet<>(
        Arrays.asList(bot.hasTops(), bot.hasMiddles(), bot.hasBottoms())
      )
    );
  }

  private void faultyNodeRelationDeclaration(
    Class<? extends NodeDescriptor> clazz,
    String expectedMsg
  ) {
    IllegalStateException exc = null;
    try {
      Constructor<? extends NodeDescriptor> ctor = clazz.getDeclaredConstructor(
        this.getClass()
      );
      ctor.setAccessible(true);
      NodeDescriptor nodeDesc = ctor.newInstance(this);
      nodeDesc.nodeRelations().count();
    } catch (
      InstantiationException
      | NoSuchMethodException
      | SecurityException
      | IllegalAccessException
      | InvocationTargetException e
    ) {
      assertFalse(true, e.getMessage());
    } catch (IllegalStateException e) {
      exc = e;
    }
    assertNotNull(exc, "fail 1");
    assertEquals(expectedMsg, exc.getMessage(), "fail 2");
  }

  @Test
  public void nonPublicNodeRelationTest() {
    class NonPublicNodeRelationDescriptor extends NodeDescriptor {

      private NodeRelationOneOne mFooBar;

      private NonPublicNodeRelationDescriptor() {
        super();
        initLabelsAndProperties(NonPublicNodeRelationDescriptor.class);
      }

      @Validate
      protected final NodeRelationOneOne hasKeks() {
        if (mFooBar == null) {
          mFooBar =
            new NodeRelationOneOne(
              SampleRelationshipRep.HasKeks,
              Direction.OUTGOING
            );
        }
        return mFooBar;
      }
    }

    faultyNodeRelationDeclaration(
      NonPublicNodeRelationDescriptor.class,
      NodeDescriptor.M_NODERELATIONS_MUST_BE_PUBLIC_FINAL
    );
  }

  @Test
  public void nonFinalNodeRelationTest() {
    class NonFinalNodeRelationDescriptor extends NodeDescriptor {

      private NodeRelationOneOne mFooBar;

      private NonFinalNodeRelationDescriptor() {
        super();
        initLabelsAndProperties(NonFinalNodeRelationDescriptor.class);
      }

      @Validate
      public NodeRelationOneOne hasKeks() {
        if (mFooBar == null) {
          mFooBar =
            new NodeRelationOneOne(
              SampleRelationshipRep.HasKeks,
              Direction.OUTGOING
            );
        }
        return mFooBar;
      }
    }

    faultyNodeRelationDeclaration(
      NonFinalNodeRelationDescriptor.class,
      NodeDescriptor.M_NODERELATIONS_MUST_BE_PUBLIC_FINAL
    );
  }

  @Test
  public void noAnnotationNodeRelationTest() {
    @SuppressWarnings("unused")
    class NoAnnotationNodeRelationDescriptor extends NodeDescriptor {

      private NodeRelationOneOne mFooBar;

      private NoAnnotationNodeRelationDescriptor() {
        super();
        initLabelsAndProperties(NoAnnotationNodeRelationDescriptor.class);
      }

      public final NodeRelationOneOne hasKeks() {
        if (mFooBar == null) {
          mFooBar =
            new NodeRelationOneOne(
              SampleRelationshipRep.HasKeks,
              Direction.OUTGOING
            );
        }
        return mFooBar;
      }
    }

    faultyNodeRelationDeclaration(
      NoAnnotationNodeRelationDescriptor.class,
      NodeDescriptor.M_NODERELATIONS_MUST_BE_ANNOTATED
    );
  }

  @Test
  public void wrongStartNodeTest() {
    class WrongStartDescriptor extends NodeDescriptor {

      private NodeRelationOneOne mFooBar;

      private WrongStartDescriptor() {
        super();
        initLabelsAndProperties(WrongStartDescriptor.class);
      }

      @Validate
      public final NodeRelationOneOne fooBar2() {
        if (mFooBar == null) {
          mFooBar =
            new NodeRelationOneOne(
              SampleRelationshipRep.FooBar,
              Direction.OUTGOING
            );
        }
        return mFooBar;
      }
    }

    faultyNodeRelationDeclaration(
      WrongStartDescriptor.class,
      NodeDescriptor.M_RELATION_HAS_INCOMPATIBLE_STARTNODE
    );
  }

  @Test
  public void wrongEndNodeTest() {
    class WrongEndDescriptor extends NodeDescriptor {

      private NodeRelationOneOne mFooBar;

      private WrongEndDescriptor() {
        super();
        initLabelsAndProperties(WrongEndDescriptor.class);
      }

      @Validate
      public final NodeRelationOneOne fooBar2() {
        if (mFooBar == null) {
          mFooBar =
            new NodeRelationOneOne(
              SampleRelationshipRep.FooBar,
              Direction.INCOMING
            );
        }
        return mFooBar;
      }
    }

    faultyNodeRelationDeclaration(
      WrongEndDescriptor.class,
      NodeDescriptor.M_RELATION_HAS_INCOMPATIBLE_ENDNODE
    );
  }

  // endregion

  // region hash, equal, compare checks

  @Test
  public void hashTest() {
    FooNodeDesc foo1 = new FooNodeDesc();
    FooNodeDesc foo2 = new FooNodeDesc();
    assertEquals(foo1.hashCode(), foo2.hashCode(), "fail foo");
    BarNodeDesc bar1 = new BarNodeDesc();
    BarNodeDesc bar2 = new BarNodeDesc();
    assertEquals(bar1.hashCode(), bar2.hashCode(), "fail bar");
    KeksNodeDesc keks1 = new KeksNodeDesc();
    KeksNodeDesc keks2 = new KeksNodeDesc();
    assertEquals(keks1.hashCode(), keks2.hashCode(), "fail keks");
    NodeDescTop top1 = new NodeDescTop();
    NodeDescTop top2 = new NodeDescTop();
    assertEquals(top1.hashCode(), top2.hashCode(), "fail top");
    NodeDescMiddle mid1 = new NodeDescMiddle();
    NodeDescMiddle mid2 = new NodeDescMiddle();
    assertEquals(mid1.hashCode(), mid2.hashCode(), "fail mid");
    NodeDescBottom bot1 = new NodeDescBottom();
    NodeDescBottom bot2 = new NodeDescBottom();
    assertEquals(bot1.hashCode(), bot2.hashCode(), "fail bot");
  }

  @Test
  public void equalTest() {
    FooNodeDesc foo1 = new FooNodeDesc();
    FooNodeDesc foo2 = new FooNodeDesc();
    assertEquals(foo1, foo1, "fail foo 1");
    assertEquals(foo1, foo2, "fail foo 2");

    BarNodeDesc bar1 = new BarNodeDesc();
    BarNodeDesc bar2 = new BarNodeDesc();
    assertEquals(bar1, bar1, "fail bar 1");
    assertEquals(bar1, bar2, "fail bar 2");

    KeksNodeDesc kek1 = new KeksNodeDesc();
    KeksNodeDesc kek2 = new KeksNodeDesc();
    assertEquals(kek1, kek1, "fail kek 1");
    assertEquals(kek1, kek2, "fail kek 2");

    NodeDescTop top1 = new NodeDescTop();
    NodeDescTop top2 = new NodeDescTop();
    assertEquals(top1, top1, "fail top 1");
    assertEquals(top1, top2, "fail top 2");

    NodeDescMiddle mid1 = new NodeDescMiddle();
    NodeDescMiddle mid2 = new NodeDescMiddle();
    assertEquals(mid1, mid1, "fail mid 1");
    assertEquals(mid1, mid2, "fail mid 2");

    NodeDescBottom bot1 = new NodeDescBottom();
    NodeDescBottom bot2 = new NodeDescBottom();
    assertEquals(bot1, bot1, "fail bot 1");
    assertEquals(bot1, bot2, "fail bot 2");

    assertNotEquals(foo1, bar1, "fail foo == bar");
    assertNotEquals(foo1, kek1, "fail foo == kek");
    assertNotEquals(kek1, bar1, "fail kek == bar");

    assertNotEquals(top1, mid1, "fail top == mid");
    assertNotEquals(top1, bot1, "fail top == mid");
    assertNotEquals(mid1, bot1, "fail mid == bot");
  }

  @Test
  public void compareTest_singleClass() {
    FooNodeDesc foo1 = new FooNodeDesc();
    FooNodeDesc foo2 = new FooNodeDesc();
    assertEquals(0, foo1.compareTo(foo2), "fail foo");

    BarNodeDesc bar1 = new BarNodeDesc();
    BarNodeDesc bar2 = new BarNodeDesc();
    assertEquals(0, bar1.compareTo(bar2), "fail bar");

    KeksNodeDesc kek1 = new KeksNodeDesc();
    KeksNodeDesc kek2 = new KeksNodeDesc();
    assertEquals(0, kek1.compareTo(kek2), "fail kek");

    NodeDescTop top1 = new NodeDescTop();
    NodeDescTop top2 = new NodeDescTop();
    assertEquals(0, top1.compareTo(top2), "fail top");

    NodeDescMiddle mid1 = new NodeDescMiddle();
    NodeDescMiddle mid2 = new NodeDescMiddle();
    assertEquals(0, mid1.compareTo(mid2), "fail mid");

    NodeDescBottom bot1 = new NodeDescBottom();
    NodeDescBottom bot2 = new NodeDescBottom();
    assertEquals(0, bot1.compareTo(bot2), "fail bot");

    int fooBar = foo1.compareTo(bar1);
    int barFoo = bar1.compareTo(foo1);
    assertNotEquals(0, fooBar, "fail foo.compareTo(bar)");
    assertNotEquals(0, barFoo, "fail bar.compareTo(foo)");
    assertEquals(fooBar, -barFoo, "fail symmetry foo bar");

    int fooKek = foo1.compareTo(kek1);
    int kekFoo = kek1.compareTo(foo1);
    assertNotEquals(0, fooKek, "fail foo.compareTo(kek)");
    assertNotEquals(0, kekFoo, "fail kek.compareTo(foo)");
    assertEquals(fooKek, -kekFoo, "fail symmetry foo kek");

    int barKek = bar1.compareTo(kek1);
    int kekBar = kek1.compareTo(bar1);
    assertNotEquals(0, barKek, "fail bar.compareTo(kek)");
    assertNotEquals(0, kekBar, "fail kek.compareTo(bar)");
    assertEquals(barKek, -kekBar, "fail symmetry bar kek");

    int topMid = top1.compareTo(mid1);
    int midTop = mid1.compareTo(top1);
    assertNotEquals(0, topMid, "fail top.compareTo(mid)");
    assertNotEquals(0, midTop, "fail mid.compareTo(top)");
    assertEquals(topMid, -midTop, "fail symmetry top mid");

    int topBot = top1.compareTo(bot1);
    int botTop = bot1.compareTo(top1);
    assertNotEquals(0, topBot, "fail top.compareTo(bot)");
    assertNotEquals(0, botTop, "fail bot.compareTo(top)");
    assertEquals(topBot, -botTop, "fail symmetry top bot");

    int midBot = mid1.compareTo(bot1);
    int botMid = bot1.compareTo(mid1);
    assertNotEquals(0, midBot, "fail mid.compareTo(bot)");
    assertNotEquals(0, botMid, "fail bot.compareTo(mid)");
    assertEquals(midBot, -botMid, "fail symmetry mid bot");
  }

  // endregion

  private void printTest(NodeDescriptor nd) {
    String mI = "...";
    String sI = "^^";
    String print = nd.print(mI, sI);
    String[] splits = print.split(System.lineSeparator());
    int expectedCount = 5; // header line, Identifier line, labels line, relations line, properties line
    expectedCount += nd.labels(Modality.BOTH).count();
    expectedCount += nd.properties(Modality.BOTH).count();
    expectedCount += nd.nodeRelations().count();
    assertEquals(
      expectedCount,
      splits.length,
      "Fail " + nd.getClass().getSimpleName()
    );
    assertTrue(Arrays.stream(splits).allMatch(o -> o.startsWith(mI)), "fail 1");
    assertTrue(
      Arrays.stream(splits).skip(1).allMatch(o -> o.startsWith(mI + sI)),
      "fail 2"
    );
  }

  @Test
  public void printTest() {
    NodeDescriptor[] samples = new NodeDescriptor[] {
      SampleNodeRep.Foo,
      SampleNodeRep.Bar,
      SampleNodeRep.Keks,
    };
    for (NodeDescriptor nd : samples) {
      printTest(nd);
    }
  }

  @Test
  public void testAllProcedures() {
    List<String> failingProcs = super.runAllTestProcedures(
      NodeDescriptorTestProcedure.TestProcedureName.class
    );
    String fails = String.join(
      System.lineSeparator(),
      failingProcs.stream().toArray(String[]::new)
    );
    assertTrue(failingProcs.isEmpty(), "failed procs: " + fails);
  }
}
