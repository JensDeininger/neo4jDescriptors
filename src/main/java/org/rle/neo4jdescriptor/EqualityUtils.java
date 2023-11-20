package org.rle.neo4jdescriptor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import org.rle.neo4jdescriptor.entity.EntityDescriptor;

public class EqualityUtils {

  private EqualityUtils() {}

  public static <T> boolean itemEquals(T item1, T item2) {
    if (item1 == null && item2 == null) {
      return true;
    }
    if (item1 == null || item2 == null) {
      return false;
    }
    if (
      item1.getClass().equals(Double.class) ||
      item1.getClass().equals(double.class)
    ) {
      return doubleEquals(Double.class.cast(item1), Double.class.cast(item2));
    }
    return item1.equals(item2);
  }

  /**
   * No epsilon trickery here, none at all. It is only supposed to be used to check
   * writing to and reading from the database by the various means works. <p>
   * For the same rason, two NaN and same sign infinities count as equal
   */
  public static boolean doubleEquals(Double d1, Double d2) {
    if (Double.isNaN(d1) && Double.isNaN(d2)) {
      return true;
    }
    if (Double.isInfinite(d1) && Double.isInfinite(d2)) {
      return Math.signum(d1) == Math.signum(d2);
    }
    // Double d1 = 0.0; Double d2 = -0.0; d1 == d2 -> false d1.equals(d2) -> false d1.doubleValue() == d2.doubleValue() -> true
    return d1.doubleValue() == d2.doubleValue();
  }

  /**
   * No epsilon trickery here, none at all. It is only supposed to be used to check
   * writing to and reading from the database by the various means works. <p>
   * For the same rason, two NaN and same sign infinities count as equal
   */
  public static boolean numberEquals(Number n1, Number n2) {
    if (n1 == null && n2 == null) {
      return true;
    }
    if (n1 == null || n2 == null) {
      return false;
    }
    return doubleEquals(n1.doubleValue(), n2.doubleValue());
  }

