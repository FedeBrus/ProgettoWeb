package com.palestra.palestra.Services.Trial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class TrialUserChecker {
    private final JdbcTemplate jdbc;

    @Autowired
    public TrialUserChecker(JdbcTemplate jdcb) {
        this.jdbc = jdcb;
    }

    public int getTimesTrained(String username) {
        final String sql = "SELECT times FROM UserUsageStatistics WHERE username = ?";
        int times = 0;
        RowMapper<Integer> rm = (r, i) -> {
            return r.getInt(1);
        };

        return jdbc.query(sql, rm, username).stream().mapToInt(i -> i).sum();
    }

}
