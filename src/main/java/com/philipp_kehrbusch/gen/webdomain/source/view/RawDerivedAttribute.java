package com.philipp_kehrbusch.gen.webdomain.source.view;

import java.util.List;

public class RawDerivedAttribute {
  private String path;
  private String alias;
  private List<String> annotations;

  public RawDerivedAttribute(String path, String alias, List<String> annotations) {
    this.path = path;
    this.alias = alias;
    this.annotations = annotations;
  }

  public String getPath() {
    return path;
  }

  public String getAlias() {
    return alias;
  }

  public List<String> getAnnotations() {
    return annotations;
  }
}
