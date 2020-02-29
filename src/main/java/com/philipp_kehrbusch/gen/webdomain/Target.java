package com.philipp_kehrbusch.gen.webdomain;

public class Target {

  public static final String JAVA = "java";
  public static final String TYPESCRIPT = "typescript";
  public static final String DART = "dart";

  private String language;
  private String basePath;
  private String basePathHandcoded;
  private FileNameResolver resolver;
  private String artifactTemplatePath;
  private String basePackage;
  private String name;

  public Target(String language, String basePath, String basePathHandcoded, FileNameResolver resolver,
                String artifactTemplatePath, String basePackage, String name) {
    this.language = language;
    this.basePath = basePath;
    this.basePathHandcoded = basePathHandcoded;
    this.resolver = resolver;
    this.artifactTemplatePath = artifactTemplatePath;
    this.basePackage = basePackage;
    this.name = name;
  }

  public String getBasePackage() {
    return basePackage;
  }

  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getBasePathHandcoded() {
    return basePathHandcoded;
  }

  public void setBasePathHandcoded(String basePathHandcoded) {
    this.basePathHandcoded = basePathHandcoded;
  }
}
