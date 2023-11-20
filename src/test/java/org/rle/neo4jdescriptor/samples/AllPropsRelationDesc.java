package org.rle.neo4jdescriptor.samples;

import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.annotation.Validate;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;
import org.rle.neo4jdescriptor.property.EnumProperty;
import org.rle.neo4jdescriptor.property.array_basic.BooleanArrayProperty;
import org.rle.neo4jdescriptor.property.array_basic.DoubleArrayProperty;
import org.rle.neo4jdescriptor.property.array_basic.LongArrayProperty;
import org.rle.neo4jdescriptor.property.array_basic.StringArrayProperty;
import org.rle.neo4jdescriptor.property.prop_basic.BooleanProperty;
import org.rle.neo4jdescriptor.property.prop_basic.DoubleProperty;
import org.rle.neo4jdescriptor.property.prop_basic.LongProperty;
import org.rle.neo4jdescriptor.property.prop_basic.NumberProperty;
import org.rle.neo4jdescriptor.property.prop_basic.ObjectProperty;
import org.rle.neo4jdescriptor.property.prop_basic.StringProperty;
import org.rle.neo4jdescriptor.property.prop_enum.dummyenums.DummyDoubleEnum;
import org.rle.neo4jdescriptor.property.prop_enum.dummyenums.DummyLongEnum;
import org.rle.neo4jdescriptor.property.prop_enum.dummyenums.DummyStringEnum;

public class AllPropsRelationDesc extends RelationshipDescriptor {

  public AllPropsRelationDesc() {
    super();
    initTypeAndProperties(AllPropsRelationDesc.class);
  }

  @Identifying
  public final RelationshipTypeDescriptor type =
    SampleRelationshipTypeRep.ALL_PROPS_REL;

  @Validate
  public final BooleanProperty boolProp = new BooleanProperty(
    "boolProp",
    "log_boolProp"
  );

  @Validate
  public final DoubleProperty doubleProp = new DoubleProperty(
    "doubleProp",
    "log_doubleProp"
  );

  @Validate
  public final LongProperty longProp = new LongProperty(
    "longProp",
    "log_longProp"
  );

  @Validate
  public final NumberProperty numberProp = new NumberProperty(
    "numberProp",
    "log_numberProp"
  );

  @Validate
  public final ObjectProperty objectProp = new ObjectProperty(
    "objectProp",
    "log_objectProp"
  );

  @Validate
  public final StringProperty stringProp = new StringProperty(
    "stringProp",
    "log_stringProp"
  );

  @Validate
  public final BooleanArrayProperty boolArrayProp = new BooleanArrayProperty(
    "boolArrayProp",
    "log_boolArrayProp"
  );

  @Validate
  public final DoubleArrayProperty doubleArrayProp = new DoubleArrayProperty(
    "doubleArrayProp",
    "log_doubleArrayProp"
  );

  @Validate
  public final LongArrayProperty longArrayProp = new LongArrayProperty(
    "longArrayProp",
    "log_longArrayProp"
  );

  @Validate
  public final StringArrayProperty stringArrayProp = new StringArrayProperty(
    "stringArrayProp",
    "log_stringArrayProp"
  );

  @Validate
  public final EnumProperty<?, ?> doubleEnumProp = new EnumProperty<Double, DummyDoubleEnum>(
    "doubleEnumProp",
    "log_doubleEnumProp",
    Double.class,
    DummyDoubleEnum.class
  );

  @Validate
  public final EnumProperty<?, ?> longEnumProp = new EnumProperty<Long, DummyLongEnum>(
    "longEnumProp",
    "log_longEnumProp",
    Long.class,
    DummyLongEnum.class
  );

  @Validate
  public final EnumProperty<?, ?> stringEnumProp = new EnumProperty<String, DummyStringEnum>(
    "stringEnumProp",
    "log_stringEnumProp",
    String.class,
    DummyStringEnum.class
  );
}
