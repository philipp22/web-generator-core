package com.philipp_kehrbusch.gen.webdomain;

public class Target {

  public static final String JAVA = "java";
  public static final String TYPESCRIPT = "typescript";

  private String language;
  private String basePath;
  private FileNameResolver resolver;
  private String artifactTemplatePath;
  private String basePackage;

  public Target(String language, String basePath, FileNameResolver resolver, String artifactTemplatePath, String basePackage) {
    this.language = language;
    this.basePath = basePath;
    this.resolver = resolver;
    this.artifactTemplatePath = artifactTemplatePath;
    this.basePackage = basePackage;
  }

  public String getBasePackage() {
    return basePackage;
  }

  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }

  public FileNameResolver getResolver() {
    return resolver;
  }

  public void setResolver(FileNameResolver resolver) {
    this.resolver = resolver;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getBasePath() {
    return basePath;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getArtifactTemplatePath() {
    return artifactTemplatePath;
  }

  public void setArtifactTemplatePath(String artifactTemplatePath) {
    this.artifactTemplatePath = artifactTemplatePath;
  }
}
