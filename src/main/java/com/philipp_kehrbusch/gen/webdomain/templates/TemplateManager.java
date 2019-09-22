package com.philipp_kehrbusch.gen.webdomain.templates;

import com.philipp_kehrbusch.gen.webdomain.target.cd.CDNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TemplateManager {

  private static TemplateManager instance;
  private Map<CDNode, TemplateConfig> templates = new HashMap<>();

  private TemplateManager() {
  }

  public static TemplateManager getInstance() {
    if (instance == null) {
      instance = new TemplateManager();
    }
    return instance;
  }

  public void setTemplate(CDNode node, String templatePath, Object... args) {
    templates.put(node, new TemplateConfig(templatePath, Arrays.asList(args)));
  }

  public TemplateConfig getTemplate(CDNode node) {
    return templates.get(node);
  }
}
