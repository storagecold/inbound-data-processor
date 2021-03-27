import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class TestUtils {
    private final String DATA_DIR = "src/test/resources/data/";
    private final String ELIG_DIR = "src/test/resources/elig/";
    private final String PPR_DIR = "src/test/resources/elig/inbound/prevalreroute/";
    private final String ENI_DIR = "src/test/resources/elig/eni/";
    private final String ENI_ARCHIVE_DIR = "src/test/resources/elig/eni/archive/";
    private final String MNR_DIR = "src/test/resources/elig/mnr/";
    private final String MNR_ARCHIVE_DIR = "src/test/resources/elig/mnr/archive";
    private final String FEDVIP_DIR = "src/test/resources/elig/fedvip/";
    private final String FEDVIP_ARCHIVE_DIR = "src/test/resources/elig/fedvip/archive";
    private final String EOE_DIR = "src/test/resources/elig/eoe/";
    private final String EOE_ARCHIVE_DIR = "src/test/resources/elig/eoe/archive";
    private static TestUtils instance = null;

    private TestUtils() {
    }

    public static TestUtils getInstance() {
        if (instance == null) {
            instance = new TestUtils();
        }
        return instance;
    }

    public void copyFilesToProcess() throws IOException {
        //copy to EnI
        copyFile(DATA_DIR, PPR_DIR, "EEMS4ENICERT.U.202010200805.gsf");
        copyFile(DATA_DIR, PPR_DIR, "EEMS4ENICERT.U.202010200805.gsf.trig");

        //copy to Market
        copyFile(DATA_DIR, PPR_DIR + "Market", "MarketVal.txt");
        copyFile(DATA_DIR, PPR_DIR + "Market", "MarketVal.txt.trig");

        //copy to VBD
        copyFile(DATA_DIR, PPR_DIR + "VBD", "EnrollmentEligibility_ALL_11132020.txt");
        copyFile(DATA_DIR, PPR_DIR + "VBD", "EnrollmentEligibility_ALL_11132020.txt.trig");

        //copy to OOPMax_Feed
        copyFile(DATA_DIR, PPR_DIR + "OOPMax_Feed", "OOPMax.0729784.111318095601");
        copyFile(DATA_DIR, PPR_DIR + "OOPMax_Feed", "OOPMax.0729784.111318095601.trig");

        //copy to MnR
        copyFile(DATA_DIR, PPR_DIR, "GPSCERTC.U.201705190001.gps");
        copyFile(DATA_DIR, PPR_DIR, "GPSCERTC.U.201705190001.gps.trig");

        //copy to fedvip
        copyFile(DATA_DIR, FEDVIP_DIR, "FEDVIPD.E.202010141552.fedvip");
        copyFile(DATA_DIR, FEDVIP_DIR, "FEDVIPD.E.202010141552.fedvip.trig");

        //copy to eoe
        copyFile(DATA_DIR, EOE_DIR, "EOE.E.202010141552.eoe");
        copyFile(DATA_DIR, EOE_DIR, "EOE.E.202010141552.eoe.trig");

    }

    public void copyFilesToClean() throws IOException {
        //copy to B2BPrcElg
        copyFile(DATA_DIR, ENI_ARCHIVE_DIR + "B2BPrcElg/", "EEMS4ENICERT.U.202010200805.gsf");

        //copy to B2BPrcElgLVL6
        copyFile(DATA_DIR, ENI_ARCHIVE_DIR + "B2BPrcElgLVL6/", "EEMS4ENICERT.U.202010200805.gsf");

        //copy to B2BPrcElgONS
        copyFile(DATA_DIR, ENI_ARCHIVE_DIR + "B2BPrcElgONS/", "EEMS4ENICERT.U.202010200805.gsf");

        //copy to B2BPrcElgOPT
        copyFile(DATA_DIR, ENI_ARCHIVE_DIR + "B2BPrcElgOPT/", "EEMS4ENICERT.U.202010200805.gsf");

        //copy to B2BPrcElgOPTLVL6
        copyFile(DATA_DIR, ENI_ARCHIVE_DIR + "B2BPrcElgOPTLVL6/", "EEMS4ENICERT.U.202010200805.gsf");

        //copy to B2BPrcElgOPTONS
        copyFile(DATA_DIR, ENI_ARCHIVE_DIR + "B2BPrcElgOPTONS/", "EEMS4ENICERT.U.202010200805.gsf");

        //copy to B2BPrcElgOPTONS
        copyFile(DATA_DIR, ENI_ARCHIVE_DIR + "B2BPrcElgOPTONS/", "EEMS4ENICERT.U.202010200805.gsf");

        //copy to B2BPrcElgUHG
        copyFile(DATA_DIR, ENI_ARCHIVE_DIR + "B2BPrcElgUHG/", "EEMS4ENICERT.U.202010200805.gsf");

        //copy to Market
        copyFile(DATA_DIR, ENI_ARCHIVE_DIR + "Market/", "market.txt");

        //copy to OOPMax_Feed
        copyFile(DATA_DIR, ENI_ARCHIVE_DIR + "OOPMax_Feed/", "OOPMax.0729784.111318095601");

        //copy to VBD
        copyFile(DATA_DIR, ENI_ARCHIVE_DIR + "VBD/", "EnrollmentEligibility_ALL_11132020.txt");

        //copy to MnR
        copyFile(DATA_DIR, MNR_ARCHIVE_DIR, "GPSCERTC.U.201705190001.gps");

        //copy to Fedvip
        copyFile(DATA_DIR, FEDVIP_ARCHIVE_DIR , "FEDVIPD.E.202010141552.fedvip");

        //copy to EOE
        copyFile(DATA_DIR, EOE_ARCHIVE_DIR , "EOE.E.202010141552.eoe");

        //copy to common log
        copyFile(DATA_DIR, ELIG_DIR + "logs/", "common.log");

        //copy to eni log
        copyFile(DATA_DIR, ELIG_DIR + "eni/logs/", "eni_lz_to_ose.log");

        //copy to MnR log
        copyFile(DATA_DIR, ELIG_DIR + "mnr/logs/", "mnr.log");

        //copy to Fedvip log
        copyFile(DATA_DIR, ELIG_DIR + "fedvip/logs/", "fedvip.log");

    }

    public void copyFile(String srcDir, String destDir, String file) throws IOException {
        Path src = Paths.get(srcDir + file);
        Path target = Paths.get(destDir + file);
        Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean containsFile(File[] files, String fileName) {
        boolean isFileAvailable;
        isFileAvailable = Stream.of(files)
                .anyMatch($ -> $.getName().equals(fileName));
        return isFileAvailable;
    }

    public File[] getFiles(String path) {
        File dir = new File(path);
        return dir.listFiles();
    }
}
