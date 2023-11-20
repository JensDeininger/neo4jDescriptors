package org.rle.neo4jdescriptor.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.entity.EntityDescriptor;

@SuppressWarnings("java:S2160") // override equals because the super class does. I handle that via the processEquals method
public abstract class EntityDescriptorDto extends DescriptorBaseDto {

  // region private fields

  @JsonProperty("className")
  private String mClassName;

  // endregion

  // region Ctor

  protected EntityDescriptorDto() {}

  protected EntityDescriptorDto(EntityDescriptor entityDescriptor) {
    if (entityDescriptor == null) {
      throw new IllegalArgumentException();
    }
    mClassName = entityDescriptor.getClass().getName();
  }

  // endregion

  // region accessors

  public String getClassName() {
    return mClassName;
  }

  public void setClassName(String className) {
    mClassName = className;
  }

  // endregion

  // region hash, equal overrides

  @Override
  protected int processHash() {
    return Objects.hash(super.processHash(), mClassName);
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof EntityDescriptorDto)) {
      return false;
    }
    EntityDescriptorDto cast = EntityDescriptorDto.class.cast(obj);
    return EqualityUtils.itemEquals(this.mClassName, cast.mClassName);
  }
  // endregion
}
