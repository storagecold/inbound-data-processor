package com.cold.storage.inbound.data.processor.service;

import com.cold.storage.inbound.data.processor.repository.ColdInfoRepo;
import com.cold.storage.inbound.data.processor.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;

@Service
public class ValidationServiceImpl implements ValidationService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String COLD_SUBMITTER_DOES_NOT_EXISTS = "cold Submitter: %s  does not exists";
    @Autowired
    ColdInfoRepo coldInfoRepo;

    @Override
    public boolean validateFile(File file) {
        boolean isColdExists = false;
        String submitter = Utils.getSubmitter(file);
        String coldSubmitter = coldInfoRepo.getSubmitterId(submitter);

        if (StringUtils.hasText(coldSubmitter)) {
            isColdExists = true;
        } else {
            log.error(String.format(COLD_SUBMITTER_DOES_NOT_EXISTS, coldSubmitter));
        }
        return isColdExists;
    }
}
