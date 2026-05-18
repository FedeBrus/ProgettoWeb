package com.palestra.palestra.Repositories;

import com.palestra.palestra.pojo.Exercise;
import com.palestra.palestra.pojo.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomExerciseRepository {
    private final JdbcTemplate jdbc;

    @Autowired
    public CustomExerciseRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public void addProgram(String username, String programName, List<Exercise> exercises) {
        final String sql = "INSERT INTO Exercises VALUES (?, ?, ?, ?, ?, ?)";
        for (Exercise e : exercises) {
            jdbc.update(sql, username, e.getName(), e.getSets(), e.getReps(), e.getKcal(), programName);
        }
    }

    public List<Program> getCustomPrograms(String username) {
        // TODO: Probabilmente si può usare una query sola e iterare i risultati e salvare man mano
        // TODO: i vari programmi, per ora faccio così

        final String getProgramNames = "SELECT DISTINCT program_name FROM EXERCISES WHERE username=?";
        List<String> programs = jdbc.query(getProgramNames, (r, i) -> r.getString(1), username);

        final String createPrograms = "SELECT * FROM EXERCISES WHERE username = ? AND program_name = ?";
        ArrayList<Program> programList = new ArrayList<>();

        for(String p : programs) {
            List<Exercise> exercises = jdbc.query(createPrograms, (r, i) -> {
                return new Exercise(r.getString(2), r.getInt(3), r.getInt(4), r.getInt(5));
            }, username, p);

            programList.add(new Program(p, username, exercises));
        }

        return programList;
    }

    public Program getCustomProgram(String username, String programName) throws ClassNotFoundException {
        final String sql = "SELECT * FROM Exercises WHERE username = ? AND program_name = ?";

        Program ret = new Program(programName, username, jdbc.query(sql, (r, i) -> {
            return new Exercise(r.getString(2), r.getInt(3), r.getInt(4), r.getInt(5));
        }, username, programName));

        if(ret.getExercises() == null || ret.getExercises().isEmpty()) {
            throw new ClassNotFoundException("Could not find program");
        }

        return ret;
    }
}
