package org.rle.neo4jdescriptor.testutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.neo4j.driver.Config;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilder;
import org.neo4j.harness.Neo4jBuilders;

/**
 * Base class for all tests that need a neo4j test database.<p>
 * It does four things:<p>
 * - build the test database (with the cypher file if one is specified)<p>
 * - registers the specified procedure classes <p>
 * - executes the initial cypher statement (if one is specified)<p>
 * - closes the database after the tests are done
 */
public abstract class TestBase {

  private static final Config driverConfig = Config
    .builder()
    .withoutEncryption()
    .build();

  private Driver driver;

  private Neo4j embeddedDatabaseServer;

  /**
   * The {@link InputStream} of the file containing the cypher statements
   * that create the test database.<p>
   * Provide null if there is none
   */
  protected abstract InputStream inputStreamOfCypherFile();

  /**
   * The classes containing the procedures to be registered for the test
   */
  protected abstract Stream<Class<?>> procedureClasses();

  /**
   * The initial cypher statement to execute. Empty or null if none
   */
  protected abstract String initialCypher();

  /**
   * Anything else one wants to happen in the BeforeAll part after the
   * initial cypher statement has run. <p>
   * To be overwritten in the derivations.
   */
  protected void otherBeforeAllStuff() {}

  protected Driver driver() {
    return driver;
  }

  @BeforeAll
  void initializeNeo4j() throws IOException {
    StringWriter sw = new StringWriter();

    InputStream cypherInputStream = inputStreamOfCypherFile();
    if (cypherInputStream != null) {
      new BufferedReader(new InputStreamReader(inputStreamOfCypherFile()))
        .transferTo(sw);
      sw.flush();
      cypherInputStream.close();
    }

    Neo4jBuilder builder = Neo4jBuilders.newInProcessBuilder();

    Stream<Class<?>> classes = procedureClasses();
    if (classes != null) {
      Iterator<Class<?>> iter = procedureClasses().iterator();
      while (iter.hasNext()) {
        builder = builder.withProcedure(iter.next());
      }
    }
    this.embeddedDatabaseServer = builder.withFixture(sw.toString()).build();

    driver =
      GraphDatabase.driver(embeddedDatabaseServer.boltURI(), driverConfig);

    String initialCypher = initialCypher();
    if (initialCypher != null && initialCypher.length() > 0) {
      try (Session session = driver.session()) {
        session.run(initialCypher);
      }
    }
    otherBeforeAllStuff();
  }

  @AfterAll
  void closeDriver() {
    driver.close();
    embeddedDatabaseServer.close();
  }

  private HashSet<String> getProcNamesNoRecursive(Class<?> procNameClass) {
    Field[] declaredFields = procNameClass.getDeclaredFields();
    HashSet<String> result = new HashSet<>();
    for (Field field : declaredFields) {
      if (
        !Modifier.isStatic(field.getModifiers()) ||
        field.getType() != String.class
      ) {
        continue;
      }
      String procName;
      try {
        procName = (String) field.get(null);
      } catch (IllegalAccessException exc) {
        procName = "Well, that did not go as planned";
      }
      result.add(procName);
    }
    return result;
  }

  protected List<String> getProcNames(Class<?> procNameClass) {
    ArrayDeque<Class<?>> classes2Process = new ArrayDeque<>();
    classes2Process.add(procNameClass);
    HashSet<String> allProcNames = new HashSet<>();
    while (!classes2Process.isEmpty()) {
      Class<?> currClass = classes2Process.poll();
      for (Class<?> c : currClass.getDeclaredClasses()) {
        classes2Process.add(c);
      }
      HashSet<String> classScan = getProcNamesNoRecursive(currClass);
      allProcNames.addAll(classScan);
    }
    return new ArrayList<>(allProcNames);
  }

  /***
   * Returns the names of all procedures that reported a fail
   */
  protected List<String> runAllTestProcedures(Class<?> procNameClass) {
    List<String> procNames = getProcNames(procNameClass);
    List<String> failingProcs = new ArrayList<>();
    for (String procName : procNames) {
      try (Session session = driver().session()) {
        String cypher = String.format(
          "CALL %s() YIELD %s RETURN %s",
          procName,
          BoolMessageWrapper.BoolComponentName,
          BoolMessageWrapper.BoolComponentName
        );
        Record record = session.run(cypher).next();
        boolean success = record
          .get(BoolMessageWrapper.BoolComponentName)
          .asBoolean();
        if (!success) {
          failingProcs.add(procName);
        }
      }
    }
    return failingProcs;
  }
}
