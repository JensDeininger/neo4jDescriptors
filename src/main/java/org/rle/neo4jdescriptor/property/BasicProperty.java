package org.rle.neo4jdescriptor.property;

import org.neo4j.graphdb.Entity;
import org.rle.neo4jdescriptor.report.PropertyReport;

public abstract class BasicProperty<T> extends PropertyDescriptor {

  private final Class<T> mDbType;

  protected BasicProperty(String key, Class<T> dbType) {
    super(key);
    if (dbType == null) {
      throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
    }
    mDbType = dbType;
  }

  protected BasicProperty(String key, String logKey, Class<T> dbType) {
    super(key, logKey);
    if (dbType == null) {
      throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
    }
    mDbType = dbType;
  }

  @Override
  public Class<T> codeType() {
    return mDbType;
  }

  @Override
  public Class<T> dbType() {
    return mDbType;
  }

  public Class<T> databaseType() {
    return mDbType;
  }

  @Override
  public abstract BasicProperty<T> copy();

  @Override
  public boolean appliesTo(Entity entity) {
    if (!super.appliesTo(entity)) {
      return false;
    }
    return mDbType.isInstance(entity.getProperty(this.key()));
  }

  public T getValueOn(Entity entity) {
    super.hasPropOrThrow(entity);
    Object nodeValue = entity.getProperty(this.key());
    if (!mDbType.isInstance(nodeValue)) {
      String msg = wrongTypeMessage(entity, nodeValue.getClass(), mDbType);
      throw new ClassCastException(msg);
    }
    return mDbType.cast(nodeValue);
  }

  public void setValueOn(Entity entity, T value) {
    if (entity == null) {
      throw new IllegalArgumentException(ARGUMENT_MUST_NOT_BE_NULL);
    }
    entity.setProperty(this.key(), value);
  }

  @Override
  protected String typedPrint() {
    return String.format("Property<%s>", dbType().getSimpleName());
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
      report.setTypeCheck(true);
    }
    report.closeReport();
  }

  // region Equals, Hash and Compare Overrides

  @Override
  public boolean equals(Object obj) {
    // The super.equals method already incorporates class and DbType
    // Only reason this method is here is to stop SonarLint from complaining.
    // Well, a reminder that semantic equality rules this class is nice, I guess
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    // This works because super.hashCode incorporates the class and the Db Type
    // via abstract methods.
    return super.hashCode();
  }
  // endregion

}
