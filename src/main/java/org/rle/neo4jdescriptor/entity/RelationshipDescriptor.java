package org.rle.neo4jdescriptor.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.rle.neo4jdescriptor.annotation.EndNode;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.annotation.StartNode;
import org.rle.neo4jdescriptor.report.RelationshipDescriptorReport;
import org.rle.neo4jdescriptor.report.RelationshipReport;

public abstract class RelationshipDescriptor extends EntityDescriptor {

  // region static strings

  protected static final String M_MUST_DEFINE_UNIQUE_RELATIONTYPE =
    "Must define a single RelationshipType";

  protected static final String M_RELATIONTYPE_MUST_BE_PUBLICFINAL =
    "The RelationshipType must be public and final";

  protected static final String M_RELATIONTYPE_MUST_BE_IDENTIFYING =
    "The RelationshipType must be identifying";

  protected static final String M_NODEDESCRIPTORS_MUST_BE_START_OR_END =
    "NodeDescriptors must be annotated as either Start or End";

  protected static final String M_ONLY_ONE_NODE_EACH =
    "Only one NodeDescriptor can be annotated as the StartNode and EndNode respectively";

  private static final String STARTNODE = "Startnode: ";

  private static final String ENDNODE = "Endnode: ";

  // endregion

  // region private fields

  private RelationshipIdentifier mIdentifier;

  // RelationshipTypeDescriptor must be identifying (i.e. part of the mIdentifier), so no need for a field here

  private boolean mNodeInitDone;

  private NodeDescriptor mStartNode; // might well be null, that means: any kind of start node is okay

  private NodeDescriptor mEndNode; // might well be null, that means: any kind of end node is okay

  // endregion

  // region ctor and init

  protected RelationshipDescriptor() {
    super();
    mIdentifier = RelationshipIdentifier.empty();
  }

  private void initType(Class<?> clazz) {
    Iterator<Field> typeFieldIter = scanFieldsUpwards(
      clazz,
      RelationshipTypeDescriptor.class
    )
      .iterator();
    if (!typeFieldIter.hasNext()) {
      throw new IllegalStateException(M_MUST_DEFINE_UNIQUE_RELATIONTYPE);
    }
    Field typeField = typeFieldIter.next();
    if (typeFieldIter.hasNext()) {
      throw new IllegalStateException(M_MUST_DEFINE_UNIQUE_RELATIONTYPE);
    }
    int mod = typeField.getModifiers();
    if (!Modifier.isPublic(mod) || !Modifier.isFinal(mod)) {
      throw new IllegalStateException(M_RELATIONTYPE_MUST_BE_PUBLICFINAL);
    }
    if (!typeField.isAnnotationPresent(Identifying.class)) {
      throw new IllegalStateException(M_RELATIONTYPE_MUST_BE_IDENTIFYING);
    }
    try {
      RelationshipTypeDescriptor type = (RelationshipTypeDescriptor) typeField.get(
        this
      );
      if (type == null) {
        throw new IllegalStateException(M_PREMATURE_INIT_CALL);
      }
      mIdentifier.setType(type);
    } catch (IllegalAccessException exc) {
      throw new IllegalStateException();
    }
  }

  protected void initTypeAndProperties(Class<?> clazz) {
    // I need to reopen the identifier because a derived class might need to add some stuff
    if (mIdentifier.isClosed()) {
      mIdentifier = mIdentifier.openCopy();
    }
    initType(clazz);
    initProperties(clazz);
    mIdentifier.closeDefinition();
  }

  private void verifyNodes(List<Method> starts, List<Method> ends) {
    if (starts.size() > 1 || ends.size() > 1) {
      throw new IllegalStateException(M_ONLY_ONE_NODE_EACH);
    }
    try {
      if (!starts.isEmpty()) {
        mStartNode = (NodeDescriptor) starts.get(0).invoke(this);
        if (mStartNode == null) {
          throw new IllegalStateException(M_PREMATURE_INIT_CALL);
        }
      }
      if (!ends.isEmpty()) {
        mEndNode = (NodeDescriptor) ends.get(0).invoke(this);
        if (mEndNode == null) {
          throw new IllegalStateException(M_PREMATURE_INIT_CALL);
        }
      }
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new IllegalStateException();
    }
  }

