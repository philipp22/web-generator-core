package com.philipp_kehrbusch.gen.webdomain.coco;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;

public class CoCoUtil {
  public static boolean doesDomainExist(String domainName, WebDomainParser.ArtifactContext ast) {
    return ast.domain().stream().anyMatch(d -> d.name.getText().equals(domainName));
  }
}
