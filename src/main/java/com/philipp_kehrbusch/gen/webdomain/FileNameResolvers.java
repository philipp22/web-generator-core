package com.philipp_kehrbusch.gen.webdomain;

import com.google.common.base.CaseFormat;

import java.util.Arrays;
import java.util.List;

public class FileNameResolvers {
  public static final FileNameResolver JAVA = name -> name + ".java";
  public static final FileNameResolver TYPESCRIPT = typescriptResolver(Arrays.asList(
          "service",
          "reducer",
          "effects",
          "actions",
          "selectors",
          "state",
          "form"
  ));

  public static FileNameResolver typescriptResolver(List<String> groups) {
    return name -> {
      for (var group : groups) {
        if (name.toLowerCase().endsWith(group)) {
          var index = name.toLowerCase().lastIndexOf(group);
          return CaseFormat.UPPER_CAMEL.to(
                  CaseFormat.LOWER_HYPHEN, name.substring(0, index)) +
                  "." + group.toLowerCase() + ".ts";
        }
      }
      return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, name) + ".ts";
    };
  }
}
