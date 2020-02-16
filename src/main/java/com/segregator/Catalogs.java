package com.segregator;

public enum Catalogs {
  HOME("HOME"),
  DEV("DEV"),
  TEST("TEST");

  public String path;

  Catalogs(String path) {
    this.path = path;
  }
}
