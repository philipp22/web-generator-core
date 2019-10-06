# web-generator-core

ANTLR- and template-based generator library to generate artifacts from textual class diagram files.

## Concepts

### Workflow

1. Class diagram models are parsed using ANTLR
1. DomainAST (AST for storing domain class information per domain class) is constructed
1. AST transformations from DomainAST to TargetAST are performed
  * Concrete generator implemenation registers transformers using an annotation-based mechanism
  * Transformers are recognized and picked up from the classpath by the generator library
  * Each transformer receives the DomainAST content as input (either all domain classes at once or one by one) and registers new elements in the TargetAST
  * TargetAST represents the concrete structure of the target system, i.e. which classes, enums, interfaces etc. exist
4. Artifacts are generated from TargetAST based on default templates or, if registered during the transformation process, using specific templates for each TargetAST node

### Generator Infrastructure

A concrete generator implementation first registers a set of targets. Each target represents a sub-system to be generated and contains a base path and a target language. Transformers are then registered and called for a specific target.

### Transfomers

Two types of AST transformers exist: `@SingleTrafo` and `@GlobalTrafo`. The first is called for each domain class from the model, while the latter is called once for all domain classes.

The following illustrates an example implementation registration of such a transformer (in this case for a DAO):

```java

@GlobalTrafo
public class DAOTrafo {

  @Transform
  public void transform(List<CDClass> allDomains, List<WebElement> elements, GeneratorSettings settings) {
    var domains = TrafoUtils.getDomains(allDomains);
    domains.forEach(domain -> {
      var imports = ImportUtil.getDefaultImports();
      imports.add(ImportPaths.getDomainImport(settings.getBasePackage(Targets.BACKEND)));
      imports.add(ImportPaths.getRTEImport());
      imports.add("org.springframework.data.jpa.repository.JpaRepository");

      var name = domain.getName() + "DAO";
      elements.add(new WebElement(Targets.BACKEND, name, "dao", imports,
              new CDArtifactBuilder()
                      .name(name)
                      .addInterface(new CDInterfaceBuilder()
                              .addModifier("public")
                              .name(name)
                              .addSuperInterface("IDAO")
                              .addSuperInterface("JpaRepository<" + domain.getName() + ", Long>")
                              .addMethods(createGetByMethods(domain))
                              .build())
                      .build()));
    });
  }

  private List<CDMethod> createGetByMethods(CDClass domain) {
    return domain.getAttributes().stream()
            .filter(attr -> attr.getAnnotations().contains("@GetOneBy"))
            .map(attr -> new CDMethodBuilder()
                    .name("getBy" + StringUtil.firstUpper(attr.getName()))
                    .returnType(domain.getName())
                    .addArgument(new CDArgumentBuilder()
                            .type(attr.getType())
                            .name(attr.getName())
                            .build())
                    .build())
            .collect(Collectors.toList());
  }
}
```

Builders are supplied for the TargetAST to simplify AST construction.

## Build

To build the project, run `mvn clean install`.
