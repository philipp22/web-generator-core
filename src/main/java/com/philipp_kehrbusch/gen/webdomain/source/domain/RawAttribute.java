package com.philipp_kehrbusch.gen.webdomain.source.domain;

import java.util.List;

public class RawAttribute {
  private String type;
  private String name;
  private boolean optional;
  private List<String> annotations;

  public RawAttribute(String type, String name, boolean optional, List<String> annotations) {
    this.type = type;
    this.name = name;
    this.optional = optional;
    this.annotations = annotations;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isOptional() {
    return optional;
  }

  public void setOptional(boolean optional) {
    this.optional = optional;
  }

  public List<String> getAnnotations() {
    return annotations;
  }

  public void setAnnotations(List<String> annotations) {
    this.annotations = annotations;
  }
}
