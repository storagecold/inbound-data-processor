package com.cold.storage.inbound.data.processor.service;

import java.io.File;

public interface ValidationService {
    boolean validateFile(File file);
}
