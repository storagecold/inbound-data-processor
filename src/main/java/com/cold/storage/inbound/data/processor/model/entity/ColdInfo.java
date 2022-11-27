package com.cold.storage.inbound.data.processor.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "COLD_INFO", schema = "COLD")
public class ColdInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COLD_ID", length = 5, nullable = false, unique = false)
    private int id;

    private String coldName;
}
