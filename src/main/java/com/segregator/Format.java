package com.segregator;

public enum Format {
  JAR("jar"),
  XML("xml");

  public String format;

  Format(String extension) {
    this.format = extension;
  }
}
