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

        //copy to eoe
        copyFile(DATA_DIR, EOE_DIR, "EOE.E.202010141552.eoe");
        copyFile(DATA_DIR, EOE_DIR, "EOE.E.202010141552.eoe.trig");

    }

    public void copyFilesToClean() throws IOException {

        //copy to EOE
        copyFile(DATA_DIR, EOE_ARCHIVE_DIR , "EOE.E.202010141552.eoe");

        //copy to EOE log
        copyFile(DATA_DIR, ELIG_DIR + "eoe/logs/", "eoe.log");
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
