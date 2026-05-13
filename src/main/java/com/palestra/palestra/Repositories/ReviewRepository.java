package com.palestra.palestra.Repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepository {
    private final JdbcTemplate jdbc;

    @Autowired
    public ReviewRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insertReview(String username, String review) {
        final String sql = "INSERT INTO Reviews(username, review) VALUES (?, ?)";
        jdbc.update(sql, username, review);
    }
}
