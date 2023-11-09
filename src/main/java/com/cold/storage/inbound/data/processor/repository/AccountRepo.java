package com.cold.storage.inbound.data.processor.repository;

import com.healthmarketscience.jackcess.Table;

public interface AccountRepo {
    void loadAccount(String name, int coldId, Table table);
}
