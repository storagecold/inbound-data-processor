package com.cold.storage.inbound.data.processor.processor;

import com.cold.storage.inbound.data.processor.utility.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessorTest {
    private final String DATA_DIR = "src/test/resources/data/";
    private final String COLD_INB_DIR = "src/test/resources/inbound/";
    private final String COLD_ARCHIVE = "src/test/resources/archive/";

    @Autowired
    private Processor processoor;

    @Test
    public void testRun() throws IOException {
        //test run
        String dataFile = "lodhirajputic.202127031710.mdb";
        String trigFile = "lodhirajputic.202127031710.mdb.trig";
        TestUtils.getInstance().copyFile(DATA_DIR, COLD_INB_DIR, dataFile);
        TestUtils.getInstance().copyFile(DATA_DIR, COLD_INB_DIR, trigFile);

        processoor.processDataFiles();

        File[] fileArray = TestUtils.getInstance().getFiles(COLD_INB_DIR);
        boolean isDataFileAvailable = TestUtils.getInstance().containsFile(fileArray, dataFile);
        Assert.assertTrue(isDataFileAvailable);
        boolean isTrigFileAvailable = TestUtils.getInstance().containsFile(fileArray, dataFile);
        Assert.assertTrue(isTrigFileAvailable);

    }
}
