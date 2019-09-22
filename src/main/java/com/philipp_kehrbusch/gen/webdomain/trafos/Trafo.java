package com.philipp_kehrbusch.gen.webdomain.trafos;

public class Trafo {
  private Class<?> trafoClass;
  private String[] includeAnnotations;
  private String[] excludeAnnotations;

  public Trafo(Class<?> trafoClass, String[] includeAnnotations, String[] excludeAnnotations) {
    this.trafoClass = trafoClass;
    this.includeAnnotations = includeAnnotations;
    this.excludeAnnotations = excludeAnnotations;
  }

  public Class<?> getTrafoClass() {
    return trafoClass;
  }

  public void setTrafoClass(Class<?> trafoClass) {
    this.trafoClass = trafoClass;
  }

  public String[] getIncludeAnnotations() {
    return includeAnnotations;
  }

  public void setIncludeAnnotations(String[] includeAnnotations) {
    this.includeAnnotations = includeAnnotations;
  }

  public String[] getExcludeAnnotations() {
    return excludeAnnotations;
  }

  public void setExcludeAnnotations(String[] excludeAnnotations) {
    this.excludeAnnotations = excludeAnnotations;
  }
}
