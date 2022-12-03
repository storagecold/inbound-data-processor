
package com.cold.storage.inbound.data.processor.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "AMAD", schema = "COLD")
@Data
public class Amad implements Serializable {
    @Id
    @Column(name = "COLD_ID")
    private int coldId;

    @Id
    @Column(name = "AMADNO")
    private int amadNo;

    @Column(name = "PARTY")
    private String party;

    @Column(name = "VILL")
    private String vill;

    @Column(name = "DISTT")
    private String dist;

    @Column(name = "PACKETS")
    private int packets;

    @Column(name = "COMM")
    private String comm;

    @Column(name = "KISM")
    private String kism;

    @Column(name = "MARK")
    private String mark;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "ROOM")
    private String room;

    @Column(name = "CHHATTA")
    private int chhatta;

    @Column(name = "GULLA")
    private int gulla;

    @Column(name = "KIRRI")
    private int kirri;
}

