package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.EndNode;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.annotation.Validate;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;
import org.rle.neo4jdescriptor.property.array_basic.LongArrayProperty;
import org.rle.neo4jdescriptor.property.prop_basic.StringProperty;

public class HasKeksRelationDesc extends RelationshipDescriptor {

  public HasKeksRelationDesc() {
    super();
    initTypeAndProperties(HasKeksRelationDesc.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type =
    SampleRelationshipTypeRep.HAS_KEKS;

  @EndNode
  public final KeksNodeDesc keksNode() {
    return SampleNodeRep.Keks;
  }

  @Identifying
  public final StringProperty necessaryStringProp1 = new StringProperty(
    "stringProp1",
    "log_stringProp1"
  );

  @Identifying
  public final StringProperty necessaryStringProp2 = new StringProperty(
    "stringProp2",
    "log_stringProp2"
  );

  @Validate
  public final LongArrayProperty longsProp1 = new LongArrayProperty(
    "longsProp1",
    "log_longsProp1"
  );

  @Validate
  public final LongArrayProperty longsProp2 = new LongArrayProperty(
    "longsProp2",
    "log_longsProp2"
  );
}
