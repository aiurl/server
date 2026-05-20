package io.theurl.identity.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("external-auth-provider-microsoft")
public class MicrosoftAuthProvider extends BaseAuthProvider {
    @Value("${external-auth.microsoft.client-id}")
    private String clientId;
    @Value("${external-auth.microsoft.client-secret}")
    private String clientSecret;
    @Value("${external-auth.redirect-uri}")
    private String redirectUri;

    @Override
    public ExternalAuthResult authenticate(String authCode) {
        Map<String, String> getTokenParams = Map.of(
            "client_id", clientId,
            "client_secret", clientSecret,
            "code", authCode,
            "redirect_uri", redirectUri,
            "grant_type", "authorization_code",
            "scope", "User.Read Mail.Read"
        );

        var token = getToken("https://login.microsoftonline.com/consumers/oauth2/v2.0/token", getTokenParams, "form");
        var user = getUserInfo(token, "https://graph.microsoft.com/v1.0/me");
        return new ExternalAuthResult() {
            @Override
            public String getId() {
                return user.findValue("id").asText();
            }

            @Override
            public String getUsername() {
                return user.findValue("userPrincipalName").asText();
            }

            @Override
            public String getNickname() {
                return user.findValue("displayName").asText();
            }

            @Override
            public String getEmail() {
                return user.findValue("email").asText();
            }

            @Override
            public String getPhone() {
                return user.findValue("mobilePhone").asText();
            }
        };
    }
}
