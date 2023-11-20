package org.rle.neo4jdescriptor.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.rle.neo4jdescriptor.testutils.BaseTestMethod;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

public abstract class ReportBaseTest<T extends ReportBase> {

  protected abstract T createSampleReport(Random ran);

  protected abstract T[] createEmptyReports();

  protected void randomiseReport(Random ran, T report) {
    int excCount = ran.nextInt(5);
    for (int i = 0; i < excCount; i++) {
      report.addException(new Exception("Exc Msg " + i));
    }
  }

  protected static Boolean randomBool(Random ran) {
    double d = ran.nextDouble();
    if (d < 0.33) {
      return null;
    }
    if (d < 0.66) {
      return true;
    }
    return false;
  }

  @BaseTestMethod
  protected void countTestsTest() {
    HashSet<String> baseTestMethodNames = new HashSet<>();
    Class<?> clazz2scan = this.getClass();
    do {
      HashSet<String> currentScan = Arrays
        .stream(ReportBaseTest.class.getDeclaredMethods())
        .filter(o -> o.isAnnotationPresent(BaseTestMethod.class))
        .map(o -> o.getName())
        .collect(Collectors.toCollection(HashSet::new));
      baseTestMethodNames.addAll(currentScan);
      clazz2scan = clazz2scan.getSuperclass();
    } while (clazz2scan != null);
    HashSet<String> testMethodNames = Arrays
      .stream(this.getClass().getMethods())
      .filter(o -> o.isAnnotationPresent(Test.class))
      .map(o -> o.getName())
      .collect(Collectors.toCollection(HashSet::new));
    for (String baseMethName : baseTestMethodNames) {
      assertTrue(
        testMethodNames.contains(baseMethName),
        "missing call of test: " + baseMethName
      );
    }
  }

  @BaseTestMethod
  protected void equalityTest() {
    Random ran1 = new Random(42l);
    Random ran2 = new Random(42l);
    for (int i = 0; i < 10; i++) {
      T rep1 = createSampleReport(ran1);
      T rep2 = createSampleReport(ran2);
      assertEquals(rep1, rep2, "Equality fail @index " + i);
      assertEquals(rep1.hashCode(), rep2.hashCode(), "hash fail @index " + i);
    }
  }

  // region calling setters tests

  @BaseTestMethod
  protected void addExceptionTest() {
    T[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      T report = reports[i];
      Exception exc1 = new Exception("excMsg 1");
      report.addException(exc1);
      assertEquals(1, report.exceptionCount(), "fail 1 @index " + i);
      assertEquals(1, report.errorCount(), "fail 2 @index " + i);

      Exception exc2 = new Exception("excMsg 2");
      report.addException(exc2);
      assertEquals(2, report.exceptionCount(), "fail 3 @index " + i);
      assertEquals(2, report.errorCount(), "fail 4 @index " + i);
    }
  }

  // endregion

  // region calling setters on a closed report test

  @BaseTestMethod
  protected void closedTest_exception() {
    T[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      T report = reports[i];
      report.closeReport();
      Exception exc2add = new Exception();
      Exception exc = null;
      try {
        report.addException(exc2add);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalStateException.class,
        ReportBase.CAN_NOT_MODIFY_CLOSED_REPORT,
        i
      );
    }
  }

  // endregion

  // region inequality tests

  @BaseTestMethod
  protected void inequalityTest_closed() {
    T[] reports1 = createEmptyReports();
    T[] reports2 = createEmptyReports();
    for (int i = 0; i < reports1.length; i++) {
      T rep1 = reports1[i];
      T rep2 = reports2[i];
      assertNotEquals(rep1, rep2.closeReport(), "inquality fail @index " + i);
      // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
      assertEquals(rep1.hashCode(), rep2.hashCode(), "hash fail @index " + i);
    }
  }

  @BaseTestMethod
  protected void inequalityTest_exceptions() {
    T[] reports1 = createEmptyReports();
    T[] reports2 = createEmptyReports();
    for (int i = 0; i < reports1.length; i++) {
      T rep1 = reports1[i];
      T rep2 = reports2[i];
      rep2.addException(new Exception("bla"));
      assertNotEquals(rep1, rep2, "inquality fail @index " + i);
      // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
      assertEquals(rep1.hashCode(), rep2.hashCode(), "hash fail @index " + i);
    }
  }

  // endregion

  // region hash reset tests

  @BaseTestMethod
  protected void hashResetTest_closed() {
    T[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      T report = reports[i];
      int origHash = report.hashCode();
      report.closeReport();
      // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
      assertEquals(origHash, report.hashCode(), "fail @index " + i);
    }
  }

  @BaseTestMethod
  protected void hashResetTest_exception() {
    T[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      T report = reports[i];
      int origHash = report.hashCode();
      report.addException(new Exception("bla"));
      // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
      assertEquals(origHash, report.hashCode(), "fail @index " + i);
    }
  }

  // endregion

  // region print tests

  private int addExceptions(T report) {
    Exception exc1 = null;
    try {
      int i = (new int[0])[3];
      i = i + i;
    } catch (Exception e) {
      exc1 = e;
    }
    Exception exc2 = null;
    try {
      String[] strings = new String[] { null };
      int s = strings[0].length();
      s = s + 1;
    } catch (Exception e) {
      exc2 = e;
    }
    report.addException(exc1);
    report.addException(exc2);
    return 2;
  }

  @BaseTestMethod
  protected void printExceptionsTest() {
    T[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      T rep = reports[i];
      int origExcCount = rep.exceptionCount();
      int excCount = origExcCount + addExceptions(rep);
      assertEquals(excCount, rep.exceptionCount(), "fail 1");
      assertTrue(rep.printExceptions("", "").length() > 0, "fail 2");
    }
  }
  // endregion

}
