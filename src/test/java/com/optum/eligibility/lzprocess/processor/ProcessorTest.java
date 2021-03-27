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
    private final String PPR_DIR = "src/test/resources/elig/inbound/prevalreroute/";
    private final String ENI_INB_DIR = "src/test/resources/elig/eni/inbound/";
    private final String MNR_INB_DIR = "src/test/resources/elig/mnr/inbound/";

    private final String OOPMAX_INB_DIR = "src/test/resources/elig/eni/inbound/OOPMax_Feed/";
    private final String OOPMAX_INBDATA_DIR = "src/test/resources/elig/eni/oopmaxinbstg/";
    private final String OOPMAX_INBTRIG_DIR = "src/test/resources/elig/eni/oopmaxtrgstg/";

    private final String VBD_INB_DIR = "src/test/resources/elig/eni/inbound/VBD/";
    private final String VBD_INBDATA_DIR = "src/test/resources/elig/eni/vbdinbstg/";
    private final String VBD_INBTRIG_DIR = "src/test/resources/elig/eni/vbdtrgstg/";

    private final String FEDVIP_INB_DIR = "src/test/resources/elig/fedvip/inbound/";
    private final String FEDVIP_INBDATA_DIR = "src/test/resources/elig/fedvip/inbstg/";
    private final String FEDVIP_INBTRIG_DIR = "src/test/resources/elig/fedvip/trgstg/";

    @Autowired
    private Processor processor;

    @Autowired
    private VBDProcessor vbdProcessor;

    @Autowired
    private OopMaxProcessor oopMaxProcessor;

    @Autowired
    private FEDVIPProcessor fedvipProcessor;

    @Test
    public void testEnIB2BPrcElg() throws IOException {
        //test to EnI
        String dataFile = "EEMS4ENICERT.U.202010200805.gsf";
        String trigFile = "EEMS4ENICERT.U.202010200805.gsf.trig";
        TestUtils.getInstance().copyFile(DATA_DIR, PPR_DIR, dataFile);
        TestUtils.getInstance().copyFile(DATA_DIR, PPR_DIR, trigFile);

        processor.redistribute();

        File[] fileArray = TestUtils.getInstance().getFiles(ENI_INB_DIR);
        boolean isDataFileAvailable = TestUtils.getInstance().containsFile(fileArray, dataFile);
        Assert.assertTrue(isDataFileAvailable);
        boolean isTrigFileAvailable = TestUtils.getInstance().containsFile(fileArray, dataFile);
        Assert.assertTrue(isTrigFileAvailable);

    }

    @Test
    public void testMnR() throws IOException {
        //test to MnR
        String dataFile = "GPSCERTC.U.201705190001.gps";
        String trigFile = "GPSCERTC.U.201705190001.gps.trig";
        TestUtils.getInstance().copyFile(DATA_DIR, PPR_DIR, dataFile);
        TestUtils.getInstance().copyFile(DATA_DIR, PPR_DIR, trigFile);

        processor.redistribute();

        File[] fileArray = TestUtils.getInstance().getFiles(MNR_INB_DIR);
        boolean isDataFileAvailable = TestUtils.getInstance().containsFile(fileArray, dataFile);
        Assert.assertTrue(isDataFileAvailable);
        boolean isTrigFileAvailable = TestUtils.getInstance().containsFile(fileArray, dataFile);
        Assert.assertTrue(isTrigFileAvailable);

    }

    @Test
    public void testUHG() throws IOException {
        //test to UHG
        String dataFile = "UNHGRPF.U.202011190113.gsf";
        String trigFile = "UNHGRPF.U.202011190113.gsf.trig";
        TestUtils.getInstance().copyFile(DATA_DIR, PPR_DIR, dataFile);
        TestUtils.getInstance().copyFile(DATA_DIR, PPR_DIR, trigFile);

        processor.redistribute();

        File[] fileArray = TestUtils.getInstance().getFiles(ENI_INB_DIR);
        boolean isDataFileAvailable = TestUtils.getInstance().containsFile(fileArray, dataFile);
        Assert.assertTrue(isDataFileAvailable);
        boolean isTrigFileAvailable = TestUtils.getInstance().containsFile(fileArray, dataFile);
        Assert.assertTrue(isTrigFileAvailable);

    }

    @Test
    public void testVBD() throws IOException {
        //test to VBD
        String dataFile = "EnrollmentEligibility_ALL_11132020.txt";
        String trigFile = "EnrollmentEligibility_ALL_11132020.txt.trig";
        TestUtils.getInstance().copyFile(DATA_DIR, VBD_INB_DIR, dataFile);
        TestUtils.getInstance().copyFile(DATA_DIR, VBD_INB_DIR, trigFile);

        vbdProcessor.runVBD();

        File[] dataFileArray = TestUtils.getInstance().getFiles(VBD_INBDATA_DIR);
        boolean isDataFileAvailable = TestUtils.getInstance().containsFile(dataFileArray, dataFile);
        Assert.assertTrue(isDataFileAvailable);

        File[] trigFileArray = TestUtils.getInstance().getFiles(VBD_INBTRIG_DIR);
        boolean isTrigFileAvailable = TestUtils.getInstance().containsFile(trigFileArray, trigFile);
        Assert.assertTrue(isTrigFileAvailable);

    }

    @Test
    public void testOOPMAX() throws IOException {
        //test to OOPMAX
        String dataFile = "OOPMax.0729784.111318095601";
        String trigFile = "OOPMax.0729784.111318095601.trig";
        TestUtils.getInstance().copyFile(DATA_DIR, OOPMAX_INB_DIR, dataFile);
        TestUtils.getInstance().copyFile(DATA_DIR, OOPMAX_INB_DIR, trigFile);

        oopMaxProcessor.runOopMax();

        File[] dataFileArray = TestUtils.getInstance().getFiles(OOPMAX_INBDATA_DIR);
        boolean isDataFileAvailable = TestUtils.getInstance().containsFile(dataFileArray, dataFile);
        Assert.assertTrue(isDataFileAvailable);

        File[] trigFileArray = TestUtils.getInstance().getFiles(OOPMAX_INBTRIG_DIR);
        boolean isTrigFileAvailable = TestUtils.getInstance().containsFile(trigFileArray, trigFile);
        Assert.assertTrue(isTrigFileAvailable);

    }

    @Test
    public void testFEDVIP() throws IOException {
        //test to FEDVIP
        String dataFile = "FEDVIPD.E.202010141552.fedvip";
        String trigFile = "FEDVIPD.E.202010141552.fedvip.trig";
        TestUtils.getInstance().copyFile(DATA_DIR, FEDVIP_INB_DIR, dataFile);
        TestUtils.getInstance().copyFile(DATA_DIR, FEDVIP_INB_DIR, trigFile);

        fedvipProcessor.runFEDVIP();

        File[] dataFileArray = TestUtils.getInstance().getFiles(FEDVIP_INBDATA_DIR);
        boolean isDataFileAvailable = TestUtils.getInstance().containsFile(dataFileArray, dataFile);
        Assert.assertTrue(isDataFileAvailable);

        File[] trigFileArray = TestUtils.getInstance().getFiles(FEDVIP_INBTRIG_DIR);
        boolean isTrigFileAvailable = TestUtils.getInstance().containsFile(trigFileArray, trigFile);
        Assert.assertTrue(isTrigFileAvailable);

    }


}
