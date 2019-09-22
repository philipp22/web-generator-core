package com.philipp_kehrbusch.gen.webdomain.templates;

import java.util.List;

public class TemplateConfig {
  private String path;
  private List<Object> args;

  public TemplateConfig(String path, List<Object> args) {
    this.path = path;
    this.args = args;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public List<Object> getArgs() {
    return args;
  }

  public void setArgs(List<Object> args) {
    this.args = args;
  }
}
