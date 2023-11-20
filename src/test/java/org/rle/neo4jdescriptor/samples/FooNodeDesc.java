package org.rle.neo4jdescriptor.samples;

import java.util.stream.Stream;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.annotation.Validate;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeRelationOneMany;
import org.rle.neo4jdescriptor.entity.NodeRelationOneOne;
import org.rle.neo4jdescriptor.entity.NodeRelationZeroMany;
import org.rle.neo4jdescriptor.property.array_basic.StringArrayProperty;
import org.rle.neo4jdescriptor.property.prop_basic.BooleanProperty;

public class FooNodeDesc extends MooNodeDesc {

  // region private fields

  private NodeRelationOneOne mFooBar;

  private NodeRelationOneOne mBarFoo;

  private NodeRelationOneMany mHasKeks;

  private NodeRelationZeroMany mHasFoo;

  // endregion

  public FooNodeDesc() {
    super();
    initLabelsAndProperties(FooNodeDesc.class);
  }

  // region labels

  @Identifying
  public final LabelDescriptor fooLabel = SampleLabelRep.Foo;

  // endregion

  // region properties

  @Identifying
  public final BooleanProperty necessaryBoolProp = new BooleanProperty(
    "boolProp",
    "log_boolProp"
  );

  @Validate
  public final StringArrayProperty stringsProp = new StringArrayProperty(
    "stringsProp",
    "log_stringsProp"
  );

  // endregion

  // region NodeRelations

  @Validate
  public final NodeRelationOneOne fooBar() {
    if (mFooBar == null) {
      mFooBar =
        new NodeRelationOneOne(
          SampleRelationshipRep.FooBar,
          Direction.OUTGOING
        );
    }
    return mFooBar;
  }

  @Validate
  public final NodeRelationOneOne barFoo() {
    if (mBarFoo == null) {
      mBarFoo =
        new NodeRelationOneOne(
          SampleRelationshipRep.BarFoo,
          Direction.INCOMING
        );
    }
    return mBarFoo;
  }

  @Validate
  public final NodeRelationOneMany hasKeks() {
    if (mHasKeks == null) {
      mHasKeks =
        new NodeRelationOneMany(
          SampleRelationshipRep.HasKeks,
          Direction.OUTGOING
        );
    }
    return mHasKeks;
  }

  @Validate
  public final NodeRelationZeroMany hasFoo() {
    if (mHasFoo == null) {
      mHasFoo =
        new NodeRelationZeroMany(
          SampleRelationshipRep.HasFoo,
          Direction.INCOMING
        );
    }
    return mHasFoo;
  }

  // endregion

  // region relationships

  public Relationship fooHasBar(Node fooNode) {
    return relationship(fooBar(), fooNode);
  }

  public Relationship barHasFoo(Node fooNode) {
    return relationship(barFoo(), fooNode);
  }

  public Relationship firstHasKeks(Node fooNode) {
    return relationship(hasKeks(), fooNode);
  }

  public Stream<Relationship> hasKeks(Node fooNode) {
    return relationships(hasKeks(), fooNode);
  }

  public Stream<Relationship> hasFoos(Node fooNode) {
    return relationships(hasFoo(), fooNode);
  }

  // endregion

  // region nodes

  public Node barViaFooHasBar(Node fooNode) {
    return node(fooBar(), fooNode);
  }

  public Node barViaBarHasFoo(Node fooNode) {
    return node(barFoo(), fooNode);
  }

  public Node firstKeks(Node fooNode) {
    return node(hasKeks(), fooNode);
  }

  public Stream<Node> getKeksViaHasKeks(Node fooNode) {
    return nodes(hasKeks(), fooNode);
  }

  public Stream<Node> getKeksViaHasFoo(Node fooNode) {
    return nodes(hasFoo(), fooNode);
  }
  // endregion
}
