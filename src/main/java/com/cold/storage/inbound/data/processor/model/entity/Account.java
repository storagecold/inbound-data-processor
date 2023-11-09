package com.cold.storage.inbound.data.processor.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ACCOUNT", schema = "COLD")
@Data
public class Account implements Serializable {
    @Id
    @Column(name = "COLD_ID")
    private int coldId;

    @Id
    @Column(name = "ACCOUNT_NUMBER")
    private long accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "ADDRESS")
    private String  address;

    @Column(name = "CITY")
    private String city;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @Column(name = "OPENING_BALANCE")
    private BigDecimal openingBalance;

    @Column(name = "CLOSING_BALANCE")
    private BigDecimal closeBalance;

    @Column(name = "INTEREST_RATE")
    private BigDecimal interestRate;

    @Column(name = "EMAIL_ID")
    private String emailId;

    @Column(name = "ENTRY_DATE")
    private LocalDate entryDate;

    @Column(name = "DEBIT_AMOUNT")
    private BigDecimal debitAmount;

    @Column(name = "CREDIT_AMOUNT")
    private BigDecimal creditAmount;
}
