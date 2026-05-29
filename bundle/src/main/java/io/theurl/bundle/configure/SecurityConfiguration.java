package io.theurl.bundle.configure;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The security configuration for the application, defining the security filter chain and authentication rules.
 * <p>
 * This configuration disables CSRF, form login, and HTTP basic authentication, and sets the session management to stateless.
 * It also configures exception handling to return a 401 Unauthorized response for unauthenticated requests.
 * The authorization rules allow unauthenticated access to specific endpoints (e.g., authentication and registration endpoints, API documentation) while requiring authentication for all other requests.
 * The JwtAuthenticationFilter is added to the filter chain before the UsernamePasswordAuthenticationFilter to handle JWT token parsing and authentication.
 * </p>
 */
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   io.theurl.framework.security.JwtAuthenticationFilter jwtAuthenticationFilter) {
        http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/error"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

