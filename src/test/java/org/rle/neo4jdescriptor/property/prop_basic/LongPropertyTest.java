package org.rle.neo4jdescriptor.property.prop_basic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.PropertyTestBase;
import org.rle.neo4jdescriptor.property.array_basic.LongArrayProperty;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LongPropertyTest extends PropertyTestBase {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return Stream.of(LongPropertyTestProcedure.class);
  }

  @Override
  protected PropertyDescriptor createPropertyDescriptor(
    String key,
    String logKey
  ) {
    return new LongProperty(key, logKey);
  }

  @Override
  protected PropertyDescriptor[] createDifferentTypedPropertyDescriptors(
    String key,
    String logKey
  ) {
    return new PropertyDescriptor[] {
      new LongArrayProperty(key, logKey),
      new NumberProperty(key, logKey),
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
    String[] fails = super.runBasicPropertyTests("LongProperty");
    String msg = String.join(System.lineSeparator(), fails);
    assertEquals(0, fails.length, msg);
  }
}