  public static <T> boolean arrayEquals(T[] a1, T[] a2) {
    if (a1 == null && a2 == null) {
      return true;
    }
    if (a1 == null || a2 == null) {
      return false;
    }
    if (a1.length != a2.length) {
      return false;
    }
    Class<?> compType = a1.getClass().getComponentType();
    if (compType.equals(Double.class) || compType.equals(double.class)) {
      Double[] d1 = Arrays
        .stream(a1)
        .map(Double.class::cast)
        .toArray(Double[]::new);
      Double[] d2 = Arrays
        .stream(a2)
        .map(Double.class::cast)
        .toArray(Double[]::new);
      return doubleArrayEquals(d1, d2);
    }
    for (int i = 0; i < a1.length; i++) {
      if (!itemEquals(a1[i], a2[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * No epsilon trickery here, none at all. It is only supposed to be used to check
   * writing to and reading from the database by the various means works. <p>
   * For the same rason, two NaN and same sign infinities count as equal
   */
  public static boolean doubleArrayEquals(Double[] a1, Double[] a2) {
    if (a1 == null && a2 == null) {
      return true;
    }
    if (a1 == null || a2 == null) {
      return false;
    }
    if (a1.length != a2.length) {
      return false;
    }
    for (int i = 0; i < a1.length; i++) {
      if (!doubleEquals(a1[i], a2[i])) {
        return false;
      }
    }
    return true;
  }

  public static <T> boolean listEquals(List<T> list1, List<T> list2) {
    if (list1 == null && list2 == null) {
      return true;
    }
    if (list1 == null || list2 == null) {
      return false;
    }
    if (list1.size() != list2.size()) {
      return false;
    }
    Iterator<T> iter1 = list1.iterator();
    Iterator<T> iter2 = list2.iterator();
    while (iter1.hasNext() && iter2.hasNext()) {
      T item1 = iter1.next();
      T item2 = iter2.next();
      if (!itemEquals(item1, item2)) {
        return false;
      }
    }
    return true;
  }

  public static <T> boolean setEquals(Set<T> set1, Set<T> set2) {
    if (set1 == null && set2 == null) {
      return true;
    }
    if (set1 == null || set2 == null) {
      return false;
    }
    if (set1.size() != set2.size()) {
      return false;
    }
    // yep, Java naturally overloads the equals and hashCode methods on all sets
    // even two differently typed sets (like a HashSet and a TreeSet) turn out
    // equal as long as the members are equal
    return set1.equals(set2);
  }

  public static <T> int arrayHash(T[] things) {
    if (things == null) {
      return -1;
    }
    int hash = things.length;
    for (T t : things) {
      hash = (397 * hash) ^ (t == null ? 0 : t.hashCode());
    }
    return hash;
  }

  public static <T> int listHash(List<T> list) {
    if (list == null) {
      return -1;
    }
    int hash = list.size();
    for (T t : list) {
      hash = (397 * hash) ^ (t == null ? 0 : t.hashCode());
    }
    return hash;
  }

  public static <T> int sortedSetHash(SortedSet<T> sortedSet) {
    if (sortedSet == null) {
      return -1;
    }
    int hash = sortedSet.size();
    for (T t : sortedSet) {
      hash = (397 * hash) ^ (t == null ? 0 : t.hashCode());
    }
    return hash;
  }

  public static <T extends Comparable<T>> int itemCompare(T item1, T item2) {
    if (item1 == null && item2 == null) {
      return 0;
    }
    if (item1 == null) {
      return -1;
    }
    if (item2 == null) {
      return 1;
    }
    return item1.compareTo(item2);
  }

  public static int itemCompare(
    EntityDescriptor item1,
    EntityDescriptor item2
  ) {
    if (item1 == null && item2 == null) {
      return 0;
    }
    if (item1 == null) {
      return -1;
    }
    if (item2 == null) {
      return 1;
    }
    return item1.compareTo(item2);
  }

  public static <T extends Comparable<T>> int listCompare(
    List<T> list1,
    List<T> list2
  ) {
    if (list1 == null && list2 == null) {
      return 0;
    }
    if (list1 == null) {
      return -1;
    }
    if (list2 == null) {
      return 1;
    }
    int sizeComp = Integer.compare(list1.size(), (list2.size()));
    if (sizeComp != 0) {
      return sizeComp;
    }
    Iterator<T> iter1 = list1.iterator();
    Iterator<T> iter2 = list2.iterator();
    while (iter1.hasNext() && iter2.hasNext()) {
      T item1 = iter1.next();
      T item2 = iter2.next();
      int itemComp = itemCompare(item1, item2);
      if (itemComp != 0) {
        return itemComp;
      }
    }
    return 0;
  }

  public static <T extends Comparable<T>> int sortedSetCompare(
    SortedSet<T> set1,
    SortedSet<T> set2
  ) {
    if (set1 == null && set2 == null) {
      return 0;
    }
    if (set1 == null) {
      return -1;
    }
    if (set2 == null) {
      return 1;
    }
    int sizeComp = Integer.compare(set1.size(), (set2.size()));
    if (sizeComp != 0) {
      return sizeComp;
    }
    Iterator<T> iter1 = set1.iterator();
    Iterator<T> iter2 = set2.iterator();
    while (iter1.hasNext() && iter2.hasNext()) {
      T item1 = iter1.next();
      T item2 = iter2.next();
      int itemComp = itemCompare(item1, item2);
      if (itemComp != 0) {
        return itemComp;
      }
    }
    return 0;
  }

  public static <T extends EntityDescriptor> int entityDescriptorSetCompare(
    SortedSet<T> set1,
    SortedSet<T> set2
  ) {
    if (set1 == null && set2 == null) {
      return 0;
    }
    if (set1 == null) {
      return -1;
    }
    if (set2 == null) {
      return 1;
    }
    int sizeComp = Integer.compare(set1.size(), (set2.size()));
    if (sizeComp != 0) {
      return sizeComp;
    }
    Iterator<T> iter1 = set1.iterator();
    Iterator<T> iter2 = set2.iterator();
    while (iter1.hasNext() && iter2.hasNext()) {
      T item1 = iter1.next();
      T item2 = iter2.next();
      int itemComp = itemCompare(item1, item2);
      if (itemComp != 0) {
        return itemComp;
      }
    }
    return 0;
  }
}
