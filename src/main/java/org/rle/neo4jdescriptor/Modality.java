package org.rle.neo4jdescriptor;

import org.rle.neo4jdescriptor.property.PropertyDescriptor;

/**
 * The {@link Modality} serves to distinguish between the {@link LabelDescriptor}s,
 * {@link RelationshipDescriptor}s, {@link PropertyDescriptor}s etc. of a
 * {@link NodeDescriptor} that are part of its {@link NodeIdentifier} and thus
 * necessary and those that are declared as to be validated, i.e. contingent.
 */
public enum Modality {
  UNDEFINED,
  CONTINGENT,
  NECESSARY,
  BOTH,
}
