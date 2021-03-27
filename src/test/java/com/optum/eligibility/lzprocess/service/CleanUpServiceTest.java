import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CleanUpServiceTest {
    private final String ELIG_DIR = "src/test/resources/elig/";

    @Autowired
    private CleanUpService cleanUpService;

    @Before
    public void setUp() throws Exception {
        TestUtils.getInstance().copyFilesToClean();
    }

    @Test
    public void startCleanUp() {
        cleanUpService.startCleanUp();

        //Assert B2BPrcElg
        File fileInB2BPrcElg = new File(ELIG_DIR + "eni/archive/B2BPrcElg/EEMS4ENICERT.U.202010200805.gsf");
        Assert.assertFalse(fileInB2BPrcElg.exists());

        //Assert B2BPrcElgLVL6
        File fileInB2BPrcElgLVL6 = new File(ELIG_DIR + "eni/archive/B2BPrcElgLVL6/EEMS4ENICERT.U.202010200805.gsf");
        Assert.assertFalse(fileInB2BPrcElgLVL6.exists());

        //Assert B2BPrcElgONS
        File fileInB2BPrcElgONS = new File(ELIG_DIR + "eni/archive/B2BPrcElgONS/EEMS4ENICERT.U.202010200805.gsf");
        Assert.assertFalse(fileInB2BPrcElgONS.exists());

        //Assert B2BPrcElgOPT
        File fileInB2BPrcElgOPT = new File(ELIG_DIR + "eni/archive/B2BPrcElgOPT/EEMS4ENICERT.U.202010200805.gsf");
        Assert.assertFalse(fileInB2BPrcElgOPT.exists());

        //Assert B2BPrcElgOPTLVL6
        File fileInB2BPrcElgOPTLVL6 = new File(ELIG_DIR + "eni/archive/B2BPrcElgOPTLVL6/EEMS4ENICERT.U.202010200805.gsf");
        Assert.assertFalse(fileInB2BPrcElgOPTLVL6.exists());

        //Assert B2BPrcElgOPTONS
        File fileInB2BPrcElgOPTONS = new File(ELIG_DIR + "eni/archive/B2BPrcElgOPTONS/EEMS4ENICERT.U.202010200805.gsf");
        Assert.assertFalse(fileInB2BPrcElgOPTONS.exists());

        //Assert B2BPrcElgUHG
        File fileInB2BPrcElgUHG = new File(ELIG_DIR + "eni/archive/B2BPrcElgUHG/EEMS4ENICERT.U.202010200805.gsf");
        Assert.assertFalse(fileInB2BPrcElgUHG.exists());

        //Assert Market
        File fileInMarket = new File(ELIG_DIR + "eni/archive/Market/market.txt");
        Assert.assertFalse(fileInMarket.exists());

        //Assert OOPMax_Feed
        File fileInOOPMax_Feed = new File(ELIG_DIR + "eni/archive/OOPMax_Feed/OOPMax.0729784.111318095601");
        Assert.assertFalse(fileInOOPMax_Feed.exists());

        //Assert VBD
        File fileInVBD = new File(ELIG_DIR + "eni/archive/VBD/EnrollmentEligibility_ALL_11132020.txt");
        Assert.assertFalse(fileInVBD.exists());

        //Assert MnR
        File fileInMnR = new File(ELIG_DIR + "mnr/archive/GPSCERTC.U.201705190001.gps");
        Assert.assertFalse(fileInMnR.exists());

        //Assert Fedvip
        File fileInFedvip = new File(ELIG_DIR + "fedvip/archive/FEDVIPD.E.202010141552.fedvip");
        Assert.assertFalse(fileInFedvip.exists());

        //Assert commonLog
        File fileIncommonLog = new File(ELIG_DIR + "logs/common.log");
        Assert.assertFalse(fileIncommonLog.exists());

        //Assert eniLog
        File fileIneniLog = new File(ELIG_DIR + "eni/logs/eni_lz_to_ose.log");
        Assert.assertFalse(fileIneniLog.exists());

        //Assert MnRLog
        File fileInMnRLog = new File(ELIG_DIR + "mnr/logs/mnr.log");
        Assert.assertFalse(fileInMnRLog.exists());

        //Assert fedvip
        File fileInfedvip = new File(ELIG_DIR + "fedvip/logs/fedvip.log");
        Assert.assertFalse(fileInfedvip.exists());
    }
}
