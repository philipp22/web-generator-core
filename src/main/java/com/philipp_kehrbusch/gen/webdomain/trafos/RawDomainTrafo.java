package com.philipp_kehrbusch.gen.webdomain.trafos;

import com.philipp_kehrbusch.gen.webdomain.GeneratorSettings;
import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;
import com.philipp_kehrbusch.gen.webdomain.source.builders.RawAttributeBuilder;
import com.philipp_kehrbusch.gen.webdomain.source.builders.RawDomainBuilder;
import com.philipp_kehrbusch.gen.webdomain.source.builders.RawRestMethodBuilder;
import com.philipp_kehrbusch.gen.webdomain.source.domain.RawRestMethod;
import com.philipp_kehrbusch.gen.webdomain.source.domain.RestMethod;
import com.philipp_kehrbusch.gen.webdomain.source.exceptions.InvalidRestMethodException;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RawDomainTrafo {

  public RawDomains transform(List<WebDomainParser.DomainContext> domains) {
    var res = new RawDomains();

    domains.forEach(domain -> {
      var restMethods = new ArrayList<RawRestMethod>();

      if (domain.rest() != null && domain.rest().restMethod() != null) {
        var methods = domain.rest().restMethod();
        methods.forEach(method -> {
          RestMethod methodEnum;
          if (method.httpMethod.getText().equalsIgnoreCase("get")) {
            methodEnum = RestMethod.GET;
          } else if (method.httpMethod.getText().equalsIgnoreCase("put")) {
            methodEnum = RestMethod.PUT;
          } else if (method.httpMethod.getText().equalsIgnoreCase("post")) {
            methodEnum = RestMethod.POST;
          } else if (method.httpMethod.getText().equalsIgnoreCase("delete")) {
            methodEnum = RestMethod.DELETE;
          } else {
            throw new InvalidRestMethodException(method.httpMethod.getText());
          }

          restMethods.add(new RawRestMethodBuilder()
                  .method(methodEnum)
                  .returnType(method.returnType.getText())
                  .build());
        });
      } else {
        restMethods.add(new RawRestMethodBuilder()
                .method(RestMethod.GET)
                .returnType(domain.name.getText())
                .build());
        restMethods.add(new RawRestMethodBuilder()
                .method(RestMethod.PUT)
                .returnType(domain.name.getText())
                .build());
        restMethods.add(new RawRestMethodBuilder()
                .method(RestMethod.POST)
                .returnType(domain.name.getText())
                .build());
        restMethods.add(new RawRestMethodBuilder()
                .method(RestMethod.DELETE)
                .returnType(domain.name.getText())
                .build());
      }

      res.add(new RawDomainBuilder()
              .addAnnotations(domain.ANNOTATION().stream().map(ParseTree::getText).collect(Collectors.toList()))
              .name(domain.name.getText())
              .addAttributes(domain.attribute().stream()
                      .map(attr -> new RawAttributeBuilder()
                              .type(attr.type.getText())
                              .name(attr.name.getText())
                              .addAnnotations(attr.ANNOTATION().stream().map(ParseTree::getText).collect(Collectors.toList()))
                              .optional(attr.optional != null)
                              .build())
                      .collect(Collectors.toList()))
              .addRestMethods(restMethods)
              .build());
    });

    return res;
  }
}
