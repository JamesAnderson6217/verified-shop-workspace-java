package example.checkout;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public final class InfraiHttpClient {
    public interface Transport { Reply send(String method, String path, String body) throws IOException, InterruptedException; }
    public record Reply(int status, String body, String retryAfter) { }
    public static final class InfraiException extends RuntimeException {
        public InfraiException(String message) { super(message); }
    }

    private final Transport transport;

    public InfraiHttpClient(InfraiConfig config) {
        HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        this.transport = (method, path, body) -> {
            HttpRequest request = HttpRequest.newBuilder(URI.create(config.baseUrl() + path))
                    .header("Authorization", "Bearer " + config.apiKey())
                    .header("Content-Type", "application/json")
                    .method(method, HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            return new Reply(response.statusCode(), response.body(), response.headers().firstValue("Retry-After").orElse(null));
        };
    }

    InfraiHttpClient(Transport transport) { this.transport = transport; }

    public String call(String method, String path, String body) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                Reply reply = transport.send(method, path, body);
                if (reply.status() == 429) {
                    Thread.sleep(waitMillis(reply.retryAfter(), attempt));
                    continue;
                }
                String envelope = reply.body(); // Decode the envelope before treating an HTTP status as transport failure.
                if (!jsonBoolean(envelope, "ok")) throw new InfraiException(jsonString(envelope, "message"));
                if (reply.status() >= 500) throw new InfraiException("Service request was not completed.");
                return envelope;
            } catch (IOException e) {
                if (attempt == 2) throw new InfraiException(e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InfraiException("Request interrupted.");
            }
        }
        throw new InfraiException("Request rate limit retry exhausted.");
    }

    private static long waitMillis(String retryAfter, int attempt) {
        if (retryAfter != null) try { return Long.parseLong(retryAfter) * 1000L; } catch (NumberFormatException ignored) { }
        return 200L * (1L << attempt);
    }
    public static boolean jsonBoolean(String json, String key) { return json.matches("(?s).*\\\"" + key + "\\\"\\s*:\\s*true.*"); }
    public static String jsonString(String json, String key) {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\\"" + key + "\\\"\\s*:\\s*\\\"([^\\\"]*)").matcher(json);
        return m.find() ? m.group(1) : "Request rejected";
    }
}
