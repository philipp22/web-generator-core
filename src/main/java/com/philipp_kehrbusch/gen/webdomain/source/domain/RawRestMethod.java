package com.philipp_kehrbusch.gen.webdomain.source.domain;

import java.util.List;
import java.util.Map;

public class RawRestMethod {
  private String name;
  private RestMethod method;
  private String bodyType;
  private String bodyTypeName;
  private String returnType;
  private List<String> annotations;
  private String route;
  private Map<String, String> routeVariables;
  private List<RawAttribute> queryParams;
  private boolean authRequired;

  public RawRestMethod(String name, RestMethod method, String bodyType, String bodyTypeName, String returnType,
                       List<String> annotations, String route, Map<String, String> routeVariables,
                       List<RawAttribute> queryParams, boolean authRequired) {
    this.name = name;
    this.method = method;
    this.bodyType = bodyType;
    this.bodyTypeName = bodyTypeName;
    this.returnType = returnType;
    this.annotations = annotations;
    this.route = route;
    this.routeVariables = routeVariables;
    this.queryParams = queryParams;
    this.authRequired = authRequired;
  }

  public RestMethod getMethod() {
    return method;
  }

  public void setMethod(RestMethod method) {
    this.method = method;
  }

  public String getReturnType() {
    return returnType;
  }

  public void setReturnType(String returnType) {
    this.returnType = returnType;
  }

  public String getName() {
    return name;
  }

  public String getBodyType() {
    return bodyType;
  }

  public List<String> getAnnotations() {
    return annotations;
  }

  public String getRoute() {
    return route;
  }

  public Map<String, String> getRouteVariables() {
    return routeVariables;
  }

  public String getBodyTypeName() {
    return bodyTypeName;
  }

  public List<RawAttribute> getQueryParams() {
    return queryParams;
  }

  public boolean isAuthRequired() {
    return authRequired;
  }

  public void setAuthRequired(boolean authRequired) {
    this.authRequired = authRequired;
  }
}
