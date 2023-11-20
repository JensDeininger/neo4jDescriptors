package org.rle.neo4jdescriptor.dto.entity;

public abstract class DescriptorBaseDto {

  protected int processHash() {
    return this.getClass().hashCode();
  }

  protected boolean processEquals(Object object) {
    if (object == null) {
      return false;
    }
    if (object == this) {
      return true;
    }
    return object instanceof DescriptorBaseDto;
  }

  @Override
  public int hashCode() {
    return processHash();
  }

  @Override
  public boolean equals(Object obj) {
    return processEquals(obj);
  }
}
