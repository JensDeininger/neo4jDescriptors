package org.rle.neo4jdescriptor.repository;

import java.util.Optional;
import java.util.stream.Stream;
import org.rle.neo4jdescriptor.dto.DtoObjectMatch;
import org.rle.neo4jdescriptor.dto.entity.LabelDescriptorDto;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;

public class LabelRepository extends RepositoryBase<LabelDescriptor> {

  protected LabelRepository() {
    super(LabelDescriptor.class);
  }

  public Stream<LabelDescriptor> labelDescriptors() {
    return thingyStream();
  }

  public LabelDescriptor findMatch(LabelDescriptorDto dto) {
    Optional<LabelDescriptor> match = thingyStream()
      .filter(o -> DtoObjectMatch.labelMatch(o, dto))
      .findFirst();
    if (!match.isPresent()) {
      return null;
    }
    return match.get();
  }

  @Override
  protected String debugName(LabelDescriptor thing) {
    return thing.name();
  }
}
