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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

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
        log.info(String.format("Rows for Account Table : {0}", count));
    }

    private Account getAccount(int coldId, Map<String, Object> row) {
        Account account = new Account();
        account.setColdId(coldId);
        account.setAccountNumber(Long.parseLong((String) row.get(Constants.ACCOUNT_NUMBER)));
        account.setAccountName((Objects.nonNull(row.get(Constants.DESCRIP))) ? ((String) row.get(Constants.DESCRIP)) : Constants.EMPTY);
        account.setAddress((Objects.nonNull(row.get(Constants.ADDRESS))) ? ((String) row.get(Constants.ADDRESS)) : Constants.EMPTY);
        account.setCity((Objects.nonNull(row.get(Constants.CITY))) ? ((String) row.get(Constants.CITY)) : Constants.EMPTY);
        account.setPhone((Objects.nonNull(row.get(Constants.PHONE))) ? ((String) row.get(Constants.PHONE)) : Constants.EMPTY);
        account.setAccountType((Objects.nonNull(row.get(Constants.ACCOUNT_TYPE))) ? ((String) row.get(Constants.ACCOUNT_TYPE)) : Constants.EMPTY);
        account.setOpeningBalance((Objects.nonNull(row.get(Constants.OPENING_BALANCE))) ? ((BigDecimal) row.get(Constants.OPENING_BALANCE)) : Constants.BIG_DECIMAL_ZERO);
        account.setCloseBalance((Objects.nonNull(row.get(Constants.CLOSING_BALANCE))) ? ((BigDecimal) row.get(Constants.CLOSING_BALANCE)) : Constants.BIG_DECIMAL_ZERO);
        account.setInterestRate((Objects.nonNull(row.get(Constants.INTEREST_RATE))) ? ((BigDecimal) row.get(Constants.INTEREST_RATE)) : Constants.BIG_DECIMAL_ZERO);
        account.setEmailId((Objects.nonNull(row.get(Constants.EMAIL_ID))) ? ((String) row.get(Constants.EMAIL_ID)) : Constants.EMPTY);
        account.setEntryDate((Objects.nonNull(row.get(Constants.ENTRY_DATE))) ? ((LocalDate) row.get(Constants.ENTRY_DATE)) : Constants.LOCAL_DATE_DEFAULT);
        account.setDebitAmount((Objects.nonNull(row.get(Constants.DEBIT_AMOUNT))) ? ((BigDecimal) row.get(Constants.DEBIT_AMOUNT)) : Constants.BIG_DECIMAL_ZERO);
        account.setCreditAmount((Objects.nonNull(row.get(Constants.CREDIT_AMOUNT))) ? ((BigDecimal) row.get(Constants.CREDIT_AMOUNT)) : Constants.BIG_DECIMAL_ZERO);
        return account;
    }
}
