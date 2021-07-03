package com.cold.storage.inbound.data.processor.repository;

import com.cold.storage.inbound.data.processor.utils.Constants;
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
public class PartyLedgerRepoImpl implements PartyLedgerRepo {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String INSERT_GRP_SQL = "INSERT INTO GRP(COLD_ID,PARTY,ADD1,UNDER,OPEN,DR,CLOSE,BALANCE,NATURE,OPENOTHER,AccName) " +
            "VALUES (:COLD_ID,:descrip,:add1,:under,:open,:dr,:close,:balance,:Nature,:openOTHER,:AccName)" +
            "ON DUPLICATE KEY UPDATE " +
            "ADD1=:add1,UNDER=:under,OPEN=:open,DR=:dr,CLOSE=:close,BALANCE=:balance,NATURE=:Nature,OPENOTHER=:openOTHER,AccName=:AccName";

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public void loadPartyLedger(String name, int coldId, Table table) {
        int count = 0;

        for (Map<String, Object> row : table) {
            try {
                Map<String, Object> params = getGRPParamMap(coldId, row);
                count++;
                namedJdbcTemplate.update(INSERT_GRP_SQL, params);
            } catch (DataIntegrityViolationException ex) {
                log.warn(String.format("DataIntegrityViolationException in Method loadGrp for file: %s , Record: %s , ex: %s", name, row.toString(), ex.getMessage()));
            } catch (Exception ex) {
                log.warn(String.format("Exception in Method loadGrp for file: %s , Record: %s , ex: %s", name, row.toString(), ex.getMessage()));
            }
        }
        log.info("Rows for GRP Table --> " + count);
    }

    private Map<String, Object> getGRPParamMap(int coldId, Map<String, Object> row) {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.COLD_ID, coldId);
        params.put(Constants.DESCRIP, row.get(Constants.DESCRIP).toString().trim());
        params.put(Constants.ADD1, row.get(Constants.ADD1));
        params.put(Constants.UNDER, row.get(Constants.UNDER));
        params.put(Constants.OPEN, row.get(Constants.OPEN));
        params.put(Constants.DR, row.get(Constants.OPEN));
        params.put(Constants.CLOSE, row.get(Constants.CLOSE));
        params.put(Constants.BALANCE, row.get(Constants.BALANCE));
        params.put(Constants.NATURE, row.get(Constants.NATURE));
        params.put(Constants.OPENOTHER, row.get(Constants.OPENOTHER));
        params.put(Constants.ACC_NAME, row.get(Constants.ACC_NAME));

        return params;
    }
}
