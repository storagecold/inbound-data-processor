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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class Processor {
    private final EligibilityLogger log = EligibilityLogger.getLogger(MarketProcessor.class);

    @Value("${marketInbound}")
    private String marketInbound;

    @Value("${eniInbStg}")
    private String marketInboundStg;

    @Value("${eniTrigStg}")
    private String marketTrigStg;

    @Value("${marketArchive}")
    private String marketArchive;

    @Value("${marketSTOP_FILE}")
    private String marketStopFile;

    @Autowired
    private Service service;

    @Autowired
    EmailService emailService;

    @Scheduled(fixedDelay = 15000, initialDelay = 1000)
    public void runEoE() {

        File eoeStopFile = new File(propertiesUtil.getEoeStopfile());

        if (!eoeStopFile.exists()) {

            processEoeFiles();
        } else {
            log.with($ -> $.addMessage("Stop File exists ")).info(EligibilityLogStatus.PROCESSFAILURE);
            String errorMessage = "eoe Stop File exists: " + eoeStopFile;
            String stopFile = propertiesUtil.getEoeStopfile();
            String subject = "eoe Stop File exists";
            emailService.generateEmailRequest(stopFile, subject, errorMessage);
        }
    }

    public void processEoeFiles() {
        try {
            File landingZoneDir = new File(propertiesUtil.getEoeInbound());

            if (landingZoneDir.exists() && landingZoneDir.isDirectory()) {
                File[] trigFiles = landingZoneDir.listFiles(new TrigFileFilter());
                if (trigFiles.length > 0) {
                    for (File trigFile : trigFiles) {
                        if (trigFile.exists()) {
                            File dataFile = service.getDataFile(trigFile);
                            if (dataFile.exists()) {

                                File dataTempFile = dataFile;

                                String fileName = dataTempFile.getName();

                                if (fileName.toUpperCase().startsWith("WF1")) {
                                    dataTempFile = Utils.renameFile(dataTempFile);
                                }
//                              keyPath = Utils.genFileType(dataFile, propertiesUtil.getObjectFolder());
                                sendFileToS3(dataTempFile);
                                service.moveFile(dataTempFile, propertiesUtil.getEoeArchive());
                                service.moveFile(trigFile, propertiesUtil.getEoeArchive());
                                messagingService.sendOutstreamMessage(dataTempFile.getName(), FileType.INBOUND, propertiesUtil.getObjectFolder() + dataTempFile.getName());

                                log.with($ -> $.addMessage("Processed EOE File : " + dataFile
                                )).info(EligibilityLogStatus.COMPLETED);
                            } else {
                                log.with($ -> $.addMessage(String.format("dataFile does not exists for trig file %s moving trig to error directory", trigFile))).error(EligibilityLogStatus.PROCESSFAILURE);
                                service.moveIncorrectFileToErrorDirectory(trigFile);
                            }
                        } else {
                            log.with($ -> $.addMessage("EOE trig file no longer exists : " + trigFile.getAbsolutePath()
                            )).info(EligibilityLogStatus.PROCESSFAILURE);
                        }
                    }
                }
            } else {
                log.with($ -> $.addMessage("EOE Directory no longer exists : " + propertiesUtil.getEoeInbound()
                )).info(EligibilityLogStatus.PROCESSFAILURE);
                String fileName = propertiesUtil.getEoeInbound();
                String subject = "EOE Directory no longer exists";
                String emailMessage = "EOE Directory no longer exists " + fileName + " is not available";
                emailService.generateEmailRequest(fileName, subject, emailMessage);
            }

        } catch (Exception ex) {
            log.with($ -> $.addMessage("Exception in redistribute method ")).error(EligibilityLogStatus.PROCESSFAILURE, ex);
        }
    }

    public void sendFileToS3(File file) {
        try {
            String fileName = file.getName();

            String key = propertiesUtil.getObjectFolder() + fileName;

            PutObjectRequest putObjectRequest = new PutObjectRequest(propertiesUtil.getBucketName(), key, file);
            amazonS3Client.putObject(putObjectRequest);

            log.with($ -> $.addMessage("File has been loaded to S3 Bucket: " + file
            )).info(EligibilityLogStatus.COMPLETED);
        } catch (Exception ex) {
            service.moveFile(file, propertiesUtil.getEoeError());
            log.with($ -> $.addMessage("Exception in sendFileToS3 method,file has been moved to error directory ")).error(EligibilityLogStatus.PROCESSFAILURE, ex);
        }

    }
}
