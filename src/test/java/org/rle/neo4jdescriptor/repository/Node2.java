package org.rle.neo4jdescriptor.repository;

import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;

public class Node2 extends NodeDescriptor {

  public Node2() {
    super();
    initLabelsAndProperties(Node2.class);
  }

  @Identifying
  public final LabelDescriptor Label = new LabelDescriptor(
    NodeRepositoryTest.NodeNames[2]
  );
}
