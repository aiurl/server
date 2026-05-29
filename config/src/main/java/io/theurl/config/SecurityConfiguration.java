package io.theurl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(auth -> {
                auth.requestMatchers("/api/**").authenticated();
                auth.anyRequest().permitAll();
            })
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> {
                System.out.println("Unauthorized access attempt: " + exception);
                exception.authenticationEntryPoint((req, resp, e) -> {
                    System.out.println("Request URI: " + req.getRequestURI());
                    System.out.println("Unauthorized access attempt: " + e);
                    resp.setStatus(401);
                });
            }); // Often needed for POST requests
        return http.build();
    }
}
