package com.philipp_kehrbusch.gen.webdomain.coco;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;

import java.util.Optional;

@RegisterContextCondition
public class ViewAttributeTypeCoCo implements ContextCondition {

  private static final String errorMsg = "There must be only derived attributes or only attribute definitions in a view";

  public ViewAttributeTypeCoCo() {
  }

  @Override
  public Optional<String> check(WebDomainParser.ArtifactContext ast) {
    for (var view : ast.view()) {
      if (view.viewAttribute().size() > 0 && view.attribute().size() > 0) {
        return Optional.of("View " + view.name.getText() + ": " + errorMsg);
      }
    }
    return Optional.empty();
  }
}
