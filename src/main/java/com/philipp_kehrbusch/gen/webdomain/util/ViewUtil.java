package com.philipp_kehrbusch.gen.webdomain.util;

import com.philipp_kehrbusch.gen.webdomain.source.domain.RawAttribute;
import com.philipp_kehrbusch.gen.webdomain.source.domain.RawDomain;
import com.philipp_kehrbusch.gen.webdomain.source.exceptions.DomainNotFoundException;
import com.philipp_kehrbusch.gen.webdomain.source.exceptions.InvalidViewAttributePathException;
import com.philipp_kehrbusch.gen.webdomain.source.view.RawView;
import com.philipp_kehrbusch.gen.webdomain.source.view.RawDerivedAttribute;
import com.philipp_kehrbusch.gen.webdomain.trafos.RawDomains;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ViewUtil {

  public static String[] getAttributePathSplitted(RawDerivedAttribute attribute) {
    return attribute.getPath().split("\\.");
  }

  public static String joinAttributePathElements(RawDerivedAttribute attribute) {
    var splitted = getAttributePathSplitted(attribute);
    return String.join("", Arrays.stream(splitted)
            .map(StringUtil::firstUpper)
            .collect(Collectors.toList()));
  }

  public static String[] getAttributePathSplittedWithoutDomain(RawDerivedAttribute attribute) {
    var splitted = getAttributePathSplitted(attribute);
    return Arrays.copyOfRange(splitted, 1, splitted.length);
  }

  public static String getAttributeName(RawDerivedAttribute attribute) {
    var origName = getAttributeOrigName(attribute);
    return attribute.getAlias() != null ? attribute.getAlias() : origName;
  }

  public static String getAttributeOrigName(RawDerivedAttribute attribute) {
    var pathSplitted = getAttributePathSplitted(attribute);
    return pathSplitted[pathSplitted.length - 1];
  }

  public static String getAttributeType(RawDomains domains, RawView view, RawDerivedAttribute attribute)
          throws InvalidViewAttributePathException, DomainNotFoundException {

//    var attrOrigName = getAttributeOrigName(attribute);
//    var domainAlias = getAttributePathSplitted(attribute)[0];
//    var domainName = view.getViewFroms().entrySet().stream()
//            .filter(entry -> entry.getValue().equals(domainAlias))
//            .map(Map.Entry::getKey)
//            .findFirst()
//            .orElseThrow(() -> new InvalidViewAttributePathException(view.getName(), attribute.getPath()));
//
//    var domain = domains.stream()
//            .filter(d -> d.getName().equals(domainName))
//            .findFirst()
//            .orElseThrow(() -> new DomainNotFoundException(domainName));
//
//    return attrOrigName.equals("id") ? "long" : domain.getAttributes().stream()
//            .filter(attr -> attr.getName().equals(attrOrigName))
//            .findFirst()
//            .orElseThrow(() -> new InvalidViewAttributePathException(view.getName(), attribute.getPath(),
//                    "Domain " + domain.getName() + " does not have an attribute named '" + attrOrigName + "'"))
//            .getType();
    var splitted = getAttributePathSplitted(attribute);
    return getAttributePathElementType(domains, view, attribute, splitted[splitted.length - 1]);
  }

  public static String getAttributeType(RawDomains domains, String domain, String attribute)
          throws InvalidViewAttributePathException, DomainNotFoundException {
    return domains.stream()
            .filter(d -> d.getName().equals(domain))
            .findFirst()
            .orElseThrow(() -> new DomainNotFoundException(domain))
            .getAttributes()
            .stream()
            .filter(attr -> attr.getName().equals(attribute))
            .findFirst()
            .map(RawAttribute::getType)
            .or(() -> {
              if (attribute.equals("id")) {
                return Optional.of("long");
              } else {
                return Optional.empty();
              }
            })
            .orElseThrow(() -> new InvalidViewAttributePathException("View", domain + "." + attribute, "Attribute not found"));
  }

  public static String getAttributePathElementType(RawDomains domains, RawView view, RawDerivedAttribute attribute, String pathElement)
          throws InvalidViewAttributePathException, DomainNotFoundException {
    var pathElements = getAttributePathSplitted(attribute);
    var currentType = getAttributeSourceDomain(domains, view, attribute).getName();

    if (pathElement.equals(pathElements[0])) {
      return currentType;
    }
    var isList = false;
    for (var pe : Arrays.copyOfRange(pathElements, 1, pathElements.length)) {
      currentType = getAttributeType(domains, TypeUtil.getPrimitiveType(currentType), pe);
      isList = isList || currentType.startsWith("List<");

      if (pe.equals(pathElement)) {
        break;
      }
    }
    if (isList && !currentType.startsWith("List<")) {
      currentType = "List<" + StringUtil.firstUpper(currentType) + ">";
    }
    return currentType;
  }

  public static RawDomain getAttributeSourceDomain(RawDomains domains, RawView view, RawDerivedAttribute attribute)
          throws DomainNotFoundException, InvalidViewAttributePathException {
    var domainAlias = getAttributePathSplitted(attribute)[0];
    var domainName = view.getViewFroms().entrySet().stream()
            .filter(entry -> entry.getValue().equals(domainAlias))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow(() -> new InvalidViewAttributePathException(view.getName(), attribute.getPath()));

    return domains.stream()
            .filter(d -> d.getName().equals(domainName))
            .findFirst()
            .orElseThrow(() -> new DomainNotFoundException(domainName));
  }

  public static boolean isList(RawDomains domains, RawView view, RawDerivedAttribute attribute)
          throws InvalidViewAttributePathException, DomainNotFoundException {
    return getAttributeType(domains, view, attribute).startsWith("List<");
  }
}
