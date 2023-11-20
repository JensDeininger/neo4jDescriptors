package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.RepositoryMember;
import org.rle.neo4jdescriptor.repository.RelationshipRepository;

public class SampleRelationshipRep extends RelationshipRepository {

  @RepositoryMember
  public static final FooHasBarRelationDesc FooBar = new FooHasBarRelationDesc();

  @RepositoryMember
  public static final BarHasFooRelationDesc BarFoo = new BarHasFooRelationDesc();

  @RepositoryMember
  public static final HasKeksRelationDesc HasKeks = new HasKeksRelationDesc();

  @RepositoryMember
  public static final IsSubKeksOfRelationDesc IsSubKeksOf = new IsSubKeksOfRelationDesc();

  @RepositoryMember
  public static final HasFooRelationDesc HasFoo = new HasFooRelationDesc();

  @RepositoryMember
  public static final HasBarRelationDesc HasBar = new HasBarRelationDesc();

  @RepositoryMember
  public static final EndsAtKeksRelationDesc EndsAtKeks = new EndsAtKeksRelationDesc();

  @RepositoryMember
  public static final AllPropsRelationDesc AllPropsRel = new AllPropsRelationDesc();

  @RepositoryMember
  public static final HasTopRelationDesc HasTop = new HasTopRelationDesc();

  @RepositoryMember
  public static final HasMiddleRelationDesc HasMiddle = new HasMiddleRelationDesc();

  @RepositoryMember
  public static final HasBottomRelationDesc HasBottom = new HasBottomRelationDesc();
}
