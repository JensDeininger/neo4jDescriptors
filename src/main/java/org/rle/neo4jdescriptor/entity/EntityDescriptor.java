package org.rle.neo4jdescriptor.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.neo4j.graphdb.Entity;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.annotation.Identifying;
import org.rle.neo4jdescriptor.annotation.Validate;
import org.rle.neo4jdescriptor.dto.DtoObjectMatch;
import org.rle.neo4jdescriptor.dto.entity.PropertyDescriptorDto;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.report.EntityReport;
import org.rle.neo4jdescriptor.report.PropertyReport;

public abstract class EntityDescriptor implements Comparable<EntityDescriptor> {

  // region static strings

  protected static final String M_ARGUMENT_NULL = "argument is null";

  protected static final String M_PREMATURE_INIT_CALL =
    "Not supposed to be null. Probably a premature init call";

  protected static final String M_DUPLICATE_PROPERTY_KEY =
    "Found duplicate property key";

  protected static final String M_DUPLICATE_PROPERTY_LOGKEY =
    "Found duplicate property log key";

  protected static final String M_PROPERTY_FIELDS_MUST_BE_PUBLIC_FINAL =
    "PropertyDescriptor fields must be public and final";

  protected static final String M_PROPERTY_FIELDS_MUST_BE_ANNOTATED =
    "PropertyDescriptor fields must be annotated as either Identifying or Validate";

  protected static final String IDENTIFIER = "Identifier:";

  protected static final String PROPERTIES = "Properties:";

  protected static final String M_DESCRIPTOR_DOES_NOT_APPLY =
    "descriptor is not applicable to the entity";

  // endregion

  // region Fields

  private Integer mHash;

  private PropertySet mContingentProperties;

  // endregion

  // region ctor and init

  protected EntityDescriptor() {
    mContingentProperties = PropertySet.empty();
  }

  /**
   * Scans all declared fields (i.e. private, protected and public) in the specified class
   * <p>
   * Returns those whose type is assignable to the specified type2Look4
   */
  protected static Stream<Field> scanFields(
    Class<?> clazz2Scan,
    Class<?> fieldType2Look4
  ) {
    return Arrays
      .stream(clazz2Scan.getDeclaredFields())
      .filter(o -> fieldType2Look4.isAssignableFrom(o.getType()));
  }

  /**
   * Scans all declared fields (i.e. private, protected and public) in the specified class
   * and all classes up the inheritance chain. <p>
   * Returns those whose type is assignable to the specified type2Look4
   */
  protected static Stream<Field> scanFieldsUpwards(
    Class<?> clazz2Scan,
    Class<?> fieldType2Look4
  ) {
    Stream<Field> result = Stream.empty();
    Class<?> c = clazz2Scan;
    while (c != null) {
      result = Stream.concat(result, scanFields(c, fieldType2Look4));
      c = c.getSuperclass();
    }
    return result;
  }

  /**
   * Scans all declared methods (i.e. private, protected and public) in the specified class
   * <p>
   * Returns those whose return type is assignable to the specified type2Look4
   */
  protected static Stream<Method> scanMethods(
    Class<?> clazz2Scan,
    Class<?> fieldType2Look4
  ) {
    return Arrays
      .stream(clazz2Scan.getDeclaredMethods())
      .filter(o -> fieldType2Look4.isAssignableFrom(o.getReturnType()));
  }

  /**
   * Scans all declared methods (i.e. private, protected and public) in the specified class
   * and all classes up the inheritance chain. <p>
   * Returns those whose return type is assignable to the specified type2Look4
   */
  protected static Stream<Method> scanMethodsUpwards(
    Class<?> clazz2Scan,
    Class<?> methodType2Look4
  ) {
    Stream<Method> result = Stream.empty();
    Class<?> c = clazz2Scan;
    while (c != null) {
      result = Stream.concat(result, scanMethods(c, methodType2Look4));
      c = c.getSuperclass();
    }
    return result;
  }

  /**
   * Verifies that no two property keys (be they from necessary or contingent properties) are the same.<p>
   */
  private void keysAreAllDistinctOrThrow() {
    HashSet<String> keys = new HashSet<>();
    Iterator<String> propKeyIter = Stream
      .concat(
        identifier().propertySet().properties().map(PropertyDescriptor::key),
        mContingentProperties.properties().map(PropertyDescriptor::key)
      )
      .iterator();
    while (propKeyIter.hasNext()) {
      if (!keys.add(propKeyIter.next())) {
        throw new IllegalStateException(M_DUPLICATE_PROPERTY_KEY);
      }
    }
  }

  /**
   * Verifies that no two property logKeys (be they from necessary or contingent properties) are the same.<p>
   */
  private void logkeysAreAllDistinctOrThrow() {
    HashSet<String> logKeys = new HashSet<>();
    Iterator<String> logKeyIter = Stream
      .concat(
        identifier().propertySet().properties().map(PropertyDescriptor::logKey),
        mContingentProperties.properties().map(PropertyDescriptor::logKey)
      )
      .iterator();
    while (logKeyIter.hasNext()) {
      if (!logKeys.add(logKeyIter.next())) {
        throw new IllegalStateException(M_DUPLICATE_PROPERTY_LOGKEY);
      }
    }
  }

