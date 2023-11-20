package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.Validate;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;

public abstract class MooNodeDesc extends NodeDescriptor {

  public MooNodeDesc() {
    super();
    initLabelsAndProperties(MooNodeDesc.class);
  }

  @Validate
  public final LabelDescriptor mooLabel = SampleLabelRep.Moo;
}
