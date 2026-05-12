package com.palestra.palestra.Repositories;

import com.palestra.palestra.pojo.SecurityUser;
import com.palestra.palestra.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRepository(JdbcTemplate jdbc,
                          UserDetailsManager userDetailsManager,
                          PasswordEncoder passwordEncoder) {
        this.jdbc = jdbc;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void addUser(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        u.setReg_date(LocalDate.now());
        userDetailsManager.createUser(new SecurityUser(u));

        String sql = "INSERT INTO UserData VALUES (?, ?, ?, ?, ?, ?)";
        jdbc.update(sql,
                u.getUsername(),
                u.getName(),
                u.getSurname(),
                u.getDate_of_birth(),
                u.getEmail(),
                java.sql.Date.valueOf(u.getReg_date())
        );
    }


    public boolean userExists(String username) {
        return userDetailsManager.userExists(username);
    }

}
