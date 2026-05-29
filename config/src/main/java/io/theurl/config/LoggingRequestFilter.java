package io.theurl.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingRequestFilter extends OncePerRequestFilter {

    private static final String[] ipHeaders = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR"
    };

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var ip = getClientIp(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUri = uri + (queryString != null ? "?" + queryString : "");
        System.out.println(request.getSession().getId() + " " + ip + " : " + method + " " + fullUri);
        filterChain.doFilter(request, response);
        System.out.println(request.getSession().getId() + " " + ip + " : " + response.getStatus());
    }

    public static String getClientIp(HttpServletRequest request) {
        for (String header : ipHeaders) {
            if (request.getHeader(header) != null) {
                return request.getHeader(header);
            }
        }

        var remoteAddr = request.getRemoteAddr();
        return switch (remoteAddr) {
            case null -> "127.0.0.1";
            case "0:0:0:0:0:0:0:1", "::1" -> "127.0.0.1";
            default -> remoteAddr;
        };
    }
}
