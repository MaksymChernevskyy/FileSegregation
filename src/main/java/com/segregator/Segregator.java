package com.segregator;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.util.List;

class Segregator {

  void runSegregator() {
    Path path = Paths.get(Catalogs.HOME.path);
    while (true) {
      try {
        WatchService watcher = path.getFileSystem().newWatchService();
        path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        WatchKey watchKey = watcher.take();
        getEventKind(path, watchKey);

      } catch (Exception e) {
        System.out.println("Error: " + e.toString());
      }
    }
  }

  private void getEventKind(Path directory, WatchKey watchKey) throws IOException {
    List<WatchEvent<?>> events = watchKey.pollEvents();
    for (WatchEvent event : events) {
      Path source = Paths.get(event.context().toString());
      if (event.kind() == ENTRY_CREATE) {
        System.out.println("Created: " + event.context().toString());
        segregate(source, directory);
      }
      if (event.kind() == ENTRY_DELETE) {
        System.out.println("Delete: " + event.context().toString());
      }
      if (event.kind() == ENTRY_MODIFY) {
        System.out.println("Modify: " + event.context().toString());
        segregate(source, directory);
      }
    }
  }

  private void segregate(Path sourceFile, Path sourceDirectory) throws IOException {
    Path sourcePath = sourceDirectory.resolve(sourceFile);
    System.out.println("source " + sourceFile);
    String format = getFormat(sourceFile.toString());
    System.out.println("format " + format);
    String path = getPath(sourceFile, sourcePath, format);
    Path destPath = getPath(path);
    moveFiles(sourceFile, sourcePath, destPath);
  }

  private String getPath(Path sourceFile, Path sourcePath, String format) {
    String path = null;
    if (Format.JAR.format.equals(format)) {
      BasicFileAttributes attr = null;
      try {
        attr = Files.readAttributes(sourcePath, BasicFileAttributes.class);
      } catch (IOException e) {
        System.out.print("Couldn't get file attributes" + sourceFile.toString() + " " + e);
      }
      FileTime creationTime = attr.creationTime();
      LocalDateTime time = LocalDateTime.now();
      System.out.print("time " + time + " " + creationTime);
      if (time.getHour() % 2 == 0 || Format.XML.format.equals(format)) {
        path = Catalogs.DEV.path;
      } else {
        path = Catalogs.TEST.path;
      }
    }
    if (null == path) {
      return null;
    }
    return path;
  }

  private void moveFiles(Path sourceFile, Path sourcePath, Path destPath) throws IOException {
    Files.move(sourcePath, destPath.resolve(sourceFile.getFileName()), REPLACE_EXISTING);
  }

  private Path getPath(String path) {
    return Paths.get(path);
  }

  private String getFormat(String path) {
    if (null == path) {
      return null;
    }
    int i = path.lastIndexOf('.');
    if (i >= 0) {
      return path.substring(i + 1);
    }
    return null;
  }
}
