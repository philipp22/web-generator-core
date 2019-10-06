package com.philipp_kehrbusch.gen.webdomain.util;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;
import com.philipp_kehrbusch.gen.webdomain.source.domain.RawAttribute;
import com.philipp_kehrbusch.gen.webdomain.target.builders.CDArgumentBuilder;
import com.philipp_kehrbusch.gen.webdomain.target.builders.CDMethodBuilder;
import com.philipp_kehrbusch.gen.webdomain.target.cd.CDMethod;
import com.philipp_kehrbusch.gen.webdomain.templates.TemplateManager;

public class MethodUtil {

  public static CDMethod createGetter(RawAttribute attribute) {
    var getter = new CDMethodBuilder()
            .name("get" + StringUtil.firstUpper(attribute.getName()))
            .returnType(attribute.getType())
            .addModifier("public")
            .build();
    TemplateManager.getInstance().setTemplate(getter, "java/methods/Getter.ftl",
            attribute.getName());
    return getter;
  }

  public static CDMethod createSetter(RawAttribute attribute) {
    var setter = new CDMethodBuilder()
            .name("set" + StringUtil.firstUpper(attribute.getName()))
            .returnType("void")
            .addArgument(new CDArgumentBuilder()
                    .name(attribute.getName())
                    .type(attribute.getType())
                    .build())
            .addModifier("public")
            .build();
    TemplateManager.getInstance().setTemplate(setter, "java/methods/Setter.ftl",
            attribute.getName());
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
