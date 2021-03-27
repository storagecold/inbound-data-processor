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

    public void runMarket() {

        if (!(new File(marketStopFile).exists())) {

            processMarketFiles();
        } else {
            log.with($ -> $.addMessage("Stop File exists ")).info(EligibilityLogStatus.PROCESSFAILURE);
            String errorMessage = "market Stop File exists: " + marketStopFile;
            String stopFile = marketStopFile;
            String subject = "market Stop File exists";
            emailService.generateEmailRequest(stopFile, subject, errorMessage);
        }
    }

    public void processMarketFiles() {
        try {
            File landingZoneDir = new File(marketInbound);
            if (landingZoneDir.exists() && landingZoneDir.isDirectory()) {
                File[] trigFiles = landingZoneDir.listFiles(new TrigFileFilter());
                if (trigFiles.length > 0) {
                    for (File trigFile : trigFiles) {
                        if (trigFile.exists()) {
                            File dataFile = service.getDataFile(trigFile);
                            if (dataFile.exists()) {
                                service.copyFile(dataFile, marketInboundStg);
                                service.copyFile(trigFile, marketTrigStg);
                                service.moveFile(dataFile, marketArchive);
                                service.moveFile(trigFile, marketArchive);

                                log.with($ -> $.addMessage("Processed market File : " + dataFile
                                )).info(EligibilityLogStatus.COMPLETED);
                            } else {
                                log.with($ -> $.addMessage(String.format("dataFile does not exists for trig file %s moving trig to error directory", trigFile))).error(EligibilityLogStatus.PROCESSFAILURE);
                                service.moveIncorrectFileToErrorDirectory(trigFile);
                            }
                        } else {
                            log.with($ -> $.addMessage("market trig file no longer exists : " + trigFile.getAbsolutePath()
                            )).info(EligibilityLogStatus.PROCESSFAILURE);
                        }
                    }
                }
            } else {
                log.with($ -> $.addMessage("Market Directory no longer exists : " + marketInbound
                )).info(EligibilityLogStatus.PROCESSFAILURE);
                String fileName = marketInbound;
                String subject = "Market Directory no longer exists";
                String emailMessage = "Market Directory no longer exists " + fileName + " is not available";
                emailService.generateEmailRequest(fileName, subject, emailMessage);
            }
        } catch (Exception ex) {
            log.with($ -> $.addMessage("Exception in redistribute method ")).error(EligibilityLogStatus.PROCESSFAILURE, ex);
        }
    }
}
