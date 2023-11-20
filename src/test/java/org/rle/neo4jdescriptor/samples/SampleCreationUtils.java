package org.rle.neo4jdescriptor.samples;

import java.util.ArrayList;
import java.util.Iterator;
import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.rle.neo4jdescriptor.CypherUtils;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.entity.*;
import org.rle.neo4jdescriptor.enuminterface.TypedEnum;
import org.rle.neo4jdescriptor.property.*;
import org.rle.neo4jdescriptor.property.array_basic.*;
import org.rle.neo4jdescriptor.property.prop_basic.*;

public class SampleCreationUtils {

  private static final String MISSING_PROPERTY_CLAUSE =
    "forgot to add clause for the PropertyDescriptor";

  private static String[] labelNames(NodeDescriptor nodeDesc) {
    return nodeDesc
      .labels(Modality.BOTH)
      .map(o -> o.name())
      .toArray(String[]::new);
  }

  private static String sampleArrayPropertyValueCypher(
    ArrayProperty<?> arrayPropertyDescriptor
  ) {
    if (arrayPropertyDescriptor instanceof BooleanArrayProperty) {
      return CypherUtils.booleanArray2Cypher(
        new Boolean[] { true, true, false }
      );
    }
    if (arrayPropertyDescriptor instanceof DoubleArrayProperty) {
      return CypherUtils.doubleArray2Cypher(new Double[] { 0.0, 23.0 });
    }
    if (arrayPropertyDescriptor instanceof LongArrayProperty) {
      return CypherUtils.longArray2Cypher(new Long[] { 0l, 23l, 42l });
    }
    if (arrayPropertyDescriptor instanceof StringArrayProperty) {
      return CypherUtils.stringArray2Cypher(
        new String[] { "", "moo", "thingy", "123" }
      );
    } else {
      throw new IllegalArgumentException(MISSING_PROPERTY_CLAUSE);
    }
  }

  private static String sampleBasicPropertyValueCypher(
    BasicProperty<?> basicPropertyDescriptor
  ) {
    if (basicPropertyDescriptor instanceof BooleanProperty) {
      return CypherUtils.bool2Cypher(true);
    }
    if (basicPropertyDescriptor instanceof DoubleProperty) {
      return CypherUtils.double2Cypher(3.14);
    }
    if (basicPropertyDescriptor instanceof LongProperty) {
      return CypherUtils.long2Cypher(3l);
    }
    if (basicPropertyDescriptor instanceof NumberProperty) {
      if (basicPropertyDescriptor.hashCode() % 2 == 0) {
        return CypherUtils.long2Cypher(3l);
      } else {
        return CypherUtils.double2Cypher(3.14);
      }
    }
    if (basicPropertyDescriptor instanceof ObjectProperty) {
      return CypherUtils.bool2Cypher(true);
    }
    if (basicPropertyDescriptor instanceof StringProperty) {
      return CypherUtils.string2Cypher("bullerbue");
    } else {
      throw new IllegalArgumentException(MISSING_PROPERTY_CLAUSE);
    }
  }

  private static String sampleEnumPropertyValueCypher(
    EnumProperty<?, ?> enumPropertyDescriptor
  ) {
    Class<?> codeType = enumPropertyDescriptor.codeType();
    Object[] enumValues = codeType.getEnumConstants();
    Object enumValue = enumValues[0];
    TypedEnum<?> cast = TypedEnum.class.cast(enumValue);
    Object dbValue = cast.dbValue();
    Class<?> dbType = enumPropertyDescriptor.dbType();
    if (dbType.equals(Double.class)) {
      return CypherUtils.double2Cypher(Double.class.cast(dbValue));
    }
    if (dbType.equals(Long.class)) {
      return CypherUtils.long2Cypher(Long.class.cast(dbValue));
    }
    if (dbType.equals(String.class)) {
      return CypherUtils.string2Cypher(String.class.cast(dbValue));
    } else {
      throw new IllegalArgumentException(MISSING_PROPERTY_CLAUSE);
    }
  }

