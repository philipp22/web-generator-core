package com.philipp_kehrbusch.gen.webdomain.trafos;

public class UnknownTrafoArgumentException extends WebDomainGeneratorException {

  public UnknownTrafoArgumentException(String type, String name) {
    super("Uknown argument for @Transform annotated method: " + type + " "  + name);
  }
}
