package org.rle.neo4jdescriptor.property.prop_basic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.PropertyTestBase;
import org.rle.neo4jdescriptor.property.array_basic.BooleanArrayProperty;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BooleanPropertyTest extends PropertyTestBase {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(BooleanPropertyTestProcedure.class);
  }

  @Override
  protected PropertyDescriptor createPropertyDescriptor(
    String key,
    String logKey
  ) {
    return new BooleanProperty(key, logKey);
  }

  @Override
  protected PropertyDescriptor[] createDifferentTypedPropertyDescriptors(
    String key,
    String logKey
  ) {
    return new PropertyDescriptor[] {
      new BooleanArrayProperty(key, logKey),
      new DoubleProperty(key, logKey),
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
    String[] fails = super.runBasicPropertyTests("BooleanProperty");
    String msg = String.join(System.lineSeparator(), fails);
    assertEquals(0, fails.length, msg);
  }
}
