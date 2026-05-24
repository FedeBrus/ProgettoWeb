package com.palestra.palestra.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Variabili statiche per rendere più leggibile la security config
    private static final String[] ALL_USERS = {"ADMIN", "USER_PROVA", "USER_BASIC", "USER_PRO"};
    private static final String[] PAYING_CUSTOMER_USER = {"USER_BASIC", "USER_PRO"};
    private static final String[] CUSTOMER_USER = {"USER_PROVA", "USER_BASIC", "USER_PRO"};

    // User details manager
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    // Password encoder
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(c ->
                c.loginPage("/login")
                        .defaultSuccessUrl("/dashboard")
                        .failureForwardUrl("/loginFailure")
        );

        http.authorizeHttpRequests(auth -> auth
            // Accessibili a tutti gli utenti autenticati
            .requestMatchers("/dashboard").hasAnyRole(ALL_USERS)

            // Dashboards
            .requestMatchers("/dashboard/prova").hasRole("USER_PROVA")
            .requestMatchers("/dashboard/basic").hasRole("USER_BASIC")
            .requestMatchers("/dashboard/pro").hasRole("USER_PRO")
            .requestMatchers("/dashboard/admin").hasRole("ADMIN")

            // Sezione admin
            .requestMatchers("/dashboard/admin/**").hasRole("ADMIN")

            // non admin
            .requestMatchers("/dashboard/user/**").hasAnyRole(CUSTOMER_USER)

            // funzionalità basic e pro
            .requestMatchers("/dashboard/user/personal_stats").hasAnyRole(PAYING_CUSTOMER_USER)
            .requestMatchers("/dashboard/user/insert_program").hasRole("USER_PRO")

            // upgrade del piano
            .requestMatchers("/dashboard/upgrade/basic").hasRole("USER_PROVA")
            .requestMatchers("/dashboard/upgrade/pro").hasAnyRole("USER_PROVA", "USER_BASIC")

            .anyRequest().permitAll()
        );

        http.logout(c ->
                c.logoutUrl("/perform_logout")
                .logoutSuccessUrl("/logout")
        );

        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
