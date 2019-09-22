# web-generator-core

ANTLR- and template-based generator library to generate artifacts from textual class diagram files.

## Workflow

1. Class diagram models are parsed using ANTLR
1. DomainAST (AST for storing domain class information per domain class) is constructed
1. AST transformations from DomainAST to TargetAST are performed
  * Concrete generator implemenation registers transformers using an annotation-based mechanism
  * Transformers are recognized and picked up from the classpath by the generator library
  * Each transformer receives the DomainAST content as input (either all domain classes at once or one by one) and registers new elements in the TargetAST
  * TargetAST represents the concrete structure of the target system, i.e. which classes, enums, interfaces etc. exist
4. Artifacts are generated from TargetAST based on default templates or, if registered during the transformation process, using specific templates for each TargetAST component
