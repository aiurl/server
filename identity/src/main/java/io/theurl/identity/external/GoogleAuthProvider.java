package io.theurl.identity.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component("external-auth-provider-google")
public class GoogleAuthProvider extends BaseAuthProvider {

    @Value("${external-auth.google.client-id}")
    private String clientId;
    @Value("${external-auth.google.client-secret}")
    private String clientSecret;
    @Value("${external-auth.redirect-uri}")
    private String redirectUri;

    @Override
    public CompletableFuture<ExternalAuthResult> authenticateAsync(String authCode) {
        return CompletableFuture.supplyAsync(() -> {
            var tokenParams = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "code", authCode,
                "grant_type", "authorization_code");

            var token = getToken("https://oauth2.googleapis.com/token", tokenParams, "form");

            var user = getUserInfo(token, "https://www.googleapis.com/oauth2/v3/userinfo");

            return new ExternalAuthResult() {
                @Override
                public String getId() {
                    return user.findValue("sub").asText();
                }

                @Override
                public String getUsername() {
                    return user.findValue("email").asText();
                }

                @Override
                public String getNickname() {
                    return user.findValue("name").asText();
                }

                @Override
                public String getEmail() {
                    return user.findValue("email").asText();
                }

                @Override
                public String getAvatarUrl() {
                    return user.findValue("picture").asText();
                }
            };
        });
    }
}
