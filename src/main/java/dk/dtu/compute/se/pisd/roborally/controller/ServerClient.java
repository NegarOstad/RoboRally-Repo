package dk.dtu.compute.se.pisd.roborally.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ServerClient {
    HttpClient httpClient =
            HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(20)).build();

  public void newGame(int players,int board){
      HttpRequest httpRequest =
              HttpRequest.newBuilder().GET().uri(URI.create("http://10.209.204.5:8080/new/"+players + "/" + board))
                      .build();
      httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenAccept(System.out::println).join();

  }

}

