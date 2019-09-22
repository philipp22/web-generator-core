package com.philipp_kehrbusch.gen.webdomain.util;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;
import com.philipp_kehrbusch.gen.webdomain.target.builders.CDArgumentBuilder;
import com.philipp_kehrbusch.gen.webdomain.target.builders.CDMethodBuilder;
import com.philipp_kehrbusch.gen.webdomain.target.cd.CDMethod;
import com.philipp_kehrbusch.gen.webdomain.templates.TemplateManager;

public class MethodUtil {

  public static CDMethod createGetter(WebDomainParser.AttributeContext attribute) {
    var getter = new CDMethodBuilder()
            .name("get" + StringUtil.firstUpper(attribute.name.getText()))
            .returnType(attribute.type.getText())
            .addModifier("public")
            .build();
    TemplateManager.getInstance().setTemplate(getter, "java/methods/Getter.ftl",
            attribute.name.getText());
    return getter;
  }

  public static CDMethod createSetter(WebDomainParser.AttributeContext attribute) {
    var setter = new CDMethodBuilder()
            .name("set" + StringUtil.firstUpper(attribute.name.getText()))
            .returnType("void")
            .addArgument(new CDArgumentBuilder()
                    .name(attribute.name.getText())
                    .type(attribute.type.getText())
                    .build())
            .addModifier("public")
            .build();
    TemplateManager.getInstance().setTemplate(setter, "java/methods/Setter.ftl",
            attribute.name.getText());
    return setter;
  }

  public static CDMethod createGetter(String type, String name) {
    var getter = new CDMethodBuilder()
            .name("get" + StringUtil.firstUpper(name))
            .returnType(type)
            .addModifier("public")
            .build();
    TemplateManager.getInstance().setTemplate(getter, "java/methods/Getter.ftl", name);
    return getter;
  }

  public static CDMethod createSetter(String type, String name) {
    var setter = new CDMethodBuilder()
            .name("set" + StringUtil.firstUpper(name))
            .returnType("void")
            .addArgument(new CDArgumentBuilder()
                    .name(name)
                    .type(type)
                    .build())
            .addModifier("public")
            .build();
    TemplateManager.getInstance().setTemplate(setter, "java/methods/Setter.ftl", name);
    return setter;
  }
}
