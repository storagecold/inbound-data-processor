package com.cold.storage.inbound.data.processor.step;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class GenerateFileIdStep {
	
	String fileId;
	
	public String getNewFileId() {
		fileId = UUID.randomUUID().toString();
		return fileId;
	}
	
	public String getPreviouslyGeneratedFileId() {
		return fileId;
	}

}
