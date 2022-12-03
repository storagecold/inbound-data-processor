package com.cold.storage.inbound.data.processor.repository;

import com.cold.storage.inbound.data.processor.model.entity.Account;
import com.cold.storage.inbound.data.processor.utils.Constants;
import com.healthmarketscience.jackcess.Table;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
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
        log.info("Rows for Account Table --> " + count);
    }

    private Account getAccount(int coldId, Map<String, Object> row) {
        Account account = new Account();
        account.setColdId(coldId);
        System.out.println(row.get(Constants.ACCOUNT_NUMBER));
        account.setAccountNumber((Long) row.get(Constants.ACCOUNT_NUMBER));
        account.setAccountName((String) row.get(Constants.DESCRIP));
        account.setAddress((String) row.get(Constants.ADDRESS));
        account.setCity((String) row.get(Constants.CITY));
        account.setPhone((String) row.get(Constants.PHONE));
        account.setAccountType((String) row.get(Constants.ACCOUNT_TYPE));
        account.setOpeningBalance((Double) row.get(Constants.OPENING_BALANCE));
        account.setCloseBalance((Double) row.get(Constants.CLOSING_BALANCE));
        account.setInterestRate((Float) row.get(Constants.INTEREST_RATE));
        account.setEmailId((String) row.get(Constants.EMAIL_ID));
        account.setEntryDate((LocalDate) row.get(Constants.ENTRY_DATE));
        account.setTransactionType((String) row.get(Constants.TRANSACTION_TYPE));
        return account;

    }
}
