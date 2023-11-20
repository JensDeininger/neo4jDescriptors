package org.rle.neo4jdescriptor.entity;

import java.util.Objects;
import java.util.stream.Stream;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;

/**
 * Describes a set of {@link Relationship}s by specifying<p>
 * - the {@link RelationshipTypeDescriptor}<p>
 * - the {@link PropertyDescriptor}s that must apply
 */
@SuppressWarnings("java:S2160") // override equals in derived classes that have new fields. Not necessary here
public class RelationshipIdentifier extends EntityIdentifier {

  // region fields

  private RelationshipTypeDescriptor mTypeDescriptor;

  // endregion

  // region ctor

  public RelationshipIdentifier(String typeName) {
    if (typeName == null) {
      throw new IllegalArgumentException(M_ARGUMENT_MUST_NOT_BE_NULL_MSG);
    }
    mTypeDescriptor = new RelationshipTypeDescriptor(typeName);
  }

  public RelationshipIdentifier(RelationshipType type) {
    if (type == null) {
      throw new IllegalArgumentException(M_ARGUMENT_MUST_NOT_BE_NULL_MSG);
    }
    mTypeDescriptor = new RelationshipTypeDescriptor(type.name());
  }

  public RelationshipIdentifier(RelationshipTypeDescriptor type) {
    if (type == null) {
      throw new IllegalArgumentException(M_ARGUMENT_MUST_NOT_BE_NULL_MSG);
    }
    mTypeDescriptor = type;
  }

  private RelationshipIdentifier() {}

  public static RelationshipIdentifier empty() {
    return new RelationshipIdentifier();
  }

  // endregion

  // region getters

  public RelationshipTypeDescriptor relationshipTypeDescriptor() {
    return mTypeDescriptor;
  }

  public RelationshipIdentifier openCopy() {
    RelationshipIdentifier copy = new RelationshipIdentifier(
      this.mTypeDescriptor.copy()
    );
    super.copyPropertiesOnto(copy);
    return copy;
  }

  @Override
  public String print(String mainIndent, String subIndent) {
    String typePart = mTypeDescriptor == null
      ? ""
      : mTypeDescriptor.print(mainIndent + subIndent);
    String propPart = propertySet().print(mainIndent, subIndent);
    if (typePart.length() == 0 || propPart.length() == 0) {
      return typePart + propPart;
    }
    return typePart + System.lineSeparator() + propPart;
  }

  // endregion

  // region fluent style config methods

  /**
   * Specifies the relationship type. This is mandatory. This {@link RelationshipIdentifier}
   * can not be closed until this is set.
   * @param relationshipTypeDescriptor - the relationship type
   * @throws IllegalArgumentException if the argument is null or not closed
   * @return this
   */
  public RelationshipIdentifier setType(
    RelationshipTypeDescriptor relationshipTypeDescriptor
  ) {
    if (relationshipTypeDescriptor == null) {
      throw new IllegalArgumentException(M_ARGUMENT_MUST_NOT_BE_NULL_MSG);
    }
    isOpenOrThrow();
    mTypeDescriptor = relationshipTypeDescriptor;
    resetHash();
    return this;
  }

  @Override
  public RelationshipIdentifier closeDefinition() {
    super.closeDefinition();
    return this;
  }

  // endregion

  // region match and find relationships

  public String cypher(String relationAlias) {
    String typeWhereClause = "";
    if (mTypeDescriptor != null) {
      typeWhereClause =
        String.format("%s:%s", relationAlias, mTypeDescriptor.name());
    }
    String[] whereClauses = Stream
      .of(typeWhereClause, super.propCypher(relationAlias))
      .filter(o -> o.length() > 0)
      .toArray(String[]::new);
    String fullWhereClause = String.join(
      " " + M_AND + System.lineSeparator(),
      whereClauses
    );

    String startBit = String.format("MATCH ()-[%s]->()", relationAlias);

    if (fullWhereClause.length() > 0) {
      startBit = startBit + " WHERE";
    }

    String endBit = String.format("RETURN %s", relationAlias);

    String[] finalBits = Stream
      .of(startBit, fullWhereClause, endBit)
      .filter(o -> o.length() > 0)
      .toArray(String[]::new);

    return String.join(System.lineSeparator(), finalBits);
  }

  public Stream<Relationship> findRelationships(Transaction tx) {
    Stream<Relationship> relStream = tx.getAllRelationships().stream();
    if (mTypeDescriptor != null) {
      relStream = relStream.filter(o -> o.isType(mTypeDescriptor));
    }
    relStream = super.filter(relStream);
    return relStream;
  }

  public boolean matches(Relationship rel) {
    if (rel == null) {
      return false;
    }
    if (mTypeDescriptor != null && !rel.isType(mTypeDescriptor)) {
      return false;
    }
    return super.propMatches(rel);
  }

  //endregion

  // region equality overrides

  @Override
  protected int processHash() {
    return Objects.hash(super.processHash(), mTypeDescriptor);
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof RelationshipIdentifier)) {
      return false;
    }
    RelationshipIdentifier cast = RelationshipIdentifier.class.cast(obj);
    return EqualityUtils.itemEquals(this.mTypeDescriptor, cast.mTypeDescriptor);
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
    RelationshipIdentifier cast = RelationshipIdentifier.class.cast(other);
    return EqualityUtils.itemCompare(mTypeDescriptor, cast.mTypeDescriptor);
  }
  // endregion
}
