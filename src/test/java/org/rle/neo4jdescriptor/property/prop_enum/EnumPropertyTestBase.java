package org.rle.neo4jdescriptor.property.prop_enum;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.rle.neo4jdescriptor.property.PropertyTestBase;
import org.rle.neo4jdescriptor.testutils.BoolMessageWrapper;

public abstract class EnumPropertyTestBase extends PropertyTestBase {

  public String[] runEnumPropertyTests(String className4FailMsg) {
    List<String> procNames = getProcNames(
      EnumPropertyTestProcedureBase.EnumSimpleTestProcedureName.class
    );
    List<String> failures = new ArrayList<>();
    for (String procName : procNames) {
      System.out.println("Running " + procName + " for " + className4FailMsg);
      try (Session session = driver().session()) {
        String cypher = String.format(
          "CALL %s() YIELD %s, %s RETURN %s, %s",
          procName,
          BoolMessageWrapper.BoolComponentName,
          BoolMessageWrapper.MessageComponentName,
          BoolMessageWrapper.BoolComponentName,
          BoolMessageWrapper.MessageComponentName
        );
        Record record = session.run(cypher).next();
        boolean success = record
          .get(BoolMessageWrapper.BoolComponentName)
          .asBoolean();
        if (!success) {
          String msg = record
            .get(BoolMessageWrapper.MessageComponentName)
            .asString();
          String failMsg = String.format(
            "basic property test failure: ProcName: %s, Class: %s, Msg: %s",
            procName,
            className4FailMsg,
            msg
          );
          failures.add(failMsg);
        }
      }
    }
    return failures.toArray(String[]::new);
  }
}
