package com.udeateampro.CotSys.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // Desactiva CSRF (para permitir POST desde Postman)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/**").permitAll() // Permite todos los endpoints de tu API
                            .anyRequest().permitAll() // Permite todo lo demás también
                    );
            return http.build();
        }
    }

