package com.philipp_kehrbusch.gen.webdomain.templates;

import com.philipp_kehrbusch.gen.webdomain.Target;
import com.philipp_kehrbusch.gen.webdomain.target.WebElement;
import com.philipp_kehrbusch.gen.webdomain.target.cd.CDNode;
import com.philipp_kehrbusch.gen.webdomain.util.GenUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TemplateController {

  private Map<String, Object> currentInput;
  private Object[] currentArgs;
  private Configuration cfg;
  private TemplateManager tm = TemplateManager.getInstance();

  public TemplateController() {
    cfg = new Configuration(Configuration.VERSION_2_3_28);
    cfg.setTemplateLoader(new ClassTemplateLoader(getClass().getClassLoader(), "templates/cd"));
    cfg.setDefaultEncoding("UTF-8");
    cfg.setLocale(Locale.US);
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
  }

  public void generate(Target target, WebElement element, Object... args) {
    if (target == null) {
      return;
    }

    currentInput = new HashMap<>();
    currentInput.put("element", element);
    currentInput.put("tc", this);
    this.currentArgs = args;

    try {
      var template = cfg.getTemplate(target.getArtifactTemplatePath());
      var file = GenUtils.getGenPath(target, element, true);
      file = adjustForHandcoded(element, target, file);
      var writer = new FileWriter(file);
      template.process(currentInput, writer);
    } catch (IOException | TemplateException e) {
      e.printStackTrace();
    }
  }

  private File adjustForHandcoded(WebElement element, Target target, File file) {
    var doesHandcodedExist = new File(file.getAbsolutePath().replace(
            target.getBasePath(), target.getBasePathHandcoded())).exists();
    if (doesHandcodedExist) {
      var newFile = new File(file.getAbsolutePath().replace(
              target.getResolver().resolve(element.getName()),
              target.getResolver().resolve("Super" + element.getName())));
      element.setName("Super" + element.getName());
      element.getArtifact().setName("Super" + element.getName());

      element.getArtifact().getClasses().forEach(clazz -> {
        clazz.setGeneratedName("Super" + clazz.getName());
        clazz.getConstructors().forEach(constructor -> constructor.setName("Super" + clazz.getName()));

        if (clazz.getAnnotations().contains("@Entity")) {
          clazz.getAnnotations().remove("@Entity");
          clazz.getAnnotations().add("@MappedSuperclass");
        }
      });

      element.getArtifact().getInterfaces().forEach(iface -> {
        iface.setGeneratedName("Super" + iface.getName());
      });
      return newFile;
    }
    return file;
  }

  public void signature(String... signatureArgs) throws Exception {
    if (currentArgs.length != signatureArgs.length) {
      throw new Exception(String.format("Passed argument count (%d) does not match signature argument count (%d)",
              currentArgs.length, signatureArgs.length));
    }

    for (int i = 0; i < signatureArgs.length; i++) {
      currentInput.put(signatureArgs[i], currentArgs[i]);
    }
  }

  public String include(String templatePath, Object... args) {
    return this.includeWithArgsArray(templatePath, args);
  }

  public String includeWithArgsArray(String templatePath, Object[] args) {
    currentInput = new HashMap<>();
    currentInput.put("tc", this);
    currentArgs = args;

    try {
      var template = cfg.getTemplate(templatePath);
      var writer = new StringWriter();
      template.process(currentInput, writer);
      return writer.toString();
    } catch (IOException | TemplateException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String defineHookPoint(CDNode node, String defaultTemplate, String... args) {
    var template = tm.getTemplate(node);
    if (template != null) {
      return includeWithArgsArray(template.getPath(), template.getArgs().toArray());
    } else if (defaultTemplate != null) {
      return includeWithArgsArray(defaultTemplate, args);
    } else {
      return "";
    }
  }
}
