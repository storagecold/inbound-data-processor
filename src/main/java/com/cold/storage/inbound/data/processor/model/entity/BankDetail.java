
package com.cold.storage.inbound.data.processor.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity(name = "BANK_DETAIL")
@Table(name = "BANK_DETAIL", schema = "COLD")
@Data
public class BankDetail {
    @Id
    @Column(name = "COLD_ID")
    private int coldId;

    @Column(name = "BRANCH")
    private String branch;

    @Column(name = "ACC_No")
    private BigInteger accNo;

    @Column(name = "IFSC")
    private String ifsc;

    @Column(name = "CHEQUE")
    private String cheque;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "ACC_NAME")
    private String accName;
}

