package com.segregator;

import com.segregator.entity.Catalogs;
import com.segregator.entity.FileParameters;
import com.segregator.entity.Format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FileSegregationApp {
    static FileParameters fileParameters = new FileParameters();
    static final String FILE_PATH = "C:\\Users\\admin\\Desktop\\folder\\" + Catalogs.home;

    public static void main(String[] args) throws IOException {
        getFiles(fileParameters);
    }


    private static List<String> getFiles(FileParameters fileParameters) throws IOException {
        try (Stream<Path> paths = getFilePaths(String.valueOf(getDirectory(fileParameters)))) {
            Catalogs catalogs = fileParameters.getCatalogs();
            List<String> filesForCatalog = getFilesForCatalog(String.valueOf(catalogs), paths);
            System.out.println(filesForCatalog.size());
            return filesForCatalog;
        }
    }

    static Stream<Path> getFilePaths(String localDirectory) throws IOException {
        return Files.walk(Paths.get(localDirectory));
    }

    private static Path getDirectory(FileParameters fileParameters) throws IOException {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) {
            if (fileParameters.getFormat() == Format.jar || fileParameters.getFormat() == Format.xml) {
                Path pathForFiles = Paths.get(FILE_PATH + Catalogs.dev);
                return Files.move(path, pathForFiles);
            }
            if (fileParameters.getFormat() == Format.jar && fileParameters.getTimeCreated().getHour() % 2 == 0) {
                Path filePathForJAR = Paths.get(FILE_PATH + Catalogs.test);
                return Files.move(path, filePathForJAR);
            }
        }
        return null;
    }

    static List<String> getFilesForCatalog(String endings, Stream<Path> path) {
        return path.filter(Files::isRegularFile)
                .map(Path::toString)
                .filter(f -> f.endsWith(endings))
                .collect(Collectors.toList());
    }
}
