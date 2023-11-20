package org.rle.neo4jdescriptor.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.annotation.Validate;
import org.rle.neo4jdescriptor.report.NodeDescriptorReport;
import org.rle.neo4jdescriptor.report.NodeRelationReport;
import org.rle.neo4jdescriptor.report.NodeReport;

public abstract class NodeDescriptor extends EntityDescriptor {

  // region static strings

  protected static final String M_LABEL_FIELDS_MUST_BE_PUBLIC_FINAL =
    "LabelDescriptor fields must be public and final";

  protected static final String M_LABEL_FIELDS_MUST_BE_ANNOTATED =
    "LabelDescriptor fields must be annotated as either Identifying or Validate";

  protected static final String M_DUPLICATE_LABEL_NAME =
    "Found duplicate label name";

  protected static final String M_DUPLICATE_LABEL_LOGNAME =
    "Found duplicate label log name";

  protected static final String M_RELATION_HAS_INCOMPATIBLE_STARTNODE =
    "Relationship has incompatible start node";

  protected static final String M_RELATION_HAS_INCOMPATIBLE_ENDNODE =
    "Relationship has incompatible end node";

  protected static final String M_NODERELATIONS_MUST_BE_PUBLIC_FINAL =
    "NodeRelation methods must be public and final";

  protected static final String M_NODERELATIONS_MUST_BE_ANNOTATED =
    "NodeRelation methods must be annotated as Validate";

  private static final String LABELS = "Labels: ";

  private static final String RELATIONS = "Relations: ";

  // endregion

  // region private fields

  private NodeIdentifier mIdentifier;

  private final LabelSet mContingentLabels;

  private boolean mRelationInitDone;

  private final TreeSet<NodeRelation> mNodeRelations = new TreeSet<>();

  // endregion

  // region ctor and init

  protected NodeDescriptor() {
    super();
    mIdentifier = NodeIdentifier.empty();
    mContingentLabels = LabelSet.empty();
  }

  private void initLabels(Class<?> clazz) {
    try {
      Set<String> lblNames = Stream
        .concat(mIdentifier.labels(), mContingentLabels.labels())
        .map(LabelDescriptor::name)
        .collect(Collectors.toSet());

      Set<String> lblLogNames = Stream
        .concat(mIdentifier.labels(), mContingentLabels.labels())
        .map(LabelDescriptor::logName)
        .collect(Collectors.toSet());

      Iterator<Field> labelFieldIter = scanFields(clazz, LabelDescriptor.class)
        .iterator();

      while (labelFieldIter.hasNext()) {
        Field field = labelFieldIter.next();
        int mod = field.getModifiers();
        if (!Modifier.isPublic(mod) || !Modifier.isFinal(mod)) {
          throw new IllegalStateException(M_LABEL_FIELDS_MUST_BE_PUBLIC_FINAL);
        }
        LabelDescriptor lbl = (LabelDescriptor) field.get(this);
        if (!lblNames.add(lbl.name())) {
          throw new IllegalStateException(M_DUPLICATE_LABEL_NAME);
        }
        if (!lblLogNames.add(lbl.logName())) {
          throw new IllegalStateException(M_DUPLICATE_LABEL_LOGNAME);
        }
        boolean labelIsIdentifying = field.isAnnotationPresent(
          Identifying.class
        );
        boolean labelIsContingent = field.isAnnotationPresent(Validate.class);
        if (labelIsIdentifying == labelIsContingent) {
          throw new IllegalStateException(M_LABEL_FIELDS_MUST_BE_ANNOTATED);
        }
        if (labelIsIdentifying) {
          identifier().add(lbl);
        } else {
          mContingentLabels.add(lbl);
        }
      }
    } catch (IllegalAccessException exc) {
      throw new IllegalStateException();
    }
  }

  private void verifyRelationsOrThrow() {
    Iterator<RelationshipDescriptor> incRelIter = mNodeRelations
      .stream()
      .filter(o -> o.direction() == Direction.INCOMING)
      .map(NodeRelation::relationshipDescriptor)
      .iterator();
    while (incRelIter.hasNext()) {
      RelationshipDescriptor incRel = incRelIter.next();
      NodeDescriptor end = incRel.endNodeDescriptor();
      if (end != null && !end.getClass().isAssignableFrom(this.getClass())) {
        throw new IllegalStateException(M_RELATION_HAS_INCOMPATIBLE_ENDNODE);
      }
    }
    Iterator<RelationshipDescriptor> outRelIter = mNodeRelations
      .stream()
      .filter(o -> o.direction() == Direction.OUTGOING)
      .map(NodeRelation::relationshipDescriptor)
      .iterator();
    while (outRelIter.hasNext()) {
      RelationshipDescriptor outRel = outRelIter.next();
      NodeDescriptor start = outRel.startNodeDescriptor();
      if (
        start != null && !start.getClass().isAssignableFrom(this.getClass())
      ) {
        throw new IllegalStateException(M_RELATION_HAS_INCOMPATIBLE_STARTNODE);
      }
    }
  }

