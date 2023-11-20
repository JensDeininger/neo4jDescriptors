package org.rle.neo4jdescriptor.property.array_basic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.PropertyTestBase;
import org.rle.neo4jdescriptor.property.prop_basic.DoubleProperty;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DoubleArrayPropertyTest extends PropertyTestBase {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(DoubleArrayPropertyTestProcedure.class);
  }

  @Override
  protected PropertyDescriptor createPropertyDescriptor(
    String key,
    String logKey
  ) {
    return new DoubleArrayProperty(key, logKey);
  }

  @Override
  protected PropertyDescriptor[] createDifferentTypedPropertyDescriptors(
    String key,
    String logKey
  ) {
    return new PropertyDescriptor[] {
      new DoubleProperty(key, logKey),
      new LongArrayProperty(key, logKey),
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
    String[] fails = super.runBasicPropertyTests("DoubleArrayProperty");
    String msg = String.join(System.lineSeparator(), fails);
    assertEquals(0, fails.length, msg);
  }
}
