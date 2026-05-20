package io.theurl.identity.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@Component
public class GithubAuthProvider implements ExternalAuthProvider {

    @Value("${external-auth.github.client-id}")
    private String clientId;
    @Value("${external-auth.github.client-secret}")
    private String secret;

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
    }

    private JsonNode getUserInfo(String token) {
        var request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/user/"))
            .header("Accept", "application/json")
            .header("User-Agent", "Linkyou")
            .header("Authorization", "Bearer " + token)
            .GET()
            .build();

        var client = HttpClient.newBuilder()
            .build();
        var response = client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
            .thenApply(java.net.http.HttpResponse::body)
            .join();

        return readJson(response);
    }

    private String getToken(String authCode) {
        var request = HttpRequest.newBuilder()
            .uri(URI.create("https://github.com/login/oauth/access_token"))
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("client_id=" + clientId + "&client_secret=" + secret + "&code=" + authCode))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        var response = client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
            .thenApply(java.net.http.HttpResponse::body)
            .join();
        try {
            var jsonNode = readJson(response);
            var accessToken = jsonNode.findValue("access_token").asText();

            if (accessToken == null) {
                throw new RuntimeException("Failed to retrieve access token from GitHub");
            }

            return accessToken;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
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
