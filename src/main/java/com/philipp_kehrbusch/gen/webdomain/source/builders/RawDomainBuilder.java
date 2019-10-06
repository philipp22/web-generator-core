package com.philipp_kehrbusch.gen.webdomain.source.builders;

import com.philipp_kehrbusch.gen.webdomain.source.domain.RawAttribute;
import com.philipp_kehrbusch.gen.webdomain.source.domain.RawDomain;
import com.philipp_kehrbusch.gen.webdomain.source.domain.RawRestMethod;
import com.philipp_kehrbusch.gen.webdomain.target.builders.IBuilder;
import com.philipp_kehrbusch.gen.webdomain.target.builders.InvalidBuilderStateException;

import java.util.ArrayList;
import java.util.List;

public class RawDomainBuilder implements IBuilder<RawDomain> {
  private String name;
  private String superType;
  private List<String> annotations = new ArrayList<>();
  private List<RawAttribute> attributes = new ArrayList<>();
  private List<RawRestMethod> restMethods = new ArrayList<>();

  public RawDomainBuilder name(String name) {
    this.name = name;
    return this;
  }

  public RawDomainBuilder superType(String superType) {
    this.superType = superType;
    return this;
  }

  public RawDomainBuilder setAnnotations(List<String> annotations) {
    this.annotations = annotations;
    return this;
  }

  public RawDomainBuilder addAnnotations(List<String> annotations) {
    this.annotations.addAll(annotations);
    return this;
  }

  public RawDomainBuilder addAnnotation(String annotation) {
    this.annotations.add(annotation);
    return this;
  }

  public RawDomainBuilder setAttributes(List<RawAttribute> attributes) {
    this.attributes = attributes;
    return this;
  }

  public RawDomainBuilder addAttributes(List<RawAttribute> attributes) {
    this.attributes.addAll(attributes);
    return this;
  }

  public RawDomainBuilder addAttribute(RawAttribute attribute) {
    this.attributes.add(attribute);
    return this;
  }

  public RawDomainBuilder setRestMethods(List<RawRestMethod> restMethods) {
    this.restMethods = restMethods;
    return this;
  }

  public RawDomainBuilder addRestMethods(List<RawRestMethod> restMethods) {
    this.restMethods.addAll(restMethods);
    return this;
  }

  public RawDomainBuilder addRestMethod(RawRestMethod restMethod) {
    this.restMethods.add(restMethod);
    return this;
  }

  @Override
  public boolean isValid() {
    return this.name != null;
  }

  @Override
  public RawDomain build() {
    if (isValid()) {
      return new RawDomain(name, superType, annotations, attributes, restMethods);
    }
    throw new InvalidBuilderStateException(this);
  }
}
