package com.cold.storage.inbound.data.processor.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ACCOUNT", schema = "COLD")
public class Account implements Serializable {
    @Id
    @Column(name = "COLD_ID")
    private int coldId;

    @Id
    @Column(name = "ACCOUNT_NUMBER")
    private long accountNumber;

    @Column(name = "ADDRESS")
    private String  address;

    @Column(name = "CITY")
    private String city;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @Column(name = "OPENING_BALANCE")
    private double openingBalance;

    @Column(name = "CLOSING_BALANCE")
    private double closeBalance;

    @Column(name = "INTEREST_RATE")
    private float interestRate;

    @Column(name = "EMAIL_ID")
    private String emailId;

    @Column(name = "ENTRY_DATE")
    private Date entryDate;

    @Column(name="TRANSACTION_TYPE")
    private String transactionType;
}
