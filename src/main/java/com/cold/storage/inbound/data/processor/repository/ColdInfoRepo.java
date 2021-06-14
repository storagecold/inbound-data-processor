package com.cold.storage.inbound.data.processor.repository;

public interface ColdInfoRepo {
    String getSubmitterId(String submitterId);

    int getColdId(String submitterId);
}
