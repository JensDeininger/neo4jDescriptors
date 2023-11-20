package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.EndNode;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class HasFooRelationDesc extends RelationshipDescriptor {

  public HasFooRelationDesc() {
    super();
    initTypeAndProperties(HasFooRelationDesc.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type =
    SampleRelationshipTypeRep.HAS_FOO;

  @EndNode
  public final FooNodeDesc fooNode() {
    return SampleNodeRep.Foo;
  }
}
