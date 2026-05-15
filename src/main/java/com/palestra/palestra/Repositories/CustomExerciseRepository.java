package com.palestra.palestra.Repositories;

import com.palestra.palestra.pojo.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomExerciseRepository {
    private final JdbcTemplate jdbc;

    @Autowired
    public CustomExerciseRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void addProgram(String programName, List<Exercise> exercises) {
        final String sql = "INSERT INTO Exercises VALUES (?, ?, ?, ?, ?)";
        for (Exercise e : exercises) {
            jdbc.update(sql, e.getName(), e.getSets(), e.getReps(), e.getKcal(), programName);
        }
    }
}
