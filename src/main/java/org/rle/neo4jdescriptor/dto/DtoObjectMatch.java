package org.rle.neo4jdescriptor.dto;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import org.rle.neo4jdescriptor.dto.entity.EntityIdentifierDto;
import org.rle.neo4jdescriptor.dto.entity.LabelDescriptorDto;
import org.rle.neo4jdescriptor.dto.entity.NodeIdentifierDto;
import org.rle.neo4jdescriptor.dto.entity.PropertyDescriptorDto;
import org.rle.neo4jdescriptor.dto.entity.RelationshipIdentifierDto;
import org.rle.neo4jdescriptor.dto.entity.RelationshipTypeDescriptorDto;
import org.rle.neo4jdescriptor.entity.EntityIdentifier;
import org.rle.neo4jdescriptor.entity.LabelDescriptor;
import org.rle.neo4jdescriptor.entity.NodeIdentifier;
import org.rle.neo4jdescriptor.entity.RelationshipIdentifier;
import org.rle.neo4jdescriptor.entity.RelationshipTypeDescriptor;
import org.rle.neo4jdescriptor.property.PropertyDescriptor;

public class DtoObjectMatch {

  private DtoObjectMatch() {}

  public static boolean labelMatch(
    LabelDescriptor desc,
    LabelDescriptorDto dto
  ) {
    if (desc == null || dto == null) {
      return false;
    }
    return (
      desc.name().equals(dto.getName()) &&
      desc.logName().equals(dto.getLogName())
    );
  }

  public static boolean relationshipTypeMatch(
    RelationshipTypeDescriptor desc,
    RelationshipTypeDescriptorDto dto
  ) {
    if (desc == null || dto == null) {
      return false;
    }
    return (
      desc.name().equals(dto.getName()) &&
      desc.logName().equals(dto.getLogName())
    );
  }

  public static boolean propertyMatch(
    PropertyDescriptor desc,
    PropertyDescriptorDto dto
  ) {
    if (desc == null || dto == null) {
      return false;
    }
    return (
      desc.key().equals(dto.getName()) && desc.logKey().equals(dto.getLogName())
    );
  }

  private static boolean entityIdMatch(
    EntityIdentifier entId,
    EntityIdentifierDto entIdDto
  ) {
    if (entId == null || entIdDto == null) {
      return false;
    }
    Set<PropertyDescriptorDto> propDtoSet = Arrays
      .stream(entIdDto.getProperties())
      .collect(Collectors.toSet());
    Iterator<PropertyDescriptor> propsIter = entId.properties().iterator();
    while (propsIter.hasNext()) {
      PropertyDescriptorDto dto = new PropertyDescriptorDto(propsIter.next());
      if (!propDtoSet.remove(dto)) {
        return false;
      }
    }
    return propDtoSet.isEmpty();
  }

  public static boolean nodeIdentifierMatch(
    NodeIdentifier nodeId,
    NodeIdentifierDto nodeIdDto
  ) {
    if (!entityIdMatch(nodeId, nodeIdDto)) {
      return false;
    }
    Set<LabelDescriptorDto> labelDtoSet = Arrays
      .stream(nodeIdDto.getLabels())
      .collect(Collectors.toSet());
    Iterator<LabelDescriptor> labelsIter = nodeId.labels().iterator();
    while (labelsIter.hasNext()) {
      LabelDescriptorDto dto = new LabelDescriptorDto(labelsIter.next());
      if (!labelDtoSet.remove(dto)) {
        return false;
      }
    }
    return labelDtoSet.isEmpty();
  }

  public static boolean relationShipIdentifierMatch(
    RelationshipIdentifier relId,
    RelationshipIdentifierDto relIdDto
  ) {
    if (!entityIdMatch(relId, relIdDto)) {
      return false;
    }
    RelationshipTypeDescriptorDto rt = relId.relationshipTypeDescriptor().dto();
    return relIdDto.getRelationshipType().equals(rt);
  }
}
