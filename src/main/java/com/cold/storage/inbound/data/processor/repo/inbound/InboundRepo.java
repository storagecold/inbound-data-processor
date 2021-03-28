package com.cold.storage.inbound.data.processor.repo.inbound;

import com.healthmarketscience.jackcess.Table;

public interface InboundRepo {
    void loadAmad(Table table);
}
