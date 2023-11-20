package org.rle.neo4jdescriptor.property;

import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.rle.neo4jdescriptor.report.PropertyReport;

/**
 * {@link PropertyDescriptor} serves as the abstract base class to all typed subclasses
 * that further specify the data type (int, string, array, enum etc.) of a property.
 * <p>
 * It is fully defined by two {@link String}s, the key and the logKey, both of which must
 * be specified in the constructor.
 * <p>
 * The key must be the property key in the database.
 * <p>
 * The logKey is optional and will be the same as the dbKey if not specified in
 * the constructor. It can be used to record database operations in logs or archives
 * so that old records are not affected by changes to property key in the database.
 */
public abstract class PropertyDescriptor
  implements Comparable<PropertyDescriptor> {

  // region static strings

  protected static final String KEY_MUST_NOT_BE_NULL_EMPTY_OR_BLANK =
    "property keys must not be null, empty or blank";

  protected static final String ARGUMENT_MUST_NOT_BE_NULL =
    "argument must not be null";

  protected static final String NODE_CYPHER_1ARG =
    "MATCH (n) WHERE ID(n) = %d RETURN n";

  protected static final String RELATIONSHIP_CYPHER_1ARG =
    "Match (a)-[r]->(b) WHERE ID(r)=%d return a,b,r";

  protected static final String NODE_LACKS_PROPERTY_2ARGS =
    "Node<%d> has no property named %s";

  protected static final String RELATIONSHIP_LACKS_PROPERTY_2ARGS =
    "Relationship<%d> has no property named %s";

  protected static final String WRONG_PROPERTY_TYPE_ON_NODE_4ARGS =
    "Property %s on Node<%d> is of type %s, expected type was %s";

  protected static final String WRONG_PROPERTY_TYPE_ON_RELATIONSHIP_4ARGS =
    "Property %s on Relationship<%d> is of type %s, expected type was %s";

  // endregion

  // region private fields

  private final String mKey;

  private final String mLogKey;

  private Integer mHash;

  // endregion

  // region ctor

  protected PropertyDescriptor(String key) {
    this(key, key);
  }

  protected PropertyDescriptor(String key, String logKey) {
    if (
      key == null ||
      key.trim().isEmpty() ||
      logKey == null ||
      logKey.trim().isEmpty()
    ) {
      throw new IllegalArgumentException(KEY_MUST_NOT_BE_NULL_EMPTY_OR_BLANK);
    }
    mKey = key;
    mLogKey = logKey;
  }

  // endregion

  // region accessors

  public abstract Class<?> codeType();

  public abstract Class<?> dbType();

  protected abstract String typedPrint();

  public String key() {
    return mKey;
  }

  public String logKey() {
    return mLogKey;
  }

  // endregion

  // region methods

  public abstract PropertyDescriptor copy();

  public boolean appliesTo(Entity entity) {
    if (entity == null) {
      return false;
    }
    // cant easily check the type here because arrays fuck it up.
    // So this method must be overriden (and tested) in all derived classes.
    return entity.hasProperty(mKey);
  }

  protected void hasPropOrThrow(Entity entity) {
    if (entity == null) {
      throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
    }
    if (entity.hasProperty(this.key())) {
      return;
    }
    String id = entity.getElementId();
    String msg = "";
    String cypher = "";
    if (entity instanceof Node) {
      msg = String.format(NODE_LACKS_PROPERTY_2ARGS, id, this.key());
      cypher = String.format(NODE_CYPHER_1ARG, id);
    } else if (entity instanceof Relationship) {
      msg = String.format(RELATIONSHIP_LACKS_PROPERTY_2ARGS, id, this.key());
      cypher = String.format(RELATIONSHIP_CYPHER_1ARG, id);
    }
    throw new IllegalArgumentException(String.format("%s. (%s)", msg, cypher));
  }

  protected String wrongTypeMessage(
    Entity entity,
    Class<?> foundType,
    Class<?> expectedType
  ) {
    String id = entity.getElementId();
    String msgTmpl = "";
    String cypTmpl = "";
    if (entity instanceof Node) {
      msgTmpl = WRONG_PROPERTY_TYPE_ON_NODE_4ARGS;
      cypTmpl = NODE_CYPHER_1ARG;
    } else {
      msgTmpl = WRONG_PROPERTY_TYPE_ON_RELATIONSHIP_4ARGS;
      cypTmpl = RELATIONSHIP_CYPHER_1ARG;
    }
    String msg = String.format(
      msgTmpl,
      this.key(),
      id,
      foundType.getSimpleName(),
      expectedType.getSimpleName()
    );
    String cyp = String.format(cypTmpl, id);
    msg = String.format("%s. (%s)", msg, cyp);
    return msg;
  }

  public String print(String indent) {
    String ind = indent == null ? "" : indent;
    String str = key().equals(logKey())
      ? String.format("%s(%s)", typedPrint(), key())
      : String.format("%s(%s, %s)", typedPrint(), key(), logKey());
    return ind + str;
  }

  protected abstract void validatePropertyValue(
    Entity entity,
    PropertyReport report
  );

  public PropertyReport validate(Entity entity) {
    PropertyReport rep = new PropertyReport(this);
    try {
      if (entity == null) {
        throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
      }
      if (!entity.hasProperty(key())) {
        rep.setKeyCheck(false).closeReport();
      } else {
        rep.setKeyCheck(true);
        validatePropertyValue(entity, rep);
      }
    } catch (Exception exc) {
      rep.addException(exc);
    }
    return rep.closeReport();
  }

  // endregion

  // region equality hash and compare

  /**
   * incorporates class, key and logKey
   */
  protected int computeHash() {
    int hash = this.getClass().hashCode();
    hash = (hash * 397) ^ codeType().hashCode();
    hash = (hash * 397) ^ dbType().hashCode();
    hash = (hash * 397) ^ mKey.hashCode();
    return (hash * 397) ^ mLogKey.hashCode();
  }

  @Override
  public int hashCode() {
    if (mHash == null) {
      mHash = computeHash();
    }
    return mHash;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof PropertyDescriptor)) {
      return false;
    }
    return equals(PropertyDescriptor.class.cast(obj));
  }

  public <T extends PropertyDescriptor> boolean equals(T other) {
    if (other == null) {
      return false;
    }
    if (other == this) {
      return true;
    }
    if (!other.getClass().equals(getClass())) {
      return false;
    }
    if (!other.codeType().equals(codeType())) {
      return false;
    }
    if (!other.dbType().equals(dbType())) {
      return false;
    }
    return this.mKey.equals(other.key()) && this.mLogKey.equals(other.logKey());
  }

  public int compareTo(PropertyDescriptor other) {
    if (other == null) {
      return 1;
    }
    int classComp =
      this.getClass().getName().compareTo(other.getClass().getName());
    if (classComp != 0) {
      return classComp;
    }
    int codeTypeComp =
      this.codeType().getName().compareTo(other.codeType().getName());
    if (codeTypeComp != 0) {
      return codeTypeComp;
    }
    int dbTypeComp =
      this.dbType().getName().compareTo(other.dbType().getName());
    if (dbTypeComp != 0) {
      return dbTypeComp;
    }
    int keyComp = this.mKey.compareTo(other.mKey);
    if (keyComp != 0) {
      return keyComp;
    }
    return this.mLogKey.compareTo(other.mLogKey);
  }
  // endregion
}
