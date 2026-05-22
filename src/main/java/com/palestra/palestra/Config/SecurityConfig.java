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
    private static final String[] ANY_CUSTOMER_USER = {"USER_PROVA", "USER_BASIC", "USER_PRO"};

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
            .requestMatchers("/dashboard",
                    "/dashboard/profile",
                    "/dashboard/change_password").hasAnyRole(ALL_USERS)

            // Solo ruoli utente (non admin)
            .requestMatchers("/dashboard/training",
                    "/dashboard/training_details",
                    "/dashboard/complete_training",
                    "/dashboard/upgrade",
                    "/dashboard/review").hasAnyRole(ANY_CUSTOMER_USER)

            // Solo utenti con abbonamento attivo
            .requestMatchers("/dashboard/personal_stats").hasAnyRole(PAYING_CUSTOMER_USER)
            .requestMatchers("/dashboard/insert_program").hasRole("USER_PRO")

            // Upgrade del piano
            .requestMatchers("/dashboard/upgrade/basic").hasRole("USER_PROVA")
            .requestMatchers("/dashboard/upgrade/pro").hasAnyRole("USER_PROVA", "USER_BASIC")

            // Dashboards
            .requestMatchers("/dashboard/prova").hasRole("USER_PROVA")
            .requestMatchers("/dashboard/basic").hasRole("USER_BASIC")
            .requestMatchers("/dashboard/pro").hasRole("USER_PRO")
            .requestMatchers("/dashboard/admin").hasRole("ADMIN")

            // Sezione admin
            .requestMatchers("/dashboard/user_list",
                    "/dashboard/global_stats",
                    "/dashboard/remove_expired_users").hasRole("ADMIN")

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
