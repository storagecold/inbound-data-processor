package com.cold.storage.inbound.data.processor.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNT", schema = "COLD")
public class Account {
    @Id
    @Column(name = "COLD_ID", length = 5, nullable = false, unique = false)
    private int coldId;
    private int accountNumber;
}