  private static String samplePropertyValueCypher(PropertyDescriptor propDesc) {
    String res;
    if (propDesc instanceof ArrayProperty<?>) {
      res = sampleArrayPropertyValueCypher(ArrayProperty.class.cast(propDesc));
    } else if (propDesc instanceof BasicProperty<?>) {
      res = sampleBasicPropertyValueCypher(BasicProperty.class.cast(propDesc));
    } else if (propDesc instanceof EnumProperty<?, ?>) {
      res = sampleEnumPropertyValueCypher(EnumProperty.class.cast(propDesc));
    } else {
      throw new IllegalArgumentException(MISSING_PROPERTY_CLAUSE);
    }
    return res;
  }

  private static String[] propertyClauses(EntityDescriptor entityDesc) {
    ArrayList<String> propClauses = new ArrayList<>();
    Iterator<PropertyDescriptor> propertyIter = entityDesc
      .properties(Modality.BOTH)
      .iterator();
    while (propertyIter.hasNext()) {
      PropertyDescriptor prop = propertyIter.next();
      String propCypher = samplePropertyValueCypher(prop);
      String clause = String.format("%s: %s", prop.key(), propCypher);
      propClauses.add(clause);
    }
    return propClauses.toArray(String[]::new);
  }

  public static String nodeCypher(NodeDescriptor nodeDesc, String alias) {
    String[] labelNames = labelNames(nodeDesc);
    String[] propClauses = propertyClauses(nodeDesc);
    String labelBit = String.join(":", labelNames);
    String propBit = String.join(", ", propClauses);
    String cypher = String.format("(%s:%s {%s})", alias, labelBit, propBit);
    return cypher;
  }

  public static String relationShipCypher(
    RelationshipDescriptor relDesc,
    String startNodeAlias,
    String endNodeAlias
  ) {
    String[] propClauses = propertyClauses(relDesc);
    String propBit = String.join(", ", propClauses);
    String cypher = String.format(
      "(%s)-[:%s {%s}]->(%s)",
      startNodeAlias,
      relDesc.type().name(),
      propBit,
      endNodeAlias
    );
    return cypher;
  }

  public static String createFullSampleCypher() {
    String foo = "foo";
    String bar = "bar";
    String keks = "keks";
    String allProps = "allProps";
    String fooNodeCypher = nodeCypher(SampleNodeRep.Foo, foo);
    String barNodeCypher = nodeCypher(SampleNodeRep.Bar, bar);
    String keksNodeCypher = nodeCypher(SampleNodeRep.Keks, keks);
    String allPropsNodeCypher = nodeCypher(SampleNodeRep.AllProps, allProps);
    String fooBarCypher = relationShipCypher(
      SampleRelationshipRep.FooBar,
      foo,
      bar
    );
    String barFooCypher = relationShipCypher(
      SampleRelationshipRep.BarFoo,
      bar,
      foo
    );
    String fooKeksCypher = relationShipCypher(
      SampleRelationshipRep.HasKeks,
      foo,
      keks
    );
    String barKeks = relationShipCypher(
      SampleRelationshipRep.HasKeks,
      bar,
      keks
    );
    String keksFoo = relationShipCypher(
      SampleRelationshipRep.HasFoo,
      keks,
      foo
    );
    String keksBar = relationShipCypher(
      SampleRelationshipRep.HasBar,
      keks,
      bar
    );
    String keksKeks = relationShipCypher(
      SampleRelationshipRep.IsSubKeksOf,
      keks,
      keks
    );
    String allPropsRel = relationShipCypher(
      SampleRelationshipRep.AllPropsRel,
      allProps,
      allProps
    );
    String[] bits = new String[] {
      fooNodeCypher,
      barNodeCypher,
      keksNodeCypher,
      allPropsNodeCypher,
      fooBarCypher,
      barFooCypher,
      fooKeksCypher,
      barKeks,
      keksFoo,
      keksBar,
      keksKeks,
      allPropsRel,
    };
    String finalCypher = String.format(
      "CREATE %s;",
      String.join(", " + System.lineSeparator(), bits)
    );
    return finalCypher;
  }

  ////////////////////////////////////////

