package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.EndNode;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class HasMiddleRelationDesc extends RelationshipDescriptor {

  public HasMiddleRelationDesc() {
    super();
    initTypeAndProperties(HasMiddleRelationDesc.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type =
    SampleRelationshipTypeRep.HAS_MIDDLE;

  @EndNode
  public final NodeDescMiddle middleEndNode() {
    return SampleNodeRep.Middle;
  }
}
