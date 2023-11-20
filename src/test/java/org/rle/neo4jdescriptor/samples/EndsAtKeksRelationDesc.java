package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.EndNode;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class EndsAtKeksRelationDesc extends RelationshipDescriptor {

  public EndsAtKeksRelationDesc() {
    super();
    initTypeAndProperties(EndsAtKeksRelationDesc.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type =
    SampleRelationshipTypeRep.HAS_KEKS;

  @EndNode
  public final KeksNodeDesc keksNode() {
    return SampleNodeRep.Keks;
  }
}
