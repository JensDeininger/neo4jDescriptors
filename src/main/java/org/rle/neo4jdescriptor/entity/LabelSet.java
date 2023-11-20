package org.rle.neo4jdescriptor.entity;

import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.rle.neo4jdescriptor.EqualityUtils;

@SuppressWarnings("java:S2160") // override equals in derived classes that have new fields. Not necessary here
public class LabelSet extends DefinitionBase {

  // region private fields

  private final TreeSet<LabelDescriptor> mLabels = new TreeSet<>();

  // endregion

  // region Ctor

  public LabelSet() {}

  public LabelSet(Iterable<LabelDescriptor> labels) {
    if (labels == null) {
      throw new IllegalArgumentException(M_ARGUMENT_MUST_NOT_BE_NULL_MSG);
    }
    for (LabelDescriptor lbl : labels) {
      mLabels.add(lbl);
    }
  }

  public LabelSet(LabelDescriptor... labels) {
    if (labels != null) {
      mLabels.addAll(Arrays.asList(labels));
    }
  }

  public LabelSet(Stream<LabelDescriptor> labels) {
    if (labels == null) {
      throw new IllegalArgumentException(M_ARGUMENT_MUST_NOT_BE_NULL_MSG);
    }
    labels.forEach(mLabels::add);
  }

  public static LabelSet empty() {
    return new LabelSet();
  }

  // endregion

  // region accessors

  public boolean isEmpty() {
    return mLabels.isEmpty();
  }

  public int labelCount() {
    return mLabels.size();
  }

  public Stream<LabelDescriptor> labels() {
    return mLabels.stream();
  }

  public LabelSet openCopy() {
    LabelSet copy = new LabelSet();
    for (LabelDescriptor lbl : mLabels) {
      copy.mLabels.add(lbl.copy());
    }
    return copy;
  }

  public String print(String mainIndent, String subIndent) {
    if (mLabels.isEmpty()) {
      return "";
    }
    String[] propStr = mLabels
      .stream()
      .map(o -> o.print(""))
      .toArray(String[]::new);
    String mI = mainIndent == null ? "" : mainIndent;
    String sI = subIndent == null ? "" : subIndent;
    String sep = System.lineSeparator() + mI + sI;
    String res = String.join(sep, propStr);
    return mI + sI + res;
  }

  // endregion

  // region config methods

  public LabelSet add(LabelDescriptor lbl) {
    if (lbl == null) {
      throw new IllegalArgumentException(M_ARGUMENT_MUST_NOT_BE_NULL_MSG);
    }
    isOpenOrThrow();
    if (mLabels.add(lbl)) {
      resetHash();
    }
    return this;
  }

  @Override
  public LabelSet closeDefinition() {
    super.closeDefinition();
    return this;
  }

  // endregion

  // region cypher, match, filter

  /**
   * Returns something like (n:Label1:Label2:Label3) <p>
   * Or an empty String if there are no labels
   */
  public String cypher(String nodeAlias) {
    if (mLabels.isEmpty()) {
      return "";
    }
    String[] labelNames = mLabels
      .stream()
      .map(LabelDescriptor::name)
      .toArray(String[]::new);
    return String.format("(%s:%s)", nodeAlias, String.join(":", labelNames));
  }

  public boolean matches(Node node) {
    for (LabelDescriptor lbl : mLabels) {
      if (!node.hasLabel(lbl)) {
        return false;
      }
    }
    return true;
  }

  public Stream<Node> filter(Stream<Node> nodeStream) {
    return nodeStream.filter(this::matches);
  }

  public Stream<Relationship> filterStartNode(Stream<Relationship> relStream) {
    return relStream.filter(o -> matches(o.getStartNode()));
  }

  public Stream<Relationship> filterEndNode(Stream<Relationship> relStream) {
    return relStream.filter(o -> matches(o.getEndNode()));
  }

  //endregion

  // region equality and compare overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      EqualityUtils.sortedSetHash(mLabels)
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof LabelSet)) {
      return false;
    }
    LabelSet cast = LabelSet.class.cast(obj);
    return EqualityUtils.setEquals(mLabels, cast.mLabels);
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
    LabelSet cast = LabelSet.class.cast(other);
    return EqualityUtils.sortedSetCompare(mLabels, cast.mLabels);
  }
  //endregion
}
