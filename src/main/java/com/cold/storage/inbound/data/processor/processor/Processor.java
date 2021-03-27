package com.cold.storage.inbound.data.processor.processor;

import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;

package com.optum.eligibility.lzprocess.processor;

import com.optum.eligibility.core.logging.EligibilityLogStatus;
import com.optum.eligibility.core.logging.EligibilityLogger;
import com.optum.eligibility.lzprocess.filter.TrigFileFilter;
import com.optum.eligibility.lzprocess.service.EmailService;
import com.optum.eligibility.lzprocess.service.Service;
import com.optum.eligibility.lzprocess.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class Processor {
    private final EligibilityLogger log = EligibilityLogger.getLogger(Processor.class);

    @Autowired
    private Service service;

    @Autowired
    private EniProcessor eniProcessor;

    @Autowired
    private MnRProcessor mnrProcessor;

    @Autowired
    private MarketProcessor marketProcessor;

    @Autowired
    private VBDProcessor vbdProcessor;

    @Autowired
    private OopMaxProcessor oopMaxProcessor;

    @Autowired
    private FEDVIPProcessor fedvipProcessor;

    @Autowired
    EmailService emailService;

    private String lob = null;

    @Value("${eems4landing}")
    String prevalRerouteDir;

    @Value("${eniInbound}")
    String eniInbound;

    @Value("${mnrInbound}")
    String mnrInbound;

    @Value("${tsoArchive}")
    String tsoArchive;

    @Scheduled(fixedDelay = 15000, initialDelay = 1000)
    public void redistribute() {
        File rerouteDir = new File(prevalRerouteDir);

        try {
            if (rerouteDir.exists() && rerouteDir.isDirectory()) {
                File[] trigFiles = rerouteDir.listFiles(new TrigFileFilter());
                if (trigFiles.length > 0) {
                    moveFilesToInbound(trigFiles);
                }
            } else {
                log.with($ -> $.addMessage(String.format("Preval Reroute Directory %s is not available", prevalRerouteDir)))
                        .info(EligibilityLogStatus.SENTFAILURE);
                String fileName = rerouteDir.getName();
                String subject = "Preval Reroute Directory is not available";
                String emailMessage = "Preval Reroute Directory" + fileName + "is not available";
                emailService.generateEmailRequest(fileName, subject, emailMessage);
            }
        } catch (Exception ex) {
            log.with($ -> $.addMessage("Exception in redistribute method ")).error(EligibilityLogStatus.PROCESSFAILURE, ex);
        }

        new Thread(() -> {
            try {
                eniProcessor.runEnI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                mnrProcessor.runMnR();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> marketProcessor.runMarket()).start();
        new Thread(() -> vbdProcessor.runVBD()).start();
        new Thread(() -> oopMaxProcessor.runOopMax()).start();
        new Thread(() -> fedvipProcessor.runFEDVIP()).start();
    }

    private void moveFilesToInbound(File[] trigFiles) throws Exception {
        for (File trigFile : trigFiles) {
            if (trigFile.exists() && trigFile.isFile()) {
                File dataFile = service.getDataFile(trigFile);
                if (dataFile.exists()) {
                    String fileType = service.getFileType(trigFile);
                    copyMove(trigFile, dataFile, fileType);

                } else {
                    log.with($ -> $.addMessage(String.format("dataFile does not exists for trig file %s moving trig to error directory", trigFile))).info(EligibilityLogStatus.COMPLETED);
                    service.moveIncorrectFileToErrorDirectory(trigFile);
                }
                log.with($ -> $.addMessage("dataFile : " + dataFile
                )).info(EligibilityLogStatus.COMPLETED);
            } else {
                log.with($ -> $.addMessage("TRIG file no longer exists : " + trigFile.getAbsolutePath()
                )).info(EligibilityLogStatus.PROCESSFAILURE);
            }
        }
    }

    private void copyMove(File trigFile, File dataFile, String fileType) throws Exception {

        switch (fileType) {
            case Constants.GSF_EXT:
                copyArchive(trigFile, dataFile, eniInbound);
                break;
            case Constants.GPS_EXT:
                copyArchive(trigFile, dataFile, mnrInbound);
                break;
            case Constants.JSON_EXT:
                jsonCopyArchive(trigFile, dataFile);
                break;
            case Constants.HIPAA_EXT:
                archiveHipaa(trigFile, dataFile);
                break;

            default:
                log.with($ -> $.addMessage(String.format("dataFile %s  is not a valid file ", dataFile
                ))).info(EligibilityLogStatus.COMPLETED);
        }

        log.with($ -> $.addMessage(String.format("trig file %s and dataFile %s have been moved to " +
                "inbound", trigFile, dataFile))).info(EligibilityLogStatus.COMPLETED);
    }

    private void archiveHipaa(File trigFile, File dataFile) {
        service.copyFile(dataFile, tsoArchive);
        if (service.moveFileToArchiveDirectory(dataFile)) {
            log.with($ -> $.addMessage(String.format("trig file %s and dataFile %s have been moved to " +
                    "archive ", dataFile, trigFile))).info(EligibilityLogStatus.COMPLETED);
        } else {
            service.moveIncorrectFileToErrorDirectory(dataFile);
            service.moveIncorrectFileToErrorDirectory(trigFile);
            String errorMessage = String.format("both trig %s and data file %s have been moved to error directory", trigFile, dataFile);
            String fileName = dataFile.getName();
            String subject = "files moved to error directory";
            emailService.generateEmailRequest(fileName, subject, errorMessage);
            log.with($ -> $.addMessage(errorMessage)).error(EligibilityLogStatus.COMPLETEDWITHERROR);
        }
    }

    private void jsonCopyArchive(File trigFile, File dataFile) throws Exception {

        try {
            lob = service.getLob(dataFile);
        } catch (RuntimeException ex) {
            log.with($ -> $.addMessage("Profile Data not available for  trigFile : " + trigFile
            )).info(EligibilityLogStatus.COMPLETEDWITHERROR);
            service.moveIncorrectFileToErrorDirectory(dataFile);
            log.with($ -> $.addMessage("Exception in redistribute method,both trig and data file have been moved to error directory ")).error(EligibilityLogStatus.PROCESSFAILURE, ex);
        }

        if (lob.equals(Constants.LOB_ENI)) {
            copyArchive(trigFile, dataFile, eniInbound);
        } else {
            copyArchive(trigFile, dataFile, mnrInbound);
        }
    }

    private void copyArchive(File trigFile, File dataFile, String inbound) {
        boolean isDataFileArchived;
        boolean isTrigFileArchived;

        service.copyFile(dataFile, inbound);
        service.copyFile(trigFile, inbound);
        service.copyFile(dataFile, tsoArchive);

        isDataFileArchived = service.moveFileToArchiveDirectory(dataFile);
        if (!isDataFileArchived) {
            service.moveIncorrectFileToErrorDirectory(dataFile);
        }

        isTrigFileArchived = service.moveFileToArchiveDirectory(trigFile);
        if (!isTrigFileArchived) {
            service.moveIncorrectFileToErrorDirectory(trigFile);
        }
    }
}
