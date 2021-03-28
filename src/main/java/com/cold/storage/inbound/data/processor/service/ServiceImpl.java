package com.cold.storage.inbound.data.processor.service;

import com.cold.storage.inbound.data.processor.utility.Constants;
import com.cold.storage.inbound.data.processor.utility.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    public static boolean invalid_fname = false;
    private final static String PROFILE_DEFINITION_ENDPOINT = "/definition?submitterid=%s";
    private final static String PROFILE_FULL_DETAIL_ENDPOINT = "/fulldetail?submitterid=%s&profileversion=%s";
    private final static String DEST_APP_NAME = "profile-api";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    PropertiesUtil propertiesUtil;

/*    @Autowired
    EmailService emailService;

    @Autowired
    CustomerProfileService customerProfileService;*/

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
                System.out.println("e.toString(): " + e.toString());
                System.out.println("Error in Method : copyFile(File file, String dest), could not copy file: " + file.getName());
                System.out.println("Invalid destination path");
                isFileCopied = false;
            }
        } else {
            System.out.println("dest or file is not valid");
            isFileCopied = false;
        }
        if (Files.isDirectory(target)) {
            target = Paths.get(dest + file.getName());
        } else {
            System.out.println("target is not a directory");
            isFileCopied = false;
        }

        if (Objects.nonNull(src) && Objects.nonNull(target)) {
            try {
                Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING);
                isFileCopied = true;
                System.out.println("File copied succesfully  :  " + file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("e.toString(): " + e.toString());
                System.out.println("Error in Method : copyFile(File file, String dest), could not copy file: " + file.getName());
                System.out.println("Copying " + file.getName() + " was not succesful");
                isFileCopied = false;
            }
        } else {
            System.out.println("src or target is not valid");
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
            System.out.println("File is Null ");
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
            System.out.println("File Null");
            isFileMoved = false;
        }
        if (!file.exists()) {
            System.out.println("File does not exist : " + file.getName());
            isFileMoved = false;
        }

        if (Objects.isNull(dest)) {
            System.out.println("Dest Null");
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
                System.out.println(String.format("File %s successfully moved to : %s ", fileName, dest)
                );
                isFileMoved = true;
            } else {
                System.out.println(String.format("File %s failed to move to %s: " + fileName, dest));
            }
            isFileMoved = false;
        } else {
            System.out.println(String.format("Destination directory %s does not exists: " + dest));
            isFileMoved = false;
        }
        return isFileMoved;
    }

    @Override
    public String getFileType(File file) {
        String fileType = null;
        System.out.println(String.format("%s in getLob Method:", file));

        if (file.getName().endsWith(Constants.MDB_TRIG_EXT)) {
            fileType = Constants.MDB_EXT;
        }
        return fileType;
    }

    public void moveIncorrectFileToErrorDirectory(File file) {

        System.out.println("Method : moveIncorrectFileToErrorDirectory(File file)");
        try {
            moveFile(file, propertiesUtil.getErrorPath());
        } catch (Exception ex) {
            System.out.println("Error in Method: moveIncorrectFileToErrorDirectory while moving file " + file.getName());
            String errorMessage = String.format("Error in Method: moveIncorrectFileToErrorDirectory while moving file %s", file.getName());
            String fileName = file.getName();
            String subject = "files moved to error directory";
            //TODO
            //emailService.generateEmailRequest(fileName, subject, errorMessage);
        }
        System.out.println(String.format("File %s moved to error directory file ", file.getName()));
    }

    public String getSubmitterIdFromFile(File file) throws Exception {
        String submitterId = null;
        try {
            String name = file.getName();
            int id = name.indexOf(".");
            if (id > -1) {

                submitterId = name.substring(0, id);
            } else {
                System.out.println("Error in getting the SubmitterId From FileName from getSubmitterIdFromFile :" + file.getName());
                throw new Exception("Can not Extract SubmitterId from File: " + name);
            }
        } catch (Exception ex) {
            System.out.println("Error in Method: getSubmitterIdFromFileName " + file.getName());
            throw ex;
        }
        return submitterId;
    }

    /*
     * Author:
     *
     * Moves file to archive directories based on the security type of the file
     */

    public boolean moveFileToArchiveDirectory(File file) {
        boolean isFileMovedToArchive = false;
        System.out.println("Method: moveFileToArchiveDirectory " + file.getAbsolutePath());

        String submitterId = "";
        String fileId = "";
        String dest = "";

        try {
            submitterId = getSubmitterIdFromFile(file);
            fileId = Long.toString(System.currentTimeMillis());
            //TODO
            /*SubmitterFullDetail submitterFullDetail = customerProfileService.getSubmitterFullDetail(fileId, submitterId);

            String eligSysCd = submitterFullDetail.getEligibilitySystem().getCode().trim();
            String mktSegCd = submitterFullDetail.getMarketSegment().getCode().trim();
            String locationCd = submitterFullDetail.getDemographics().getLocTypeCode().trim();
            String policyNumber = submitterFullDetail.getPolicyNumber();
            dest = getSecurityPath(eligSysCd, mktSegCd, locationCd, policyNumber);*/

        } catch (Exception ex) {
            System.out.println("Error in Method: moveFileToArachiveDirectory " + file.getName() + ex);
            isFileMovedToArchive = false;
        }
        if (StringUtils.hasText(dest)) {
            isFileMovedToArchive = moveFile(file, dest);
            if (!isFileMovedToArchive) {
                System.out.println("Moving to Archive directory was not succesfull, file: " + file.getName());
                isFileMovedToArchive = false;
            }
        }
        return isFileMovedToArchive;
    }


}
