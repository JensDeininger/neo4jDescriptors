package org.rle.neo4jdescriptor.repository;

import java.util.stream.Stream;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;

public class NodeRepository extends RepositoryBase<NodeDescriptor> {

  protected NodeRepository() {
    super(NodeDescriptor.class);
  }

  public Stream<NodeDescriptor> nodeDescriptors() {
    return thingyStream();
  }

  @Override
  protected String debugName(NodeDescriptor thing) {
    return thing.getClass().getName();
  }
}
