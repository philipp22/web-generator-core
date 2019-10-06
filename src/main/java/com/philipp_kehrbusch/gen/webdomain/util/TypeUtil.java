package com.philipp_kehrbusch.gen.webdomain.util;

import java.util.regex.Pattern;

public class TypeUtil {

  public static String javaToTypescript(String type) {
    if (type.equals("int") || type.equals("long") || type.equals("float") || type.equals("double")) {
      return "number";
    }
    if (type.equals("String")) {
      return "string";
    }
    if (type.matches("List<.*>")) {
      String regex = "List<(.*)>";
      type = type.replaceAll(regex, "$1");
      return javaToTypescript(type) + "[]";
    }
    return type;
  }
}
