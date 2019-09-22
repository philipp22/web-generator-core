package com.philipp_kehrbusch.gen.webdomain.trafos;

public class TrafoConstructorMissingException extends WebDomainGeneratorException {
  public TrafoConstructorMissingException(Class<?> trafo) {
    super("Missing no-param constructor for trafo " + trafo.getName());
  }
}
