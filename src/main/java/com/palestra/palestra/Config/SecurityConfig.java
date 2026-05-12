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

        http.authorizeHttpRequests(auth ->
                auth.requestMatchers("/dashboard")
                        .hasAnyRole("USER_ADMIN", "USER_PROVA", "USER_BASIC", "USER_PRO")
                .requestMatchers("/dashboard/prova").hasRole("USER_PROVA")
                .requestMatchers("/dashboard/profile").hasAnyRole("USER_ADMIN", "USER_PROVA", "USER_BASIC", "USER_PRO")
                .requestMatchers("/dashboard/change_password").hasAnyRole("USER_ADMIN", "USER_PROVA", "USER_BASIC", "USER_PRO")
                .requestMatchers("/dashboard/upgrade").hasAnyRole("USER_PROVA", "USER_BASIC", "USER_PRO")
                .requestMatchers("/dashboard/upgrade/basic").hasAnyRole("USER_PROVA")
                .requestMatchers("/dashboard/upgrade/pro").hasAnyRole("USER_PROVA", "USER_BASIC")
                .anyRequest().permitAll()
        );

        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
