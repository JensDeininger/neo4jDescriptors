package org.rle.neo4jdescriptor.property.prop_enum;

import org.rle.neo4jdescriptor.property.prop_enum.dummyenums.DummyLongEnum;
import org.rle.neo4jdescriptor.property.prop_enum.dummyenums.DummyDoubleEnum;
import org.rle.neo4jdescriptor.property.prop_enum.dummyenums.DummyDoubleEnum2;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.property.EnumProperty;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.array_basic.DoubleArrayProperty;
import org.rle.neo4jdescriptor.property.prop_basic.DoubleProperty;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DoubleEnumPropertyTest extends EnumPropertyTestBase {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(DoubleEnumPropertyTestProcedure.class);
  }

  @Override
  protected PropertyDescriptor createPropertyDescriptor(
    String key,
    String logKey
  ) {
    return new EnumProperty<Double, DummyDoubleEnum>(
      key,
      logKey,
      Double.class,
      DummyDoubleEnum.class
    );
  }

  @Override
  protected PropertyDescriptor[] createDifferentTypedPropertyDescriptors(
    String key,
    String logKey
  ) {
    return new PropertyDescriptor[] {
      new DoubleProperty(key, logKey),
      new DoubleArrayProperty(key, logKey),
      new EnumProperty<>(key, logKey, Long.class, DummyLongEnum.class),
      new EnumProperty<>(key, logKey, Double.class, DummyDoubleEnum2.class),
    };
  }

  @Test
  public void crapArgsTest() {
    super.crapArgsTest();
  }

  @Test
  public void identicalDeclarationTest() {
    super.identicalDeclarationTest();
  }

  @Test
  public void copyTest() {
    super.copyTest();
  }

  @Test
  public void unequalsTest() {
    super.unequalsTest();
  }

  @Test
  public void basicPropTests() {
    String[] fails = super.runBasicPropertyTests("DoubleEnumProperty");
    String msg = String.join(System.lineSeparator(), fails);
    assertEquals(0, fails.length, msg);
  }

  @Test
  public void enumPropTests() {
    String[] fails = super.runEnumPropertyTests("DoubleEnumProperty");
    String msg = String.join(System.lineSeparator(), fails);
    assertEquals(0, fails.length, msg);
  }
}
