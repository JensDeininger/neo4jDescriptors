package org.rle.neo4jdescriptor.annotation;

import java.lang.annotation.*;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EndNode {
}
