package com.segregator;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class Segregator {
  private Logger log = LoggerFactory.getLogger(Segregator.class);
  private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  private WriteInFile writeInFile = new WriteInFile();

  void runSegregation() {
    Path path = Paths.get(Catalogs.HOME.path);
    while (true) {
      try {
        WatchService watcher = path.getFileSystem().newWatchService();
        path.register(watcher, ENTRY_CREATE);
        WatchKey watchKey = watcher.take();
        getEventKind(path, watchKey);

      } catch (Exception e) {
        log.error("Error: " + e.toString());
      }
    }
  }

  private void getEventKind(Path directory, WatchKey watchKey) throws IOException {
    List<WatchEvent<?>> events = watchKey.pollEvents();
    for (WatchEvent event : events) {
      Path source = Paths.get(event.context().toString());
      if (event.kind() == ENTRY_CREATE) {
        log.info("Created: " + event.context().toString() + " " + writeInFile.filesCount.incrementAndGet());
        segregate(source, directory);
      }
    }
  }

  private void segregate(Path sourceFile, Path sourceDirectory) throws IOException {
    Path sourcePath = sourceDirectory.resolve(sourceFile);
    log.info("source " + sourceFile);
    String format = getFormat(sourceFile.toString());
    log.info("format " + format);
    String path = getPath(sourceFile, sourcePath, format);
    Path destPath = getPath(path);
    moveFiles(sourceFile, sourcePath, destPath);
  }

  private String getPath(Path sourceFile, Path sourcePath, String format) {
    String path = null;
    try {
      writeInFile.writeInFileAndCount();
      BasicFileAttributes basicFileAttributes = Files.readAttributes(sourcePath, BasicFileAttributes.class);
      if (Format.JAR.format.equals(format)) {
        writeInFile.jarFilesCount.incrementAndGet();
        FileTime creationTime = getFileTime(basicFileAttributes);
        if (creationTime.to(TimeUnit.HOURS) % 2 == 0) {
          path = Catalogs.DEV.path;
        } else {
          path = Catalogs.TEST.path;
        }
      }
      if (Format.XML.format.equals(format)) {
        writeInFile.xmlFilesCount.incrementAndGet();
        path = Catalogs.DEV.path;
      }
    } catch (IOException e) {
      log.info("Couldn't get file attributes" + sourceFile.toString() + " " + e);
    }
    return path;
  }

  private FileTime getFileTime(BasicFileAttributes basicFileAttributes) {
    FileTime creationTime = basicFileAttributes.creationTime();
    String dateCreated = dataFormat.format(creationTime.toMillis());
    log.info("Created time " + dateCreated);
    return creationTime;
  }

  private void moveFiles(Path sourceFile, Path sourcePath, Path destPath) throws IOException {
    Files.move(sourcePath, destPath.resolve(sourceFile.getFileName()), REPLACE_EXISTING);
  }

  private Path getPath(String path) {
    return Paths.get(path);
  }

  private String getFormat(String path) {
    if (path == null) {
      return null;
    }
    int i = path.lastIndexOf('.');
    if (i >= 0) {
      return path.substring(i + 1);
    }
    return null;
  }
}
