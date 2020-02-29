package com.philipp_kehrbusch.gen.webdomain.coco;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;

import java.util.Optional;

public interface ContextCondition {

  Optional<String> check(WebDomainParser.ArtifactContext ast);
}
