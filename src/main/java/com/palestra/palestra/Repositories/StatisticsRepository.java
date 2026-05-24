package com.palestra.palestra.Repositories;

import com.palestra.palestra.pojo.Stats.GlobalStatEntry;
import com.palestra.palestra.pojo.Stats.PersonalStatEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class StatisticsRepository {
    private final JdbcTemplate jdbc;

    @Autowired
    public StatisticsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<GlobalStatEntry> getGlobalStatistics() {
        String sql = """
            SELECT a.authority, uus.program, AVG(uus.times)
            FROM UserData AS ud JOIN Authorities AS a ON ud.username = a.username JOIN UserUsageStatistics uus ON ud.username = uus.username
            WHERE a.authority IN ('ROLE_USER_PRO', 'ROLE_USER_BASIC')
            GROUP BY a.authority, uus.program;
        """;

        RowMapper<GlobalStatEntry> rm = (r, i) -> {
            return new GlobalStatEntry(r.getString(1), r.getString(2), r.getDouble(3));
        };

        return jdbc.query(sql, rm);
    }

    public List<PersonalStatEntry> getPersonalStats(String username) {
        String sql = """
            SELECT uus.program, uus.times
            FROM UserUsageStatistics AS uus
            WHERE uus.username = ?;
        """;

        RowMapper<PersonalStatEntry> rm = (r, i) -> {
            return new PersonalStatEntry(r.getString(1), r.getInt(2));
        };

        return jdbc.query(sql, rm, username);
    }
}
