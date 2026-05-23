package io.theurl.identity.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component("external-auth-provider-facebook")
public class FacebookAuthProvider extends BaseAuthProvider {

    @Value("${external-auth.facebook.client-id}")
    private String clientId;
    @Value("${external-auth.facebook.client-secret}")
    private String clientSecret;
    @Value("${external-auth.redirect-uri}")
    private String redirectUri;

    @Override
    public CompletableFuture<ExternalAuthResult> authenticateAsync(String authCode) {
        return CompletableFuture.supplyAsync(() -> {
            var token = getToken("https://graph.facebook.com//v22.0/oauth/access_token",
                Map.of(
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "code", authCode,
                    "redirect_uri", redirectUri
                ), "query");

            var userInfo = getUserInfo(token, "https://graph.facebook.com/v12.0/me?fields=id,name,email,picture");
            return new ExternalAuthResult() {
                @Override
                public String getId() {
                    return userInfo.get("id").asText();
                }

                @Override
                public String getEmail() {
                    return userInfo.get("email").asText();
                }

                @Override
                public String getNickname() {
                    return userInfo.get("name").asText();
                }
            };
        });
    }
}
