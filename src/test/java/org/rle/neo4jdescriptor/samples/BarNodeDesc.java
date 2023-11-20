package org.rle.neo4jdescriptor.samples;

import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.annotation.Validate;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeRelationOneMany;
import org.rle.neo4jdescriptor.entity.NodeRelationOneOne;
import org.rle.neo4jdescriptor.entity.NodeRelationZeroMany;
import org.rle.neo4jdescriptor.property.array_basic.LongArrayProperty;
import org.rle.neo4jdescriptor.property.prop_basic.DoubleProperty;

public class BarNodeDesc extends MooNodeDesc {

  // region private fields

  private NodeRelationOneOne mFooBar;

  private NodeRelationOneOne mBarFoo;

  private NodeRelationOneMany mHasKeks;

  private NodeRelationZeroMany mHasBar;

  // endregion

  public BarNodeDesc() {
    super();
    initLabelsAndProperties(BarNodeDesc.class);
  }

  // region labels

  @Identifying
  public final LabelDescriptor barLabel = SampleLabelRep.Bar;

  // endregion

  // region properties

  @Identifying
  public final DoubleProperty necessaryDoubleProp = new DoubleProperty(
    "doubleProp",
    "log_doubleProp"
  );

  @Validate
  public final LongArrayProperty longsProp = new LongArrayProperty(
    "longsProp",
    "log_longsProp"
  );

  // endregion

  // region NodeRelations

  @Validate
  public final NodeRelationOneOne fooBar() {
    if (mFooBar == null) {
      mFooBar =
        new NodeRelationOneOne(
          SampleRelationshipRep.FooBar,
          Direction.INCOMING
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
          Direction.OUTGOING
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
  public final NodeRelationZeroMany hasBar() {
    if (mHasBar == null) {
      mHasBar =
        new NodeRelationZeroMany(
          SampleRelationshipRep.HasBar,
          Direction.INCOMING
        );
    }
    return mHasBar;
  }
  // endregion

}
