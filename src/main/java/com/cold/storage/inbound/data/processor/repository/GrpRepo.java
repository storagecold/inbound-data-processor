package com.cold.storage.inbound.data.processor.repository;

import com.healthmarketscience.jackcess.Table;

public interface GrpRepo {
    void loadGrp(String name, int coldId, Table table);

}
