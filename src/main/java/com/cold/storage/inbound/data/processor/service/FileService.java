package com.cold.storage.inbound.data.processor.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {
    boolean copyFile(File file, String dest);

    File getTrigFile(File srcFile);

    File getDataFile(File trigFile);

    boolean moveFile(File file, String dest) throws IOException;

    String getFileType(File file);
}