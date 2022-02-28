package com.cold.storage.inbound.data.processor.utils;

import com.healthmarketscience.jackcess.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

public class MsAccessDB {
    private static final String DATA_DIR = "C:/Program Files (x86)/Cold Storage Software/1/2022/";
    private static final String LOG_DIR = "src/test/resources/data/log/";
    private static final String FAILED_TO_READ_MSACCESS_DB = "failed to read msAccessDataBase %s: ,Ex: %s";

    static Logger  log = LoggerFactory.getLogger(MsAccessDB.class);

    public static void main(String[] args) {
        giveMSAccessDB_WhenTable_ThenPrintContent();
    }
    public static void giveMSAccessDB_WhenTable_ThenPrintContent() {
        try {
            Database database = null;

            //test run
            String dataFile = "acc.mdb";
            try {
                File msAccessFile = new File(DATA_DIR + dataFile);
                database = Database.open(msAccessFile);
                printTables(database);
            } finally {
                assert database != null;
                //close db connection.
                database.close();
            }
        } catch (IOException ioException) {
            log.error("IOException while reading");
        }
    }
    private static void printTables(Database database) throws IOException {
        Set<String> tables = getTableNames(database);
        for (String table : tables) {
            printTable(database, table);
            System.out.println(table);
        }
    }


    private static  void printTable(Database database, String tableName) throws IOException {
        int count = 0;
        Path path = Paths.get(LOG_DIR + tableName + ".log");
        File targetFile = new File(String.valueOf(path));
        OutputStream outStream = new FileOutputStream(String.valueOf(targetFile));

        System.out.println("========== Printing data for Table | " + tableName + " |========================================");
        String tableLine = "========== Printing data for Table | " + tableName + " |========================================";
        outStream.write(tableLine.getBytes());
        for (Map<String, Object> row : database.getTable(tableName)) {
            String line = row.entrySet().toString();
            byte[] buffer = line.getBytes();
            outStream.write(buffer);
            count++;

        }
        System.out.println("row present in table: " + tableName + " ==> " + count);
        outStream.close();
    }

    public static Set<String> getTableNames(Database database) {
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

