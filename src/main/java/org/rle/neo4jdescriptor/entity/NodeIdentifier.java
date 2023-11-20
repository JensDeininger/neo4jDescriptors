package org.rle.neo4jdescriptor.entity;

import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * Describes a set of {@link Node}s by specifying <p>
 * - the {@link LabelDescriptor}s these {@link Node}s must have <p>
 * - the {@link PropertyDescriptor}s that must apply <p>
 * Closing the definition is not required but strongly recommended because it speeds
 * up the hashCode computation.<p>
 * {@link NodeIdentifier} overrides equality and hashCode and is immutable once
 * closed.
 */
@SuppressWarnings("java:S2160") // override equals in derived classes that have new fields. Not necessary here
public class NodeIdentifier extends EntityIdentifier {

  //region fields

  private LabelSet mLabels = new LabelSet();

  //endregion

  //region ctor

  private NodeIdentifier() {}

  public NodeIdentifier(String labelName) {
    this(new LabelDescriptor(labelName));
  }

  public NodeIdentifier(Label label) {
    this(new LabelDescriptor(label == null ? null : label.name()));
  }

  public NodeIdentifier(LabelDescriptor labelDescriptor) {
    if (labelDescriptor == null) {
      throw new IllegalArgumentException(M_ARGUMENT_MUST_NOT_BE_NULL_MSG);
    }
    mLabels.add(labelDescriptor);
  }

  public static NodeIdentifier empty() {
    return new NodeIdentifier();
  }

  //endregion

  //region getters

  public int labelCount() {
    return mLabels.labelCount();
  }

  public Stream<LabelDescriptor> labels() {
    return mLabels.labels();
  }

  public LabelSet labelSet() {
    return mLabels;
  }

  public NodeIdentifier openCopy() {
    NodeIdentifier copy = new NodeIdentifier();
    copy.mLabels = mLabels.openCopy();
    this.copyPropertiesOnto(copy);
    return copy;
  }

  public String print(String mainIndent, String subIndent) {
    String lblPart = mLabels.print(mainIndent, subIndent);
    String propPart = propertySet().print(mainIndent, subIndent);
    if (lblPart.length() == 0 || propPart.length() == 0) {
      return lblPart + propPart;
    }
    return lblPart + System.lineSeparator() + propPart;
  }

  //endregion

  //region fluent like config methods

  public NodeIdentifier add(LabelDescriptor labelDescriptor) {
    isOpenOrThrow();
    mLabels.add(labelDescriptor);
    resetHash();
    return this;
  }

  @Override
  public NodeIdentifier add(PropertyDescriptor propertyDescriptor) {
    return (NodeIdentifier) super.add(propertyDescriptor);
  }

  @Override
  public NodeIdentifier closeDefinition() {
    super.closeDefinition();
    mLabels.closeDefinition();
    return this;
  }

  // endregion

  // region match and find nodes

  /*
   * Match (n) WHERE
   * n:CompanySize:Meta                AND   // required labels. I can chain the required ones!
   * n.name IS NOT NULL                AND   // required prop 1
   * n.prop1 IS NOT NULL               AND   // required prop 2
   * n.value = 2.0                     AND   // required propValuePair 1
   * n.keks = 'whatever'               AND   // required propValuePair 2
   * RETURN n
   */

  /**
   * Returns a cypher statement of the kind 'MATCH (n: ... ) WHERE ... RETURN n'
   * that will find the nodes identified by this NodeIdentifier.
   */
  public String cypher(String nodeAlias) {
    String[] bits = Stream
      .of(mLabels.cypher(nodeAlias), super.propCypher(nodeAlias))
      .filter(o -> o.length() > 0)
      .toArray(String[]::new);
    String conditions = String.join(" " + M_AND + System.lineSeparator(), bits);
    String start = String.format("MATCH (%s)", nodeAlias);
    if (conditions.length() > 0) {
      start = start + " WHERE";
    }
    String end = "RETURN " + nodeAlias;
    String[] finalBits = Stream
      .of(start, conditions, end)
      .filter(o -> o.length() > 0)
      .toArray(String[]::new);
    return String.join(System.lineSeparator(), finalBits);
  }

  public boolean matches(Node node) {
    if (!mLabels.matches(node)) {
      return false;
    }
    return super.propMatches(node);
  }

  public Stream<Node> findNodes(Transaction tx) {
    Stream<Node> nodeStream;
    Iterator<LabelDescriptor> reqLabelIter = mLabels.labels().iterator();
    if (!reqLabelIter.hasNext()) {
      nodeStream = tx.getAllNodes().stream();
    } else {
      Label firstLabel = reqLabelIter.next();
      nodeStream = tx.findNodes(firstLabel).stream();
    }
    while (reqLabelIter.hasNext()) {
      Label label = reqLabelIter.next();
      nodeStream = nodeStream.filter(o -> o.hasLabel(label));
    }

    nodeStream = super.filter(nodeStream);
    return nodeStream;
  }

  //endregion

  //region equality overrides

  @Override
  protected int processHash() {
    return Objects.hash(super.processHash(), mLabels);
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof NodeIdentifier)) {
      return false;
    }
    NodeIdentifier cast = NodeIdentifier.class.cast(obj);
    return this.mLabels.equals(cast.mLabels);
  }

  @Override
  protected int processCompare(DefinitionBase other) {
    if (other == this) {
      return 0;
    }
    int superComp = super.processCompare(other);
    if (superComp != 0) {
      return superComp;
    }
    NodeIdentifier cast = NodeIdentifier.class.cast(other);
    return this.mLabels.compareTo(cast.mLabels);
  }
  //endregion
}
