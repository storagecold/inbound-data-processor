package com.cold.storage.inbound.data.processor.service;

import java.io.File;

public interface Service {
    boolean copyFile(File file, String dest);

    File getTrigFile(File srcFile);

    File getDataFile(File trigFile);

    boolean moveFile(File file, String dest);

    String getFileType(File file);

    void moveIncorrectFileToErrorDirectory(File file);

    String getSubmitterIdFromFile(File file) throws Exception;

    boolean moveFileToArchiveDirectory(File file);
}