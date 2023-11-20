package org.rle.neo4jdescriptor.repository;

import java.util.Optional;
import java.util.stream.Stream;
import org.rle.neo4jdescriptor.dto.DtoObjectMatch;
import org.rle.neo4jdescriptor.dto.entity.RelationshipTypeDescriptorDto;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;

public class RelationshipTypeRepository
  extends RepositoryBase<RelationshipTypeDescriptor> {

  protected RelationshipTypeRepository() {
    super(RelationshipTypeDescriptor.class);
  }

  public Stream<RelationshipTypeDescriptor> relationshipTypeDescriptors() {
    return thingyStream();
  }

  public RelationshipTypeDescriptor findMatch(
    RelationshipTypeDescriptorDto dto
  ) {
    Optional<RelationshipTypeDescriptor> match = thingyStream()
      .filter(o -> DtoObjectMatch.relationshipTypeMatch(o, dto))
      .findFirst();
    if (!match.isPresent()) {
      return null;
    }
    return match.get();
  }

  @Override
  protected String debugName(RelationshipTypeDescriptor thing) {
    return thing.name();
  }
}
