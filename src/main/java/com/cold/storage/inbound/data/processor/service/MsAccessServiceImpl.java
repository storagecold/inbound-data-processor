package com.cold.storage.inbound.data.processor.service;

import com.cold.storage.inbound.data.processor.repository.*;
import com.cold.storage.inbound.data.processor.utils.Constants;
import com.cold.storage.inbound.data.processor.utils.Utils;
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
    Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String FAILED_TO_READ_MSACCESS_DB = "failed to read msAccessDataBase %s: ,Ex: %s";

    @Autowired
    GrpRepo grpRepo;

    @Autowired
    PartyAccRepo partyAccRepo;

    @Autowired
    AmadRepo amadRepo;

    @Autowired
    PartyLedgerRepo partyLedgerRepo;

    @Autowired
    ColdInfoRepo coldInfoRepo;

    @Autowired
    FileDetailRepo fileDetailRepo;

    @Override
    public void readMsAccessDB(File msAccessFile) throws IOException {
        Database database = null;
        try {
            //insert file processing detail.
            fileDetailRepo.insertFileDetail(msAccessFile);
            database = Database.open(msAccessFile);

            String SubmitterId = Utils.getSubmitter(msAccessFile);
            int coldId = coldInfoRepo.getColdId(SubmitterId);

            //load grp
            Table grpTable = database.getTable(Constants.GRP);
            grpRepo.loadGrp(msAccessFile.getName(), coldId, grpTable);

            //load partyAcc
            Table partyAccTable = database.getTable(Constants.PARTY_ACC);
            partyAccRepo.loadPartyAcc(msAccessFile.getName(), coldId, partyAccTable);

            //load Amad Table
            Table amadTable = database.getTable(Constants.AMAD);
            amadRepo.loadAmad(msAccessFile.getName(), coldId, amadTable);

            //load partyledger Table
            Table partyLedgerTable = database.getTable(Constants.AMAD);
            //partyLedgerRepo.loadPartyLedger(msAccessFile.getName(), coldId, partyLedgerTable);


        } catch (IOException ex) {
            log.error(String.format(FAILED_TO_READ_MSACCESS_DB, msAccessFile.getName(), ex.getMessage()));
        } finally {
            assert database != null;
            //close db connection.
            database.close();
        }
    }

    @Override
    public Set<String> getTableNames(Database database) {
        String dbName = Constants.EMPTY_STRING;
        Set<String> allTables = null;
        try {
            allTables = database.getTableNames();
            dbName = database.getDatabaseProperties().getName();
        } catch (IOException ex) {
            log.error(String.format(FAILED_TO_READ_MSACCESS_DB, dbName, ex.getMessage()));
        }
        return allTables;
    }
}