  private static Object sampleArrayPropertyValue(
    ArrayProperty<?> arrayPropertyDescriptor
  ) {
    if (arrayPropertyDescriptor instanceof BooleanArrayProperty) {
      return new Boolean[] { true, true, false };
    }
    if (arrayPropertyDescriptor instanceof DoubleArrayProperty) {
      return new Double[] { 0.0, 23.0 };
    }
    if (arrayPropertyDescriptor instanceof LongArrayProperty) {
      return new Long[] { 0l, 23l, 42l };
    }
    if (arrayPropertyDescriptor instanceof StringArrayProperty) {
      return new String[] { "", "moo", "thingy", "123" };
    } else {
      throw new IllegalArgumentException(MISSING_PROPERTY_CLAUSE);
    }
  }

  private static Object sampleBasicPropertyValue(
    BasicProperty<?> basicPropertyDescriptor
  ) {
    if (basicPropertyDescriptor instanceof BooleanProperty) {
      return true;
    }
    if (basicPropertyDescriptor instanceof DoubleProperty) {
      return 3.14;
    }
    if (basicPropertyDescriptor instanceof LongProperty) {
      return 3l;
    }
    if (basicPropertyDescriptor instanceof NumberProperty) {
      if (basicPropertyDescriptor.hashCode() % 2 == 0) {
        return 3l;
      } else {
        return 3.14;
      }
    }
    if (basicPropertyDescriptor instanceof ObjectProperty) {
      return true;
    }
    if (basicPropertyDescriptor instanceof StringProperty) {
      return "bullerbue";
    } else {
      throw new IllegalArgumentException(MISSING_PROPERTY_CLAUSE);
    }
  }

  private static Object sampleEnumPropertyValue(
    EnumProperty<?, ?> enumPropertyDescriptor
  ) {
    Class<?> codeType = enumPropertyDescriptor.codeType();
    Object[] enumValues = codeType.getEnumConstants();
    Object value = enumValues[0];
    if (value instanceof Double) {
      return Double.class.cast(value);
    }
    if (value instanceof Long) {
      return Long.class.cast(value);
    }
    if (value instanceof String) {
      return String.class.cast(value);
    } else {
      throw new IllegalArgumentException(MISSING_PROPERTY_CLAUSE);
    }
  }

  public static Object sampleDbPropertyValue(PropertyDescriptor propDesc) {
    Object res;
    if (propDesc instanceof ArrayProperty<?>) {
      res = sampleArrayPropertyValue(ArrayProperty.class.cast(propDesc));
    } else if (propDesc instanceof BasicProperty<?>) {
      return sampleBasicPropertyValue(BasicProperty.class.cast(propDesc));
    } else if (propDesc instanceof EnumProperty<?, ?>) {
      return sampleEnumPropertyValue(EnumProperty.class.cast(propDesc));
    } else {
      throw new IllegalArgumentException(MISSING_PROPERTY_CLAUSE);
    }
    return res;
  }

  public static void assignDummyPropValues(
    EntityDescriptor entDesc,
    Entity entity
  ) {
    Iterator<PropertyDescriptor> propIter = entDesc
      .properties(Modality.BOTH)
      .iterator();
    while (propIter.hasNext()) {
      PropertyDescriptor prop = propIter.next();
      Object value = sampleDbPropertyValue(prop);
      entity.setProperty(prop.key(), value);
    }
  }

  public static Node createSampleNode(Transaction tx, NodeDescriptor nodeDesc) {
    LabelDescriptor[] labels = nodeDesc
      .labels(Modality.BOTH)
      .toArray(LabelDescriptor[]::new);
    Node node = tx.createNode(labels);
    assignDummyPropValues(nodeDesc, node);
    return node;
  }

  public static Relationship createSampleRelationship(
    Transaction tx,
    Node startNode,
    RelationshipTypeDescriptor relTypeDesc,
    Node endNode
  ) {
    Relationship rel = startNode.createRelationshipTo(endNode, relTypeDesc);
    return rel;
  }

  public static Relationship createSampleRelationship(
    Transaction tx,
    Node startNode,
    RelationshipDescriptor relDesc,
    Node endNode
  ) {
    Relationship rel = startNode.createRelationshipTo(endNode, relDesc.type());
    assignDummyPropValues(relDesc, rel);
    return rel;
  }
}
