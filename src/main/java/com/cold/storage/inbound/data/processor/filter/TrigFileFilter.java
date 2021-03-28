package com.cold.storage.inbound.data.processor.filter;

import com.cold.storage.inbound.data.processor.utility.Constants;

import java.io.File;
import java.io.FileFilter;
import java.util.Locale;

public class TrigFileFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        boolean isTrigFileAvailable = false;
        if (file.getName().toLowerCase(Locale.US).endsWith(Constants.DOT_TRIG_EXT)) {
            isTrigFileAvailable = true;
        }
        return isTrigFileAvailable;
    }
}
