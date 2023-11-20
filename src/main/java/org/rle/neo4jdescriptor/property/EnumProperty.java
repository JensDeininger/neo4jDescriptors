package org.rle.neo4jdescriptor.property;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.rle.neo4jdescriptor.enuminterface.TypedEnum;
import org.rle.neo4jdescriptor.report.PropertyReport;

public class EnumProperty<D, E extends Enum<E> & TypedEnum<D>>
  extends PropertyDescriptor {

  protected static final String INVALID_ENUM_VALUE_ON_NODE =
    "Invalid value for enum property %s on Node<%d>. (MATCH (n) WHERE ID(n) = %d RETURN n)";

  protected static final String INVALID_ENUM_VALUE_ON_RELATIONSHIP =
    "Invalid value for enum property %s on relationship<%d>. (Match (a)-[r]->(b) WHERE ID(r)=%d return a,b,r)";

  private final Class<E> mCodeType;

  private final Class<D> mDbType;

  public EnumProperty(String key, Class<D> dbType, Class<E> enumType) {
    this(key, key, dbType, enumType);
  }

  public EnumProperty(
    String key,
    String logKey,
    Class<D> dbType,
    Class<E> enumType
  ) {
    super(key, logKey);
    if (enumType == null || dbType == null) {
      throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
    }
    mCodeType = enumType;
    mDbType = dbType;
  }

  @Override
  public Class<E> codeType() {
    return mCodeType;
  }

  @Override
  public Class<D> dbType() {
    return mDbType;
  }

  @Override
  public EnumProperty<D, E> copy() {
    return new EnumProperty<>(key(), logKey(), mDbType, mCodeType);
  }

  public Stream<D> validDbValues() {
    return Arrays.stream(mCodeType.getEnumConstants()).map(TypedEnum::dbValue);
  }

  // Suggests replacing .map(o -> o.cypherString()) with .map(TypedEnum::cypherString) but that actually breaks the code
  @java.lang.SuppressWarnings("java:S1612")
  public String[] validDbValuesPrintVersion2() {
    // Dont change the return value to Stream<String>. Sure, it would be better, but the only place I actually need this
    // method I do actually need an array. And a Stream produces some weird warning
    return Arrays
      .stream(mCodeType.getEnumConstants())
      .map(o -> o.cypherString())
      .toArray(String[]::new);
  }

  protected Optional<E> tryTranslate(D dbValue) {
    E[] values = mCodeType.getEnumConstants();
    return Arrays
      .stream(values)
      .filter(o -> o.isValueEqualTo(dbValue))
      .findFirst();
  }

  @Override
  public boolean appliesTo(Entity entity) {
    if (!super.appliesTo(entity)) {
      return false;
    }
    Object dbValue = entity.getProperty(this.key(), null);
    if (!mDbType.isInstance(dbValue)) {
      return false;
    }
    Optional<E> translatedValue = tryTranslate(mDbType.cast(dbValue));
    return translatedValue.isPresent();
  }

  private String invalidValueMsg(Entity entity) {
    String msg = "";
    if (entity instanceof Node) {
      msg = INVALID_ENUM_VALUE_ON_NODE;
    } else if (entity instanceof Relationship) {
      msg = INVALID_ENUM_VALUE_ON_RELATIONSHIP;
    }
    String id = entity.getElementId();
    msg = String.format(msg, this.key(), id, id);
    return msg;
  }

  public E getValueOn(Entity entity) {
    super.hasPropOrThrow(entity);
    Object dbValue = entity.getProperty(this.key());
    if (!mDbType.isInstance(dbValue)) {
      String msg = wrongTypeMessage(entity, dbValue.getClass(), mDbType);
      throw new ClassCastException(msg);
    }
    Optional<E> translation = tryTranslate(mDbType.cast(dbValue));
    if (!translation.isPresent()) {
      String msg = invalidValueMsg(entity);
      throw new ClassCastException(msg);
    }
    return translation.get();
  }

  public void setValueOn(Entity entity, E value) {
    if (entity == null) {
      throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
    }
    entity.setProperty(this.key(), value.dbValue());
  }

  @Override
  protected String typedPrint() {
    return String.format(
      "EnumProperty<%s, %s>",
      dbType().getSimpleName(),
      codeType().getSimpleName()
    );
  }

  @Override
  protected void validatePropertyValue(Entity entity, PropertyReport report) {
    // As this method is only called in one spot, I can assume that
    // the property key does exist on the entity AND that the
    // report is still open
    Object nodeValue = entity.getProperty(this.key());
    if (!mDbType.isInstance(nodeValue)) {
      report
        .setTypeCheck(false)
        .setDeviantType(nodeValue.getClass().getSimpleName());
    } else {
      report
        .setTypeCheck(true)
        .setEnumCheck(tryTranslate(mDbType.cast(nodeValue)).isPresent());
    }
    report.closeReport();
  }

  // region Equaly, Hash and Compare overrides

  @Override
  public boolean equals(Object obj) {
    // The super.equals method already incorporates class, CodeType and DbType
    // Only reason this method is here is to stop SonarLint from complaining.
    // Well, a reminder that semantic equality rules this class is nice, I guess
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    // This works because super.hashCode incorporates the class, the CodeType and the Db Type
    // via abstract methods.
    return super.hashCode();
  }
  // endregion
}
