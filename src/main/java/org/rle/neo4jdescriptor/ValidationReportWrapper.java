package org.rle.neo4jdescriptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rle.neo4jdescriptor.dto.report.FullReportDto;
import org.rle.neo4jdescriptor.report.FullReport;

/**
 * These classes can only have public non-final fields, and the fields must
 * be one of the following types:
 *
 * <ul>
 *     <li>{@link String}</li>
 *     <li>{@link Long} or {@code long}</li>
 *     <li>{@link Double} or {@code double}</li>
 *     <li>{@link Number}</li>
 *     <li>{@link Boolean} or {@code boolean}</li>
 *     <li>{@link Node}</li>
 *     <li>{@link org.neo4j.graphdb.Relationship}</li>
 *     <li>{@link org.neo4j.graphdb.Path}</li>
 *     <li>{@link Map} with key {@link String} and value {@link Object}</li>
 *     <li>{@link List} of elements of any valid field type, including {@link List}</li>
 *     <li>{@link Object}, meaning any of the valid field types</li>
 * </ul>
 */
@SuppressWarnings("java:S1104") // complains about there being public non static non final fields and no accessors. But Neo4j needs those in its wrapper objects
public class ValidationReportWrapper {

  public static final String JSON_DTO_COMPONENT_NAME = "jsonDto";

  public static final String JSON_EXC_COMPONENT_NAME = "jsonException";

  /* The name of that public field must always be the same as the static String ComponentName.
   * This literal string is used on Map<String, Object> that gets returned from the neo4j database. */

  public String jsonDto;

  public String jsonException;

  public ValidationReportWrapper(FullReport fullReport) {
    FullReportDto dto = fullReport.getDto();
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      jsonDto =
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
    } catch (JsonProcessingException exc) {
      jsonException = exc.getMessage();
    }
  }
}
