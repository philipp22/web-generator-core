package com.philipp_kehrbusch.gen.webdomain.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportUtil {

  public static List<String> getDefaultImports() {
    return new ArrayList<>(Arrays.asList(
            "java.util.*",
            "java.time.*"
    ));
  }
}
