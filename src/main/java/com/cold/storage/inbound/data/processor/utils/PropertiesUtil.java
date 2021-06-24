package com.cold.storage.inbound.data.processor.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesUtil {

    @Value("${inbound}")
    @Getter
    private String inbound;

    @Value("${archive}")
    @Getter
    private String archive;

    @Value("${stopFile}")
    @Getter
    private String stopFile;

    @Value("${errorPath}")
    @Getter
    String errorPath;

/*    @Value("${profile.api.url}")
    @Getter
    private String baseUrl;*/

    @Value("${spring.application.name}")
    @Getter
    private String appName;

    @Value("${extension.file.trig}")
    @Getter
    private String trigFileExtension;
}
