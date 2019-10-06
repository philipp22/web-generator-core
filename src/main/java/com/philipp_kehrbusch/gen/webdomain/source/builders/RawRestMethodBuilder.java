package com.philipp_kehrbusch.gen.webdomain.source.builders;

import com.philipp_kehrbusch.gen.webdomain.source.domain.RawRestMethod;
import com.philipp_kehrbusch.gen.webdomain.source.domain.RestMethod;
import com.philipp_kehrbusch.gen.webdomain.target.builders.IBuilder;
import com.philipp_kehrbusch.gen.webdomain.target.builders.InvalidBuilderStateException;

public class RawRestMethodBuilder implements IBuilder<RawRestMethod> {
  private RestMethod method;
  private String returnType;

  public RawRestMethodBuilder method(RestMethod method) {
    this.method = method;
    return this;
  }

  public RawRestMethodBuilder returnType(String returnType) {
    this.returnType = returnType;
    return this;
  }

  @Override
  public boolean isValid() {
    return method != null && returnType != null;
  }

  @Override
  public RawRestMethod build() {
    if (isValid()) {
      return new RawRestMethod(method, returnType);
    } else {
      throw new InvalidBuilderStateException(this);
    }
  }
}
