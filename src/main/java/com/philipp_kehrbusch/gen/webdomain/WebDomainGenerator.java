package com.philipp_kehrbusch.gen.webdomain;

import com.philipp_kehrbusch.gen.webdomain.coco.ContextCondition;
import com.philipp_kehrbusch.gen.webdomain.coco.RegisterContextCondition;
import com.philipp_kehrbusch.gen.webdomain.source.domain.RawDomain;
import com.philipp_kehrbusch.gen.webdomain.source.view.RawView;
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
      var views = new ArrayList<WebDomainParser.ViewContext>();

      var cocos = getContextConditions();

      for (var file : Objects.requireNonNull(modelDir.listFiles())) {
        System.out.println("Parsing file " + file.getName());
        var stream = CharStreams.fromStream(new FileInputStream(file));
        WebDomainLexer lexer = new WebDomainLexer(stream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        WebDomainParser parser = new WebDomainParser(commonTokenStream);
        WebDomainParser.ArtifactContext artifact = parser.artifact();

        for (var coco : cocos) {
          var error = coco.check(artifact);
          if (error.isPresent()) {
            throw new WebDomainGeneratorException("Context condition failed: " + error.get());
          }
        }

        domains.addAll(artifact.domain());
        views.addAll(artifact.view());
      }

      var elements = new WebElements();
      var generator = new TemplateController();
      var rawDomainClasses = new RawDomainTrafo().transform(domains);
      var rawViewClasses = new RawViewTrafo().transform(views);

      for (var trafo : getGlobalDomainTrafos(settings.getTrafoBasePackage())) {
        var transform = getTransformMethod(trafo.getTrafoClass());
        callDomainTransform(
                trafo.getTrafoClass(),
                transform,
                rawDomainClasses.stream()
                        .filter(domain -> isIncluded(trafo, domain))
                        .collect(Collectors.toCollection(RawDomains::new)),
                RawDomains.class,
                rawViewClasses,
                elements);
      }

      for (var trafo : getGlobalViewTrafos(settings.getTrafoBasePackage())) {
        var transform = getTransformMethod(trafo.getTrafoClass());
        callViewTransform(
                trafo.getTrafoClass(),
                transform,
                rawViewClasses.stream()
                        .filter(view -> isIncluded(trafo, view))
                        .collect(Collectors.toCollection(RawViews::new)),
                RawViews.class,
                rawDomainClasses,
                elements);
      }

      for (var trafo : getSingleDomainTrafos(settings.getTrafoBasePackage())) {
        var transform = getTransformMethod(trafo.getTrafoClass());
        for (var domainClass : rawDomainClasses) {
          if (isIncluded(trafo, domainClass)) {
            callDomainTransform(trafo.getTrafoClass(), transform, domainClass, RawDomain.class, rawViewClasses, elements);
          }
        }
      }

      for (var trafo : getSingleViewTrafos(settings.getTrafoBasePackage())) {
        var transform = getTransformMethod(trafo.getTrafoClass());
        for (var viewClass : rawViewClasses) {
          if (isIncluded(trafo, viewClass)) {
            callViewTransform(trafo.getTrafoClass(), transform, viewClass, RawView.class, rawDomainClasses, elements);
          }
        }
      }

      elements.forEach(el -> {
        var targetName = el.getTarget();
        if (targetName != null) {
          generator.generate(settings.getTargets().get(el.getTarget()), el);
        }
      });
    } catch (IOException | GeneratorException e) {
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

  private boolean isIncluded(Trafo trafo, RawView view) {
    var included = trafo.getIncludeAnnotations().length == 0 ||
            view.getAnnotations().stream().anyMatch(annotation ->
                    Arrays.asList(trafo.getIncludeAnnotations()).contains(annotation.substring(1)));
    var excluded = trafo.getExcludeAnnotations().length > 0 &&
            view.getAnnotations().stream().anyMatch(annotation ->
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

  private List<ContextCondition> getContextConditions() throws GeneratorException {
    var cocoClasses = getAnnotatedClasses(RegisterContextCondition.class, "com.philipp_kehrbusch.gen.webdomain.coco");
    var res = new ArrayList<ContextCondition>();

    for (var cocoClass : cocoClasses) {
      var constructorFound = false;
      for (var constructor : cocoClass.getConstructors()) {
        if (constructor.getParameters().length == 0) {
          try {
            var coco = constructor.newInstance();
            if (!(coco instanceof ContextCondition)) {
              throw new GeneratorException("Context condition " + cocoClass.getName() + " must implement ContextCondition");
            }
            res.add((ContextCondition) coco);
            constructorFound = true;
            break;
          } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
          }
        }
      }
      if (!constructorFound) {
        throw new GeneratorException("Context condition " + cocoClass.getName() + " requires a no-argument constructor");
      }
    }
    return res;
  }

  private List<Trafo> getSingleDomainTrafos(String basePackage) {
    return getAnnotatedClasses(SingleDomainTrafo.class, basePackage).stream()
            .map(clazz -> {
              var includes = clazz.getAnnotation(SingleDomainTrafo.class).includeAnnotated();
              var excludes = clazz.getAnnotation(SingleDomainTrafo.class).excludeAnnotated();
              return new Trafo(clazz, includes, excludes);
            })
            .collect(Collectors.toList());
  }

  private List<Trafo> getGlobalDomainTrafos(String basePackage) {
    return getAnnotatedClasses(GlobalDomainTrafo.class, basePackage).stream()
            .map(clazz -> {
              var includes = clazz.getAnnotation(GlobalDomainTrafo.class).includeAnnotated();
              var excludes = clazz.getAnnotation(GlobalDomainTrafo.class).excludeAnnotated();
              return new Trafo(clazz, includes, excludes);
            })
            .collect(Collectors.toList());
  }

  private List<Trafo> getSingleViewTrafos(String basePackage) {
    return getAnnotatedClasses(SingleViewTrafo.class, basePackage).stream()
            .map(clazz -> {
              var includes = clazz.getAnnotation(SingleViewTrafo.class).includeAnnotated();
              var excludes = clazz.getAnnotation(SingleViewTrafo.class).excludeAnnotated();
              return new Trafo(clazz, includes, excludes);
            })
            .collect(Collectors.toList());
  }

  private List<Trafo> getGlobalViewTrafos(String basePackage) {
    return getAnnotatedClasses(GlobalViewTrafo.class, basePackage).stream()
            .map(clazz -> {
              var includes = clazz.getAnnotation(GlobalViewTrafo.class).includeAnnotated();
              var excludes = clazz.getAnnotation(GlobalViewTrafo.class).excludeAnnotated();
              return new Trafo(clazz, includes, excludes);
            })
            .collect(Collectors.toList());
  }

  private Method getTransformMethod(Class<?> trafo) throws TransformMethodNotFoundException {
    for (var method : trafo.getMethods()) {
      if (method.getAnnotation(Transform.class) != null) {
        return method;
      }
    }
    throw new TransformMethodNotFoundException(trafo.getName());
  }

  private <T> void callDomainTransform(Class<?> trafoClass,
                                       Method transform,
                                       T classes,
                                       Class<T> classesClass,
                                       RawViews views,
                                       WebElements elements) throws WebDomainGeneratorException {
    var trafo = getTrafoInstance(trafoClass);
    try {
      var params = new Object[transform.getParameterCount()];
      for (int i = 0; i < transform.getParameterCount(); i++) {
        var paramType = transform.getParameterTypes()[i];
        if (paramType == classesClass) {
          params[i] = classes;
        } else if (paramType == RawViews.class) {
          params[i] = views;
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

  private <T> void callViewTransform(Class<?> trafoClass,
                                     Method transform,
                                     T classes,
                                     Class<T> classesClass,
                                     RawDomains domainClasses,
                                     WebElements elements) throws WebDomainGeneratorException {
    var trafo = getTrafoInstance(trafoClass);
    try {
      var params = new Object[transform.getParameterCount()];
      for (int i = 0; i < transform.getParameterCount(); i++) {
        var paramType = transform.getParameterTypes()[i];
        if (paramType == classesClass) {
          params[i] = classes;
        } else if (paramType == RawDomains.class) {
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
