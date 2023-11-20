package org.rle.neo4jdescriptor.repository;

import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class Relation1 extends RelationshipDescriptor {

  public Relation1() {
    super();
    initTypeAndProperties(Relation1.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
    RelationshipRepositoryTest.TypeNames[1]
  );
}
