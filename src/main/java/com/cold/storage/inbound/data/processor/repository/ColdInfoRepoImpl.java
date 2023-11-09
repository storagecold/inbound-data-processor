package com.cold.storage.inbound.data.processor.repository;

import com.cold.storage.inbound.data.processor.model.pojo.ColdInfo;
import com.cold.storage.inbound.data.processor.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Repository
public class ColdInfoRepoImpl implements ColdInfoRepo {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String SELECT_SUBMITTER_QUERY = "select * from cold_info where COLD_SUBMITTER = :submitterId";
    private static final String SELECT_COLD_ID_QUERY = "select * from cold_info where COLD_SUBMITTER = :submitterId";

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public String getSubmitterId(String submitterId) {
        return Objects.requireNonNull(namedJdbcTemplate.queryForObject(SELECT_SUBMITTER_QUERY, new MapSqlParameterSource(
                "submitterId", submitterId), new ColdInfoRowMapper())).getColdSubmitter();
    }

    @Override
    public int getColdId(String submitterId) {
        return Objects.requireNonNull(namedJdbcTemplate.queryForObject(SELECT_SUBMITTER_QUERY, new MapSqlParameterSource(
                "submitterId", submitterId), new ColdInfoRowMapper())).getColdId();
    }

    public static final class ColdInfoRowMapper implements RowMapper<ColdInfo> {

        @Override
        public ColdInfo mapRow(ResultSet rs, int i) throws SQLException {
            ColdInfo coldInfo = new ColdInfo();
            coldInfo.setColdId(rs.getInt(Constants.COLD_ID));
            coldInfo.setColdSubmitter(rs.getString(Constants.COLD_SUBMITTER));
            coldInfo.setFaceBookUrl(rs.getString(Constants.FACEBOOK_URL));
            return coldInfo;
        }
    }
}