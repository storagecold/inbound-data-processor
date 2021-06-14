package com.cold.storage.inbound.data.processor.repository;

import java.io.File;

public interface FileDetailRepo {

    void insertFileDetail(File file);

    int updateFileDetail(int fileId, String status);
}
