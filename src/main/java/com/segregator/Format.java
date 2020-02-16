package com.segregator;

public enum Format {
  JAR("jar"),
  XML("xml");

  public String format;

  Format(String format) {
    this.format = format;
  }
}
