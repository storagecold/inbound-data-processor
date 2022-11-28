package com.cold.storage.inbound.data.processor.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ACCOUNT", schema = "COLD")
public class Account {
    @Id
    @Column(name = "COLD_ID", length = 5, nullable = false, unique = false)
    private int coldId;
    private int accountNumber;
    private float code;
    private String party;
    private String add1;
    private String City;
    private String ph;
    private int open;
    private int dr;
    private int cr;
    private int close;
    private int balance;
    private int rate;
    private String email;
    private Date IntDate;
    private String Nature;
}
