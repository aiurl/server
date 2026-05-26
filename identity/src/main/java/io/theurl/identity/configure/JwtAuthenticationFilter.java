package io.theurl.identity.configure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * The JWT Authentication Filter is responsible for parsing the JWT token from the Authorization header of incoming HTTP requests and setting the authentication information in the SecurityContext.
 * It extends OncePerRequestFilter to ensure that it is executed once per request. The filter checks if the Authorization header contains a Bearer token, and if so, it attempts to parse the token using the configured signing key.
 * If the token is valid, it extracts the user ID from the token claims and creates an authentication object, which is then set in the SecurityContext for downstream processing.
 * If token parsing fails, it logs the error and allows the request to proceed without authentication, which will be handled by subsequent security filters.
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String signingKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = authHeader.substring(7);
            try {
                var claims = Jwts.parser()
                                 .verifyWith(Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8)))
                                 .build()
                                 .parseSignedClaims(token)
                                 .getPayload();

                String userId = claims.getSubject();
                if (userId != null && !userId.isBlank()) {
                    var authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Don't throw an exception when token parsing fails, let the subsequent authentication process handle it and return 401.
                log.debug("JWT parse failed: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}

