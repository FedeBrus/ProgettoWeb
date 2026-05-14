package com.palestra.palestra.Repositories;

import com.palestra.palestra.pojo.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<Review> getReviews() {
        return jdbc.query("SELECT username, review FROM Reviews", (r, i) ->
            new Review(r.getString(1), r.getString(2))
        );
    }
}
