package com.philipp_kehrbusch.gen.webdomain.coco;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;

import java.util.Optional;

@RegisterContextCondition
public class ViewAttributeFromListDomainsCoCo implements ContextCondition {

  public ViewAttributeFromListDomainsCoCo() {
  }

  private String getErrorMessage(WebDomainParser.ViewContext view, String domain) {
    return "View " + view.name.getText() + ": Domain " + domain + " does not exist";
  }

  @Override
  public Optional<String> check(WebDomainParser.ArtifactContext ast) {
    for (var view : ast.view()) {
      if (view.fromList() != null) {
        for (var viewFrom : view.fromList().viewFrom()) {
          if (!CoCoUtil.doesDomainExist(viewFrom.domainName.getText(), ast)) {
            return Optional.of(getErrorMessage(view, viewFrom.domainName.getText()));
          }
        }
      }
    }
    return Optional.empty();
  }
}
