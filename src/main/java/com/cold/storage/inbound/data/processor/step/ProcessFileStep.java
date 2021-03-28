package com.cold.storage.inbound.data.processor.step;

import com.cold.storage.inbound.data.processor.utility.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.File;


@Component
@Slf4j
public class ProcessFileStep {

/*    @Autowired
    SubmittersUtil submittersUtil;*/

    @Autowired
    PropertiesUtil propertiesUtil;

    File file;
    String fileSubmitterId;
    String fileName = "";

    public void setFile(File f) {
        file = f;

        if (null != file) {
            fileSubmitterId = file.getName().substring(0, file.getName().indexOf('.'));
            fileName = file.getName().replace(propertiesUtil.getTrigFileExtension(), "");
        }
    }

    public ProcessFileStep() {
    }
}
