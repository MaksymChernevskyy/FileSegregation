package com.segregator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

class WriteInFile {
  AtomicLong filesCount = new AtomicLong(0L);
  AtomicLong jarFilesCount = new AtomicLong(0L);
  AtomicLong xmlFilesCount = new AtomicLong(0L);

  void writeInFileAndCount() {
    try {
      FileWriter writer = new FileWriter(Catalogs.HOME + "/" + "CountingFiles.txt");
      writer.write("Files count " + filesCount + "\n");
      writer.write("Jar count " + jarFilesCount + "\n");
      writer.write("Xml count " + xmlFilesCount);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