  private void initNodeRelations() {
    mNodeRelations.clear();
    try {
      Iterator<Method> methodIter = scanMethodsUpwards(
        this.getClass(),
        NodeRelation.class
      )
        .iterator();
      while (methodIter.hasNext()) {
        Method meth = methodIter.next();
        NodeRelation nodeRel = (NodeRelation) meth.invoke(this);
        if (nodeRel == null) {
          throw new IllegalStateException(M_PREMATURE_INIT_CALL);
        }
        int mod = meth.getModifiers();
        if (!Modifier.isPublic(mod) || !Modifier.isFinal(mod)) {
          throw new IllegalStateException(M_NODERELATIONS_MUST_BE_PUBLIC_FINAL);
        }
        if (!meth.isAnnotationPresent(Validate.class)) {
          throw new IllegalStateException(M_NODERELATIONS_MUST_BE_ANNOTATED);
        }
        mNodeRelations.add(nodeRel);
      }
    } catch (InvocationTargetException | IllegalAccessException exc) {
      throw new IllegalStateException();
    }
    verifyRelationsOrThrow();
    mRelationInitDone = true;
  }

  protected void initLabelsAndProperties(Class<?> clazz) {
    // I need to reopen the identifier because a derived class might need to add some stuff
    if (mIdentifier.isClosed()) {
      mIdentifier = mIdentifier.openCopy();
    }
    initLabels(clazz);
    initProperties(clazz);
    mIdentifier.closeDefinition();
  }

  // endregion

  // region accessors

  @Override
  public NodeIdentifier identifier() {
    return mIdentifier;
  }

  public Stream<LabelDescriptor> labels(Modality modality) {
    switch (modality) {
      case NECESSARY:
        return mIdentifier.labels();
      case CONTINGENT:
        return mContingentLabels.labels();
      case BOTH:
        return Stream
          .concat(mIdentifier.labels(), mContingentLabels.labels())
          .distinct();
      default:
        throw new IllegalArgumentException();
    }
  }

  public Stream<NodeRelation> nodeRelations() {
    if (!mRelationInitDone) {
      initNodeRelations();
    }
    return mNodeRelations.stream();
  }

  public <T extends NodeRelation> Stream<T> nodeRelations(Class<T> clazz) {
    if (!mRelationInitDone) {
      initNodeRelations();
    }
    return mNodeRelations
      .stream()
      .filter(o -> o.getClass().equals(clazz))
      .map(clazz::cast);
  }

  private String labelBit(String mI, String sI) {
    String lblPrint = mContingentLabels.print(mI, sI + sI);
    if (lblPrint.length() == 0) {
      lblPrint = "-";
    } else {
      lblPrint = System.lineSeparator() + lblPrint;
    }
    return mI + sI + LABELS + lblPrint;
  }

  private String nodeRelationBit(String mI, String sI) {
    String nodeRelPrint = "-";
    String[] bits = nodeRelations()
      .map(o -> o.print(mI + sI + sI))
      .toArray(String[]::new);
    if (bits.length > 0) {
      nodeRelPrint =
        System.lineSeparator() + String.join(System.lineSeparator(), bits);
    }
    return mI + sI + RELATIONS + nodeRelPrint;
  }

  public String print(String mainIndent, String subIndent) {
    String mI = mainIndent == null ? "" : mainIndent;
    String sI = subIndent == null ? "" : subIndent;
    String head = String.format(
      "%s%s: %s",
      mI,
      NodeDescriptor.class.getSimpleName(),
      this.getClass().getSimpleName()
    );
    String[] bits = new String[] {
      head,
      idPrintBit(mI, sI),
      labelBit(mI, sI),
      propBit(mI, sI),
      nodeRelationBit(mI, sI),
    };
    return String.join(System.lineSeparator(), bits);
  }

  /////////////////////////////////

  private void checkOtherNodeArgsOrThrow(NodeRelation nodeRel, Node node) {
    if (!mRelationInitDone) {
      initNodeRelations();
    }
    if (
      nodeRel == null ||
      node == null ||
      !mNodeRelations.contains(nodeRel) ||
      !this.mIdentifier.matches(node)
    ) {
      throw new IllegalArgumentException();
    }
  }

  public Relationship relationship(NodeRelationOneOne nodeRel, Node node) {
    checkOtherNodeArgsOrThrow(nodeRel, node);
    Direction dir = nodeRel.direction();
    RelationshipType relType = nodeRel.relationshipDescriptor().type();
    return node.getRelationships(dir, relType).iterator().next();
  }

