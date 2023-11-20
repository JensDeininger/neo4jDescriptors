package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.EndNode;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.annotation.StartNode;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class IsSubKeksOfRelationDesc extends RelationshipDescriptor {

  public IsSubKeksOfRelationDesc() {
    super();
    initTypeAndProperties(IsSubKeksOfRelationDesc.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type =
    SampleRelationshipTypeRep.IS_SUB_KEKS_OF;

  @EndNode
  public final KeksNodeDesc superKeksNode() {
    return SampleNodeRep.Keks;
  }

  @StartNode
  public final KeksNodeDesc subKeksNode() {
    return SampleNodeRep.Keks;
  }
}
