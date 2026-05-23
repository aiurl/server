package io.theurl.identity.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component("external-auth-provider-github")
public class GithubAuthProvider extends BaseAuthProvider {

    @Value("${external-auth.github.client-id}")
    private String clientId;
    @Value("${external-auth.github.client-secret}")
    private String secret;

    @Override
    public CompletableFuture<ExternalAuthResult> authenticateAsync(String authCode) {
        return CompletableFuture.supplyAsync(() -> {
            var token = getToken("https://github.com/login/oauth/access_token", Map.of(
                "client_id", clientId,
                "client_secret", secret,
                "code", authCode
            ), "query");
            var user = getUserInfo(token, "https://api.github.com/user");
            return new ExternalAuthResult() {
                @Override
                public String getId() {
                    return user.findValue("id").asText();
                }

                @Override
                public String getUsername() {
                    return user.findValue("login").asText();
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
                    return user.findValue("avatar_url").asText();
                }
            };
        });
    }
}
