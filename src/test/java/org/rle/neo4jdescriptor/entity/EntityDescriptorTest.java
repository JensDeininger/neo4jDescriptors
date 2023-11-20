package org.rle.neo4jdescriptor.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.dto.entity.PropertyDescriptorDto;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.testutils.TestBase;

public abstract class EntityDescriptorTest extends TestBase {

  protected void expectedPropertyCheck(
    EntityDescriptor entDesc,
    TreeSet<PropertyDescriptor> contingents,
    TreeSet<PropertyDescriptor> necessarys
  ) {
    TreeSet<PropertyDescriptor> conts = entDesc
      .properties(Modality.CONTINGENT)
      .collect(Collectors.toCollection(TreeSet::new));

    TreeSet<PropertyDescriptor> neccs = entDesc
      .properties(Modality.NECESSARY)
      .collect(Collectors.toCollection(TreeSet::new));

    TreeSet<PropertyDescriptor> boths = entDesc
      .properties(Modality.BOTH)
      .collect(Collectors.toCollection(TreeSet::new));

    String msg = String.format("Fail %s ", entDesc.getClass().getName());
    assertEquals(contingents.size(), conts.size(), msg + "contingent size");
    assertEquals(necessarys.size(), neccs.size(), msg + "necessary size");
    assertEquals(
      contingents.size() + necessarys.size(),
      boths.size(),
      msg + "both size"
    );

    assertTrue(
      EqualityUtils.setEquals(contingents, conts),
      msg + "cont equals"
    );
    assertTrue(EqualityUtils.setEquals(necessarys, neccs), msg + "cont equals");
    TreeSet<PropertyDescriptor> combo = new TreeSet<>();
    combo.addAll(contingents);
    combo.addAll(necessarys);
    assertTrue(EqualityUtils.setEquals(combo, boths), msg + "combo equals");
  }

  protected void matchDtoTest(EntityDescriptor entDesc) {
    PropertyDescriptor[] props = entDesc
      .properties(Modality.BOTH)
      .toArray(PropertyDescriptor[]::new);
    for (PropertyDescriptor propDesc : props) {
      PropertyDescriptorDto dto = new PropertyDescriptorDto(
        propDesc.key(),
        propDesc.logKey()
      );
      PropertyDescriptor propDescRev = entDesc.findMatch(dto);
      assertSame(propDesc, propDescRev);
    }
  }

  protected void faultyDeclarationTest(
    Object testClassInstance,
    Class<? extends EntityDescriptor> clazz,
    String expectedMsg
  ) {
    Throwable exc = null;
    try {
      Constructor<? extends EntityDescriptor> ctor = clazz.getDeclaredConstructor(
        testClassInstance.getClass()
      );
      ctor.setAccessible(true);
      ctor.newInstance(testClassInstance);
    } catch (
      InstantiationException
      | NoSuchMethodException
      | SecurityException
      | IllegalAccessException e
    ) {
      assertFalse(true, e.getMessage());
    } catch (InvocationTargetException e) {
      exc = e.getCause();
    }
    assertTrue(exc instanceof IllegalStateException, "fail 1");
    assertEquals(expectedMsg, exc.getMessage(), "fail 2");
  }
}
