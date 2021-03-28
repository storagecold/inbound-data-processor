package com.cold.storage.inbound.data.processor.step;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RenameFileStep {

    @Value("${extension.file.trig}")
    String trigFileExtension;

    @Value("${extension.file.lock}")
    String lockFileExtension;

    @Value("${extension.file.error}")
    String errFileExtension;

    private File renameFile(File file, String fromExt, String toExt) {
        boolean fileRenamed = false;
        String fromFilePath = file.getAbsolutePath();
        String toFilePath = null;

        if (null != fromFilePath && fromFilePath.length() > 0) {
            if (fromFilePath.endsWith(fromExt)) {
                int index = fromFilePath.lastIndexOf(fromExt);
                toFilePath = fromFilePath.substring(0, index) + toExt;
                File renamedFile = new File(toFilePath);

                if (renamedFile.exists()) {
                    renamedFile.delete();
                }

                if (file.renameTo(renamedFile)) {
                    fileRenamed = true;
                }
            }
        }

        if (fileRenamed) {
            File lockFile = new File(toFilePath);
            if (lockFile.exists()) {
                return lockFile;
            }
        }

        return null;
    }

    public File renameTrigExtToLock(File file) {
        return renameFile(file, trigFileExtension, lockFileExtension);
    }

    public File renameLockExtToTrig(File file) {
        return renameFile(file, lockFileExtension, trigFileExtension);
    }

    public File renameTrigExtToErr(File file) {
        return renameFile(file, trigFileExtension, errFileExtension);
    }

    public File renameLockExtToErr(File file) {
        return renameFile(file, lockFileExtension, errFileExtension);
    }
}
