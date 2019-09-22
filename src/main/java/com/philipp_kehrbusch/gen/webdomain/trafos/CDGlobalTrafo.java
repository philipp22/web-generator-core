package com.philipp_kehrbusch.gen.webdomain.trafos;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;
import com.philipp_kehrbusch.gen.webdomain.target.WebElement;
import com.philipp_kehrbusch.gen.webdomain.target.cd.CDClass;
import com.philipp_kehrbusch.gen.webdomain.templates.TemplateManager;

import java.util.List;

public interface CDGlobalTrafo {
  void transform(final List<WebDomainParser.DomainContext> domain,
                 List<CDClass> domainClasses,
                 List<WebElement> elements,
                 final TemplateManager tm);
}
