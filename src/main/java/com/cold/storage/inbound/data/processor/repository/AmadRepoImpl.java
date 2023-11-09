package com.cold.storage.inbound.data.processor.repository;

import com.cold.storage.inbound.data.processor.model.entity.Amad;
import com.cold.storage.inbound.data.processor.utils.Constants;
import com.healthmarketscience.jackcess.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

@Repository
public class AmadRepoImpl implements AmadRepo {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void loadAmad(String name, int coldId, Table table) {
        int count = 0;

        for (Map<String, Object> row : table) {
            try {
                Amad amad = getAmad(coldId, row);
                this.entityManager.persist(amad);
                count++;
            } catch (Exception ex) {
                log.warn(String.format("Exception in Method loadGrp for file: %s , Record: %s , ex: %s", name, row.toString(), ex.getMessage()));
            }
        }
        log.info("Rows for Amad Table --> " + count);
    }

    private Amad getAmad(int coldId, Map<String, Object> row) {
        Amad amad = new Amad();
        amad.setColdId(coldId);
        amad.setAmadNo((Integer) row.get(Constants.AMADNO));
        amad.setParty((String) row.get(Constants.PARTY));
        amad.setVill((String) row.get(Constants.VILL));
        amad.setDist((String) row.get(Constants.PACKETS));
        amad.setComm((String) row.get(Constants.COMM));
        amad.setKism((String) row.get(Constants.KISM));
        amad.setMark((String) row.get(Constants.MARK));
        amad.setYear((Integer) row.get(Constants.YEAR));
        amad.setRoom((String) row.get(Constants.ROOM));
        amad.setChhatta((Integer) row.get(Constants.CHHATTA));
        amad.setGulla((Integer) row.get(Constants.GULLA));
        amad.setKirri((Integer) row.get(Constants.KIRRI));
        return amad;
    }
}