package org.rle.neo4jdescriptor.dto.report;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.rle.neo4jdescriptor.dto.entity.RelationshipDescriptorDto;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;

public class RelationshipDescriptorReportDtoTest extends ReportBaseDtoTest {

  @Override
  protected RelationshipDescriptorReportDto createSampleDto(long seed) {
    SampleRelationshipRep sampleRep = new SampleRelationshipRep();
    RelationshipDescriptor[] relDescs = sampleRep
      .relationshipDescriptors()
      .toArray(RelationshipDescriptor[]::new);
    Random ran = new Random(seed);
    RelationshipDescriptor relDesc = relDescs[ran.nextInt(relDescs.length)];
    return sample(relDesc, ran.nextLong());
  }

  protected static RelationshipDescriptorReportDto sample(
    RelationshipDescriptor relationshipDescriptor,
    long seed
  ) {
    Random ran = new Random(seed);
    RelationshipDescriptorReportDto dto = new RelationshipDescriptorReportDto(
      relationshipDescriptor
    );
    dto.setExceptionMsgs(ranExcMessages(ran, 0));
    RelationshipReportDto[] relRepDtos = new RelationshipReportDto[10];
    for (int i = 0; i < 10; i++) {
      relRepDtos[i] =
        RelationshipReportDtoTest.sample(
          relationshipDescriptor,
          ran.nextLong()
        );
    }
    dto.setRelationshipReportDtos(relRepDtos);
    return dto;
  }

  @Test
  public void equalityTest() {
    List<Long> seeds = createSeeds(0, 20);
    for (Long seed : seeds) {
      RelationshipDescriptorReportDto dto1 = createSampleDto(seed);
      RelationshipDescriptorReportDto dto2 = createSampleDto(seed);
      assertEquals(dto1, dto2, "equality fail @ seed " + seed);
      assertEquals(
        dto1.hashCode(),
        dto2.hashCode(),
        "hash equality fail @ seed " + seed
      );
    }
  }

  // region inequality tests

  @Override
  @Test
  @java.lang.SuppressWarnings("java:S2699") // compains about there being no assertions here
  public void inequalityTest_exceptions() {
    super.inequalityTest_exceptions();
  }

  @Test
  public void inequalityTest_relationshipDescriptor() {
    RelationshipDescriptorReportDto dto1 = sample(
      SampleRelationshipRep.FooBar,
      0
    );
    RelationshipDescriptorReportDto dto2 = sample(
      SampleRelationshipRep.FooBar,
      0
    );
    assertEquals(dto1, dto2, "equality fail");
    dto2.setRelationshipDescriptorDto(
      new RelationshipDescriptorDto(SampleRelationshipRep.BarFoo)
    );
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  @Test
  public void inequalityTest_relationshipReports() {
    RelationshipDescriptorReportDto dto1 = sample(
      SampleRelationshipRep.FooBar,
      0
    );
    RelationshipDescriptorReportDto dto2 = sample(
      SampleRelationshipRep.FooBar,
      0
    );
    assertEquals(dto1, dto2, "equality fail");
    dto2.setRelationshipReportDtos(null);
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

  @Test
  public void hashResetCheck_relationshipDescriptor() {
    RelationshipDescriptorReportDto dto = sample(
      SampleRelationshipRep.FooBar,
      0
    );
    int origHash = dto.hashCode();
    dto.setRelationshipDescriptorDto(
      new RelationshipDescriptorDto(SampleRelationshipRep.BarFoo)
    );
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode(), "hash diff fail");
  }

  @Test
  public void hashResetCheck_relationshipReports() {
    RelationshipDescriptorReportDto dto = sample(
      SampleRelationshipRep.FooBar,
      0
    );
    int origHash = dto.hashCode();
    dto.setRelationshipReportDtos(null);
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }

  // endregion

  @Test
  public void roundRobinJson() {
    List<Long> seeds = createSeeds(0, 20);
    ObjectMapper objectMapper = new ObjectMapper();
    for (Long seed : seeds) {
      RelationshipDescriptorReportDto dto = createSampleDto(seed);
      String jsonString = assertDoesNotThrow(() ->
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto)
      );
      RelationshipDescriptorReportDto revDto = assertDoesNotThrow(() ->
        objectMapper.readValue(
          jsonString,
          RelationshipDescriptorReportDto.class
        )
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