  protected void initProperties(Class<?> clazz) {
    mContingentProperties = mContingentProperties.openCopy();
    try {
      Set<String> propKeys = Stream
        .concat(identifier().properties(), mContingentProperties.properties())
        .map(PropertyDescriptor::key)
        .collect(Collectors.toSet());

      Set<String> propLogKeys = Stream
        .concat(identifier().properties(), mContingentProperties.properties())
        .map(PropertyDescriptor::logKey)
        .collect(Collectors.toSet());

      Iterator<Field> fieldsIter = scanFields(clazz, PropertyDescriptor.class)
        .iterator();

      while (fieldsIter.hasNext()) {
        Field field = fieldsIter.next();
        int mod = field.getModifiers();
        if (!Modifier.isPublic(mod) || !Modifier.isFinal(mod)) {
          throw new IllegalStateException(
            M_PROPERTY_FIELDS_MUST_BE_PUBLIC_FINAL
          );
        }
        PropertyDescriptor prop = (PropertyDescriptor) field.get(this);
        if (!propKeys.add(prop.key())) {
          throw new IllegalStateException(M_DUPLICATE_PROPERTY_KEY);
        }
        if (!propLogKeys.add(prop.logKey())) {
          throw new IllegalStateException(M_DUPLICATE_PROPERTY_LOGKEY);
        }

        boolean propIsId = field.isAnnotationPresent(Identifying.class);
        boolean propIsCont = field.isAnnotationPresent(Validate.class);
        if (propIsId == propIsCont) {
          throw new IllegalStateException(M_PROPERTY_FIELDS_MUST_BE_ANNOTATED);
        }
        if (propIsId) {
          identifier().add(prop);
        } else {
          mContingentProperties.add(prop);
        }
      }
    } catch (IllegalAccessException exc) {
      throw new IllegalStateException();
    }
    keysAreAllDistinctOrThrow();
    logkeysAreAllDistinctOrThrow();
  }

  /**
   * Returns a list of all public final fields whose type is equal to or derived from the specified class
   */
  protected List<Method> scanPublicFinalMethods(Class<?> fieldType2Look4) {
    ArrayList<Method> result = new ArrayList<>();
    Method[] allMethods = this.getClass().getMethods();
    for (Method meth : allMethods) {
      int mods = meth.getModifiers();
      if (!Modifier.isFinal(mods) || !Modifier.isPublic(mods)) {
        continue;
      }
      Class<?> fieldType = meth.getReturnType();
      if (fieldType2Look4.isAssignableFrom(fieldType)) {
        result.add(meth);
      }
    }
    return result;
  }

  // endregion

  // region accessors

  public abstract EntityIdentifier identifier();

  public Stream<PropertyDescriptor> properties(Modality modality) {
    switch (modality) {
      case NECESSARY:
        return identifier().properties();
      case CONTINGENT:
        return mContingentProperties.properties();
      case BOTH:
        return Stream
          .concat(identifier().properties(), mContingentProperties.properties())
          .distinct();
      default:
        throw new IllegalArgumentException();
    }
  }

  public PropertyDescriptor findMatch(PropertyDescriptorDto dto) {
    Optional<PropertyDescriptor> match = Stream
      .concat(identifier().properties(), mContingentProperties.properties())
      .filter(o -> DtoObjectMatch.propertyMatch(o, dto))
      .findFirst();
    if (match.isPresent()) {
      return match.get();
    }
    return null;
  }

  protected String idPrintBit(String mI, String sI) {
    String idPrint = identifier().print(mI, sI + sI);
    if (idPrint.length() == 0) {
      idPrint = "-";
    } else {
      idPrint = System.lineSeparator() + idPrint;
    }
    return mI + sI + IDENTIFIER + idPrint;
  }

  protected String propBit(String mI, String sI) {
    String propPrint = mContingentProperties.print(mI, sI + sI);
    if (propPrint.length() == 0) {
      propPrint = "-";
    } else {
      propPrint = System.lineSeparator() + propPrint;
    }
    return mI + sI + PROPERTIES + propPrint;
  }

  // endregion

  protected void validateProperties(EntityReport report2write2, Entity entity) {
    Iterator<PropertyDescriptor> propIter = mContingentProperties
      .properties()
      .iterator();
    while (propIter.hasNext()) {
      PropertyDescriptor prop = propIter.next();
      PropertyReport propReport = prop.validate(entity);
      report2write2.addPropertyReport(propReport);
    }
  }

  // region equality, has and compare overrides

  protected int processHash() {
    return this.getClass().hashCode();
  }

  protected boolean processEquals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    return this.getClass() == obj.getClass();
  }

  protected int processCompare(EntityDescriptor other) {
    if (other == null) {
      return 1;
    }
    if (other == this) {
      return 0;
    }
    String thisClassName = this.getClass().getName();
    String thatClassName = other.getClass().getName();
    return thisClassName.compareTo(thatClassName);
  }

  /* Why use only the class for hash, equals and compare rather the private fields?
   *
   * Because NodeDescriptor <-> RelationshipDescriptor know each other
   *
   * To avoid a stack overflow, I could stick to using the class instead of the
   * instance's fields on one side. But which one?
   * Seems arbitrary. So I will stick to using the class on both sides.
   *
   * Both NodeDescriptor and RelationshipDescriptor are supposed to be immutable,
   * and could all be singletons, so this should work out.
   */

  @Override
  public final int hashCode() {
    if (mHash == null) {
      mHash = processHash();
    }
    return mHash;
  }

  @Override
  public final boolean equals(Object obj) {
    return processEquals(obj);
  }

  public final int compareTo(EntityDescriptor other) {
    return processCompare(other);
  }
  // endregion
}
