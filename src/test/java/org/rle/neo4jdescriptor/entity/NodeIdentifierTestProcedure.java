package org.rle.neo4jdescriptor.entity;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.testutils.BoolMessageWrapper;

public class NodeIdentifierTestProcedure extends EntityIdentifierTestProcedure {

  private static final NodeIdentifier sampleNodeIdentifier(
    boolean yayLabels,
    boolean yayProps
  ) {
    NodeIdentifier nodeId = NodeIdentifier.empty();
    Stream<LabelDescriptor> startLabels = yayLabels
      ? yayLabels().labels()
      : nayLabels().labels();
    for (LabelDescriptor lbl : startLabels.toArray(LabelDescriptor[]::new)) {
      nodeId.add(lbl);
    }
    Stream<PropertyDescriptor> props = yayProps
      ? yayProps().properties()
      : nayProps().properties();
    for (PropertyDescriptor prop : props.toArray(PropertyDescriptor[]::new)) {
      nodeId.add(prop);
    }
    return nodeId.closeDefinition();
  }

  private static final NodeIdentifier[] samples() {
    return new NodeIdentifier[] {
      sampleNodeIdentifier(true, true),
      sampleNodeIdentifier(true, false),
      sampleNodeIdentifier(false, true),
      sampleNodeIdentifier(false, false),
    };
  }

  private static final String singleCypher(
    boolean yayLabels,
    boolean yayProps,
    int index
  ) {
    LabelSet labels = yayLabels ? yayLabels() : nayLabels();
    String[] labelNames = labels
      .labels()
      .map(o -> o.name())
      .toArray(String[]::new);
    String labelBit = String.format(
      "%s:%s",
      "n" + index,
      String.join(":", labelNames)
    );
    String propCypher = yayProps ? yayPropsCypher() : nayPropsCypher();
    return String.format("(%s %s)", labelBit, propCypher);
  }

  public static final String cypherSetup() {
    String[] singleBits = new String[4];
    int i = 0;
    Boolean[] yayNay = new Boolean[] { true, false };
    for (Boolean b1 : yayNay) {
      for (Boolean b2 : yayNay) {
        singleBits[i++] = singleCypher(b1, b2, i);
      }
    }
    String s = String.format("CREATE %s;", String.join(",", singleBits));
    return s;
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.cypher_in_where_clause_test
  )
  public Stream<BoolMessageWrapper> cypher_in_where_clause_test() {
    NodeIdentifier[] samples = samples();
    ArrayList<Integer> failingIndices = new ArrayList<>();
    for (int i = 0; i < samples.length; i++) {
      NodeIdentifier sample = samples[i];
      String getNodeCypher = sample.cypher("n");
      Result res = tx.execute(getNodeCypher);
      long counter = res.stream().count();
      if (counter != 1) {
        failingIndices.add(i);
      }
    }
    return Stream.of(new BoolMessageWrapper(failingIndices.isEmpty()));
  }

  @Procedure(mode = Mode.WRITE, name = SimpleTestProcedureName.matches_test)
  public Stream<BoolMessageWrapper> matches_test() {
    Node[] nodes = tx.getAllNodes().stream().toArray(Node[]::new);
    NodeIdentifier[] samples = samples();
    ArrayList<Integer> failingIndices = new ArrayList<>();
    for (int i = 0; i < samples.length; i++) {
      NodeIdentifier nodeId = samples[i];
      int matchCount = 0;
      for (int j = 0; j < nodes.length; j++) {
        if (nodeId.matches(nodes[j])) {
          matchCount++;
        }
      }
      if (matchCount != 1) {
        failingIndices.add(i);
      }
    }
    return Stream.of(new BoolMessageWrapper(failingIndices.isEmpty()));
  }

  @Procedure(mode = Mode.WRITE, name = SimpleTestProcedureName.find_test)
  public Stream<BoolMessageWrapper> find_test() {
    NodeIdentifier[] samples = samples();
    ArrayList<Integer> failingIndices = new ArrayList<>();
    for (int i = 0; i < samples.length; i++) {
      NodeIdentifier relId = samples[i];
      long matchCount = relId.findNodes(tx).count();
      if (matchCount != 1) {
        failingIndices.add(i);
      }
    }
    return Stream.of(new BoolMessageWrapper(failingIndices.isEmpty()));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.find_match_cypher_test
  )
  public Stream<BoolMessageWrapper> find_match_cypher_test() {
    NodeIdentifier[] samples = samples();
    ArrayList<Integer> failingIndices = new ArrayList<>();
    for (int i = 0; i < samples.length; i++) {
      NodeIdentifier nodeId = samples[i];
      Set<String> s1 = nodeId
        .findNodes(tx)
        .map(o -> o.getElementId())
        .collect(Collectors.toSet());
      Set<String> s2 = tx
        .getAllNodes()
        .stream()
        .filter(o -> nodeId.matches(o))
        .map(o -> o.getElementId())
        .collect(Collectors.toSet());
      String getNodesCypher = nodeId.cypher("n");
      Set<String> s3 = tx
        .execute(getNodesCypher)
        .stream()
        .map(o -> (Node) o.get("n"))
        .map(o -> o.getElementId())
        .collect(Collectors.toSet());
      if (!s1.equals(s2) || !s1.equals(s3)) {
        failingIndices.add(i);
      }
    }
    return Stream.of(new BoolMessageWrapper(failingIndices.isEmpty()));
  }

  @Procedure(
    mode = Mode.WRITE,
    name = SimpleTestProcedureName.empty_identifier_test
  )
  public Stream<BoolMessageWrapper> empty_identifier_test() {
    NodeIdentifier nodeId = NodeIdentifier.empty().closeDefinition();
    long c1 = nodeId.findNodes(tx).count();
    long c2 = tx.getAllNodes().stream().filter(o -> nodeId.matches(o)).count();
    String getNodesCypher = nodeId.cypher("n");
    long c3 = tx
      .execute(getNodesCypher)
      .stream()
      .map(o -> (Node) o.get("n"))
      .count();
    boolean yay = c1 == 4 && c2 == 4 && c3 == 4;
    return Stream.of(new BoolMessageWrapper(yay));
  }
}
