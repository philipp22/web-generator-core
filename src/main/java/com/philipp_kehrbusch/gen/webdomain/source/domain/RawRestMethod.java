package com.philipp_kehrbusch.gen.webdomain.source.domain;

public class RawRestMethod {
  private RestMethod method;
  private String returnType;

  public RawRestMethod(RestMethod method, String returnType) {
    this.method = method;
    this.returnType = returnType;
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
}
