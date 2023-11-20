package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.RepositoryMember;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;
import org.rle.neo4jdescriptor.repository.RelationshipTypeRepository;

public class SampleRelationshipTypeRep extends RelationshipTypeRepository {

  private SampleRelationshipTypeRep() {
    super();
  }

  @RepositoryMember
  public static final RelationshipTypeDescriptor FOOBAR = new RelationshipTypeDescriptor(
    "FOOBAR",
    "log_FOOBAR"
  );

  @RepositoryMember
  public static final RelationshipTypeDescriptor BARFOO = new RelationshipTypeDescriptor(
    "BARFOO",
    "log_BARFOO"
  );

  @RepositoryMember
  public static final RelationshipTypeDescriptor HAS_KEKS = new RelationshipTypeDescriptor(
    "HAS_KEKS",
    "log_HAS_KEKS"
  );

  @RepositoryMember
  public static final RelationshipTypeDescriptor IS_SUB_KEKS_OF = new RelationshipTypeDescriptor(
    "IS_SUB_KEKS_OF",
    "log_IS_SUB_KEKS_OF"
  );

  @RepositoryMember
  public static final RelationshipTypeDescriptor HAS_FOO = new RelationshipTypeDescriptor(
    "HAS_FOO",
    "log_HAS_FOO"
  );

  @RepositoryMember
  public static final RelationshipTypeDescriptor HAS_BAR = new RelationshipTypeDescriptor(
    "HAS_BAR",
    "log_HAS_BAR"
  );

  @RepositoryMember
  public static final RelationshipTypeDescriptor ALL_PROPS_REL = new RelationshipTypeDescriptor(
    "ALL_PROPS_REL",
    "log_ALL_PROPS_REL"
  );

  @RepositoryMember
  public static final RelationshipTypeDescriptor HAS_TOP = new RelationshipTypeDescriptor(
    "HAS_TOP",
    "log_HAS_TOP"
  );

  @RepositoryMember
  public static final RelationshipTypeDescriptor HAS_MIDDLE = new RelationshipTypeDescriptor(
    "HAS_MIDDLE",
    "log_HAS_MIDDLE"
  );

  @RepositoryMember
  public static final RelationshipTypeDescriptor HAS_BOTTOM = new RelationshipTypeDescriptor(
    "HAS_BOTTOM",
    "log_HAS_BOTTOM"
  );
}
