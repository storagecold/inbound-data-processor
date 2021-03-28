package com.cold.storage.inbound.data.processor.step;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLockStep {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private File file;
    FileLock lock;

    public FileLockStep(File f) {

        file = f;
        lock = null;
    }

    public boolean lock() {

        boolean lockObtained = false;

        try (FileChannel channel = new RandomAccessFile(file, "rw").getChannel()) {
            lock = channel.tryLock();

            if (null != lock && lock.isValid()) {
                logger.info("Lock obtained on file : " + file);
                lockObtained = true;
            }
        } catch (FileNotFoundException ex) {
            logger.error("File %s was not found when attempting to place a lock on it", file.getAbsolutePath());
        } catch (OverlappingFileLockException | IOException ex) {
            logger.error("Exception occurred while attempting to place a lock on file (%s), exception message : %s", file.getAbsolutePath(), ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } finally {
            if (lock != null && lock.isValid()) {
                logger.info("Releasing the lock placed on the file : " + file);
                try {
                    lock.release();
                    logger.info("Lock placed on file (%s) has been released", file);
                } catch (IOException e) {
                    logger.error("IOException encountered when release lock placed on file : " + file);
                    logger.error(e.getMessage(), e);
                }
            }
        }

        return lockObtained;
    }

    public boolean isLocked() {
        boolean flag = false;

        if (lock != null && lock.isValid()) {
            flag = true;
        }

        return flag;
    }

    public boolean unlock() {

        boolean lockReleased = false;

        if (lock != null && lock.isValid()) {
            logger.info("Releasing the lock placed on the file : " + file);
            try {
                lock.release();
                lockReleased = true;
                logger.info("Lock placed on file (%s) has been released", file);
            } catch (IOException e) {
                logger.error("IOException encountered when release lock placed on file : " + file);
                logger.error(e.getMessage(), e);
            }
        }


        return lockReleased;
    }
}
