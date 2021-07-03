package com.cold.storage.inbound.data.processor.utils;

import com.cold.storage.inbound.data.processor.service.MsAccessService;
import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrintTableDefinitionTests {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private final String DATA_DIR = "src/test/resources/data/";

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
        for (String tableName : tables) {
            Table table = database.getTable(tableName);
            if (tableName.equals("partyledger")) {
                printColumns(table);
            }
        }
    }

    private void printColumns(Table table) {
        List<Column> columns = table.getColumns();
        System.out.println("--------started table definition ---> " + table.getName());
        System.out.println("create table " + table.getName() + "(");
        for (Column column : columns) {
            String mysqlName = "";
            String columnType = column.getType().name();
            switch (columnType) {
                case "TEXT":
                    mysqlName = "VARCHAR(50)";
                    break;
                case "MONEY":
                    mysqlName = "DECIMAL(15,2)";
                    break;
                case "LONG":
                    mysqlName = "INT";
                    break;
                case "INT":
                    mysqlName = "INT";
                    break;
                case "SHORT_DATE_TIME":
                    mysqlName = "DATE";
                    break;
                case "DOUBLE":
                    mysqlName = "INT";
                    break;
                default:
                    System.out.println("column not in list.");
            }
            //build data
            System.out.println("    " + column.getName() + " " + mysqlName + " ,");
        }
        System.out.println("--------ended table definition ---> " + table.getName());
    }
}