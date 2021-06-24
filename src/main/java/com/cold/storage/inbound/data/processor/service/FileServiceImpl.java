package com.cold.storage.inbound.data.processor.service;

import com.cold.storage.inbound.data.processor.utils.Constants;
import com.cold.storage.inbound.data.processor.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@org.springframework.stereotype.Service
public class FileServiceImpl implements FileService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PropertiesUtil propertiesUtil;

    @Override
    public boolean copyFile(File file, String dest) {
        boolean isFileCopied = false;
        Path src = null;
        Path target = null;

        if (Files.isDirectory(target)) {
            target = Paths.get(dest + file.getName());
        } else {
            log.error(String.format("%s is not a directory", target));
        }

        if (Objects.nonNull(src) && Objects.nonNull(target)) {
            try {
                Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING);
                isFileCopied = true;
                log.info(String.format("File copied succesfully  : %s ", file.getAbsolutePath()));
            } catch (IOException e) {
                log.error(String.format("Error in Method : copyFile, could not copy file: %s", file.getName()));
                isFileCopied = false;
            }
        } else {
            log.error("src or target is not valid");
            isFileCopied = false;
        }
        return isFileCopied;
    }

    public File getTrigFile(File srcFile) {
        String trig = Constants.DOT_TRIG_EXT;
        String fp = "";
        try {
            fp = srcFile.getAbsolutePath() + trig;
        } catch (Exception ex) {
            log.info(String.format("File is Null %s", srcFile.getName()));
        }
        return new File(fp);
    }

    @Override
    public File getDataFile(File trigFile) {
        String trigFilePath = trigFile.getAbsolutePath();
        String dataFilePath = trigFilePath.substring(0, trigFilePath.lastIndexOf(Constants.DOT_TRIG_EXT));
        return new File(dataFilePath);
    }

    @Override
    public boolean moveFile(File file, String dest) throws IOException {
        boolean isFileMoved = false;

        String fileName = file.getName();
        dest = dest.trim();

        File destDirectory = new File(dest);
        if (destDirectory.exists()) {
            File destFile = new File(dest + File.separator + fileName);
            if (destFile.exists()) {
                destFile.delete();
            }
            InputStream targetStream = new FileInputStream(file);
            targetStream.close();

            if (file.renameTo(destFile)) {
                log.info(String.format("File %s successfully moved to : %s ", fileName, dest));
                isFileMoved = true;
            } else {
                log.error(String.format("File %s failed to move to %s: ", fileName, dest));
                isFileMoved = false;
            }
        } else {
            log.error(String.format("Destination directory %s does not exists: ", dest));
        }
        return isFileMoved;
    }

    @Override
    public String getFileType(File file) {
        String fileType = null;
        if (file.getName().endsWith(Constants.MDB_TRIG_EXT)) {
            fileType = Constants.MDB_EXT;
        }
        return fileType;
    }
}
