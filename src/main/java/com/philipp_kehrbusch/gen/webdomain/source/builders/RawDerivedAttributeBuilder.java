package com.philipp_kehrbusch.gen.webdomain.source.builders;

import com.philipp_kehrbusch.gen.webdomain.source.view.RawDerivedAttribute;
import com.philipp_kehrbusch.gen.webdomain.target.builders.IBuilder;
import com.philipp_kehrbusch.gen.webdomain.target.builders.InvalidBuilderStateException;

import java.util.ArrayList;
import java.util.List;

public class RawDerivedAttributeBuilder implements IBuilder<RawDerivedAttribute> {
  private String path;
  private String alias;
  private List<String> annotations = new ArrayList<>();

  public RawDerivedAttributeBuilder path(String path) {
    this.path = path;
    return this;
  }

  public RawDerivedAttributeBuilder alias(String alias) {
    this.alias = alias;
    return this;
  }

  public RawDerivedAttributeBuilder setAnnotations(List<String> annotations) {
    this.annotations = annotations;
    return this;
  }

  public RawDerivedAttributeBuilder addAnnotations(List<String> annotations) {
    this.annotations.addAll(annotations);
    return this;
  }

  public RawDerivedAttributeBuilder addAnnotation(String annotation) {
    this.annotations.add(annotation);
    return this;
  }

  @Override
  public boolean isValid() {
    return path != null;
  }

  @Override
  public RawDerivedAttribute build() {
    if (isValid()) {
      return new RawDerivedAttribute(path, alias, annotations);
    } else {
      throw new InvalidBuilderStateException(this);
    }
  }
}
