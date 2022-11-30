package com.cold.storage.inbound.data.processor.repository;

import com.cold.storage.inbound.data.processor.model.entity.Account;
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
public class AccountRepoImpl implements AccountRepo {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void loadAccount(String name, int coldId, Table table) {
        int count = 0;

        for (Map<String, Object> row : table) {
            try {
                Account account = getAccount(coldId, row);
                this.entityManager.persist(account);
                count++;
            } catch (Exception ex) {
                log.warn(String.format("Exception in Method loadGrp for file: %s , Record: %s , ex: %s", name, row.toString(), ex.getMessage()));
            }

        }
        log.info("Rows for GRP Table --> " + count);


    }

    private Account getAccount(int coldId, Map<String, Object> row) {
        Account account = new Account();
        account.setColdId(coldId);
        account.set(row.get(Constants.DESCRIP).toString().trim());
        account.setColdId(row.get(Constants.ADD1));
        account.setColdId(row.get(Constants.UNDER));
        account.setColdId(row.get(Constants.OPEN));
        account.setColdId(row.get(Constants.OPEN));
        account.setColdId(row.get(Constants.CLOSE));
        account.setColdId(row.get(Constants.BALANCE));
        account.setColdId(row.get(Constants.NATURE));
        account.setColdId(row.get(Constants.OPENOTHER));
        account.setColdId(row.get(Constants.ACC_NAME));

        return account;
    }
}
