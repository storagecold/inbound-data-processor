package com.cold.storage.inbound.data.processor.repository;

import com.cold.storage.inbound.data.processor.utility.Constants;
import com.cold.storage.inbound.data.processor.utility.Utils;
import com.healthmarketscience.jackcess.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AmadRepoImpl implements AmadRepo {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String INSERT_AMAD_SQL = "INSERT INTO AMAD(COLD_ID,AMADNO,ENTRY,PARTY,VILL,PACKETS,KISM,YEAR,MARK) VALUES (:COLD_ID,:AMADNO,:ENTRY,:PARTY,:VILL,:PACKETS,:KISM,:YEAR,:MARK)";

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public void loadAmad(String name, int coldId, Table table) {
        int count = 0;

        for (Map<String, Object> row : table) {
            try {
                Map params = new HashMap<>();
                params.put(Constants.COLD_ID, coldId);
                params.put(Constants.AMADNO, row.get(Constants.AMADNO));
                params.put(Constants.ENTRY, row.get(Constants.DATE));
                params.put(Constants.PARTY, row.get(Constants.PARTY));
                params.put(Constants.VILL, row.get(Constants.VILL));
                params.put(Constants.PACKETS, row.get(Constants.PKT3));
                params.put(Constants.KISM, row.get(Constants.KISM));
                params.put(Constants.YEAR, Utils.getYear(row.get(Constants.DATE)));
                params.put(Constants.MARK, row.get(Constants.MARK1));
                count++;
                namedJdbcTemplate.update(INSERT_AMAD_SQL, params);
            } catch (DataIntegrityViolationException ex) {
                log.warn(String.format("DataIntegrityViolationException in Method loadAmad for file: %s , Record: %s , ex: %s", name, row.toString(), ex.getMessage()));
            } catch (Exception ex) {
                log.warn(String.format("Exception in Method loadAmad for file: %s , Record: %s , ex: %s", name, row.toString(), ex.getMessage()));
            }
        }
        log.info("count --> " + count);
    }

}
