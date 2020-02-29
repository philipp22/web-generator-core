package com.philipp_kehrbusch.gen.webdomain.coco;

import com.philipp_kehrbusch.gen.webdomain.WebDomainParser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;

@RegisterContextCondition
public class RestMethodOptionCoCo implements ContextCondition {

  private static List<String> validOptionNames = Arrays.asList(
          "queryParams"
  );

  public RestMethodOptionCoCo() {
  }

  private String getErrorMessage(String domain, WebDomainParser.RestMethodOptionContext restOptions) {
    return "Domain " + domain + ": Unknown REST method option: '" + restOptions.optionName.getText();
  }

  @Override
  public Optional<String> check(WebDomainParser.ArtifactContext ast) {
    for (var domain : ast.domain()) {
      if (domain.rest() != null) {
        for (var restMethod : domain.rest().restMethod()) {
          if (restMethod.restMethodOptions() != null) {
            for (var option : restMethod.restMethodOptions().restMethodOption()) {
              if (!validOptionNames.contains(option.optionName.getText())) {
                return of(getErrorMessage(domain.name.getText(), option));
              }
            }
          }
        }
      }
    }
    return Optional.empty();
  }
}
