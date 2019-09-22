package com.philipp_kehrbusch.gen.webdomain.trafos;

public class TransformMethodNotFoundException extends WebDomainGeneratorException {
  public TransformMethodNotFoundException(String className) {
    super("Could not find @Transform annotated method in " + className);
  }
}
