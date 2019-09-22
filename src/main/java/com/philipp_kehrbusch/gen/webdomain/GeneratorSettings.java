package com.philipp_kehrbusch.gen.webdomain;

import java.util.HashMap;
import java.util.Map;

public class GeneratorSettings {
  private String modelPath;
  private String trafoBasePackage;
  private Map<String, Target> targets = new HashMap<>();

  public GeneratorSettings(String modelPath) {
    this.modelPath = modelPath;
  }

  public String getModelPath() {
    return modelPath;
  }

  public void setModelPath(String modelPath) {
    this.modelPath = modelPath;
  }

  public GeneratorSettings trafoBasePackage(String trafoBasePackage) {
    this.trafoBasePackage = trafoBasePackage;
    return this;
  }

  public GeneratorSettings addTarget(String name, Target target) {
    this.targets.put(name, target);
    return this;
  }

  public String getTrafoBasePackage() {
    return trafoBasePackage;
  }

  public Map<String, Target> getTargets() {
    return targets;
  }

  public String getBasePackage(String targetName) {
    var target = targets.get(targetName);
    return target != null ? target.getBasePackage() : null;
  }
}
