package com.philipp_kehrbusch.gen.webdomain.util;

public class TypeUtil {

  public static String javaToTypescript(String type) {
    if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("long") ||
            type.equalsIgnoreCase("float") || type.equalsIgnoreCase("double") ||
            type.equalsIgnoreCase("Integer")) {
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

  public static String javaToDart(String type) {
    switch (type) {
      case "boolean":
        return "bool";
      case "long":
      case "Long":
        return "int";
      case "List<Long>":
        return "List<int>";
      case "float":
      case "Float":
        return "double";
      case "List<Float>":
        return "List<double>";
      default:
        return type;
    }
  }

  public static String getDefaultComparisonString(String type, String name) {
    switch (type) {
      case "long":
      case "int":
      case "float":
      case "double":
        return name + " != 0";
      case "boolean":
        return name;
      default:
        return name + " != null";
    }
  }

  public static String getDefaultValueString(String type, String name) {
    switch (type) {
      case "long":
      case "int":
      case "float":
      case "double":
        return "0";
      case "boolean":
        return "false";
      default:
        return "null";
    }
  }

  public static String getPrimitiveType(String type) {
    String regex = "List<(.*)>";
    return type.replaceAll(regex, "$1");
  }

}
