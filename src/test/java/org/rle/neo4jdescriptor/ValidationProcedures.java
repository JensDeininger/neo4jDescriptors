package org.rle.neo4jdescriptor;

import java.util.stream.Stream;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
import org.rle.neo4jdescriptor.repository.NodeRepository;
import org.rle.neo4jdescriptor.repository.RelationshipRepository;
import org.rle.neo4jdescriptor.samples.SampleNodeRep;
import org.rle.neo4jdescriptor.samples.SampleRelationshipRep;

public class ValidationProcedures extends ValidationProcedureBase {

  @Context
  public Transaction tx;

  protected static final NodeRepository smNodeRepo = new SampleNodeRep();

  protected static final RelationshipRepository smRelationshipRepo = new SampleRelationshipRep();

  @Override
  protected NodeRepository nodeRepository() {
    return smNodeRepo;
  }

  @Override
  protected RelationshipRepository relationshipRepository() {
    return smRelationshipRepo;
  }

  @Override
  protected Transaction getTransaction() {
    return tx;
  }

  public static class ProcedureName {

    public static final String PrintValidation = "printValidation";

    public static final String GetValidationReport = "getValidationReport";

    public static final String ValidateNodeDescriptor =
      "validateNodeDescriptor";

    public static final String ValidateRelationshipDescriptor =
      "validateRelationshipDescriptor";
  }

  @Procedure(mode = Mode.READ, name = ProcedureName.PrintValidation)
  public Stream<ValidationPrintoutWrapper> printValidation() {
    return super.streamValidationPrint();
  }

  @Procedure(mode = Mode.READ, name = ProcedureName.GetValidationReport)
  public Stream<ValidationReportWrapper> getValidationReport() {
    return super.streamValidationDto();
  }

  @Procedure(mode = Mode.READ, name = ProcedureName.ValidateNodeDescriptor)
  public Stream<ValidationReportWrapper> validateNodeDescriptor(
    @Name("nodeDescriptorClassName") String nodeDescriptorClassName
  ) {
    return super.validateNodeDescriptor(nodeDescriptorClassName);
  }

  @Procedure(
    mode = Mode.READ,
    name = ProcedureName.ValidateRelationshipDescriptor
  )
  public Stream<ValidationReportWrapper> validateRelationshipDescriptor(
    @Name(
      "relationshipDescriptorClassName"
    ) String relationshipDescriptorClassName
  ) {
    return super.validateRelationshipDescriptor(
      relationshipDescriptorClassName
    );
  }
  // @Procedure(
  //   mode = Mode.WRITE,
  //   name = ProcedureName.ScrewData_RemoveSomeProperties
  // )
  // public Stream<LongWrapper> removeSomeProperties() {
  //   Iterator<NodeDescriptor> nodeDescIter = smNodeRepo
  //     .nodeDescriptors()
  //     .iterator();
  //   Long killCount = 0l;
  //   while (nodeDescIter.hasNext()) {
  //     NodeDescriptor nodeDesc = nodeDescIter.next();
  //     Optional<PropertyDescriptor> optPropDesc = nodeDesc
  //       .properties(Modality.CONTINGENT)
  //       .findFirst();
  //     if (optPropDesc.isEmpty()) {
  //       continue;
  //     }
  //     Optional<Node> optNode = nodeDesc.identifier().findNodes(tx).findFirst();
  //     if (optNode.isEmpty()) {
  //       continue;
  //     }
  //     Node node = optNode.get();
  //     PropertyDescriptor propDesc = optPropDesc.get();
  //     node.removeProperty(propDesc.key());
  //     killCount++;
  //   }
  //   return Stream.of(new LongWrapper(killCount));
  // }

  // @Procedure(
  //   mode = Mode.WRITE,
  //   name = ProcedureName.ScrewData_ChangePropTypeToString
  // )
  // public Stream<LongWrapper> changePropTypeToStringArray() {
  //   Iterator<NodeDescriptor> nodeDescIter = smNodeRepo
  //     .nodeDescriptors()
  //     .iterator();
  //   Long changeCount = 0l;
  //   while (nodeDescIter.hasNext()) {
  //     NodeDescriptor nodeDesc = nodeDescIter.next();
  //     Optional<PropertyDescriptor> optPropDesc = nodeDesc
  //       .properties(Modality.CONTINGENT)
  //       .filter(o -> !o.dbType().equals(String.class))
  //       .findFirst();
  //     if (optPropDesc.isEmpty()) {
  //       continue;
  //     }
  //     Optional<Node> optNode = nodeDesc.identifier().findNodes(tx).findFirst();
  //     if (optNode.isEmpty()) {
  //       continue;
  //     }
  //     Node node = optNode.get();
  //     PropertyDescriptor propDesc = optPropDesc.get();
  //     node.setProperty(propDesc.key(), "dis be da wrong type");
  //     changeCount++;
  //   }
  //   return Stream.of(new LongWrapper(changeCount));
  // }
}
