package org.rle.neo4jdescriptor.samples;

import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.annotation.Validate;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;
import org.rle.neo4jdescriptor.entity.NodeRelationOneMany;
import org.rle.neo4jdescriptor.entity.NodeRelationZeroMany;
import org.rle.neo4jdescriptor.entity.NodeRelationZeroOne;
import org.rle.neo4jdescriptor.property.prop_basic.DoubleProperty;
import org.rle.neo4jdescriptor.property.prop_basic.StringProperty;

public class KeksNodeDesc extends NodeDescriptor {

  // region private fields

  private NodeRelationOneMany mHasFoo;

  private NodeRelationOneMany mHasBar;

  private NodeRelationZeroMany mHasKeks;

  private NodeRelationZeroOne mIsSubKeksOf;

  private NodeRelationZeroMany mHasSubKekse;

  // endregion

  // region ctor

  public KeksNodeDesc() {
    super();
    initLabelsAndProperties(KeksNodeDesc.class);
  }

  // endregion

  // region labels

  @Identifying
  public final LabelDescriptor keksLabel = SampleLabelRep.Keks;

  @Identifying
  public final LabelDescriptor MooLabel = SampleLabelRep.Moo;

  // endregion

  // region properties

  @Identifying
  public final DoubleProperty cookieContent = new DoubleProperty(
    "cookieContent",
    "log_cookieContent"
  );

  @Identifying
  public final DoubleProperty nutContent = new DoubleProperty(
    "nutContent",
    "log_nutContent"
  );

  @Validate
  public final StringProperty name = new StringProperty("name", "log_name");

  @Validate
  public final StringProperty description = new StringProperty(
    "description",
    "log_description"
  );

  // endregion

  // region NodeRelations

  @Validate
  public final NodeRelationOneMany hasFoo() {
    if (mHasFoo == null) {
      mHasFoo =
        new NodeRelationOneMany(
          SampleRelationshipRep.HasFoo,
          Direction.OUTGOING
        );
    }
    return mHasFoo;
  }

  @Validate
  public final NodeRelationOneMany hasBar() {
    if (mHasBar == null) {
      mHasBar =
        new NodeRelationOneMany(
          SampleRelationshipRep.HasBar,
          Direction.OUTGOING
        );
    }
    return mHasBar;
  }

  @Validate
  public final NodeRelationZeroMany hasKeks() {
    if (mHasKeks == null) {
      mHasKeks =
        new NodeRelationZeroMany(
          SampleRelationshipRep.HasKeks,
          Direction.INCOMING
        );
    }
    return mHasKeks;
  }

  @Validate
  public final NodeRelationZeroMany hasSubKekse() {
    if (mHasSubKekse == null) {
      mHasSubKekse =
        new NodeRelationZeroMany(
          SampleRelationshipRep.IsSubKeksOf,
          Direction.INCOMING
        );
    }
    return mHasSubKekse;
  }

  @Validate
  public final NodeRelationZeroOne isSubKeksOf() {
    if (mIsSubKeksOf == null) {
      mIsSubKeksOf =
        new NodeRelationZeroOne(
          SampleRelationshipRep.IsSubKeksOf,
          Direction.OUTGOING
        );
    }
    return mIsSubKeksOf;
  }
  // endregion

}
