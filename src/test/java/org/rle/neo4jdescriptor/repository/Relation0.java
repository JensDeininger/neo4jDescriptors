package org.rle.neo4jdescriptor.repository;

import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class Relation0 extends RelationshipDescriptor {

  public Relation0() {
    super();
    initTypeAndProperties(Relation0.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
    RelationshipRepositoryTest.TypeNames[0]
  );
}
