package org.rle.neo4jdescriptor.property.prop_enum;

import org.rle.neo4jdescriptor.property.prop_enum.dummyenums.DummyStringEnum;
import org.rle.neo4jdescriptor.property.prop_enum.dummyenums.DummyStringEnum2;
import org.rle.neo4jdescriptor.property.prop_enum.dummyenums.DummyDoubleEnum;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.property.EnumProperty;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.array_basic.StringArrayProperty;
import org.rle.neo4jdescriptor.property.prop_basic.StringProperty;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringEnumPropertyTest extends EnumPropertyTestBase {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(StringEnumPropertyTestProcedure.class);
  }

  @Override
  protected PropertyDescriptor createPropertyDescriptor(
    String key,
    String logKey
  ) {
    return new EnumProperty<String, DummyStringEnum>(
      key,
      logKey,
      String.class,
      DummyStringEnum.class
    );
  }

  @Override
  protected PropertyDescriptor[] createDifferentTypedPropertyDescriptors(
    String key,
    String logKey
  ) {
    return new PropertyDescriptor[] {
      new StringProperty(key, logKey),
      new StringArrayProperty(key, logKey),
      new EnumProperty<>(key, logKey, Double.class, DummyDoubleEnum.class),
      new EnumProperty<>(key, logKey, String.class, DummyStringEnum2.class),
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
    String[] fails = super.runBasicPropertyTests("StringEnumProperty");
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
