package com.philipp_kehrbusch.gen.webdomain.util;

import com.philipp_kehrbusch.gen.webdomain.source.domain.RawRestMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RestUtil {

  public static Map<String, String> getRouteVariables(String route) {
    var pattern = Pattern.compile("\\{([^\\s]+) ([^\\s]+)}");
    var matcher = pattern.matcher(route);
    var res = new HashMap<String, String>();

    while (matcher.find()) {
      res.put(matcher.group(2), matcher.group(1));
    }
    return res;
  }

  public static String createRestUrl(RawRestMethod restMethod) {
    var queryString = restMethod.getQueryParams().size() > 0 ?
            "+ \"?" + String.join("&",
                    restMethod.getQueryParams().entrySet().stream()
                            .map(entry -> entry.getKey() + "=\" + " + entry.getKey() + "+ \"")
                            .collect(Collectors.toList())) + "\""
            : "";
    return "baseUrl + \"" + restMethod.getRoute().replaceAll("\\{([^\\s]+) ([^\\s]+)}", "\" + $2 + \"") +
            "\"" + queryString;
  }

  public static String getBodyName(RawRestMethod restMethod) {
    var bodyName = restMethod.getBodyTypeName();
    if (bodyName == null) {
      bodyName = StringUtil.firstLower(restMethod.getBodyType());
    }
    return bodyName;
  }
}
