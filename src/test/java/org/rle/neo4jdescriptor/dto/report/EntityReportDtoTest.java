package org.rle.neo4jdescriptor.dto.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.entity.EntityDescriptor;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;

public abstract class EntityReportDtoTest extends ReportBaseDtoTest {

  @Override
  protected abstract EntityReportDto createSampleDto(long seed);

  protected PropertyReportDto[] createSamplePropertyReports(
    EntityDescriptor entityDesc,
    Random ran
  ) {
    Iterator<PropertyDescriptor> propIter = entityDesc
      .properties(Modality.CONTINGENT)
      .iterator();
    List<PropertyReportDto> propReports = new ArrayList<>();
    while (propIter.hasNext()) {
      if (ran.nextDouble() < 0.3) {
        continue;
      }
      PropertyReportDto propReport = PropertyDescriptorReportDtoTest.sample(
        propIter.next(),
        ran.nextLong()
      );
      propReports.add(propReport);
    }
    return propReports.toArray(PropertyReportDto[]::new);
  }

  protected void inequalityTest_dbId() {
    EntityReportDto dto1 = createSampleDto(0);
    EntityReportDto dto2 = createSampleDto(0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setDbId("23");
    dto1.setDbId("42");
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  protected void inequalityTest_properties() {
    EntityReportDto dto1 = createSampleDto(0);
    EntityReportDto dto2 = createSampleDto(0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setPropertyReportDtos(
      new PropertyReportDto[dto1.getPropertyReportDtos().length + 1]
    );
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  protected void hashResetCheck_dbId() {
    EntityReportDto dto = createSampleDto(0);
    int origHash = dto.hashCode();
    dto.setDbId(dto.getDbId() + 1);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  protected void hashResetCheck_properties() {
    EntityReportDto dto = createSampleDto(0);
    int origHash = dto.hashCode();
    dto.setPropertyReportDtos(
      new PropertyReportDto[dto.getPropertyReportDtos().length + 1]
    );
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }
}
