package com.cold.storage.inbound.data.processor.service;

import com.healthmarketscience.jackcess.Database;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface MsAccessService {
    void readMsAccessFile(File file) throws IOException;

    Set<String> getTableNames(Database database);

}
