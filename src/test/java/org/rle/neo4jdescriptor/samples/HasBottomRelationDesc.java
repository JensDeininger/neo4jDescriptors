package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.EndNode;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class HasBottomRelationDesc extends RelationshipDescriptor {

  public HasBottomRelationDesc() {
    super();
    initTypeAndProperties(HasBottomRelationDesc.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type =
    SampleRelationshipTypeRep.HAS_BOTTOM;

  @EndNode
  public final NodeDescBottom bottomEndNode() {
    return SampleNodeRep.Bottom;
  }
}
