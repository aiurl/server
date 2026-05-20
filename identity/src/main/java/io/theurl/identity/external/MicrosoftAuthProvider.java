package io.theurl.identity.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class MicrosoftAuthProvider implements ExternalAuthProvider {
    @Value("${external-auth.microsoft.client-id}")
    private String clientId;
    @Value("${external-auth.microsoft.client-secret}")
    private String clientSecret;
    @Value("${external-auth.redirect-uri}")
    private String redirectUri;

    @Override
    public ExternalAuthResult authenticate(String authCode) {
        var token = getToken(authCode);
        var user = getUserInfo(token);
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

    private JsonNode getUserInfo(String token) {
        var request = HttpRequest.newBuilder()
            .header("User-Agent", "Linkyou")
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + token)
            .uri(URI.create("https://graph.microsoft.com/v1.0/me"))
            .GET()
            .build();

        var response = HttpClient.newHttpClient()
            .sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
            .join();
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get user info from Microsoft: " + response.body());
        }

        return readJson(response.body());
    }

    private String getToken(String authCode) {
        Map<String, String> formData = Map.of(
            "client_id", clientId,
            "client_secret", clientSecret,
            "code", authCode,
            "redirect_uri", redirectUri,
            "grant_type", "authorization_code",
            "scope", "User.Read Mail.Read"
        );

        var formString = formData.entrySet().stream()
            .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
            .reduce((a, b) -> a + "&" + b)
            .orElse("");

        var request = HttpRequest.newBuilder()
            .uri(URI.create("https://login.microsoftonline.com/consumers/oauth2/v2.0/token"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(formString))
            .build();

        var response = HttpClient.newHttpClient()
            .sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
            .join();
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get token from Microsoft: " + response.body());
        }

        var jsonObject = readJson(response.body());
        return jsonObject.get("access_token").asText();
    }

    private JsonNode readJson(String json) {
        try {
            var objectMapper = new ObjectMapper();
            return objectMapper.readTree(json);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
