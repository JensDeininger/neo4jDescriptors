package org.rle.neo4jdescriptor.dto.report;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.dto.entity.RelationshipDescriptorDto;
import org.rle.neo4jdescriptor.entity.*;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;

public class RelationshipReportDtoTest extends EntityReportDtoTest {

  @Override
  protected RelationshipReportDto createSampleDto(long seed) {
    RelationshipDescriptor[] relDescs = new RelationshipDescriptor[] {
      SampleRelationshipRep.BarFoo,
      SampleRelationshipRep.FooBar,
      SampleRelationshipRep.HasBar,
      SampleRelationshipRep.HasFoo,
      SampleRelationshipRep.HasKeks,
      SampleRelationshipRep.IsSubKeksOf,
      SampleRelationshipRep.EndsAtKeks,
    };
    return sample(relDescs[new Random(seed).nextInt(relDescs.length)], seed);
  }

  protected static RelationshipReportDto sample(
    RelationshipDescriptor relationshipDescriptor,
    long seed
  ) {
    Random ran = new Random(seed);
    RelationshipReportDto dto = new RelationshipReportDto(
      relationshipDescriptor
    );
    dto.setExceptionMsgs(ranExcMessages(ran, 0));
    dto.setDbId(String.valueOf(ran.nextLong()));
    dto.setStartNodeCheck(ranBool(ran));
    dto.setEndNodeCheck(ranBool(ran));
    PropertyReportDto[] propertyReports = relationshipDescriptor
      .properties(Modality.CONTINGENT)
      .filter(o -> ran.nextDouble() < 0.8)
      .map(o -> new PropertyReportDto(o))
      .toArray(PropertyReportDto[]::new);
    dto.setPropertyReportDtos(propertyReports);
    return dto;
  }

  @Test
  public void equalityTest() {
    List<Long> seeds = createSeeds(0, 20);
    for (Long seed : seeds) {
      RelationshipReportDto dto1 = createSampleDto(seed);
      RelationshipReportDto dto2 = createSampleDto(seed);
      assertEquals(dto1, dto2, "fail @ seed " + seed);
      assertEquals(dto1.hashCode(), dto2.hashCode(), "fail @ seed " + seed);
    }
  }

  // region inequality tests

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions here
  public void inequalityTest_exceptions() {
    super.inequalityTest_exceptions();
  }

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions here
  public void inequalityTest_dbId() {
    super.inequalityTest_dbId();
  }

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions here
  public void inequalityTest_properties() {
    super.inequalityTest_properties();
  }

  @Test
  public void inequalityTest_relationshipDescriptor() {
    RelationshipReportDto dto1 = sample(SampleRelationshipRep.FooBar, 0);
    RelationshipReportDto dto2 = sample(SampleRelationshipRep.FooBar, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto2.setRelationshipDescriptorDto(
      new RelationshipDescriptorDto(SampleRelationshipRep.BarFoo)
    );
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_startCheck() {
    RelationshipReportDto dto1 = sample(SampleRelationshipRep.FooBar, 0);
    RelationshipReportDto dto2 = sample(SampleRelationshipRep.FooBar, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto1.setStartNodeCheck(true);
    dto2.setStartNodeCheck(null);
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_endCheck() {
    RelationshipReportDto dto1 = sample(SampleRelationshipRep.FooBar, 0);
    RelationshipReportDto dto2 = sample(SampleRelationshipRep.FooBar, 0);
    assertEquals(dto1, dto2, "equality fail");
    dto1.setEndNodeCheck(true);
    dto2.setEndNodeCheck(null);
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  // endregion

  // region hash reset tests

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions in the methods that just call super
  public void hashResetCheck_exceptions() {
    super.hashResetCheck_exceptions();
  }

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions in the methods that just call super
  public void hashResetCheck_dbId() {
    super.hashResetCheck_dbId();
  }

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions in the methods that just call super
  public void hashResetCheck_properties() {
    super.hashResetCheck_properties();
  }

  @Test
  public void hashResetCheck_relationshipDescriptor() {
    RelationshipReportDto dto = sample(SampleRelationshipRep.FooBar, 0);
    int origHash = dto.hashCode();
    dto.setRelationshipDescriptorDto(
      new RelationshipDescriptorDto(SampleRelationshipRep.HasKeks)
    );
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode(), "hash diff fail");
  }

  @Test
  public void hashResetCheck_startCheck() {
    RelationshipReportDto dto = sample(SampleRelationshipRep.FooBar, 0);
    int origHash = dto.hashCode();
    dto.setStartNodeCheck(dto.getStartNodeCheck() == null ? true : null);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode(), "hash diff fail");
  }

  @Test
  public void hashResetCheck_endCheck() {
    RelationshipReportDto dto = sample(SampleRelationshipRep.FooBar, 0);
    int origHash = dto.hashCode();
    dto.setEndNodeCheck(dto.getEndNodeCheck() == null ? true : null);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode(), "hash diff fail");
  }

  // endregion

  @Test
  public void roundRobinJson() {
    List<Long> seeds = createSeeds(0, 20);
    ObjectMapper objectMapper = new ObjectMapper();
    for (Long seed : seeds) {
      RelationshipReportDto dto = createSampleDto(seed);
      String jsonString = assertDoesNotThrow(() ->
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto)
      );
      RelationshipReportDto revDto = assertDoesNotThrow(() ->
        objectMapper.readValue(jsonString, RelationshipReportDto.class)
      );
      assertEquals(dto, revDto, "eq fail @ seed " + seed);
      assertEquals(
        dto.hashCode(),
        revDto.hashCode(),
        "hash fail @ seed " + seed
      );
    }
  }
}
