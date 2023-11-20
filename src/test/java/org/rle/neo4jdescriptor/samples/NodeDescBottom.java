package org.rle.neo4jdescriptor.samples;

import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.annotation.*;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeRelationZeroMany;
import org.rle.neo4jdescriptor.property.prop_basic.StringProperty;

public class NodeDescBottom extends NodeDescMiddle {

  private NodeRelationZeroMany mHasBottoms;

  public NodeDescBottom() {
    super();
    initLabelsAndProperties(NodeDescBottom.class);
  }

  @Identifying
  public final LabelDescriptor bottomIdLabel = SampleLabelRep.BottomIdLabel;

  @Validate
  public final LabelDescriptor bottomBonusLabel =
    SampleLabelRep.BottomBonusLabel;

  @Identifying
  public final StringProperty idBottomProp = new StringProperty(
    "idBottomProp",
    "log_idBottomProp"
  );

  @Validate
  public final StringProperty valBottomProp = new StringProperty(
    "valBottomProp",
    "log_valBottomProp"
  );

  @Validate
  public final NodeRelationZeroMany hasBottoms() {
    if (mHasBottoms == null) {
      mHasBottoms =
        new NodeRelationZeroMany(
          SampleRelationshipRep.HasBottom,
          Direction.OUTGOING
        );
    }
    return mHasBottoms;
  }
}
