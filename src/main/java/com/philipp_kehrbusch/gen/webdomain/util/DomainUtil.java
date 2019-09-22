package com.philipp_kehrbusch.gen.webdomain.util;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;
import com.philipp_kehrbusch.gen.webdomain.target.builders.CDAttributeBuilder;
import com.philipp_kehrbusch.gen.webdomain.target.cd.CDAttribute;

import java.util.stream.Collectors;

public class DomainUtil {

  public static CDAttribute createAttribute(WebDomainParser.AttributeContext attr) {
    return new CDAttributeBuilder()
            .name(attr.name.getText())
            .type(attr.type.getText())
            .setAnnotations(attr.ANNOTATION().stream()
                    .map(annotation -> annotation.getSymbol().getText())
                    .collect(Collectors.toList()))
            .build();
  }
}
