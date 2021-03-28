package com.cold.storage.inbound.data.processor.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class TestUtils {
    private static TestUtils instance = null;

    private TestUtils() {
    }

    public static TestUtils getInstance() {
        if (instance == null) {
            instance = new TestUtils();
        }
        return instance;
    }

    public void copyFile(String srcDir, String destDir, String file) throws IOException {
        Path src = Paths.get(srcDir + file);
        Path target = Paths.get(destDir + file);
        Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean containsFile(File[] files, String fileName) {
        boolean isFileAvailable;
        isFileAvailable = Stream.of(files)
                .anyMatch($ -> $.getName().equals(fileName));
        return isFileAvailable;
    }

    public File[] getFiles(String path) {
        File dir = new File(path);
        return dir.listFiles();
    }
}
