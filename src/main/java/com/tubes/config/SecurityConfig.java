package com.tubes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                // Permit all untuk resources dan halaman login/register
                                                .requestMatchers(
                                                                "/login",
                                                                "/register",
                                                                "/webjars/**",
                                                                "/assets/**",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/images/**",
                                                                "/favicon.ico")
                                                .permitAll()

                                                // Endpoint spesifik yang hanya untuk ADMIN
                                                .requestMatchers(
                                                                "/projects/new",
                                                                "/projects/create",
                                                                "/projects/*/edit",
                                                                "/projects/*/delete")
                                                .hasAuthority("ROLE_ADMIN")

                                                // Endpoint umum projects dan tasks untuk USER dan ADMIN
                                                .requestMatchers("/projects", "/projects/", "/projects/**", "/tasks/**")
                                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                                                // Semua request lainnya membutuhkan autentikasi
                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/login?error=true"))

                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/projects"))

                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login?logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"));
                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}