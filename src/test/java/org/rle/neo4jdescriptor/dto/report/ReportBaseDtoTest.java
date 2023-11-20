package org.rle.neo4jdescriptor.dto.report;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class ReportBaseDtoTest {

  public static List<Long> createSeeds(long metaSeed, int count) {
    Random ran = new Random(metaSeed);
    List<Long> seeds = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      seeds.add(ran.nextLong());
    }
    return seeds;
  }

  public static Boolean ranBool(Random ran) {
    if (ran.nextDouble() < 0.3) {
      return null;
    }
    if (ran.nextDouble() < 0.6) {
      return true;
    }
    return false;
  }

  public static Integer ranInteger(Random ran) {
    if (ran.nextDouble() < 0.3) {
      return null;
    }
    if (ran.nextDouble() < 0.6) {
      return 23;
    }
    return 67;
  }

  public static String ranString(Random ran) {
    if (ran.nextDouble() < 0.3) {
      return null;
    }
    if (ran.nextDouble() < 0.6) {
      return "blablub1243";
    }
    return "";
  }

  public static String[] ranExcMessages(Random ran, int minCount) {
    int count = ran.nextInt(10) + minCount;
    String[] res = new String[count];
    byte[] bytes = new byte[8];
    for (int i = 0; i < count; i++) {
      ran.nextBytes(bytes);
      res[i] = UUID.nameUUIDFromBytes(bytes).toString();
    }
    return res;
  }

  protected abstract ReportBaseDto createSampleDto(long seed);

  protected void inequalityTest_exceptions() {
    ReportBaseDto dto1 = createSampleDto(0);
    ReportBaseDto dto2 = createSampleDto(0);
    dto1.setExceptionMsgs(ranExcMessages(new Random(0), 1));
    dto2.setExceptionMsgs(ranExcMessages(new Random(1), 1));
    assertNotEquals(dto1, dto2, "inequality fail");
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(dto1.hashCode(), dto2.hashCode(), "hash diff fail");
  }

  protected void hashResetCheck_exceptions() {
    ReportBaseDto dto = createSampleDto(0);
    String[] excMsgs = ranExcMessages(new Random(0), 1);
    dto.setExceptionMsgs(excMsgs);
    int origHash = dto.hashCode();
    excMsgs[0] += "bla";
    // Of course, the hashes dont HAVE to be different, but it would be VERY suspicious if they aint, so I better test that
    assertNotEquals(origHash, dto.hashCode());
  }
}
