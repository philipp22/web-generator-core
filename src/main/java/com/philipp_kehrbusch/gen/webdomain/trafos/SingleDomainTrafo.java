package com.philipp_kehrbusch.gen.webdomain.trafos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SingleDomainTrafo {
  String[] excludeAnnotated() default {};
  String[] includeAnnotated() default {};
}
