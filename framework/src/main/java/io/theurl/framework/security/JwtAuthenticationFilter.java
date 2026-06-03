package io.theurl.framework.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

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
                // Validate signing key is configured
                if (signingKey == null || signingKey.isBlank()) {
                    LOGGER.warn("JWT signing key is not properly configured. Using default or empty key.");
                }

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
                    LOGGER.debug("JWT authentication successful for user: {}", userId);
                } else {
                    LOGGER.debug("JWT token has no subject (userId)");
                }
            } catch (io.jsonwebtoken.security.SignatureException e) {
                // Don't throw an exception when token parsing fails, let the subsequent authentication process handle it and return 401.
                LOGGER.debug("JWT signature validation failed: {}", e.getMessage());
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                LOGGER.debug("JWT token has expired: {}", e.getMessage());
            } catch (io.jsonwebtoken.MalformedJwtException e) {
                LOGGER.debug("JWT token is malformed: {}", e.getMessage());
            } catch (Exception e) {
                LOGGER.debug("JWT parse failed: {}", e.getMessage());
            }
        } else if (authHeader == null) {
            LOGGER.trace("No Authorization header found in request");
        } else if (!authHeader.startsWith("Bearer ")) {
            LOGGER.debug("Authorization header does not start with 'Bearer '");
        }

        filterChain.doFilter(request, response);
    }
}
