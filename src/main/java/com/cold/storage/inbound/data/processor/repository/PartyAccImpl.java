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
public class PartyAccImpl implements PartyAccRepo {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String INSERT_PARTY_ACC_SQL = "INSERT INTO PartyAcc(COLD_ID,Code,BankCode,Branch,AccNo,IFSC,Chq1,Chq2,Chq3,BankName,AccName,Want2Print) " +
            "VALUES (:COLD_ID,:Code,:BankCode,:Branch,:AccNo,:IFSC,:Chq1,:Chq2,:Chq3,:BankName,:AccName,Want2Print)" +
            "ON DUPLICATE KEY UPDATE " +
            "Code=:Code,BankCode=:BankCode,Branch=:Branch,Chq1=:Chq1,Chq2=:Chq2,Chq3=:Chq3,BankName=:BankName,AccName=:AccName,Want2Print=:Want2Print";

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public void loadPartyAcc(String name, int coldId, Table table) {
        int count = 0;

        for (Map<String, Object> row : table) {
            try {
                Map<String, Object> params = getPartyAccParamMap(coldId, row);
                count++;
                namedJdbcTemplate.update(INSERT_PARTY_ACC_SQL, params);
            } catch (DataIntegrityViolationException ex) {
                log.warn(String.format("DataIntegrityViolationException in Method loadPartyAcc for file: %s , Record: %s , ex: %s", name, row.toString(), ex.getMessage()));
            } catch (Exception ex) {
                log.warn(String.format("Exception in Method loadPartyAcc for file: %s , Record: %s , ex: %s", name, row.toString(), ex.getMessage()));
            }
        }
        log.info("Rows for PartyAcc Table --> " + count);
    }

    private Map<String, Object> getPartyAccParamMap(int coldId, Map<String, Object> row) {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.COLD_ID, coldId);
        params.put(Constants.CODE, row.get(Constants.CODE));
        params.put(Constants.BANK_CODE, row.get(Constants.BANK_CODE));
        params.put(Constants.BRANCH, row.get(Constants.BRANCH));
        params.put(Constants.ACC_NO, row.get(Constants.ACC_NO));
        params.put(Constants.IFSC, row.get(Constants.IFSC));
        params.put(Constants.CHQ1, row.get(Constants.CHQ1));
        params.put(Constants.CHQ2, row.get(Constants.CHQ2));
        params.put(Constants.CHQ3, row.get(Constants.CHQ3));
        params.put(Constants.BANK_NAME, row.get(Constants.BANK_NAME));
        params.put(Constants.ACCOUNT_NAME, row.get(Constants.ACCOUNT_NAME));
        params.put(Constants.WANT_2PRINT, row.get(Constants.WANT_2PRINT));

        return params;
    }
}
