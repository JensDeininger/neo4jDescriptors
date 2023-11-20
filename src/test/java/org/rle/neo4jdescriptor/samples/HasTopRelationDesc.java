package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.EndNode;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class HasTopRelationDesc extends RelationshipDescriptor {

  public HasTopRelationDesc() {
    super();
    initTypeAndProperties(HasTopRelationDesc.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type =
    SampleRelationshipTypeRep.HAS_TOP;

  @EndNode
  public final NodeDescTop topEndNode() {
    return SampleNodeRep.Top;
  }
}
