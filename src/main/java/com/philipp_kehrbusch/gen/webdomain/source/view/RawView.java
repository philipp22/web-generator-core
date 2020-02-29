package com.philipp_kehrbusch.gen.webdomain.source.view;

import com.philipp_kehrbusch.gen.webdomain.source.domain.RawAttribute;

import java.util.List;
import java.util.Map;

public class RawView {
  private String name;
  private List<RawDerivedAttribute> derivedAttributes;
  private List<RawAttribute> attributes;
  private Map<String, String> viewFroms;
  private List<String> annotations;

  public RawView(String name, List<RawDerivedAttribute> derivedAttributes, List<RawAttribute> attributes,
                 Map<String, String> viewFroms, List<String> annotations) {
    this.name = name;
    this.derivedAttributes = derivedAttributes;
    this.attributes = attributes;
    this.viewFroms = viewFroms;
    this.annotations = annotations;
  }

  public String getName() {
    return name;
  }

  public List<RawDerivedAttribute> getDerivedAttributes() {
    return derivedAttributes;
  }

  public List<RawAttribute> getAttributes() {
    return attributes;
  }

  public Map<String, String> getViewFroms() {
    return viewFroms;
  }

  public List<String> getAnnotations() {
    return annotations;
  }
}
