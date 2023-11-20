package org.rle.neo4jdescriptor.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.entity.RelationshipIdentifier;

@SuppressWarnings("java:S2160") // override equals in derived classes that have new fields. Not necessary here
public class RelationshipIdentifierDto extends EntityIdentifierDto {

  // region private fields

  @JsonProperty("relationshipType")
  private RelationshipTypeDescriptorDto mRelationshipType;

  //endregion

  //region Ctor

  public RelationshipIdentifierDto() {}

  public RelationshipIdentifierDto(RelationshipIdentifier relationshipId) {
    super(relationshipId);
    mRelationshipType = relationshipId.relationshipTypeDescriptor().dto();
  }

  //endregion

  // region accessors

  public RelationshipTypeDescriptorDto getRelationshipType() {
    return mRelationshipType;
  }

  public void setRelationshipType(
    RelationshipTypeDescriptorDto relationshipType
  ) {
    mRelationshipType = relationshipType;
  }

  // endregion

  // region hash and eqials overrides

  @Override
  protected int processHash() {
    return Objects.hash(super.processHash(), mRelationshipType);
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof RelationshipIdentifierDto)) {
      return false;
    }
    RelationshipIdentifierDto cast = RelationshipIdentifierDto.class.cast(obj);
    return EqualityUtils.itemEquals(mRelationshipType, cast.mRelationshipType);
  }
  //endregion
}
