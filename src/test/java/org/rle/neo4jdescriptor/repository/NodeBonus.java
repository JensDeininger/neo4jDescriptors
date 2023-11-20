package org.rle.neo4jdescriptor.repository;

import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;

public class NodeBonus extends NodeDescriptor {

  public NodeBonus() {
    super();
    initLabelsAndProperties(NodeBonus.class);
  }

  @Identifying
  public final LabelDescriptor Label = new LabelDescriptor("bonus");
}
