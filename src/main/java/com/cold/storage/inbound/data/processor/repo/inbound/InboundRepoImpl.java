package com.cold.storage.inbound.data.processor.repo.inbound;

import com.cold.storage.inbound.data.processor.model.Amad;
import com.cold.storage.inbound.data.processor.utility.Constants;
import com.healthmarketscience.jackcess.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class InboundRepoImpl implements InboundRepo {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void loadAmad(Table table) {
        int count = 0;
        List<Amad> amadList = new ArrayList<>(table.getRowCount());

        for (Map<String, Object> row : table) {
            Amad amad = new Amad();
            amad.setAmadno((Integer) row.get(Constants.AMADNO));
            amad.setParty((String) row.get(Constants.PARTY));
            amad.setVill((String) row.get(Constants.VILL));
            amad.setMark1((String) row.get(Constants.MARK1));
            amad.setYear((Integer) row.get(Constants.YR));
            amadList.add(amad);
            count++;
        }
        logger.info("amadList --> " + amadList.toString());
        System.out.println("amadList --> " + amadList.toString());
        logger.info("count --> " + count);
    }
}
