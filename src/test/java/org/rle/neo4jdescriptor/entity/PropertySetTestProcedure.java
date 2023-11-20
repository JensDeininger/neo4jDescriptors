package org.rle.neo4jdescriptor.entity;

import java.util.Iterator;
import java.util.stream.Stream;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;
import org.rle.neo4jdescriptor.CypherUtils;
import org.rle.neo4jdescriptor.property.array_basic.StringArrayProperty;
import org.rle.neo4jdescriptor.property.prop_basic.*;
import org.rle.neo4jdescriptor.testutils.BoolMessageWrapper;

public class PropertySetTestProcedure {

  @Context
  public Transaction tx;

  private static final BooleanProperty boolProp = new BooleanProperty(
    "boolProp"
  );

  private static final DoubleProperty doubleProp = new DoubleProperty(
    "doubleProp"
  );

  private static final StringArrayProperty stringArrayProp = new StringArrayProperty(
    "strArray"
  );

  private static final Label yayLabel = Label.label("Yay");

  private static final Label nayLabel = Label.label("Nay");

  private static final RelationshipTypeDescriptor yay2NayRel = new RelationshipTypeDescriptor(
    "YAY2NAY"
  );

  private static final RelationshipTypeDescriptor nay2YayRel = new RelationshipTypeDescriptor(
    "NAY2YAY"
  );

  private static final RelationshipTypeDescriptor yayRel = new RelationshipTypeDescriptor(
    "YAY"
  );

  private static final PropertySet propertySpecification() {
    PropertySet propSpec = PropertySet.empty();
    propSpec.add(boolProp);
    propSpec.add(doubleProp);
    propSpec.add(stringArrayProp);
    return propSpec.closeDefinition();
  }

  private static String yayNodeCypher(String alias) {
    return String.format(
      "(%s:%s {%s: %s, %s: %s, %s: %s})",
      alias,
      yayLabel.name(),
      boolProp.key(),
      CypherUtils.bool2Cypher(true),
      doubleProp.key(),
      CypherUtils.double2Cypher(1.23),
      stringArrayProp.key(),
      CypherUtils.stringArray2Cypher(new String[] { "foo", "bar" })
    );
  }

  private static String nayNodeCypher(String alias) {
    return String.format(
      "(%s:%s {%s: %s, %s: %s})",
      alias,
      nayLabel.name(),
      boolProp.key(),
      CypherUtils.long2Cypher(23l),
      doubleProp.key(),
      CypherUtils.string2Cypher("keks")
    );
  }

  private static String yayRelationCypher(String relAlias) {
    return String.format(
      "%s:%s {%s: %s, %s: %s, %s: %s}",
      relAlias,
      yayRel.name(),
      boolProp.key(),
      CypherUtils.bool2Cypher(true),
      doubleProp.key(),
      CypherUtils.double2Cypher(1.23),
      stringArrayProp.key(),
      CypherUtils.stringArray2Cypher(new String[] { "foo", "bar" })
    );
  }

  public static String cypherSetup() {
    String yay2Nay = String.format(
      "%s -[:%s]-> %s",
      yayNodeCypher("y1"),
      yay2NayRel.name(),
      nayNodeCypher("n1")
    );
    String nay2Yay = String.format(
      "%s <-[:%s]- %s",
      yayNodeCypher("y2"),
      nay2YayRel.name(),
      nayNodeCypher("n2")
    );
    String yayRel = String.format(
      "(:Dummy1)-[%s]->(:Dummy2)",
      yayRelationCypher("r")
    );
    return String.format("CREATE %s, %s, %s;", yay2Nay, nay2Yay, yayRel);
  }

  public static class SimpleTestProcedureName {

    public static final String cypher_in_nodes_where_clause_test =
      "cypher_in_nodes_where_clause_test";

    public static final String cypher_in_relationship_where_clause_test =
      "cypher_in_relationship_where_clause_test";

    public static final String matches_node_test = "matches_node_test";

    public static final String matches_relation_test = "matches_relation_test";

    public static final String filter_node_stream_test =
      "filter_node_stream_test";

    public static final String filter_relationship_stream_test =
      "filter_relationship_stream_test";

    public static final String empty_test = "empty_test";
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.cypher_in_nodes_where_clause_test
  )
  public Stream<BoolMessageWrapper> cypher_in_nodes_where_clause_test() {
    String getNodesCypher = String.format(
      "MATCH (n) WHERE %s RETURN n",
      propertySpecification().propertiesCypher("n")
    );
    Result res = tx.execute(getNodesCypher);
    int nodeCounter = 0;
    while (res.hasNext()) {
      res.next();
      nodeCounter++;
    }
    return Stream.of(new BoolMessageWrapper(nodeCounter == 2));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.cypher_in_relationship_where_clause_test
  )
  public Stream<BoolMessageWrapper> cypher_in_relationship_where_clause_test() {
    String getNodesCypher = String.format(
      "MATCH ()-[r]->() WHERE %s RETURN r",
      propertySpecification().propertiesCypher("r")
    );
    Result res = tx.execute(getNodesCypher);
    int nodeCounter = 0;
    while (res.hasNext()) {
      res.next();
      nodeCounter++;
    }
    return Stream.of(new BoolMessageWrapper(nodeCounter == 1));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.matches_node_test
  )
  public Stream<BoolMessageWrapper> matches_node_test() {
    Iterator<Node> nodeIter = tx.getAllNodes().iterator();
    int count = 0;
    PropertySet propSpec = propertySpecification();
    while (nodeIter.hasNext()) {
      Node node = nodeIter.next();
      if (propSpec.matches(node)) {
        count++;
      }
    }
    return Stream.of(new BoolMessageWrapper(count == 2));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.matches_relation_test
  )
  public Stream<BoolMessageWrapper> matches_relation_test() {
    Iterator<Relationship> relIter = tx.getAllRelationships().iterator();
    int count = 0;
    PropertySet propSpec = propertySpecification();
    while (relIter.hasNext()) {
      Relationship rel = relIter.next();
      if (propSpec.matches(rel)) {
        count++;
      }
    }
    return Stream.of(new BoolMessageWrapper(count == 1));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.filter_node_stream_test
  )
  public Stream<BoolMessageWrapper> filter_node_stream_test() {
    Stream<Node> nodeStream = tx.getAllNodes().stream();
    PropertySet propSpec = propertySpecification();
    nodeStream = propSpec.filter(nodeStream);
    Node[] nodes = nodeStream.toArray(Node[]::new);
    return Stream.of(new BoolMessageWrapper(nodes.length == 2));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.filter_relationship_stream_test
  )
  public Stream<BoolMessageWrapper> filter_relationship_stream_test() {
    Stream<Relationship> relStream = tx.getAllRelationships().stream();
    PropertySet propSpec = propertySpecification();
    relStream = propSpec.filter(relStream);
    Relationship[] relationships = relStream.toArray(Relationship[]::new);
    return Stream.of(new BoolMessageWrapper(relationships.length == 1));
  }

  @Procedure(mode = Mode.WRITE, name = SimpleTestProcedureName.empty_test)
  public Stream<BoolMessageWrapper> empty_test() {
    PropertySet emptySpec = PropertySet.empty();
    long count = tx
      .getAllNodes()
      .stream()
      .filter(o -> emptySpec.matches(o))
      .count();
    return Stream.of(new BoolMessageWrapper(count == 6));
  }
}