  private void initNodes() {
    List<Method> nodeDescMethods = scanPublicFinalMethods(NodeDescriptor.class);
    List<Method> starts = new ArrayList<>();
    List<Method> ends = new ArrayList<>();
    for (Method meth : nodeDescMethods) {
      StartNode startTag = meth.getAnnotation(StartNode.class);
      EndNode endTag = meth.getAnnotation(EndNode.class);
      if (startTag == null && endTag == null) {
        throw new IllegalStateException(M_NODEDESCRIPTORS_MUST_BE_START_OR_END);
      }
      if (startTag != null) {
        starts.add(meth);
      }
      if (endTag != null) {
        ends.add(meth);
      }
    }
    verifyNodes(starts, ends);
    mNodeInitDone = true;
  }

  // endregion

  // region accessors

  public RelationshipTypeDescriptor type() {
    return mIdentifier.relationshipTypeDescriptor();
  }

  public NodeDescriptor startNodeDescriptor() {
    if (!mNodeInitDone) {
      initNodes();
    }
    return mStartNode;
  }

  public NodeDescriptor endNodeDescriptor() {
    if (!mNodeInitDone) {
      initNodes();
    }
    return mEndNode;
  }

  @Override
  public RelationshipIdentifier identifier() {
    return mIdentifier;
  }

  //endregion

  private String nodeBit(String mI, String sI) {
    if (!mNodeInitDone) {
      initNodes();
    }
    String startBit = "";
    if (mStartNode != null) {
      startBit =
        String.format(
          "%s: %s",
          mI + sI + STARTNODE,
          mStartNode.getClass().getSimpleName()
        );
    }
    String endBit = "";
    if (mEndNode != null) {
      endBit =
        String.format(
          "%s: %s",
          mI + sI + ENDNODE,
          mEndNode.getClass().getSimpleName()
        );
    }
    if (startBit.length() > 0 && endBit.length() > 0) {
      return startBit + System.lineSeparator() + endBit;
    } else {
      return startBit + endBit;
    }
  }

  public String print(String mainIndent, String subIndent) {
    String mI = mainIndent == null ? "" : mainIndent;
    String sI = subIndent == null ? "" : subIndent;
    String head = String.format(
      "%s%s: %s",
      mI,
      RelationshipDescriptor.class.getSimpleName(),
      this.getClass().getSimpleName()
    );
    String[] bits = new String[] {
      head,
      idPrintBit(mI, sI),
      nodeBit(mI, sI),
      propBit(mI, sI),
    };
    return String.join(System.lineSeparator(), bits);
  }

  public RelationshipReport validate(Relationship rel) {
    if (rel == null) {
      throw new IllegalArgumentException(M_ARGUMENT_NULL);
    }
    if (!identifier().matches(rel)) {
      throw new IllegalArgumentException(M_DESCRIPTOR_DOES_NOT_APPLY);
    }
    RelationshipReport report = new RelationshipReport(
      this,
      rel.getElementId()
    );
    try {
      super.validateProperties(report, rel);
      NodeDescriptor startNodeDesc = startNodeDescriptor(); // need to use the method rather than the field because I may have to trigger its initialisation
      if (startNodeDesc != null) {
        boolean startNodeCheck = startNodeDesc
          .identifier()
          .matches(rel.getStartNode());
        report.setStartNodeCheck(startNodeCheck);
      }
      NodeDescriptor endNodeDesc = endNodeDescriptor(); // need to use the method rather than the field because I may have to trigger its initialisation
      if (endNodeDesc != null) {
        boolean endNodeCheck = endNodeDesc
          .identifier()
          .matches(rel.getEndNode());
        report.setEndNodeCheck(endNodeCheck);
      }
    } catch (Exception exc) {
      report.addException(exc);
    }
    return report.closeReport();
  }

  public RelationshipDescriptorReport validate(Transaction tx) {
    RelationshipDescriptorReport rdReport = new RelationshipDescriptorReport(
      this
    );
    Iterator<Relationship> relIter;
    try {
      relIter = identifier().findRelationships(tx).iterator();
    } catch (Exception exc) {
      rdReport.addException(exc);
      return rdReport.closeReport();
    }
    while (relIter.hasNext()) {
      Relationship rel = relIter.next();
      RelationshipReport relReport = validate(rel);
      rdReport.addRelationshipReport(relReport);
    }
    return rdReport.closeReport();
  }
  // region Equality, hash and compare overrides

  // Nuffin to do here, I only use the class in all of those and
  // that can be done in the EntityDescriptor

  // endregion
}
