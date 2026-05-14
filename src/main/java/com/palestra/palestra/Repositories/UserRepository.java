package com.palestra.palestra.Repositories;

import com.palestra.palestra.pojo.SecurityUser;
import com.palestra.palestra.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Transactional
    public Authentication changeUserRole(String username, SimpleGrantedAuthority newRole, Authentication auth) {
        UserDetails deets = userDetailsManager.loadUserByUsername(username);
        SecurityUser s = new SecurityUser(new User(username, deets.getPassword(), newRole.getAuthority()));
        userDetailsManager.updateUser(s);

        UserDetails updated = userDetailsManager.loadUserByUsername(username);
        Authentication refreshed = new UsernamePasswordAuthenticationToken(updated, auth.getCredentials(), updated.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(refreshed);
        return refreshed;
    }

    @Transactional
    public void changePassword(String username, String newPassword) {
        String password = userDetailsManager.loadUserByUsername(username).getPassword();

        userDetailsManager.changePassword(Objects.requireNonNull(password), Objects.requireNonNull(passwordEncoder.encode(newPassword)));
    }

    public boolean userExists(String username) {
        return userDetailsManager.userExists(username);
    }

    public int getTotalTrainingProgramsUsed(String username) {
        final String sql = "SELECT times FROM UserUsageStatistics WHERE username = ?";
        RowMapper<Integer> rm = (r, i) -> r.getInt(1);

        return jdbc.query(sql, rm, username).stream().mapToInt(i -> i).sum();
    }

    public User getUserDetails(String username) {
        final String sql = "SELECT * FROM UserData WHERE username = ?";
        RowMapper<User> rm = (r, i) -> {
            // String name, String surname, String email, String username, String password, LocalDate date_of_birth, String role
            return new User(
                    r.getString(2),
                    r.getString(3),
                    r.getString(5),
                    r.getString(1),
                    "REDACTED",
                    r.getDate(4).toLocalDate(),
                    ""
            );
        };

        return jdbc.query(sql, rm, username).getFirst();
    }

    @Transactional
    public List<User> getAllUserDetails() {
        final String sql = "SELECT ud.USERNAME, NAME, SURNAME, DATE_OF_BIRTH, EMAIL, REG_DATE, AUTHORITY, ENABLED FROM USERDATA AS ud JOIN AUTHORITIES AS auth ON auth.USERNAME = ud.USERNAME JOIN USERS AS u ON ud.USERNAME = u.USERNAME ORDER BY AUTHORITY, REG_DATE";
        RowMapper<User> rm = (r, i) -> {
            return new User(
                r.getString(2),
                r.getString(3),
                r.getString(5),
                r.getString(1),
                "REDACTED",
                r.getDate(4).toLocalDate(),
                r.getString(7),
                r.getDate(6).toLocalDate(),
                r.getBoolean(8)
            );
        };

        return jdbc.query(sql, rm);
    }

    @Transactional
    public int removeExpiredUsers() {
        String sql = "DELETE FROM USERS WHERE ENABLED = FALSE";
        return jdbc.update(sql);
    }
}
