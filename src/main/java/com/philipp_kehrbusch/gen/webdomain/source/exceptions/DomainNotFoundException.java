package com.philipp_kehrbusch.gen.webdomain.source.exceptions;

import com.philipp_kehrbusch.gen.webdomain.trafos.WebDomainGeneratorException;

public class DomainNotFoundException extends WebDomainGeneratorException {

  public DomainNotFoundException(String domain) {
    super("Domain '" + domain + "' does not exist");
  }
}
