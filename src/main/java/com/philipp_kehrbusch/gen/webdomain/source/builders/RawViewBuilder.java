package com.philipp_kehrbusch.gen.webdomain.source.builders;

import com.philipp_kehrbusch.gen.webdomain.source.domain.RawAttribute;
import com.philipp_kehrbusch.gen.webdomain.source.view.RawView;
import com.philipp_kehrbusch.gen.webdomain.source.view.RawDerivedAttribute;
import com.philipp_kehrbusch.gen.webdomain.target.builders.IBuilder;
import com.philipp_kehrbusch.gen.webdomain.target.builders.InvalidBuilderStateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawViewBuilder implements IBuilder<RawView> {
  private String name;
  private List<RawAttribute> attributes = new ArrayList<>();
  private List<RawDerivedAttribute> derivedAttributes = new ArrayList<>();
  private Map<String, String> viewFroms = new HashMap<>();
  private List<String> annotations = new ArrayList<>();

  public RawViewBuilder name(String name) {
    this.name = name;
    return this;
  }

  public RawViewBuilder setViewFroms(HashMap<String, String> viewFroms) {
    this.viewFroms = viewFroms;
    return this;
  }

  public RawViewBuilder addViewFrom(String viewFrom, String alias) {
    this.viewFroms.put(viewFrom, alias);
    return this;
  }

  public RawViewBuilder setAttributes(List<RawAttribute> attributes) {
    this.attributes = attributes;
    return this;
  }

  public RawViewBuilder addAttributes(List<RawAttribute> attributes) {
    this.attributes.addAll(attributes);
    return this;
  }

  public RawViewBuilder addAttribute(RawAttribute attribute) {
    this.attributes.add(attribute);
    return this;
  }

  public RawViewBuilder setDerivedAttributes(List<RawDerivedAttribute> derivedAttributes) {
    this.derivedAttributes = derivedAttributes;
    return this;
  }

  public RawViewBuilder addDerivedAttributes(List<RawDerivedAttribute> derivedAttributes) {
    this.derivedAttributes.addAll(derivedAttributes);
    return this;
  }

  public RawViewBuilder addDerivedAttribute(RawDerivedAttribute attribute) {
    this.derivedAttributes.add(attribute);
    return this;
  }

  public RawViewBuilder setAnnotations(List<String> annotations) {
    this.annotations = annotations;
    return this;
  }

  public RawViewBuilder addAnnotations(List<String> annotations) {
    this.annotations.addAll(annotations);
    return this;
  }

  public RawViewBuilder addAnnotation(String annotation) {
    this.annotations.add(annotation);
    return this;
  }

  @Override
  public boolean isValid() {
    return name != null;
  }

  @Override
  public RawView build() {
    if (isValid()) {
      return new RawView(name, derivedAttributes, attributes, viewFroms, annotations);
    } else {
      throw new InvalidBuilderStateException(this);
    }
  }
}
