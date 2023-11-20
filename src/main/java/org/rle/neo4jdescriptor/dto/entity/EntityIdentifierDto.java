package org.rle.neo4jdescriptor.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.entity.EntityIdentifier;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public abstract class EntityIdentifierDto extends DescriptorBaseDto {

  // region private fields

  @JsonProperty("properties")
  private PropertyDescriptorDto[] mProperties;

  // endregion

  // region Ctor

  protected EntityIdentifierDto() {}

  protected EntityIdentifierDto(EntityIdentifier entityId) {
    setProperties(
      entityId
        .properties()
        .map(PropertyDescriptorDto::new)
        .toArray(PropertyDescriptorDto[]::new)
    );
  }

  // endregion

  // region accessors

  public PropertyDescriptorDto[] getProperties() {
    return mProperties;
  }

  public void setProperties(PropertyDescriptorDto[] props) {
    mProperties = props;
  }

  // endregion

  // region hash, equal overrides

  @Override
  protected int processHash() {
    return Objects.hash(
      super.processHash(),
      EqualityUtils.arrayHash(mProperties)
    );
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof EntityIdentifierDto)) {
      return false;
    }
    EntityIdentifierDto cast = EntityIdentifierDto.class.cast(obj);
    return EqualityUtils.arrayEquals(this.mProperties, cast.mProperties);
  }
  // endregion
}
