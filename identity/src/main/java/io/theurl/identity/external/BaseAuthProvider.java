package io.theurl.identity.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides common methods for external authentication providers, such as fetching user info and access tokens.
 * Concrete providers (e.g., Google, Microsoft, GitHub) can extend this class to implement their specific authentication logic while reusing these common methods.
 */
public abstract class BaseAuthProvider implements ExternalAuthProvider {

    private final Logger logger = LoggerFactory.getLogger(BaseAuthProvider.class);

    protected JsonNode getUserInfo(String token, String url) {
        var request = HttpRequest.newBuilder()
                                 .uri(java.net.URI.create(url))
                                 .header("Accept", "application/json")
                                 .header("User-Agent", "Linkyou")
                                 .header("Authorization", "Bearer " + token)
                                 .GET()
                                 .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                  .join();
            if (response.statusCode() == 200) {
                return readJson(response.body());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    protected String getToken(String url, Map<String, String> params, String paramsType) {
        var form = params.entrySet().stream()
                         .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                         .collect(Collectors.joining("&"));

        var builder = HttpRequest.newBuilder()
                                 .header("Accept", "application/json")
                                 .header("User-Agent", "Linkyou");

        switch (paramsType) {
            case "form":
                builder.uri(java.net.URI.create(url))
                       .header("Content-Type", "application/x-www-form-urlencoded")
                       .POST(HttpRequest.BodyPublishers.ofString(form));
                break;
            case "query":
                builder.uri(URI.create(url + "?" + form))
                       .POST(HttpRequest.BodyPublishers.noBody());
                break;
            default:
                throw new IllegalArgumentException("Unsupported params type: " + paramsType);
        }

        var request = builder.build();


        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                                  .join();
            if (response.statusCode() == 200) {
                var json = readJson(response.body());
                return json.findValue("access_token").asText();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    protected JsonNode readJson(String json) {
        try {
            var objectMapper = new ObjectMapper();
            return objectMapper.readTree(json);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
