package com.philipp_kehrbusch.gen.webdomain.source.domain;

import java.util.List;

public class RawDomain {
  private String name;
  private String superType;
  private List<String> annotations;
  private List<RawAttribute> attributes;
  private List<RawRestMethod> restMethods;

  public RawDomain(String name, String superType, List<String> annotations, List<RawAttribute> attributes,
                   List<RawRestMethod> restMethods) {
    this.name = name;
    this.superType = superType;
    this.annotations = annotations;
    this.attributes = attributes;
    this.restMethods = restMethods;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getAnnotations() {
    return annotations;
  }

  public void setAnnotations(List<String> annotations) {
    this.annotations = annotations;
  }

  public String getSuperType() {
    return superType;
  }

  public void setSuperType(String superType) {
    this.superType = superType;
  }

  public List<RawRestMethod> getRestMethods() {
    return restMethods;
  }

  public void setRestMethods(List<RawRestMethod> restMethods) {
    this.restMethods = restMethods;
  }

  public List<RawAttribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<RawAttribute> attributes) {
    this.attributes = attributes;
  }
}
