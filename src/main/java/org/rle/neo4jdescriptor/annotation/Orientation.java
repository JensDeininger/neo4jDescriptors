package org.rle.neo4jdescriptor.annotation;

import java.lang.annotation.*;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.neo4j.graphdb.Direction;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Orientation {
  public Direction direction();
}
