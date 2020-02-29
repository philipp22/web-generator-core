package com.philipp_kehrbusch.gen.webdomain.coco;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;

import java.util.Optional;

@RegisterContextCondition
public class ViewAttributeFromListExistsCoCo implements ContextCondition {

  private static final String errorMsg = "Derived attributes can only be specified if view is derived from at least one domain";

  public ViewAttributeFromListExistsCoCo() {
  }

  @Override
  public Optional<String> check(WebDomainParser.ArtifactContext ast) {
    for (var view : ast.view()) {
      if (view.viewAttribute().size() > 0 && view.fromList() == null) {
        return Optional.of("View " + view.name.getText() + ": " + errorMsg);
      }
    }
    return Optional.empty();
  }
}
