package org.rle.neo4jdescriptor.property.prop_basic;

import java.io.InputStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.PropertyTestBase;
import org.rle.neo4jdescriptor.property.array_basic.LongArrayProperty;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ObjectPropertyTest extends PropertyTestBase {

  @Override
  protected InputStream inputStreamOfCypherFile() {
    return null;
  }

  @Override
  protected Stream<Class<?>> procedureClasses() {
    return null;
  }

  @Override
  protected String initialCypher() {
    return null;
  }

  @Override
  protected PropertyDescriptor createPropertyDescriptor(
    String key,
    String logKey
  ) {
    return new ObjectProperty(key, logKey);
  }

  @Override
  protected PropertyDescriptor[] createDifferentTypedPropertyDescriptors(
    String key,
    String logKey
  ) {
    return new PropertyDescriptor[] {
      new LongArrayProperty(key, logKey),
      new StringProperty(key, logKey),
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
}
