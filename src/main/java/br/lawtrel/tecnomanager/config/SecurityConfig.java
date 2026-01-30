package br.lawtrel.tecnomanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Libera o console
                        .anyRequest().permitAll() // Libera tudo (por enquanto, para teste)
                )
                .csrf(csrf -> csrf.disable()) // Desativa proteção CSRF (necessário pro H2)
                .headers(headers -> headers.frameOptions(frame -> frame.disable())); // Permite frames

        return http.build();
    }
}