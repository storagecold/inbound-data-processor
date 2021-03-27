import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Objects;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {
    private final EligibilityLogger log = EligibilityLogger.getLogger(ServiceImpl.class);

    public static boolean invalid_fname = false;
    private final static String PROFILE_DEFINITION_ENDPOINT = "/definition?submitterid=%s";
    private final static String PROFILE_FULL_DETAIL_ENDPOINT = "/fulldetail?submitterid=%s&profileversion=%s";
    private final static String DEST_APP_NAME = "profile-api";
    private final static String CALLER_APP_NAME = EligibilityApps.DEFAULT.getAppName();

    @Value("${eniErrorPath}")
    String eniErrorPath;

    @Value("${mnrErrorPath}")
    String mnrErrorPath;

    @Value("${profile.api.url}")
    private String baseUrl;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${eniArchiveRoot}")
    String eniArchiveRoot;

    @Value("${mnrArchive}")
    String mnrArchive;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    EmailService emailService;

    @Autowired
    CustomerProfileService customerProfileService;


    @Override
    public boolean copyFile(File file, String dest) {
        boolean isFileCopied = false;
        Path src = null;
        Path target = null;

        if (Objects.nonNull(file) && StringUtils.hasText(dest)) {
            try {
                src = Paths.get(file.getAbsolutePath());
                target = Paths.get(dest);
            } catch (Exception e) {
                log.with($ -> {
                    $.addField("e.toString(): ", e.toString());
                    $.addMessage("Error in Method : copyFile(File file, String dest), could not copy file: " + file.getName());
                    $.addMessage("Invalid destination path");
                }).error(EligibilityLogStatus.PROCESSFAILURE);
                isFileCopied = false;
            }
        } else {
            log.with($ -> $.addMessage("dest or file is not valid")).info(EligibilityLogStatus.COMPLETEDWITHERROR);
            isFileCopied = false;
        }

        if (Files.isDirectory(target)) {
            target = Paths.get(dest + file.getName());
        } else {
            log.with($ -> $.addMessage("target is not a directory")).info(EligibilityLogStatus.COMPLETEDWITHERROR);
            isFileCopied = false;
        }

        if (Objects.nonNull(src) && Objects.nonNull(target)) {
            try {
                Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING);
                isFileCopied = true;
                log.with($ -> $.addMessage("File copied succesfully  :  " + file.getAbsolutePath())).
                        info(EligibilityLogStatus.COMPLETED);

            } catch (IOException e) {
                log.with($ -> {
                    $.addField("e.toString(): ", e.toString());
                    $.addMessage("Error in Method : copyFile(File file, String dest), could not copy file: " + file.getName());
                    $.addMessage("Copying " + file.getName() + " was not succesful");
                }).error(EligibilityLogStatus.PROCESSFAILURE);
                isFileCopied = false;
            }
        } else {
            log.with($ -> $.addMessage("src or target is not valid")).info(EligibilityLogStatus.COMPLETEDWITHERROR);
            isFileCopied = false;
        }
        return isFileCopied;
    }

    public File getTrigFile(File srcFile) {
        String trig = Constants.DOT_TRIG_EXT;
        String fp = "";
        try {
            fp = srcFile.getAbsolutePath() + trig;
        } catch (Exception ex) {
            log.with($ -> $.addMessage("File is Null ")).
                    error(EligibilityLogStatus.PROCESSFAILURE, ex);
        }
        return new File(fp);
    }

    @Override
    public File getDataFile(File trigFile) {
        String trigFilePath = trigFile.getAbsolutePath();
        String dataFilePath = trigFilePath.substring(0, trigFilePath.lastIndexOf(Constants.DOT_TRIG_EXT));
        return new File(dataFilePath);
    }

    /*
     * Moves files
     * that are given to the specified destination by not renaming the file Reusable
     * code
     */
    @Override
    public boolean moveFile(File file, String dest) {
        boolean isFileMoved = false;
        String finalDest = dest;

        if (Objects.isNull(file)) {
            log.with($ -> $.addMessage("File Null")
            ).info(EligibilityLogStatus.RECEIVEDWITHERROR);
            isFileMoved = false;
        }
        if (!file.exists()) {
            log.with($ -> $.addMessage("File does not exist : " + file.getName())
            ).info(EligibilityLogStatus.COMPLETEDWITHERROR);
            isFileMoved = false;
        }

        if (Objects.isNull(dest)) {
            log.with($ -> $.addMessage("Dest Null")
            ).info(EligibilityLogStatus.RECEIVEDWITHERROR);
            isFileMoved = false;
        }

        String fileName = file.getName();
        dest = dest.trim();

        File destDirectory = new File(dest);
        if (destDirectory.exists()) {
            File tempFile = new File(dest + File.separator + fileName);
            if (tempFile.exists()) {
                tempFile.delete();
            }
            if (file.renameTo(new File(dest + File.separator + fileName))) {
                String finalDest1 = dest;
                log.with($ -> $.addMessage(String.format("File %s successfully moved to : %s ", fileName, finalDest)
                )).info(EligibilityLogStatus.COMPLETED);
                isFileMoved = true;
            } else {
                log.with($ -> {
                            $.addMessage(String.format("File %s failed to move to %s: " + fileName, finalDest));
                        }
                ).info(EligibilityLogStatus.PROCESSFAILURE);
                isFileMoved = false;
            }
        } else {
            log.with($ -> $.addMessage(String.format("Destination directory %s does not exists: ", finalDest))
            ).info(EligibilityLogStatus.PROCESSFAILURE);
            isFileMoved = false;
        }
        return isFileMoved;
    }

    @Override
    public String getFileType(File file) {
        log.with($ -> $.addMessage(String.format("%s in getLob Method:", file))
        ).info(EligibilityLogStatus.RECEIVED);

        if (file.getName().endsWith(Constants.GSF_TRIG_EXT)) {
            return Constants.GSF_EXT;
        } else if (file.getName().endsWith(Constants.GPS_TRIG_EXT)) {
            return Constants.GPS_EXT;
        } else if (file.getName().endsWith(Constants.HIPAA_TRIG_EXT)) {
            return Constants.HIPAA_EXT;
        } else if (file.getName().endsWith(Constants.JSON_TRIG_EXT)) {
            return Constants.JSON_EXT;
        }
        return null;
    }

    @Override
    public String getLob(File file) throws Exception {
        // String submitterId;
        log.with($ -> $.addMessage(String.format("%s in getHipaaLob Method:", file))
        ).info(EligibilityLogStatus.RECEIVED);

        String submitterId = getSubmitterIdFromFile(file);

        String fileId = Long.toString(System.currentTimeMillis());

        SubmitterDefinition submitterDefinition = customerProfileService.getCustomerProfile(fileId, submitterId);
        String eligSysCd = submitterDefinition.getEligibilitySystem().getCode();

        if (eligSysCd.equals(Constants.ELIG_SYS_CD_GPS)) {
            log.with($ -> $.addMessage(String.format("Submitter %s belong to mnr lob", submitterId))
            ).info(EligibilityLogStatus.RECEIVED);
            return Constants.LOB_MNR;
        } else if (eligSysCd.equals(Constants.ELIG_SYS_CD_ENI)) {
            log.with($ -> $.addMessage(String.format("Submitter %s belong to eni lob", submitterId))
            ).info(EligibilityLogStatus.RECEIVED);
            return Constants.LOB_ENI;
        } else {
            log.with($ -> $.addMessage(String.format("Submitter %s does not belong to any lob", submitterId))
            ).info(EligibilityLogStatus.COMPLETEDWITHERROR);
            return null;
        }
    }

    public void moveIncorrectFileToErrorDirectory(File file) {

        log.with($ -> $.addMessage("Method : moveIncorrectFileToErrorDirectory(File file)"
        )).info(EligibilityLogStatus.PROCESS);

        try {
            String lob = getLob(file);
            if (Constants.LOB_ENI.equalsIgnoreCase(lob)) {
                moveFile(file, eniErrorPath);
            }
            if (Constants.LOB_MNR.equalsIgnoreCase(lob)) {
                moveFile(file, mnrErrorPath);
            }
        } catch (Exception ex) {
            log.with($ -> {
                $.addMessage("Error in Method: moveIncorrectFileToErrorDirectory while moving file " + file.getName());
            }).error(EligibilityLogStatus.PROCESSFAILURE, ex);
            String errorMessage = String.format("Error in Method: moveIncorrectFileToErrorDirectory while moving file %s", file.getName());
            String fileName = file.getName();
            String subject = "files moved to error directory";
            emailService.generateEmailRequest(fileName, subject, errorMessage);
        }
        log.with($ -> $.addMessage(String.format("File %s moved to error directory file ", file.getName()
        ))).info(EligibilityLogStatus.COMPLETEDWITHERROR);
    }

    public String getSubmitterIdFromFile(File file) throws Exception {
        String submitterId = null;
        try {
            String name = file.getName();
            int id = name.indexOf(".");
            if (id > -1) {

                submitterId = name.substring(0, id);
            } else {
                log.with($ -> $.addMessage("Error in getting the SubmitterId From FileName from getSubmitterIdFromFile :" + file.getName()
                )).info(EligibilityLogStatus.PROCESS);
                throw new Exception("Can not Extract SubmitterId from File: " + name);
            }
        } catch (Exception ex) {
            log.with($ -> {
                $.addMessage("Error in Method: getSubmitterIdFromFileName " + file.getName());
            }).error(EligibilityLogStatus.PROCESSFAILURE, ex);
            throw ex;
        }
        return submitterId;
    }

    public String getSecurityPath(String eligSysCd, String mktSegCd, String locationCd, String policyNumber) {
        log.with($ -> $.addMessage("in Method getSecurityPath"
        )).debug(EligibilityLogStatus.PROCESS);

        boolean optFlag = false;
        boolean uhgFlag = false;
        boolean onsiteFlag = false;
        boolean offSiteFlag = false;
        String dest = "";

        if (eligSysCd.equals(Constants.ELIG_SYS_CD_GPS)) {
            dest = mnrArchive;
        } else {
            if (mktSegCd.equalsIgnoreCase(Constants.MKT_SEG_CD_5)) {
                optFlag = true;
            }
            if (StringUtils.hasText(locationCd)) {
                if (locationCd.equalsIgnoreCase(Constants.LOCATION_CD_1)) {
                    onsiteFlag = true;
                } else if (locationCd.equalsIgnoreCase(Constants.LOCATION_CD_2)) {
                    offSiteFlag = true;
                }
            }
            if (policyNumber.equalsIgnoreCase(Constants.UHG_POLICY_NBRS)) {
                uhgFlag = true;
            }
            if (uhgFlag == true) {
                return eniArchiveRoot + Constants.B2BPrcElgUHG;
            } else if (optFlag == true && onsiteFlag == true) {
                return eniArchiveRoot + Constants.B2BPrcElgOPTONS;
            } else if (optFlag == true && offSiteFlag == true) {
                return eniArchiveRoot + Constants.B2BPrcElgOPTLVL6;
            }

            if (optFlag == true) {
                return eniArchiveRoot + Constants.B2BPrcElgOPT;
            } else if (onsiteFlag == true) {
                return eniArchiveRoot + Constants.B2BPrcElgONS;
            } else if (offSiteFlag == true) {
                return eniArchiveRoot + Constants.B2BPrcElgLVL6;
            } else {
                dest = eniArchiveRoot + Constants.B2BPrcElg;
            }
        }
        return dest;
    }
    /*
     * Author:
     *
     * Moves file to archive directories based on the security type of the file
     */

    public boolean moveFileToArchiveDirectory(File file) {
        boolean isFileMovedToArchive = false;
        log.with($ -> $.addMessage("Method: moveFileToArchiveDirectory " + file.getAbsolutePath()
        )).info(EligibilityLogStatus.PROCESS);

        String submitterId = "";
        String fileId = "";
        String dest = "";

        try {
            submitterId = getSubmitterIdFromFile(file);
            fileId = Long.toString(System.currentTimeMillis());

            SubmitterFullDetail submitterFullDetail = customerProfileService.getSubmitterFullDetail(fileId, submitterId);

            String eligSysCd = submitterFullDetail.getEligibilitySystem().getCode().trim();
            String mktSegCd = submitterFullDetail.getMarketSegment().getCode().trim();
            String locationCd = submitterFullDetail.getDemographics().getLocTypeCode().trim();
            String policyNumber = submitterFullDetail.getPolicyNumber();
            dest = getSecurityPath(eligSysCd, mktSegCd, locationCd, policyNumber);

        } catch (Exception ex) {
            log.with($ -> $.addMessage("Error in Method: moveFileToArachiveDirectory " + file.getName())).
                    error(EligibilityLogStatus.PROCESSFAILURE, ex);
            isFileMovedToArchive = false;
        }
        if (StringUtils.hasText(dest)) {
            isFileMovedToArchive = moveFile(file, dest);
            if (!isFileMovedToArchive) {
                log.with($ -> $.addMessage("Moving to Archive directory was not succesfull, file: " + file.getName()
                )).error(EligibilityLogStatus.PROCESSFAILURE);
                isFileMovedToArchive = false;
            }
        }
        return isFileMovedToArchive;
    }
}
