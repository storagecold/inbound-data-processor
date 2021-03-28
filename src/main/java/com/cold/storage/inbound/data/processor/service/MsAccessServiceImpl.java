package com.cold.storage.inbound.data.processor.service;

import com.cold.storage.inbound.data.processor.repo.inbound.InboundRepo;
import com.cold.storage.inbound.data.processor.utility.Constants;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@Service
public class MsAccessServiceImpl implements MsAccessService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    InboundRepo inboundRepo;

    @Override
    public void readMsAccessFile(File msAccessFile) {
        try {
            Database database = Database.open(msAccessFile);
            Table amadTable = database.getTable(Constants.AMADNO);
            inboundRepo.loadAmad(amadTable);
        } catch (IOException ex) {
            logger.error("failed to read msAccessDataBase %s: ", msAccessFile.getName());
        }
    }

    @Override
    public Set<String> getAllTables(Database database) {
        String dbName = "";
        Set<String> allTables = null;
        try {
            allTables = database.getTableNames();
            dbName = database.getDatabaseProperties().getName();
        } catch (IOException ex) {
            logger.error("failed to read msAccessDataBase %s: ", dbName);
        }
        return allTables;
    }
}
