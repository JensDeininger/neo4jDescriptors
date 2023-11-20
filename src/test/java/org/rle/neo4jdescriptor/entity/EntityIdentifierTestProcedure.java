package org.rle.neo4jdescriptor.entity;

import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.rle.neo4jdescriptor.property.array_basic.BooleanArrayProperty;
import org.rle.neo4jdescriptor.property.array_basic.DoubleArrayProperty;
import org.rle.neo4jdescriptor.property.array_basic.LongArrayProperty;
import org.rle.neo4jdescriptor.property.array_basic.StringArrayProperty;
import org.rle.neo4jdescriptor.property.prop_basic.BooleanProperty;
import org.rle.neo4jdescriptor.property.prop_basic.DoubleProperty;
import org.rle.neo4jdescriptor.property.prop_basic.LongProperty;
import org.rle.neo4jdescriptor.property.prop_basic.StringProperty;

public abstract class EntityIdentifierTestProcedure {

  @Context
  public Transaction tx;

  protected static final LabelSet yayLabels() {
    return LabelSet
      .empty()
      .add(new LabelDescriptor("YayLabel1"))
      .add(new LabelDescriptor("YayLabel2"))
      .add(new LabelDescriptor("YayLabel3"))
      .closeDefinition();
  }

  protected static final LabelSet nayLabels() {
    return LabelSet
      .empty()
      .add(new LabelDescriptor("NayLabel1"))
      .add(new LabelDescriptor("NayLabel2"))
      .closeDefinition();
  }

  protected static final PropertySet yayProps() {
    return PropertySet
      .empty()
      .add(new BooleanProperty("boolProp"))
      .add(new DoubleProperty("doubleProp"))
      .add(new LongArrayProperty("longArrayProp"))
      .add(new StringArrayProperty("stringArrayProp"))
      .closeDefinition();
  }

  protected static final String yayPropsCypher() {
    String[] singleBits = new String[] {
      "boolProp:true",
      "doubleProp:1.23",
      "longArrayProp:[1,2,3]",
      "stringArrayProp:['foo', 'bar']",
    };
    return String.format("{%s}", String.join(", ", singleBits));
  }

  protected static final PropertySet nayProps() {
    return PropertySet
      .empty()
      .add(new BooleanArrayProperty("boolArrayProp"))
      .add(new DoubleArrayProperty("doubleArrayProp"))
      .add(new LongProperty("longProp"))
      .add(new StringProperty("stringProp"))
      .closeDefinition();
  }

  protected static final String nayPropsCypher() {
    String[] singleBits = new String[] {
      "boolArrayProp:[true, true, false]",
      "doubleArrayProp:[1.23, 0.0]",
      "longProp:23",
      "stringProp:'foo'",
    };
    return String.format("{%s}", String.join(", ", singleBits));
  }

  public static class SimpleTestProcedureName {

    public static final String cypher_in_where_clause_test =
      "cypher_in_where_clause_test";

    public static final String matches_test = "matches_test";

    public static final String find_test = "find_test";

    public static final String find_match_cypher_test =
      "find_matches_cypher_test";

    public static final String empty_identifier_test = "empty_identifier_test";
  }
}
