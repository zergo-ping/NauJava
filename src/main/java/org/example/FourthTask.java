package main.java.org.example;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class FourthTask {

    public static void main() throws IOException, InterruptedException
    {
        try (HttpClient client = HttpClient.newHttpClient())
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/ip"))
                    .build();
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            String ip = root.get("origin").asText();

            System.out.println(ip);
        }
    }

}
