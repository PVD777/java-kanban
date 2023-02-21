package Server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String API_TOKEN;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String getAPI_TOKEN() {
        return API_TOKEN;
    }

    public KVTaskClient(URI uri) throws IOException, InterruptedException {
        uri = uri.resolve("/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application-json")
                .uri(uri)
                .build();
        HttpResponse<String> apiToken = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        API_TOKEN = apiToken.body();
    }

    public String load(String key) {
        URI uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest httpRequestTask = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = httpClient.send(httpRequestTask, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }

    public void put(String key, String json) {
        URI uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + API_TOKEN);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();
        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
