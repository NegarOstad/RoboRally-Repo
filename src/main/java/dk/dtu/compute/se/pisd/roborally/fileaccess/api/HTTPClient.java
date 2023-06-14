package dk.dtu.compute.se.pisd.roborally.fileaccess.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
/**
 * This class provides a simple HTTP client for making HTTP requests.
 */
public class HTTPClient {

    private static HTTPClient instance;

    HttpClient httpClient;

    private final String URL = "http://localhost:8080/";
    //private final String URL = "http://10.209.204.5:8080/";
    //private final String URL = "http://192.168.1.57:8080/";
    //private final String URL = "http://10.209.211.169:8080/";
    //private final String URL = "http://10.209.212.0:8080/";
    //private final String URL = "http://10.209.205.30:8080/";

    // Private constructor to prevent instantiation from outside the class
    private HTTPClient() {
        httpClient =  HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(20)).build();
    }

    /***
     *
     * @return singelton instance of http client
     */
    public static HTTPClient getInstance() {
        if (instance == null) {
            // Create a new instance if it doesn't exist
            instance = new HTTPClient();
        }
        return instance;
    }

    /***
     * Sends a post request with specified path
     * @param path to which the post is sent
     * @param content contens of request
     */

    public  void makePostRequest(String path, String content){

        HttpRequest httpRequestSave =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(content, StandardCharsets.UTF_8))
                        .uri(URI.create(URL + path))
                        .build();

        httpClient.sendAsync(httpRequestSave, HttpResponse.BodyHandlers.ofString()).thenAccept(System.out::println).join();

    }

    /***
     *
     * @param path to which request is sent
     * @return response  of the request
     * @throws Exception if an error occurs during the request
     */
    public HttpResponse<String> makeGetRequest(String path) throws Exception{

       /* HttpRequest httpRequestBoard = HttpRequest.newBuilder().GET().uri(URI.create(URL+path)).build();
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

    /***
     *
     * @param path where request of deleting is sent
     */
    public void makeDeleteRequest(String path) {
        HttpRequest httpRequestDelete =
                HttpRequest.newBuilder()
                        .DELETE()
                        .uri(URI.create(URL + path))
                        .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequestDelete, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();

            System.out.println("Response Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    }
