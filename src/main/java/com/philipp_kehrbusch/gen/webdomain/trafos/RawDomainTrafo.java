package com.philipp_kehrbusch.gen.webdomain.trafos;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;
import com.philipp_kehrbusch.gen.webdomain.source.builders.RawAttributeBuilder;
import com.philipp_kehrbusch.gen.webdomain.source.builders.RawDomainBuilder;
import com.philipp_kehrbusch.gen.webdomain.source.builders.RawRestMethodBuilder;
import com.philipp_kehrbusch.gen.webdomain.source.domain.RawRestMethod;
import com.philipp_kehrbusch.gen.webdomain.source.domain.RestMethod;
import com.philipp_kehrbusch.gen.webdomain.source.exceptions.InvalidRestMethodException;
import com.philipp_kehrbusch.gen.webdomain.util.RestUtil;
import com.philipp_kehrbusch.gen.webdomain.util.StringUtil;
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

          var builder = new RawRestMethodBuilder()
                  .method(methodEnum)
                  .name(method.name.getText())
                  .route(method.path.getText())
                  .setRouteVariables(RestUtil.getRouteVariables(method.path.getText()))
                  .returnType(method.returnType.getText());

          if (method.bodyType != null) {
            builder.bodyType(method.bodyType.getText());
            builder.bodyTypeName(method.bodyTypeName != null ?
                    method.bodyTypeName.getText() :
                    StringUtil.firstLower(method.bodyType.getText()));
          }

          if (method.restMethodOptions() != null) {
            method.restMethodOptions().restMethodOption().forEach(option -> {
              switch (option.optionName.getText()) {
                case "queryParams":
                  option.queryParams().attribute().forEach(attr -> {
                    builder.addQueryParam(new RawAttributeBuilder()
                            .type(attr.type.getText())
                            .name(attr.name.getText())
                            .optional(attr.optional != null)
                            .build());
                  });
                  break;
                case "auth":
                  var authRequired = option.auth.getText();
                  if (authRequired.equals("false")) {
                    builder.authRequired(false);
                  }
              }
            });
          }

          restMethods.add(builder.build());
        });
      }

      var builder = new RawDomainBuilder()
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
              .addRestMethods(restMethods);

      if (domain.dependencies() != null) {
        builder.addDependencies(domain.dependencies().NAME().stream().map(ParseTree::getText).collect(Collectors.toList()));
      }
      res.add(builder.build());
    });

    return res;
  }
}
