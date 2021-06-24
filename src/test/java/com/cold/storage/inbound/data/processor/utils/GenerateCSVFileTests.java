package com.cold.storage.inbound.data.processor.utils;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.PropertyMap;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.query.Query;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenerateCSVFileTests {

    static Logger log = LoggerFactory.getLogger(GenerateCSVFileTests.class);
    static String LOG_DIR = "src/test/resources/data/log/";

    public static void main(String[] args) {
        String DATA_DIR = "src/test/resources/data/";
        try {
            Database database = null;
            //test run
            String dataFile = "lodhirajputic.202127031710.mdb";
            try {
                File msAccessFile = new File(DATA_DIR + dataFile);
                database = Database.open(msAccessFile);
                System.out.println("password: " + database.getDatabasePassword());
                PropertyMap map = database.getDatabaseProperties();
                File file = database.getFile();
                List<Query> queryList = database.getQueries();


                printTables(database);
                System.out.println();
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
        Set<String> tables = database.getTableNames();
        for (String table : tables) {
            printTable(database, table);
            //  System.out.println(table);
        }
    }

    private static void printTable(Database database, String tableName) throws IOException {

        int count = 0;
        Path path = Paths.get(LOG_DIR + tableName + ".log");
        File targetFile = new File(String.valueOf(path));
        OutputStream outStream = new FileOutputStream(String.valueOf(targetFile));

        System.out.println("========== Printing data for Table | " + tableName + " |========================================");
        // String tableLine = "========== Printing data for Table | " + tableName + " |========================================";
        //  outStream.write(tableLine.getBytes());
        Table amad = database.getTable("Amad");
        Table grp = database.getTable("grp");
        //List<Column> columnList = amad.getColumns();
        // List<Relationship> relationships = database.getSummaryProperties();

     /*   for (Map<String, Object> row : database.getTable(tableName)) {
            String line = row.entrySet().toString();
            byte[] buffer = line.getBytes();
            outStream.write(buffer);
            count++;
        }*/
        System.out.println("row present in table: " + tableName + " ==> " + count);
        outStream.close();
    }

}