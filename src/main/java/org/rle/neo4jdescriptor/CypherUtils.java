package org.rle.neo4jdescriptor;

import org.rle.neo4jdescriptor.enuminterface.StringEnum;
import org.rle.neo4jdescriptor.enuminterface.DoubleEnum;
import org.rle.neo4jdescriptor.enuminterface.LongEnum;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.IntStream;

public class CypherUtils {

  private CypherUtils() {}

  public static String bool2Cypher(Boolean bool) {
    return Boolean.toString(bool);
  }

  public static String double2Cypher(Double number) {
    if (Double.isNaN(number)) {
      return "0.0/0.0";
    }
    if (Double.isInfinite(number)) {
      if (number > 0) {
        return "1.0/0.0";
      } else {
        return "-1.0/0.0";
      }
    }
    return String.valueOf(number);
  }

  public static String long2Cypher(Long number) {
    return Long.toString(number);
  }

  public static String number2Cypher(Number number) {
    if (
      number instanceof BigInteger ||
      number instanceof Byte ||
      number instanceof Integer ||
      number instanceof Long
    ) {
      return long2Cypher(number.longValue());
    } else {
      return double2Cypher(number.doubleValue());
    }
  }

  public static String string2Cypher(String input) {
    String s = input.replace("\\", "\\\\");
    s = s.replace("\"", "\\\"");
    s = s.replace("'", "\\'");
    return "'" + s + "'";
  }

  public static String object2Cypher(Object obj) {
    if (obj instanceof Boolean) {
      return bool2Cypher((Boolean) obj);
    }
    if (obj instanceof Double) {
      return double2Cypher((Double) obj);
    }
    if (obj instanceof Long) {
      return long2Cypher((Long) obj);
    }
    if (obj instanceof Number) {
      return number2Cypher((Number) obj);
    }
    if (obj instanceof String) {
      return string2Cypher((String) obj);
    }
    throw new IllegalArgumentException();
  }

  /////////////////////////////////////

  public static String doubleEnum2Cypher(DoubleEnum doubleEnum) {
    return double2Cypher(doubleEnum.dbValue());
  }

  public static String longEnum2Cypher(LongEnum longEnum) {
    return long2Cypher(longEnum.dbValue());
  }

  public static String stringEnum2Cypher(StringEnum stringEnum) {
    return string2Cypher(stringEnum.dbValue());
  }

  ////////////////////////////////////////

  public static String booleanArray2Cypher(Boolean[] array) {
    String[] itemStrings = Arrays
      .stream(array)
      .map(CypherUtils::bool2Cypher)
      .toArray(String[]::new);
    String allItemsString = String.join(",", itemStrings);
    return String.format("[%s]", allItemsString);
  }

  public static String booleanArray2Cypher(boolean[] array) {
    String[] itemStrings = IntStream
      .range(0, array.length)
      .mapToObj(idx -> array[idx])
      .map(CypherUtils::bool2Cypher)
      .toArray(String[]::new);
    String allItemsString = String.join(",", itemStrings);
    return String.format("[%s]", allItemsString);
  }

  public static String doubleArray2Cypher(Double[] array) {
    String[] itemStrings = Arrays
      .stream(array)
      .map(CypherUtils::double2Cypher)
      .toArray(String[]::new);
    String allItemsString = String.join(",", itemStrings);
    return String.format("[%s]", allItemsString);
  }

  public static String doubleArray2Cypher(double[] array) {
    String[] itemStrings = Arrays
      .stream(array)
      .mapToObj(o -> (Double) (o))
      .map(CypherUtils::double2Cypher)
      .toArray(String[]::new);
    String allItemsString = String.join(",", itemStrings);
    return String.format("[%s]", allItemsString);
  }

  public static String longArray2Cypher(Long[] array) {
    String[] itemStrings = Arrays
      .stream(array)
      .map(CypherUtils::long2Cypher)
      .toArray(String[]::new);
    String allItemsString = String.join(",", itemStrings);
    return String.format("[%s]", allItemsString);
  }

  public static String longArray2Cypher(long[] array) {
    String[] itemStrings = Arrays
      .stream(array)
      .mapToObj(o -> (Long) (o))
      .map(CypherUtils::long2Cypher)
      .toArray(String[]::new);
    String allItemsString = String.join(",", itemStrings);
    return String.format("[%s]", allItemsString);
  }

  public static String stringArray2Cypher(String[] array) {
    String[] itemStrings = Arrays
      .stream(array)
      .map(CypherUtils::string2Cypher)
      .toArray(String[]::new);
    String allItemsString = String.join(",", itemStrings);
    return String.format("[%s]", allItemsString);
  }
}
