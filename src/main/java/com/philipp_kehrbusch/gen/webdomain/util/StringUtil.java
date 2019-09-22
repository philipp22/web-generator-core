package com.philipp_kehrbusch.gen.webdomain.util;

public class StringUtil {
  public static String firstUpper(String name) {
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }

  public static String firstLower(String name) {
    return name.substring(0, 1).toLowerCase() + name.substring(1);
  }
}
