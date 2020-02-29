package com.philipp_kehrbusch.gen.webdomain.source.exceptions;

import com.philipp_kehrbusch.gen.webdomain.trafos.WebDomainGeneratorException;

public class InvalidViewAttributePathException extends WebDomainGeneratorException {

  public InvalidViewAttributePathException(String view, String path) {
    super("Invalid view attribute path: View '" + view + "': '" + path + "'");
  }

  public InvalidViewAttributePathException(String view, String path, String details) {
    super("Invalid view attribute path: View '" + view + "': '" + path + "'\n" + details);
  }
}
