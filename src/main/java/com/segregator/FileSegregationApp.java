package com.segregator;

public class FileSegregationApp {

  public static void main(String[] args) {
    CreateCatalogs catalogs = new CreateCatalogs();
    catalogs.createCatalogsStructure();
    Segregator segregator = new Segregator();
    segregator.runSegregator();
  }
}
