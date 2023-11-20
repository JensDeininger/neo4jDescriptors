package org.rle.neo4jdescriptor.repository;

import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class Relation2 extends RelationshipDescriptor {

  public Relation2() {
    super();
    initTypeAndProperties(Relation2.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
    RelationshipRepositoryTest.TypeNames[2]
  );
}
