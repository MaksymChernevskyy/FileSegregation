package com.segregator;

import java.io.File;

public class CreateCatalogs {


  private boolean createCatalog(String path) {
    File file = new File(path);
    if (!file.exists()) {
      if (file.mkdir()) {
        System.out.println("Created catalog " + path);
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
