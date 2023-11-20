package org.rle.neo4jdescriptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import org.neo4j.graphdb.Transaction;
import org.rle.neo4jdescriptor.entity.NodeDescriptor;
import org.rle.neo4jdescriptor.entity.RelationshipDescriptor;
import org.rle.neo4jdescriptor.report.FullReport;
import org.rle.neo4jdescriptor.report.NodeDescriptorReport;
import org.rle.neo4jdescriptor.report.RelationshipDescriptorReport;
import org.rle.neo4jdescriptor.repository.NodeRepository;
import org.rle.neo4jdescriptor.repository.RelationshipRepository;

public abstract class ValidationProcedureBase {

  protected static final String UNKNOWN_NODEDESCRIPTOR =
    "unknown NodeDescriptor";

  protected static final String UNKNOWN_RELATIONSHIPDESCRIPTOR =
    "unknown RelationshipDescriptor";

  private static final String GLOBAL_INDENT = "";

  private static final String LOCAL_INDENT = "  ";

  protected abstract NodeRepository nodeRepository();

  protected abstract RelationshipRepository relationshipRepository();

  protected abstract Transaction getTransaction();

  private void addNodeDescriptorReport(
    FullReport fullReport,
    NodeDescriptor nd
  ) {
    try {
      NodeDescriptorReport ndReport = nd.validate(getTransaction());
      fullReport.addNodeDescriptorReport(ndReport);
    } catch (Exception exc) {
      fullReport.addException(exc);
    }
  }

  private void addRelationshipDescriptorReport(
    FullReport fullReport,
    RelationshipDescriptor rd
  ) {
    try {
      RelationshipDescriptorReport rdReport = rd.validate(getTransaction());
      fullReport.addRelationshipDescriptorReport(rdReport);
    } catch (Exception exc) {
      fullReport.addException(exc);
    }
  }

  protected FullReport craftValidationReport() {
    FullReport fullReport = new FullReport();
    Iterator<NodeDescriptor> nodeDescIter = nodeRepository()
      .nodeDescriptors()
      .iterator();
    while (nodeDescIter.hasNext()) {
      NodeDescriptor nd = nodeDescIter.next();
      addNodeDescriptorReport(fullReport, nd);
    }
    Iterator<RelationshipDescriptor> relDescIter = relationshipRepository()
      .relationshipDescriptors()
      .iterator();
    while (relDescIter.hasNext()) {
      RelationshipDescriptor rd = relDescIter.next();
      addRelationshipDescriptorReport(fullReport, rd);
    }
    return fullReport.closeReport();
  }

  /**
   * Returns a single {@link ValidationReportWrapper} with either the String representation
   * of the {@link FullReport} or the message of the  {@link JsonProcessingException} should
   * there have been any. <p>
   * While the serialised report is a {@link FullReport}, there is only one
   * {@link NodeDescriptorReport} in it
   */
  protected Stream<ValidationReportWrapper> validateNodeDescriptor(
    String nodeDescriptorClassName
  ) {
    Optional<NodeDescriptor> nd = nodeRepository()
      .nodeDescriptors()
      .filter(o -> o.getClass().getSimpleName().equals(nodeDescriptorClassName))
      .findFirst();
    if (nd.isEmpty()) {
      throw new IllegalArgumentException(UNKNOWN_NODEDESCRIPTOR);
    }
    NodeDescriptorReport nodeDescReport = nd.get().validate(getTransaction());
    FullReport fullReport = new FullReport();
    fullReport.addNodeDescriptorReport(nodeDescReport);
    ValidationReportWrapper wrapper = new ValidationReportWrapper(fullReport);
    return Stream.of(wrapper);
  }

  /**
   * Returns a single {@link ValidationReportWrapper} with either the String representation
   * of the {@link FullReport} or the message of the  {@link JsonProcessingException} should
   * there have been any. <p>
   * While the serialised report is a {@link FullReport}, there is only one
   * {@link RelationshipDescriptorReport} in it
   */
  protected Stream<ValidationReportWrapper> validateRelationshipDescriptor(
    String relationshipDescriptorClassName
  ) {
    Optional<RelationshipDescriptor> rd = relationshipRepository()
      .relationshipDescriptors()
      .filter(o ->
        o.getClass().getSimpleName().equals(relationshipDescriptorClassName)
      )
      .findFirst();
    if (rd.isEmpty()) {
      throw new IllegalArgumentException(UNKNOWN_NODEDESCRIPTOR);
    }
    RelationshipDescriptorReport relDescReport = rd
      .get()
      .validate(getTransaction());
    FullReport fullReport = new FullReport();
    fullReport.addRelationshipDescriptorReport(relDescReport);
    ValidationReportWrapper wrapper = new ValidationReportWrapper(fullReport);
    return Stream.of(wrapper);
  }

  /**
   * Returns a single {@link ValidationReportWrapper} with either the String representation
   * of the {@link FullReport} or the message of the  {@link JsonProcessingException} should
   * there have been any.
   */
  protected Stream<ValidationReportWrapper> streamValidationDto() {
    FullReport fullReport = craftValidationReport();
    ValidationReportWrapper wrapper = new ValidationReportWrapper(fullReport);
    return Stream.of(wrapper);
  }

  /**
   * Returns a single single {@link ValidationReportWrapper} that has only its validationPrint
   * field initialised
   */
  protected Stream<ValidationPrintoutWrapper> streamValidationPrint() {
    FullReport fullReport = craftValidationReport();
    String s = fullReport.print(GLOBAL_INDENT, LOCAL_INDENT);
    ValidationPrintoutWrapper wrapper = new ValidationPrintoutWrapper(s);
    return Stream.of(wrapper);
  }
}
