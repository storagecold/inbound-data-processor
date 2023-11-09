package com.cold.storage.inbound.data.processor.model.entity;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "COLD_INFO", schema = "COLD")
public class ColdInfo {
    @Id
    @Column(name = "COLD_ID")
    private int coldId;

    @Column(name = "COLD_SUBMITTER")
    private String coldSubmitter;

    @Column(name = "COLD_NAME")
    private String coldName;

    @Column(name = "ADDRESS_ID")
    private int addressId;

    @Column(name = "CONTACT_ID")
    private int contactId;

    @Column(name = "OWNER_ID")
    private int ownerId;

    @Column(name = "FACEBOOK_URL")
    private String facebookUrl;

    @Column(name = "LAST_UPDATED")
    private LocalDateTime lastUpdated;
}
