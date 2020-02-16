package com.segregator;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateCatalogs {
  private Logger log = LoggerFactory.getLogger(CreateCatalogs.class);

  private boolean createCatalog(String path) {
    File file = new File(path);
    if (!file.exists()) {
      if (file.mkdir()) {
        log.info("Created catalog " + path);
      }
    }
    return true;
  }

  void createCatalogsStructure() {
    for (Catalogs catalogs : Catalogs.values()) {
      if (!createCatalog(catalogs.path)) {
        return;
      }
    }
  }
}
