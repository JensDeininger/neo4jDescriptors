package org.rle.neo4jdescriptor.repository;

import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;

public class Node0 extends NodeDescriptor {

  public Node0() {
    super();
    initLabelsAndProperties(Node0.class);
  }

  @Identifying
  public final LabelDescriptor Label = new LabelDescriptor(
    NodeRepositoryTest.NodeNames[0]
  );
}
