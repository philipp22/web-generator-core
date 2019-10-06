package com.philipp_kehrbusch.gen.webdomain.source.builders;

import com.philipp_kehrbusch.gen.webdomain.source.domain.RawAttribute;
import com.philipp_kehrbusch.gen.webdomain.target.builders.IBuilder;
import com.philipp_kehrbusch.gen.webdomain.target.builders.InvalidBuilderStateException;

import java.util.ArrayList;
import java.util.List;

public class RawAttributeBuilder implements IBuilder<RawAttribute> {
  private String type;
  private String name;
  private boolean optional = false;
  private List<String> annotations = new ArrayList<>();

  public RawAttributeBuilder type(String type) {
    this.type = type;
    return this;
  }

  public RawAttributeBuilder name(String name) {
    this.name = name;
    return this;
  }

  public RawAttributeBuilder optional(boolean optional) {
    this.optional = optional;
    return this;
  }

  public RawAttributeBuilder setAnnotations(List<String> annotations) {
    this.annotations = annotations;
    return this;
  }

  public RawAttributeBuilder addAnnotations(List<String> annotations) {
    this.annotations.addAll(annotations);
    return this;
  }

  public RawAttributeBuilder addAnnotation(String annotation) {
    this.annotations.add(annotation);
    return this;
  }

  @Override
  public boolean isValid() {
    return this.type != null && this.name != null;
  }

  @Override
  public RawAttribute build() {
    if (isValid()) {
      return new RawAttribute(type, name, optional, annotations);
    } else {
      throw new InvalidBuilderStateException(this);
    }
  }
}
