package com.cold.storage.inbound.data.processor.model;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Data
public class ColdInfo {
    private int coldId;
    private String coldSubmitter;
    private String coldName;
    private String faceBookUrl;
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date lastUpdated;
}
