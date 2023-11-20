package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.RepositoryMember;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.repository.LabelRepository;

public class SampleLabelRep extends LabelRepository {

  private SampleLabelRep() {
    super();
  }

  @RepositoryMember
  public static final LabelDescriptor Foo = new LabelDescriptor(
    "Foo",
    "log_Foo"
  );

  @RepositoryMember
  public static final LabelDescriptor Bar = new LabelDescriptor(
    "Bar",
    "log_Bar"
  );

  @RepositoryMember
  public static final LabelDescriptor Keks = new LabelDescriptor(
    "Keks",
    "log_Keks"
  );

  @RepositoryMember
  public static final LabelDescriptor Moo = new LabelDescriptor(
    "Moo",
    "log_Moo"
  );

  @RepositoryMember
  public static final LabelDescriptor AllProps = new LabelDescriptor(
    "AllProps",
    "log_AllProps"
  );

  @RepositoryMember
  public static final LabelDescriptor TopIdLabel = new LabelDescriptor(
    "TopIdLabel",
    "log_TopIdLabel"
  );

  @RepositoryMember
  public static final LabelDescriptor TopBonusLabel = new LabelDescriptor(
    "TopBonusLabel",
    "log_TopBonusLabel"
  );

  @RepositoryMember
  public static final LabelDescriptor MiddleIdLabel = new LabelDescriptor(
    "MiddleIdLabel",
    "log_MiddleIdLabel"
  );

  @RepositoryMember
  public static final LabelDescriptor MiddleBonusLabel = new LabelDescriptor(
    "MiddleBonusLabel",
    "log_MiddleBonusLabel"
  );

  @RepositoryMember
  public static final LabelDescriptor BottomIdLabel = new LabelDescriptor(
    "BottomIdLabel",
    "log_BottomIdLabel"
  );

  @RepositoryMember
  public static final LabelDescriptor BottomBonusLabel = new LabelDescriptor(
    "BottomBonusLabel",
    "log_BottomBonusLabel"
  );
}
