package com.cold.storage.inbound.data.processor.utils;

import com.cold.storage.inbound.data.processor.service.MsAccessService;
import com.healthmarketscience.jackcess.Database;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrintTableDataTests {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private final String DATA_DIR = "src/test/resources/data/";
    private final String LOG_DIR = "src/test/resources/data/log/";


    @Autowired
    private MsAccessService msAccessService;

    @Test
    public void giveMSAccessDB_WhenTable_ThenPrintContent() {
        try {
            Database database = null;

            //test run
            String dataFile = "lodhirajputic.202127031710.mdb";
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

    private void printTables(Database database) throws IOException {
        Set<String> tables = msAccessService.getTableNames(database);
        for (String table : tables) {
            printTable(database, table);
            System.out.println(table);
        }
    }

    private void printTable(Database database, String tableName) throws IOException {
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
}