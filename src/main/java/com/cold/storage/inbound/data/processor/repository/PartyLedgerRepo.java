package com.cold.storage.inbound.data.processor.repository;

import com.healthmarketscience.jackcess.Table;

public interface PartyLedgerRepo {
    void loadPartyLedger(String name, int coldId, Table table);

}
