package com.philipp_kehrbusch.gen.webdomain.util;

public class TypeUtil {

  public static String javaToTypescript(String type) {
    if (type.equals("int") || type.equals("long") || type.equals("float") || type.equals("double")) {
      return "number";
    }
    if (type.equals("String")) {
      return "string";
    }
    return type;
  }
}
