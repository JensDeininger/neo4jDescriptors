package org.rle.neo4jdescriptor.entity;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.testutils.BoolMessageWrapper;

public class RelationshipIdentifierTestProcedure
  extends EntityIdentifierTestProcedure {

  private static final RelationshipTypeDescriptor yayRelType = new RelationshipTypeDescriptor(
    "YAY"
  );

  private static final RelationshipTypeDescriptor nayRelType = new RelationshipTypeDescriptor(
    "NAY"
  );

  private static final RelationshipIdentifier relationshipIdentifier(
    boolean yayType,
    boolean yayProps
  ) {
    RelationshipIdentifier relId = RelationshipIdentifier.empty();
    relId = yayType ? relId.setType(yayRelType) : relId.setType(nayRelType);
    Stream<PropertyDescriptor> props = yayProps
      ? yayProps().properties()
      : nayProps().properties();
    for (PropertyDescriptor prop : props.toArray(PropertyDescriptor[]::new)) {
      relId.add(prop);
    }
    return relId.closeDefinition();
  }

  private static final RelationshipIdentifier[] samples() {
    RelationshipIdentifier[] singles = new RelationshipIdentifier[4];
    int i = 0;
    Boolean[] yayNay = new Boolean[] { true, false };
    for (Boolean b1 : yayNay) {
      for (Boolean b2 : yayNay) {
        singles[i++] = relationshipIdentifier(b1, b2);
      }
    }
    return singles;
  }

  private static final String singleCypher(
    boolean yayType,
    boolean yayProps,
    int index
  ) {
    String relTypeName = yayType ? yayRelType.name() : nayRelType.name();
    String propCypher = yayProps ? yayPropsCypher() : nayPropsCypher();
    return String.format("()-[r%d:%s %s]->()", index, relTypeName, propCypher);
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
    RelationshipIdentifier[] samples = samples();
    ArrayList<Integer> failingIndices = new ArrayList<>();
    for (int i = 0; i < samples.length; i++) {
      RelationshipIdentifier sample = samples[i];
      String getCypher = sample.cypher("r");
      Result res = tx.execute(getCypher);
      long counter = res.stream().count();
      if (counter != 1) {
        failingIndices.add(i);
      }
    }
    return Stream.of(new BoolMessageWrapper(failingIndices.isEmpty()));
  }

  @Procedure(mode = Mode.WRITE, name = SimpleTestProcedureName.matches_test)
  public Stream<BoolMessageWrapper> matches_test() {
    Relationship[] relations = tx
      .getAllRelationships()
      .stream()
      .toArray(Relationship[]::new);
    RelationshipIdentifier[] samples = samples();
    ArrayList<Integer> failingIndices = new ArrayList<>();
    for (int i = 0; i < samples.length; i++) {
      RelationshipIdentifier relId = samples[i];
      int matchCount = 0;
      for (int j = 0; j < relations.length; j++) {
        if (relId.matches(relations[j])) {
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
    RelationshipIdentifier[] samples = samples();
    ArrayList<Integer> failingIndices = new ArrayList<>();
    for (int i = 0; i < samples.length; i++) {
      RelationshipIdentifier relId = samples[i];
      long matchCount = relId.findRelationships(tx).count();
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
    RelationshipIdentifier[] samples = samples();
    ArrayList<Integer> failingIndices = new ArrayList<>();
    for (int i = 0; i < samples.length; i++) {
      RelationshipIdentifier relId = samples[i];
      Set<String> s1 = relId
        .findRelationships(tx)
        .map(o -> o.getElementId())
        .collect(Collectors.toSet());
      Set<String> s2 = tx
        .getAllRelationships()
        .stream()
        .filter(o -> relId.matches(o))
        .map(o -> o.getElementId())
        .collect(Collectors.toSet());
      String getRelationshipsCypher = relId.cypher("r");
      Set<String> s3 = tx
        .execute(getRelationshipsCypher)
        .stream()
        .map(o -> (Relationship) o.get("r"))
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
    RelationshipIdentifier relId = RelationshipIdentifier
      .empty()
      .closeDefinition();
    long c1 = relId.findRelationships(tx).count();
    long c2 = tx
      .getAllRelationships()
      .stream()
      .filter(o -> relId.matches(o))
      .count();
    String getRelationshipsCypher = relId.cypher("r");
    long c3 = tx
      .execute(getRelationshipsCypher)
      .stream()
      .map(o -> (Relationship) o.get("r"))
      .count();
    boolean yay = c1 == 4 && c2 == 4 && c3 == 4;
    return Stream.of(new BoolMessageWrapper(yay));
  }
}
