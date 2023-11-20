package org.rle.neo4jdescriptor.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.rle.neo4jdescriptor.annotation.RepositoryMember;

public abstract class RepositoryBase<T> {

  private static final String M_REPOSITORY_MEMBER_STATIC =
    "All Repository Members must be static final fields";

  private static final String M_FIELD_VALUE_IS_NULL =
    "Repository member field value is null. This may happen half way through the type initialisation";

  private static final String M_UNEXPECTED_INIT_EXCEPTION =
    "Unexpected exception during repository initialisation";

  private TreeSet<T> mThingies = new TreeSet<>();

  private Class<T> mThingieClass;

  // region ctor and init

  /**
   * Scans the specified class for fields of the specified type that are annotated with {@link RepositoryMember}. <p>
   * Will verify that all such fields are both static and final.
   * @param type2Look4 - the field type to look for (this can be a supertype.
   * This method will look for this type and all derived subtypes)
   * @param class2Scan - the class to scan
   * @param outThingies - the values of the fields. This is an out parameter!
   * @param outSubClasses2Scan - all subclasses to scan as well. This is an out parameter!
   * @throws IllegalStateException <p>
   *  1) in case of an unexpected reflection error <p>
   *  2) if there is a tagged field that isnt both final and static <p>
   *  3) if the value of the field is null (this may happen in case of weird in between stages of the type init process)
   */
  private void scanStaticFields4RepositoryMembers(
    Class<T> type2Look4,
    Class<?> class2Scan,
    List<T> outThingies,
    List<Class<?>> outSubClasses2Scan
  ) {
    Field[] allFields = class2Scan.getFields();
    for (Field field : allFields) {
      if (!field.isAnnotationPresent(RepositoryMember.class)) {
        continue;
      }
      if (
        !Modifier.isStatic(field.getModifiers()) ||
        !Modifier.isFinal(field.getModifiers())
      ) {
        throw new IllegalStateException(M_REPOSITORY_MEMBER_STATIC);
      }
      try {
        Object fieldValue = field.get(null);
        if (fieldValue == null) {
          throw new IllegalStateException(M_FIELD_VALUE_IS_NULL);
        }
        if (type2Look4.isAssignableFrom(fieldValue.getClass())) {
          outThingies.add(type2Look4.cast(fieldValue));
        }
      } catch (IllegalArgumentException | IllegalAccessException e) {
        throw new IllegalStateException(M_UNEXPECTED_INIT_EXCEPTION);
      }
    }
    outSubClasses2Scan.addAll(Arrays.asList(class2Scan.getDeclaredClasses()));
  }

  private List<T> scan4Thingies() {
    ArrayList<T> allThingies = new ArrayList<>();
    ArrayDeque<Class<?>> classes2Scan = new ArrayDeque<>();
    classes2Scan.add(this.getClass());
    ArrayList<T> thingies = new ArrayList<>();
    ArrayList<Class<?>> subClasses = new ArrayList<>();
    while (!classes2Scan.isEmpty()) {
      Class<?> clazz2Scan = classes2Scan.poll();
      thingies.clear();
      subClasses.clear();
      scanStaticFields4RepositoryMembers(
        mThingieClass,
        clazz2Scan,
        thingies,
        subClasses
      );
      allThingies.addAll(thingies);
      for (Class<?> c : subClasses) {
        classes2Scan.add(c);
      }
    }
    return allThingies;
  }

  private void initThingies() {
    mThingies.clear();
    List<T> scanResult = scan4Thingies();
    for (T thing : scanResult) {
      if (!register(thing)) {
        String msg = String.format(
          "duplicate %s found: %s",
          mThingieClass.getName(),
          debugName(thing)
        );
        throw new IllegalStateException(msg);
      }
    }
  }

  protected RepositoryBase(Class<T> thingieClass) {
    mThingieClass = thingieClass;
    initThingies();
  }

  // endregion

  public boolean register(T thing) {
    return mThingies.add(thing);
  }

  public int register(Iterable<T> things) {
    int count = 0;
    for (T t : things) {
      if (register(t)) {
        count++;
      }
    }
    return count;
  }

  protected abstract String debugName(T thing);

  protected Stream<T> thingyStream() {
    return mThingies.stream();
  }
}
