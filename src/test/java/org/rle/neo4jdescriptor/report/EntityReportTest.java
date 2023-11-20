package org.rle.neo4jdescriptor.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Optional;
import java.util.Random;
import org.rle.neo4jdescriptor.Modality;
import org.rle.neo4jdescriptor.entity.EntityDescriptor;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;
import org.rle.neo4jdescriptor.property.prop_basic.LongProperty;
import org.rle.neo4jdescriptor.testutils.BaseTestMethod;
import org.rle.neo4jdescriptor.testutils.TestUtilities;

public abstract class EntityReportTest<T extends EntityReport>
  extends ReportBaseTest<T> {

  private static PropertyDescriptorReportTest smPropReportTest = new PropertyDescriptorReportTest();

  protected abstract T createSampleReport(Random ran);

  @Override
  protected abstract T[] createEmptyReports();

  @Override
  protected void randomiseReport(Random ran, T entityReport) {
    super.randomiseReport(ran, entityReport);
    PropertyDescriptor[] props = entityReport
      .entityDescriptor()
      .properties(Modality.CONTINGENT)
      .toArray(PropertyDescriptor[]::new);
    for (PropertyDescriptor p : props) {
      if (ran.nextDouble() < 0.5) {
        continue;
      }
      PropertyReport propReport = new PropertyReport(p);
      smPropReportTest.randomiseReport(ran, propReport);
      entityReport.addPropertyReport(propReport.closeReport());
    }
  }

  // region calling setters tests

  @BaseTestMethod
  protected void addPropertyReportTest_yay() {
    T[] reports = createEmptyReports();
    for (int i = 0; i < reports.length; i++) {
      T entityReport = reports[i];
      assertEquals(
        0,
        entityReport.propertyReportCount(),
        "fail 0a @index " + i
      );
      assertEquals(0, entityReport.errorCount(), "fail 0b @index " + i);
      PropertyDescriptor[] props = entityReport
        .entityDescriptor()
        .properties(Modality.CONTINGENT)
        .toArray(PropertyDescriptor[]::new);
      int excpectedCount = 0;
      for (PropertyDescriptor p : props) {
        PropertyReport propReport = new PropertyReport(p);
        propReport.setKeyCheck(false).closeReport();
        entityReport.addPropertyReport(propReport);
        excpectedCount++;
        assertEquals(
          excpectedCount,
          entityReport.propertyReportCount(),
          String.format("fail 1a @ indices %d/%d", i, excpectedCount)
        );
        assertEquals(
          excpectedCount,
          entityReport.errorCount(),
          String.format("fail 1b @ indices %d/%d", i, excpectedCount)
        );
      }
    }
  }

  @BaseTestMethod
  protected void addPropertyReportTest_wrong() {
    T[] entityReports = createEmptyReports();
    for (int i = 0; i < entityReports.length; i++) {
      T entityReport = entityReports[i];
      PropertyReport propRep = new PropertyReport(new LongProperty("xyz1234"))
        .setKeyCheck(false)
        .closeReport();
      Exception exc = null;
      try {
        entityReport.addPropertyReport(propRep);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalArgumentException.class,
        EntityReport.NO_SUCH_PROPERTY,
        i
      );
    }
  }

  @BaseTestMethod
  protected void addPropertyReportTest_duplicate() {
    T[] entityReports = createEmptyReports();
    for (int i = 0; i < entityReports.length; i++) {
      T entityReport = entityReports[i];
      Optional<PropertyDescriptor> optPropDesc = entityReport
        .entityDescriptor()
        .properties(Modality.CONTINGENT)
        .findFirst();
      if (optPropDesc.isEmpty()) {
        continue;
      }
      PropertyDescriptor propDesc = optPropDesc.get();
      PropertyReport propRep1 = new PropertyReport(propDesc)
        .setKeyCheck(false)
        .closeReport();
      entityReport.addPropertyReport(propRep1);
      PropertyReport propRep2 = new PropertyReport(propDesc)
        .setTypeCheck(false)
        .closeReport();
      Exception exc = null;
      try {
        entityReport.addPropertyReport(propRep2);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalArgumentException.class,
        EntityReport.DUPLICATE_PROPERTY_REPORT
      );
    }
  }

  @BaseTestMethod
  protected void addPropertyReportTest_openReport() {
    T[] entityReports = createEmptyReports();
    for (int i = 0; i < entityReports.length; i++) {
      T entityReport = entityReports[i];
      Optional<PropertyDescriptor> optPropDesc = entityReport
        .entityDescriptor()
        .properties(Modality.CONTINGENT)
        .findFirst();
      if (optPropDesc.isEmpty()) {
        continue;
      }
      PropertyDescriptor propDesc = optPropDesc.get();
      PropertyReport propRep = new PropertyReport(propDesc).setKeyCheck(false);
      Exception exc = null;
      try {
        entityReport.addPropertyReport(propRep);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalArgumentException.class,
        ReportBase.SUBREPORT_MUST_BE_CLOSED
      );
    }
  }

  // endregion

  // region calling setters on a closed report test

  @BaseTestMethod
  protected void closedTest_properties() {
    T[] entityReports = createEmptyReports();
    for (int i = 0; i < entityReports.length; i++) {
      T entityReport = entityReports[i];
      entityReport.closeReport();
      Optional<PropertyDescriptor> optPropDesc = entityReport
        .entityDescriptor()
        .properties(Modality.CONTINGENT)
        .findFirst();
      if (optPropDesc.isEmpty()) {
        continue;
      }
      PropertyDescriptor propDesc = optPropDesc.get();
      PropertyReport propRep = new PropertyReport(propDesc)
        .setKeyCheck(false)
        .closeReport();
      Exception exc = null;
      try {
        entityReport.addPropertyReport(propRep);
      } catch (Exception e) {
        exc = e;
      }
      TestUtilities.checkException(
        exc,
        IllegalStateException.class,
        ReportBase.CAN_NOT_MODIFY_CLOSED_REPORT
      );
    }
  }

  // endregion

  // region inequality tests

  @BaseTestMethod
  protected void inequalityTest_properties() {
    T[] reps1 = createEmptyReports();
    T[] reps2 = createEmptyReports();
    for (int i = 0; i < reps1.length; i++) {
      EntityDescriptor entDesc = reps2[i].entityDescriptor();
      PropertyDescriptor[] props = entDesc
        .properties(Modality.CONTINGENT)
        .toArray(PropertyDescriptor[]::new);
      if (props.length == 0) {
        continue;
      }
      for (PropertyDescriptor p : props) {
        PropertyReport propRep = new PropertyReport(p);
        propRep.setKeyCheck(false).closeReport();
        reps2[i].addPropertyReport(propRep);
      }
      assertNotEquals(reps1[i], reps2[i], "inquality fail @ index " + i);
      // strictly speaking, the hashes dont HAVE to differ, but it'd be really weird if they didnt
      assertNotEquals(
        reps1[i].hashCode(),
        reps2[i].hashCode(),
        "hash fail @ index " + i
      );
    }
  }

  // endregion

  // region hash reset tests

  @BaseTestMethod
  protected void hashResetTest_properties() {
    T[] reps = createEmptyReports();
    for (int i = 0; i < reps.length; i++) {
      T report = reps[i];
      EntityDescriptor entDesc = reps[i].entityDescriptor();
      PropertyDescriptor[] props = entDesc
        .properties(Modality.CONTINGENT)
        .toArray(PropertyDescriptor[]::new);
      for (PropertyDescriptor p : props) {
        int origHash = report.hashCode();
        PropertyReport propRep = new PropertyReport(p);
        propRep.setKeyCheck(false).closeReport();
        report.addPropertyReport(propRep);
        // Strictly speaking, the hash does not HAVE to change, but if it does not, chances are, this is a bug
        assertNotEquals(origHash, report.hashCode());
      }
    }
  }
  // endregion
}
