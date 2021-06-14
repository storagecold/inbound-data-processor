package com.cold.storage.inbound.data.processor.repository;

import com.cold.storage.inbound.data.processor.utility.Constants;
import com.cold.storage.inbound.data.processor.utility.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class FileDetailRepoImpl implements FileDetailRepo {
    private static final String INSERT_FILE_DETAIL_SQL = "INSERT INTO FILE_DETAIL(FILE_NAME,COLD_SUBMITTER,RECEIVED,COMEPLETED,STATUS) VALUES (:FILE_NAME,:COLD_SUBMITTER,:RECEIVED,:COMEPLETED,:STATUS)";
    private static final String UPDATE_QUERY = "UPDATE FILE_DETAIL SET STATUS = :STATUS WHERE FILE_ID= :FILE_ID";

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public void insertFileDetail(File file) {
        Map params = new HashMap<>();
        params.put(Constants.FILE_NAME, file.getName());
        params.put(Constants.COLD_SUBMITTER, Utils.getSubmitter(file));
        params.put(Constants.RECEIVED, new Date());
        params.put(Constants.COMEPLETED, new Date());
        params.put(Constants.STATUS, Constants.INPROGRESS);

        namedJdbcTemplate.update(INSERT_FILE_DETAIL_SQL, params);
    }

    @Override
    public int updateFileDetail(int fileId, String status) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(Constants.FILE_ID, fileId);
        paramMap.put(Constants.STATUS, status);
        return namedJdbcTemplate.update(UPDATE_QUERY, paramMap);
    }
}
