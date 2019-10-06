package com.philipp_kehrbusch.gen.webdomain.source.exceptions;

public class InvalidRestMethodException extends RuntimeException {

  public InvalidRestMethodException(String givenMethod) {
    super("Unknown REST method: " + givenMethod);
  }
}