  public Node node(NodeRelationOneOne nodeRel, Node node) {
    Relationship rel = relationship(nodeRel, node);
    Direction dir = nodeRel.direction();
    Node result;
    switch (dir) {
      case INCOMING:
        result = rel.getStartNode();
        break;
      case OUTGOING:
        result = rel.getEndNode();
        break;
      default:
        throw new UnsupportedOperationException();
    }
    return result;
  }

  public Optional<Relationship> relationship(
    NodeRelationZeroOne nodeRel,
    Node node
  ) {
    checkOtherNodeArgsOrThrow(nodeRel, node);
    Direction dir = nodeRel.direction();
    RelationshipType relType = nodeRel.relationshipDescriptor().type();
    Iterator<Relationship> relIter = node
      .getRelationships(dir, relType)
      .iterator();
    if (!relIter.hasNext()) {
      return Optional.empty();
    }
    return Optional.of(relIter.next());
  }

  public Optional<Node> node(NodeRelationZeroOne nodeRel, Node node) {
    Optional<Relationship> relOrNot = relationship(nodeRel, node);
    if (relOrNot.isEmpty()) {
      return Optional.empty();
    }
    Direction dir = nodeRel.direction();
    Relationship rel = relOrNot.get();
    Node result;
    switch (dir) {
      case INCOMING:
        result = rel.getStartNode();
        break;
      case OUTGOING:
        result = rel.getEndNode();
        break;
      default:
        throw new UnsupportedOperationException();
    }
    return Optional.of(result);
  }

  public Relationship relationship(NodeRelationOneMany nodeRel, Node node) {
    checkOtherNodeArgsOrThrow(nodeRel, node);
    Direction dir = nodeRel.direction();
    RelationshipType relType = nodeRel.relationshipDescriptor().type();
    return node.getRelationships(dir, relType).iterator().next();
  }

  public Node node(NodeRelationOneMany nodeRel, Node node) {
    Relationship rel = relationship(nodeRel, node);
    Direction dir = nodeRel.direction();
    Node result;
    switch (dir) {
      case INCOMING:
        result = rel.getStartNode();
        break;
      case OUTGOING:
        result = rel.getEndNode();
        break;
      default:
        throw new UnsupportedOperationException();
    }
    return result;
  }

  public Stream<Relationship> relationships(NodeRelation nodeRel, Node node) {
    checkOtherNodeArgsOrThrow(nodeRel, node);
    Direction dir = nodeRel.direction();
    RelationshipType relType = nodeRel.relationshipDescriptor().type();
    return StreamSupport.stream(
      node.getRelationships(dir, relType).spliterator(),
      false
    );
  }

  public Stream<Node> nodes(NodeRelation nodeRel, Node node) {
    Stream<Relationship> rels = relationships(nodeRel, node);
    Direction dir = nodeRel.direction();
    Stream<Node> result;
    switch (dir) {
      case INCOMING:
        result = rels.map(Relationship::getStartNode);
        break;
      case OUTGOING:
        result = rels.map(Relationship::getEndNode);
        break;
      default:
        throw new UnsupportedOperationException();
    }
    return result;
  }

  // endregion

  public NodeReport validate(Node node) {
    if (node == null) {
      throw new IllegalArgumentException(M_ARGUMENT_NULL);
    }
    if (!identifier().matches(node)) {
      throw new IllegalArgumentException(M_DESCRIPTOR_DOES_NOT_APPLY);
    }
    NodeReport report = new NodeReport(this, node.getElementId());
    try {
      super.validateProperties(report, node);
      this.labels(Modality.CONTINGENT)
        .filter(o -> !node.hasLabel(o))
        .forEach(report::addMissingLabel);
      Iterator<NodeRelation> nodeRelIter = this.nodeRelations().iterator();
      while (nodeRelIter.hasNext()) {
        NodeRelation nodeRel = nodeRelIter.next();
        NodeRelationReport nodeRelReport = nodeRel.validate(node);
        report.addNodeRelationReport(nodeRelReport);
      }
    } catch (Exception exc) {
      report.addException(exc);
    }
    return report.closeReport();
  }

  public NodeDescriptorReport validate(Transaction tx) {
    NodeDescriptorReport ndReport = new NodeDescriptorReport(this);
    Iterator<Node> nodesIter;
    try {
      nodesIter = identifier().findNodes(tx).iterator();
    } catch (Exception exc) {
      ndReport.addException(exc);
      return ndReport.closeReport();
    }
    while (nodesIter.hasNext()) {
      Node node = nodesIter.next();
      NodeReport nodeReport = validate(node);
      ndReport.addNodeReport(nodeReport);
    }
    return ndReport.closeReport();
  }
  // region Equality, hash and compare overrides

  // Nuffin to do here, I only use the class in all of those and
  // that can be done in the EntityDescriptor

  // endregion
}
