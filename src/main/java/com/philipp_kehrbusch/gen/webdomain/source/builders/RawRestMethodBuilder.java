package com.philipp_kehrbusch.gen.webdomain.source.builders;

import com.philipp_kehrbusch.gen.webdomain.source.domain.RawRestMethod;
import com.philipp_kehrbusch.gen.webdomain.source.domain.RestMethod;
import com.philipp_kehrbusch.gen.webdomain.target.builders.IBuilder;
import com.philipp_kehrbusch.gen.webdomain.target.builders.InvalidBuilderStateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawRestMethodBuilder implements IBuilder<RawRestMethod> {
  private RestMethod httpMethod;
  private String name;
  private String bodyType;
  private String bodyTypeName;
  private String returnType;
  private String route;
  private List<String> annotations = new ArrayList<>();
  private Map<String, String> routeVariables = new HashMap<>();
  private Map<String, String> queryParams = new HashMap<>();

  public RawRestMethodBuilder route(String route) {
    this.route = route;
    return this;
  }

  public RawRestMethodBuilder method(RestMethod method) {
    this.httpMethod = method;
    return this;
  }

  public RawRestMethodBuilder returnType(String returnType) {
    this.returnType = returnType;
    return this;
  }

  public RawRestMethodBuilder bodyType(String bodyType) {
    this.bodyType = bodyType;
    return this;
  }

  public RawRestMethodBuilder bodyTypeName(String bodyTypeName) {
    this.bodyTypeName = bodyTypeName;
    return this;
  }

  public RawRestMethodBuilder name(String name) {
    this.name = name;
    return this;
  }

  public RawRestMethodBuilder setAnnotations(List<String> annotations) {
    this.annotations = annotations;
    return this;
  }

  public RawRestMethodBuilder addAnnotations(List<String> annotations) {
    this.annotations.addAll(annotations);
    return this;
  }

  public RawRestMethodBuilder addAnnotation(String annotation) {
    this.annotations.add(annotation);
    return this;
  }

  public RawRestMethodBuilder setRouteVariables(Map<String, String> routeVariables) {
    this.routeVariables = routeVariables;
    return this;
  }

  public RawRestMethodBuilder addRouteVariable(String type, String name) {
    this.routeVariables.put(name, type);
    return this;
  }

  public RawRestMethodBuilder setQueryParams(Map<String, String> queryParams) {
    this.queryParams = queryParams;
    return this;
  }

  public RawRestMethodBuilder addQueryParam(String type, String name) {
    this.queryParams.put(name, type);
    return this;
  }

  @Override
  public boolean isValid() {
    return httpMethod != null && returnType != null;
  }

  @Override
  public RawRestMethod build() {
    if (isValid()) {
      return new RawRestMethod(name, httpMethod, bodyType, bodyTypeName, returnType, annotations, route, routeVariables,
              queryParams);
    } else {
      throw new InvalidBuilderStateException(this);
    }
  }
}
