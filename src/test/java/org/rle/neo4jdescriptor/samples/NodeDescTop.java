package org.rle.neo4jdescriptor.samples;

import org.neo4j.graphdb.Direction;
import org.rle.neo4jdescriptor.annotation.*;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;
import org.rle.neo4jdescriptor.entity.NodeRelationZeroMany;
import org.rle.neo4jdescriptor.property.prop_basic.StringProperty;

public class NodeDescTop extends NodeDescriptor {

  private NodeRelationZeroMany mHasTops;

  public NodeDescTop() {
    super();
    initLabelsAndProperties(NodeDescTop.class);
  }

  @Identifying
  public final LabelDescriptor topIdLabel = SampleLabelRep.TopIdLabel;

  @Validate
  public final LabelDescriptor topBonusLabel = SampleLabelRep.TopBonusLabel;

  @Identifying
  public final StringProperty idTopProp = new StringProperty(
    "idTopProp",
    "log_idTopProp"
  );

  @Validate
  public final StringProperty valTopProp = new StringProperty(
    "valTopProp",
    "log_valTopProp"
  );

  @Validate
  public final NodeRelationZeroMany hasTops() {
    if (mHasTops == null) {
      mHasTops =
        new NodeRelationZeroMany(
          SampleRelationshipRep.HasTop,
          Direction.OUTGOING
        );
    }
    return mHasTops;
  }
}
