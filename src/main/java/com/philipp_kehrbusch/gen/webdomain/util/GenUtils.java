package com.philipp_kehrbusch.gen.webdomain.util;

import com.philipp_kehrbusch.gen.webdomain.Target;
import com.philipp_kehrbusch.gen.webdomain.target.WebElement;

import java.io.File;
import java.nio.file.Paths;

public class GenUtils {

  public static File getGenPath(Target target, WebElement element, boolean create) {
    var path = Paths.get(target.getBasePath(), target.getBasePackage().replace(".", "/"),
            element.getPath());
    var parentDir = path.toFile();
    if (!parentDir.exists() && create) {
      parentDir.mkdirs();
    }
    element.setBasePath(target.getBasePackage());
    return new File(Paths.get(path.toString(), target.getResolver().resolve(element.getName())).toString());
  }
}
