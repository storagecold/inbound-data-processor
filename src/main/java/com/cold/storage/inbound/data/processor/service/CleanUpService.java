package com.cold.storage.inbound.data.processor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.Date;
import java.util.Objects;

//@Service
public class CleanUpService {

    @Value("${archive}")
    private String archive;

    @Value("${log}")
    private String log;

    @Value("${logRetention}")
    private int logRetention;

    private long currentTime;

    @Scheduled(cron = "0 0 0 ? * ?")
    public void startCleanUp() {
        currentTime = new Date().getTime();
        System.out.println("Starting Cleanup at :" + currentTime);

        //clean inbound
        listEligibleFilesToDelete(archive, logRetention);

        //clean  log Files
        listEligibleFilesToDelete(log, logRetention);
    }

    private void listEligibleFilesToDelete(String dirPath, int fileRetention) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (Objects.requireNonNull(files).length > 0) {
            for (File file : files) {
                deleteFile(file, fileRetention);
            }
        }
    }

    private void deleteFile(File file, int daysToRetainFile) {
        long deleteBeforeTime = currentTime - file.lastModified();
        long timeToRetainFile = daysToRetainFile * 24 * 60 * 60 * 1000;
        boolean isFileDeleted = false;

        if (file.isFile() && file.exists()) {
            if (deleteBeforeTime > timeToRetainFile) {
                try {
                    isFileDeleted = file.delete();
                } catch (Exception ex) {
                    System.out.println(String.format("could not delete file: %s got Exception %s", file.getName(), ex));
                }
                if (isFileDeleted) {
                    if (file.delete()) System.out.println("file deleted: " + file.getName());
                } else {
                    System.out.println(String.format("could not delete file: %s", file.getName()));
                }
            } else {
                System.out.println(String.format("file: %s is newer than %s days so not deleted.", file.getName(), daysToRetainFile));
            }
        } else {
            System.out.println(String.format("%s : is not file", file.getName()));
        }
    }
}
