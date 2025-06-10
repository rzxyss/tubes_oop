package com.tubes.config;

import com.tubes.Services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationService authenticationService)
                        throws Exception {
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

                                                // Endpoint untuk admin
                                                .requestMatchers(
                                                                "/projects/new",
                                                                "/projects/create",
                                                                "/projects/*/edit",
                                                                "/projects/*/delete")
                                                .hasAuthority("ROLE_ADMIN")

                                                // Endpoint untuk mengedit/menghapus task (hanya pemilik task atau
                                                // admin)
                                                .requestMatchers("/projects/*/tasks/*/edit",
                                                                "/projects/*/tasks/*/delete")
                                                .access(new WebExpressionAuthorizationManager(
                                                                "@authenticationService.canEditTask(#taskId) or hasAuthority('ROLE_ADMIN')"))

                                                .requestMatchers("/projects", "/projects/", "/projects/**")
                                                .authenticated()

                                                .requestMatchers("/projects", "/projects/", "/projects/**", "/tasks/**")
                                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                                                // Semua request lainnya membutuhkan autentikasi
                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/login?error=true"))

                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/access-denied"))

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