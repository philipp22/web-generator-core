package com.philipp_kehrbusch.gen.webdomain;

import com.philipp_kehrbusch.gen.webdomain.source.domain.RawDomain;
import com.philipp_kehrbusch.gen.webdomain.target.WebElement;
import com.philipp_kehrbusch.gen.webdomain.templates.TemplateController;
import com.philipp_kehrbusch.gen.webdomain.trafos.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WebDomainGenerator {

  private GeneratorSettings settings;

  public WebDomainGenerator(GeneratorSettings settings) {
    this.settings = settings;
  }

  public void generate() throws WebDomainGeneratorException {
    try {
      for (var target : settings.getTargets().values()) {
        FileUtils.deleteDirectory(new File(target.getBasePath()));
      }

      var modelDir = new File(settings.getModelPath());
      if (!modelDir.isDirectory()) {
        throw new WebDomainGeneratorException("Model path not found!");
      }
      var domains = new ArrayList<WebDomainParser.DomainContext>();
      for (var file : Objects.requireNonNull(modelDir.listFiles())) {
        System.out.println("Parsing file " + file.getName());
        var stream = CharStreams.fromStream(new FileInputStream(file));
        WebDomainLexer lexer = new WebDomainLexer(stream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        WebDomainParser parser = new WebDomainParser(commonTokenStream);
        WebDomainParser.ArtifactContext artifact = parser.artifact();
        domains.addAll(artifact.domain());
      }

      var elements = new WebElements();
      var generator = new TemplateController();
      var rawDomainClasses = new RawDomainTrafo().transform(domains);

      for (var trafo : getGlobalTrafos(settings.getTrafoBasePackage())) {
        var transform = getTransformMethod(trafo.getTrafoClass());
        callGlobalTransform(trafo.getTrafoClass(), transform, rawDomainClasses.stream()
                        .filter(domain -> isIncluded(trafo, domain)).collect(Collectors.toCollection(RawDomains::new)),
                elements);
      }

      for (var trafo : getSingleTrafos(settings.getTrafoBasePackage())) {
        var transform = getTransformMethod(trafo.getTrafoClass());
        for (var domainClass : rawDomainClasses) {
          if (isIncluded(trafo, domainClass)) {
            callSingleTransform(trafo.getTrafoClass(), transform, domainClass, elements);
          }
        }
      }

      elements.forEach(el -> {
        var targetName = el.getTarget();
        if (targetName != null) {
          generator.generate(settings.getTargets().get(el.getTarget()), el);
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private boolean isIncluded(Trafo trafo, RawDomain domain) {
    var included = trafo.getIncludeAnnotations().length == 0 ||
            domain.getAnnotations().stream().anyMatch(annotation ->
                    Arrays.asList(trafo.getIncludeAnnotations()).contains(annotation.substring(1)));
    var excluded = trafo.getExcludeAnnotations().length > 0 &&
            domain.getAnnotations().stream().anyMatch(annotation ->
                    Arrays.asList(trafo.getExcludeAnnotations()).contains(annotation.substring(1)));

    return included && !excluded;
  }

  private List<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotationClass, String basePackage) {
    var scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(annotationClass));
    var res = new ArrayList<Class<?>>();
    for (var bd : scanner.findCandidateComponents(basePackage)) {
      try {
        res.add(Class.forName(bd.getBeanClassName()));
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    return res;
  }

  private List<Trafo> getSingleTrafos(String basePackage) {
    return getAnnotatedClasses(SingleTrafo.class, basePackage).stream()
            .map(clazz -> {
              var includes = clazz.getAnnotation(SingleTrafo.class).includeAnnotated();
              var excludes = clazz.getAnnotation(SingleTrafo.class).excludeAnnotated();
              return new Trafo(clazz, includes, excludes);
            })
            .collect(Collectors.toList());
  }

  private List<Trafo> getGlobalTrafos(String basePackage) {
    return getAnnotatedClasses(GlobalTrafo.class, basePackage).stream()
            .map(clazz -> {
              var includes = clazz.getAnnotation(GlobalTrafo.class).includeAnnotated();
              var excludes = clazz.getAnnotation(GlobalTrafo.class).excludeAnnotated();
              return new Trafo(clazz, includes, excludes);
            })
            .collect(Collectors.toList());
  }

  private Class<?> getDomainTrafo(String basePackage) {
    return getAnnotatedClasses(DomainTrafo.class, basePackage).get(0);
  }

  private Method getTransformMethod(Class<?> trafo) throws TransformMethodNotFoundException {
    for (var method : trafo.getMethods()) {
      if (method.getAnnotation(Transform.class) != null) {
        return method;
      }
    }
    throw new TransformMethodNotFoundException(trafo.getName());
  }

  private void callGlobalTransform(Class<?> trafoClass,
                                   Method transform,
                                   RawDomains domainClasses,
                                   WebElements elements) throws WebDomainGeneratorException {
    var trafo = getTrafoInstance(trafoClass);
    try {
      var params = new Object[transform.getParameterCount()];
      for (int i = 0; i < transform.getParameterCount(); i++) {
        var paramType = transform.getParameterTypes()[i];
        if (paramType == RawDomains.class) {
          params[i] = domainClasses;
        } else if (paramType == GeneratorSettings.class) {
          params[i] = settings;
        } else if (paramType == WebElements.class) {
          params[i] = elements;
        } else {
          throw new UnknownTrafoArgumentException(paramType.getTypeName(), paramType.getName());
        }
      }
      transform.invoke(trafo, params);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  private void callSingleTransform(Class<?> trafoClass,
                                   Method transform,
                                   RawDomain domainClass,
                                   WebElements elements) throws WebDomainGeneratorException {
    var trafo = getTrafoInstance(trafoClass);
    try {
      var params = new Object[transform.getParameterCount()];
      for (int i = 0; i < transform.getParameterCount(); i++) {
        var paramType = transform.getParameterTypes()[i];
        if (paramType == RawDomain.class) {
          params[i] = domainClass;
        } else if (paramType == GeneratorSettings.class) {
          params[i] = settings;
        } else if (paramType == WebElements.class) {
          params[i] = elements;
        } else {
          throw new UnknownTrafoArgumentException(paramType.getTypeName(), paramType.getName());
        }
      }
      transform.invoke(trafo, params);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  private void callDomainTrafo(Class<?> trafoClass,
                               Method transform,
                               List<WebDomainParser.DomainContext> domains,
                               List<WebElement> elements) throws TrafoConstructorMissingException {
    var trafo = getTrafoInstance(trafoClass);
    try {
      transform.invoke(trafo, domains, elements, settings);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  private Object getTrafoInstance(Class<?> trafoClass) throws TrafoConstructorMissingException {
    for (var constructor : trafoClass.getConstructors()) {
      if (constructor.getParameters().length == 0) {
        try {
          return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
    throw new TrafoConstructorMissingException(trafoClass);
  }
}
