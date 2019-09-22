package com.philipp_kehrbusch.gen.webdomain.trafos;

public class WebDomainGeneratorException extends Exception {
  public WebDomainGeneratorException() {
    super();
  }

  public WebDomainGeneratorException(String message) {
    super(message);
  }

  public WebDomainGeneratorException(String message, Throwable cause) {
    super(message, cause);
  }

  public WebDomainGeneratorException(Throwable cause) {
    super(cause);
  }

  protected WebDomainGeneratorException(String message, Throwable cause, boolean enableSuppression,
                                        boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
