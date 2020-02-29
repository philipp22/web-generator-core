package com.philipp_kehrbusch.gen.webdomain.trafos;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;
import com.philipp_kehrbusch.gen.webdomain.source.builders.RawAttributeBuilder;
import com.philipp_kehrbusch.gen.webdomain.source.builders.RawDerivedAttributeBuilder;
import com.philipp_kehrbusch.gen.webdomain.source.builders.RawViewBuilder;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.stream.Collectors;

public class RawViewTrafo {

  public RawViews transform(List<WebDomainParser.ViewContext> views) {
    var res = new RawViews();

    views.forEach(view -> {
      var builder = new RawViewBuilder()
              .addAnnotations(view.ANNOTATION().stream().map(ParseTree::getText).collect(Collectors.toList()))
              .name(view.name.getText())
              .addAttributes(view.attribute().stream()
                      .map(attr -> new RawAttributeBuilder()
                              .type(attr.type.getText())
                              .name(attr.name.getText())
                              .addAnnotations(attr.ANNOTATION().stream()
                                      .map(ParseTree::getText)
                                      .collect(Collectors.toList()))
                              .build())
                      .collect(Collectors.toList()))
              .addDerivedAttributes(view.viewAttribute().stream()
                      .map(attr -> new RawDerivedAttributeBuilder()
                              .path(attr.path.getText())
                              .alias(attr.alias != null ? attr.alias.getText() : null)
                              .addAnnotations(attr.ANNOTATION().stream()
                                      .map(ParseTree::getText)
                                      .collect(Collectors.toList()))
                              .build())
                      .collect(Collectors.toList()));

      if (view.fromList() != null) {
        view.fromList().viewFrom().forEach(viewFrom -> builder.addViewFrom(
                viewFrom.domainName.getText(),
                viewFrom.alias != null ? viewFrom.alias.getText() : viewFrom.domainName.getText()));
      }

      res.add(builder.build());
    });

    return res;
  }
}
