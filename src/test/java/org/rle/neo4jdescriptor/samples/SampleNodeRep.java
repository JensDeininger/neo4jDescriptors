package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.RepositoryMember;
import org.rle.neo4jdescriptor.repository.NodeRepository;

public class SampleNodeRep extends NodeRepository {

  @RepositoryMember
  public static final FooNodeDesc Foo = new FooNodeDesc();

  @RepositoryMember
  public static final BarNodeDesc Bar = new BarNodeDesc();

  @RepositoryMember
  public static final KeksNodeDesc Keks = new KeksNodeDesc();

  @RepositoryMember
  public static final AllPropsNodeDesc AllProps = new AllPropsNodeDesc();

  @RepositoryMember
  public static final NodeDescTop Top = new NodeDescTop();

  @RepositoryMember
  public static final NodeDescMiddle Middle = new NodeDescMiddle();

  @RepositoryMember
  public static final NodeDescBottom Bottom = new NodeDescBottom();
}
