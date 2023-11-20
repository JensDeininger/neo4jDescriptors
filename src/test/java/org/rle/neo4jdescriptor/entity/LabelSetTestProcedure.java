package org.rle.neo4jdescriptor.entity;

import java.util.Iterator;
import java.util.stream.Stream;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;
import org.rle.neo4jdescriptor.testutils.BoolMessageWrapper;

public class LabelSetTestProcedure {

  @Context
  public Transaction tx;

  private static final LabelDescriptor label1 = new LabelDescriptor("label1");

  private static final LabelDescriptor label2 = new LabelDescriptor("label2");

  private static final LabelDescriptor label3 = new LabelDescriptor("label3");

  private static final LabelSet labelSpecification() {
    return LabelSet
      .empty()
      .add(label1)
      .add(label2)
      .add(label3)
      .closeDefinition();
  }

  private static final String yayNodeCypher(String alias) {
    return String.format(
      "(%s:%s:%s:%s)",
      alias,
      label1.name(),
      label2.name(),
      label3.name()
    );
  }

  private static final String nayNodeCypher(String alias) {
    return String.format(
      "(%s:%s:%s:%s)",
      alias,
      label1.name(),
      label2.name() + "foo",
      label3.name()
    );
  }

  private static final String singlePair(Boolean a, Boolean b) {
    return String.format(
      "%s-[:%s_%s]->%s",
      a ? yayNodeCypher("") : nayNodeCypher(""),
      a ? "YAY" : "NAY",
      b ? "YAY" : "NAY",
      b ? yayNodeCypher("") : nayNodeCypher("")
    );
  }

  public static String cypherSetup() {
    // 7 x yay node, 4 start, 3 end
    // 5 x nay node, 2 start, 3 end
    return String.format(
      "CREATE %s, %s, %s, %s, %s, %s;",
      singlePair(true, true),
      singlePair(true, true),
      singlePair(true, false),
      singlePair(true, false),
      singlePair(false, true),
      singlePair(false, false)
    );
  }

  public static class SimpleTestProcedureName {

    public static final String cypher_in_where_clause_test =
      "cypher_in_where_clause_test";

    public static final String matches_node_test = "matches_node_test";

    public static final String filter_node_stream_test =
      "filter_node_stream_test";

    public static final String filter_relationship_stream_start_test =
      "filter_relationship_stream_start_test";

    public static final String filter_relationship_stream_end_test =
      "filter_relationship_stream_end_test";

    public static final String node_empty_test = "node_empty_test";

    public static final String relationship_empty_test =
      "relationship_empty_test";
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.cypher_in_where_clause_test
  )
  public Stream<BoolMessageWrapper> cypher_in_where_clause_test() {
    String getNodesCypher = String.format(
      "MATCH (n) WHERE %s RETURN n",
      labelSpecification().cypher("n")
    );
    Result res = tx.execute(getNodesCypher);
    long nodeCounter = res.stream().count();
    return Stream.of(new BoolMessageWrapper(nodeCounter == 7));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.matches_node_test
  )
  public Stream<BoolMessageWrapper> matches_node_test() {
    Iterator<Node> nodeIter = tx.getAllNodes().iterator();
    int yayCount = 0;
    int nayCount = 0;
    LabelSet lblSpec = labelSpecification();
    while (nodeIter.hasNext()) {
      Node node = nodeIter.next();
      if (lblSpec.matches(node)) {
        yayCount++;
      } else {
        nayCount++;
      }
    }
    return Stream.of(new BoolMessageWrapper(yayCount == 7 && nayCount == 5));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.filter_node_stream_test
  )
  public Stream<BoolMessageWrapper> filter_node_stream_test() {
    Stream<Node> nodeStream = tx.getAllNodes().stream();
    LabelSet lblSpec = labelSpecification();
    nodeStream = lblSpec.filter(nodeStream);
    Node[] nodes = nodeStream.toArray(Node[]::new);
    return Stream.of(new BoolMessageWrapper(nodes.length == 7));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.filter_relationship_stream_start_test
  )
  public Stream<BoolMessageWrapper> filter_relationship_stream_start_test() {
    Stream<Relationship> relStream = tx.getAllRelationships().stream();
    LabelSet lblSpec = labelSpecification();
    relStream = lblSpec.filterStartNode(relStream);
    Relationship[] relationships = relStream.toArray(Relationship[]::new);
    return Stream.of(new BoolMessageWrapper(relationships.length == 4));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.filter_relationship_stream_end_test
  )
  public Stream<BoolMessageWrapper> filter_relationship_stream_end_test() {
    Stream<Relationship> relStream = tx.getAllRelationships().stream();
    LabelSet lblSpec = labelSpecification();
    relStream = lblSpec.filterEndNode(relStream);
    Relationship[] relationships = relStream.toArray(Relationship[]::new);
    return Stream.of(new BoolMessageWrapper(relationships.length == 3));
  }

  @Procedure(mode = Mode.WRITE, name = SimpleTestProcedureName.node_empty_test)
  public Stream<BoolMessageWrapper> node_empty_test() {
    LabelSet emptySpec = LabelSet.empty();
    String whereClause = emptySpec.cypher("n");
    long nodeCount = tx
      .getAllNodes()
      .stream()
      .filter(o -> emptySpec.matches(o))
      .count();
    return Stream.of(
      new BoolMessageWrapper(whereClause == "" && nodeCount == 12)
    );
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.relationship_empty_test
  )
  public Stream<BoolMessageWrapper> relationship_empty_test() {
    LabelSet emptySpec = LabelSet.empty();
    long c1 = emptySpec
      .filterStartNode(tx.getAllRelationships().stream())
      .count();
    long c2 = emptySpec
      .filterEndNode(tx.getAllRelationships().stream())
      .count();
    return Stream.of(new BoolMessageWrapper(c1 == 6 && c2 == 6));
  }
}
