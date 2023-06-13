package dk.dtu.compute.se.pisd.roborally.fileaccess.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HTTPClient {

    private static HTTPClient instance;

    HttpClient httpClient;

    private final String URL = "http://localhost:8080/";
    //private final String URL = "http://10.209.204.5:8080/";
    //private final String URL = "http://192.168.1.57:8080/";
    //private final String URL = "http://10.209.212.0:8080/";

    //private final String URL = "http://10.209.211.169:8080/";
    //private final String URL = "http://10.209.212.0:8080/";

    // Private constructor to prevent instantiation from outside the class
    private HTTPClient() {
        httpClient =  HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(20)).build();
    }

    // Public static method to get the Singleton instance
    public static HTTPClient getInstance() {
        if (instance == null) {
            // Create a new instance if it doesn't exist
            instance = new HTTPClient();
        }
        return instance;
    }

    public  void makePostRequest(String path, String content){

        HttpRequest httpRequestSave =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(content, StandardCharsets.UTF_8))
                        .uri(URI.create(URL + path))
                        .build();

        httpClient.sendAsync(httpRequestSave, HttpResponse.BodyHandlers.ofString()).thenAccept(System.out::println).join();

    }

    public HttpResponse<String> makeGetRequest(String path) throws Exception{

        HttpRequest httpRequestBoard = HttpRequest.newBuilder().GET().uri(URI.create(URL+path)).build();
        httpClient.sendAsync(httpRequestBoard, HttpResponse.BodyHandlers.ofString()).thenAccept(System.out::println).join();
        HttpResponse<String> response = httpClient.send(httpRequestBoard, HttpResponse.BodyHandlers.ofString());
        CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(httpRequestBoard, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = future.thenCompose(HttpResponse::body).join();

        System.out.println("Server response: " + response.body());
        return response;*/
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create(URL+path)).build();

        CompletableFuture<HttpResponse<String>> futureResponse = httpClient.sendAsync(httpRequest,
                HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = futureResponse.join(); // Wait for the response to be available

        return response;

    }

}
