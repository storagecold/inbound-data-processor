package com.cold.storage.inbound.data.processor.repository;

import com.healthmarketscience.jackcess.Table;

public interface AmadRepo {
    void loadAmad(String name, int coldId, Table table);

}
