package com.projeto.document_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // desabilita CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/projects/**",
                    "/api/documents/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated() // exige autenticação para o resto
            );
        return http.build();
    }
}
