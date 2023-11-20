package org.rle.neo4jdescriptor.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.rle.neo4jdescriptor.EqualityUtils;
import org.rle.neo4jdescriptor.entity.NodeIdentifier;

@SuppressWarnings("java:S2160") // override equals in derived classes that have new fields. Not necessary here
public class NodeIdentifierDto extends EntityIdentifierDto {

  // region private fields

  @JsonProperty("labels")
  private LabelDescriptorDto[] mLabels;

  //endregion

  //region Ctor

  protected NodeIdentifierDto() {}

  public NodeIdentifierDto(NodeIdentifier nodeId) {
    super(nodeId);
    setLabels(
      nodeId
        .labels()
        .map(LabelDescriptorDto::new)
        .toArray(LabelDescriptorDto[]::new)
    );
  }

  //endregion

  // region accessors

  public LabelDescriptorDto[] getLabels() {
    return mLabels;
  }

  public void setLabels(LabelDescriptorDto[] labels) {
    mLabels = labels;
  }

  // endregion

  // region hash and eqials overrides

  @Override
  protected int processHash() {
    return Objects.hash(super.processHash(), EqualityUtils.arrayHash(mLabels));
  }

  @Override
  protected boolean processEquals(Object obj) {
    if (!super.processEquals(obj)) {
      return false;
    }
    if (!(obj instanceof NodeIdentifierDto)) {
      return false;
    }
    NodeIdentifierDto cast = NodeIdentifierDto.class.cast(obj);
    return EqualityUtils.arrayEquals(mLabels, cast.mLabels);
  }
  //endregion

}
