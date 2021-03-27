import java.io.File;
import java.util.Date;
import java.util.Objects;

//@Service
public class CleanUpService {
    private final EligibilityLogger log = EligibilityLogger.getLogger(CleanUpService.class);

    @Value("${eniArchiveRoot}")
    private String eniArchiveRoot;

    @Value("${eniInbRetain}")
    private int eniInbRetain;

    @Value("${marketArchive}")
    private String marketArchive;

    @Value("${marketRetain}")
    private int marketRetain;

    @Value("${vbdArchive}")
    private String vbdArchive;

    @Value("${vbdRetain}")
    private int vbdRetain;

    @Value("${oopMaxArchive}")
    private String oopMaxArchive;

    @Value("${oopMaxRetain}")
    private int oopMaxRetain;

    @Value("${mnrArchive}")
    private String mnrArchive;

    @Value("${mnrInbRetain}")
    private int mnrInbRetain;

    @Value("${FEDVIPArchive}")
    private String fedvipArchive;

    @Value("${FEDVIPRetain}")
    private int fedvipRetain;

    @Value("${commonLog}")
    private String commonLog;

    @Value("${eniLog}")
    private String eniLog;

    @Value("${mnrLog}")
    private String mnrLog;

    @Value("${fedvipLog}")
    private String fedvipLog;

    @Value("${logRetention}")
    private int logRetention;

    private long currentTime;

    @Scheduled(cron = "0 0 0 ? * ?")
    public void startCleanUp() {
        currentTime = new Date().getTime();
        log.with($ -> $.addMessage("Starting Cleanup at :" + currentTime))
                .info(EligibilityLogStatus.PROCESS);

        //clean B2BPrcElg
        listEligibleFilesToDelete(eniArchiveRoot + Constants.B2BPrcElg, eniInbRetain);

        //clean B2BPrcElgLVL6
        listEligibleFilesToDelete(eniArchiveRoot + Constants.B2BPrcElgLVL6, eniInbRetain);

        //clean B2BPrcElgONS
        listEligibleFilesToDelete(eniArchiveRoot + Constants.B2BPrcElgONS, eniInbRetain);

        //clean B2BPrcElgOPT
        listEligibleFilesToDelete(eniArchiveRoot + Constants.B2BPrcElgOPT, eniInbRetain);

        //clean B2BPrcElgOPTLVL6
        listEligibleFilesToDelete(eniArchiveRoot + Constants.B2BPrcElgOPTLVL6, eniInbRetain);

        //clean B2BPrcElgOPTONS
        listEligibleFilesToDelete(eniArchiveRoot + Constants.B2BPrcElgOPTONS, eniInbRetain);

        //clean B2BPrcElgUHG
        listEligibleFilesToDelete(eniArchiveRoot + Constants.B2BPrcElgUHG, eniInbRetain);

        //clean OOPMax_Feed
        listEligibleFilesToDelete(eniArchiveRoot + Constants.OOPMax_Feed, oopMaxRetain);

        //clean VBD
        listEligibleFilesToDelete(eniArchiveRoot + Constants.VBD, vbdRetain);

        //clean Market
        listEligibleFilesToDelete(marketArchive, marketRetain);

        //clean MnR
        listEligibleFilesToDelete(mnrArchive, mnrInbRetain);

        //clean Fedvip
        listEligibleFilesToDelete(fedvipArchive, fedvipRetain);

        //clean common log Files
        listEligibleFilesToDelete(commonLog, logRetention);

        //clean eni log Files
        listEligibleFilesToDelete(eniLog, logRetention);

        //clean mnr log Files
        listEligibleFilesToDelete(mnrLog, logRetention);

        //clean fedvip log Files
        listEligibleFilesToDelete(fedvipLog, logRetention);
    }

    private void listEligibleFilesToDelete(String dirPath, int fileRetention) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (Objects.requireNonNull(files).length > 0) {
            for (File file : files) {
                deleteFile(file, fileRetention);
            }
        }
    }

    private void deleteFile(File file, int daysToRetainFile) {
        long deleteBeforeTime = currentTime - file.lastModified();
        long timeToRetainFile = daysToRetainFile * 24 * 60 * 60 * 1000;
        boolean isFileDeleted = false;

        if (file.isFile() && file.exists()) {
            if (deleteBeforeTime > timeToRetainFile) {
                try {
                    isFileDeleted = file.delete();
                } catch (Exception ex) {
                    log.with($ -> $.addMessage(String.format("could not delete file: %s got Exception %s", file.getName(), ex)))
                            .error(EligibilityLogStatus.SENTFAILURE);
                }
                if (isFileDeleted) {
                    if (file.delete()) log.with($ -> $.addMessage("file deleted: " + file.getName()))
                            .info(EligibilityLogStatus.PROCESS);
                } else {
                    log.with($ -> $.addMessage(String.format("could not delete file: %s", file.getName())))
                            .error(EligibilityLogStatus.SENTFAILURE);
                }
            } else {
                log.with($ -> $.addMessage(String.format("file: %s is newer than %s days so not deleted.", file.getName(), daysToRetainFile)))
                        .info(EligibilityLogStatus.PROCESS);
            }
        } else {
            log.with($ -> $.addMessage(String.format("%s : is not file", file.getName())))
                    .error(EligibilityLogStatus.SENTFAILURE);
        }
    }
}
