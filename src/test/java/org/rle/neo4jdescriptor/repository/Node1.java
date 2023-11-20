package org.rle.neo4jdescriptor.repository;

import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;

public class Node1 extends NodeDescriptor {

  public Node1() {
    super();
    initLabelsAndProperties(Node1.class);
  }

  @Identifying
  public final LabelDescriptor Label = new LabelDescriptor(
    NodeRepositoryTest.NodeNames[1]
  );
}
