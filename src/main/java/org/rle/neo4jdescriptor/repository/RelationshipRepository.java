package org.rle.neo4jdescriptor.repository;

import java.util.stream.Stream;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;

public class RelationshipRepository
  extends RepositoryBase<RelationshipDescriptor> {

  protected RelationshipRepository() {
    super(RelationshipDescriptor.class);
  }

  public Stream<RelationshipDescriptor> relationshipDescriptors() {
    return thingyStream();
  }

  @Override
  protected String debugName(RelationshipDescriptor thing) {
    return thing.getClass().getName();
  }
}
