package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.EndNode;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.annotation.StartNode;
import org.rle.neo4jdescriptor.annotation.Validate;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;
import org.rle.neo4jdescriptor.property.array_basic.LongArrayProperty;
import org.rle.neo4jdescriptor.property.prop_basic.StringProperty;

public class BarHasFooRelationDesc extends RelationshipDescriptor {

  public BarHasFooRelationDesc() {
    super();
    initTypeAndProperties(BarHasFooRelationDesc.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type =
    SampleRelationshipTypeRep.BARFOO;

  @StartNode
  public final BarNodeDesc barNode() {
    return SampleNodeRep.Bar;
  }

  @EndNode
  public final FooNodeDesc fooNode() {
    return SampleNodeRep.Foo;
  }

  @Identifying
  public final StringProperty necessaryStringProp = new StringProperty(
    "stringProp",
    "log_stringProp"
  );

  @Validate
  public final LongArrayProperty longsProp = new LongArrayProperty(
    "longsProp",
    "log_longsProp"
  );
}
