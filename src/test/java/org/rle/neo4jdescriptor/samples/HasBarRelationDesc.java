package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.EndNode;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class HasBarRelationDesc extends RelationshipDescriptor {

  public HasBarRelationDesc() {
    super();
    initTypeAndProperties(HasBarRelationDesc.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type =
    SampleRelationshipTypeRep.HAS_BAR;

  @EndNode
  public final BarNodeDesc barNode() {
    return SampleNodeRep.Bar;
  }
}
