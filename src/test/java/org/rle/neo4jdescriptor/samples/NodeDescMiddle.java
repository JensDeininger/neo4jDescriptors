package org.rle.neo4jdescriptor.samples;

import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.annotation.*;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeRelationZeroMany;
import org.rle.neo4jdescriptor.property.prop_basic.StringProperty;

public class NodeDescMiddle extends NodeDescTop {

  private NodeRelationZeroMany mHasMiddles;

  public NodeDescMiddle() {
    super();
    initLabelsAndProperties(NodeDescMiddle.class);
  }

  @Identifying
  public final LabelDescriptor midIdLabel = SampleLabelRep.MiddleIdLabel;

  @Validate
  public final LabelDescriptor midBonusLabel = SampleLabelRep.MiddleBonusLabel;

  @Identifying
  public final StringProperty idMiddleProp = new StringProperty(
    "idMiddleProp",
    "log_idMiddleProp"
  );

  @Validate
  public final StringProperty valMiddleProp = new StringProperty(
    "valMiddleProp",
    "log_valMiddleProp"
  );

  @Validate
  public final NodeRelationZeroMany hasMiddles() {
    if (mHasMiddles == null) {
      mHasMiddles =
        new NodeRelationZeroMany(
          SampleRelationshipRep.HasMiddle,
          Direction.OUTGOING
        );
    }
    return mHasMiddles;
  }
}
