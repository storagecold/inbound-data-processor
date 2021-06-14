package com.cold.storage.inbound.data.processor.processor;

import com.cold.storage.inbound.data.processor.filter.TrigFileFilter;
import com.cold.storage.inbound.data.processor.service.FileService;
import com.cold.storage.inbound.data.processor.service.MsAccessService;
import com.cold.storage.inbound.data.processor.service.ValidationServiceImpl;
import com.cold.storage.inbound.data.processor.utility.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
public class Processor {

    @Autowired
    private PropertiesUtil propertiesUtil;

    @Autowired
    private FileService fileService;

    @Autowired
    private MsAccessService msAccessService;

    @Autowired
    private ValidationServiceImpl validationServiceImpl;

/*    @Autowired
    EmailService emailService;*/

    @Scheduled(fixedDelay = 15000, initialDelay = 1000)
    public void run() {
        System.out.println("application started=> " + new Date().toString());
        File stopFile = new File(propertiesUtil.getStopFile());

        if (!stopFile.exists()) {
            processDataFiles();
        } else {
            System.out.println("Stop File exists ");
            String errorMessage = "Stop File exists: " + stopFile;
            String stopFileName = propertiesUtil.getStopFile();
            String subject = "Stop File exists";
            //emailService.generateEmailRequest(stopFileName, subject, errorMessage);
        }
    }

    public void processDataFiles() {
        try {
            File landingZoneDir = new File(propertiesUtil.getInbound());

            if (landingZoneDir.exists() && landingZoneDir.isDirectory()) {
                File[] trigFiles = landingZoneDir.listFiles(new TrigFileFilter());
                if (trigFiles.length > 0) {
                    for (File trigFile : trigFiles) {
                        if (trigFile.exists()) {
                            File dataFile = fileService.getDataFile(trigFile);
                            if (dataFile.exists()) {
                                boolean isValidFile = validationServiceImpl.validateFile(dataFile);
                                if (isValidFile) {
                                    System.out.println("start loading file: " + dataFile.getName());
                                    msAccessService.readMsAccessFile(dataFile);
                                    System.out.println("finished loading file: " + dataFile.getName());
                                    fileService.moveFile(dataFile, propertiesUtil.getArchive());
                                    fileService.moveFile(trigFile, propertiesUtil.getArchive());
                                    System.out.println("Processed cold storage File : " + dataFile.getName());
                                } else {
                                    System.out.println("invalid file: " + dataFile.getName());
                                    fileService.moveFile(trigFile, propertiesUtil.getErrorPath());
                                    fileService.moveFile(dataFile, propertiesUtil.getErrorPath());
                                }
                            } else {
                                System.out.println(String.format("dataFile does not exists for trig file %s moving trig to error directory", trigFile));
                                fileService.moveFile(trigFile, propertiesUtil.getErrorPath());
                            }
                        } else {
                            System.out.println("cold data trig file no longer exists : " + trigFile.getAbsolutePath());
                        }
                    }
                }
            } else {
                System.out.println("cold data Directory no longer exists : " + propertiesUtil.getInbound());
                String fileName = propertiesUtil.getInbound();
                String subject = "cold data Directory no longer exists";
                String emailMessage = "cold data Directory no longer exists " + fileName + " is not available";
                //emailService.generateEmailRequest(fileName, subject, emailMessage);
            }
        } catch (Exception ex) {
            System.out.println("Exception in processDataFiles method ");
        }
    }
}
