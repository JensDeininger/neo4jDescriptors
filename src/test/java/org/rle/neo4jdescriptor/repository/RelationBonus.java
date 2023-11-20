package org.rle.neo4jdescriptor.repository;

import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class RelationBonus extends RelationshipDescriptor {

  public RelationBonus() {
    super();
    initTypeAndProperties(RelationBonus.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type = new RelationshipTypeDescriptor(
    "bonus"
  );
}
