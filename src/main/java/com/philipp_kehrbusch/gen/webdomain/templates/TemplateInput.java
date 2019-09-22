package com.philipp_kehrbusch.gen.webdomain.templates;

import com.philipp_kehrbusch.gen.webdomain.target.cd.CDNode;

public class TemplateInput {
  private CDNode node;
  private TemplateController tc;

  public TemplateInput(CDNode node, TemplateController tc) {
    this.node = node;
    this.tc = tc;
  }

  public CDNode getNode() {
    return node;
  }

  public TemplateController getTc() {
    return tc;
  }
}
