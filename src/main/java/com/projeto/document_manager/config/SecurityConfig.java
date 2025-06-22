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
            .csrf(csrf -> csrf.disable()) // Forma atual para desabilitar CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/projects/**").permitAll() // libera acesso para /api/projects (GET, POST, etc)
                .anyRequest().authenticated() // para o resto, exige autenticação
            );
        return http.build();
    }
}
