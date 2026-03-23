package com.management;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class OllamaClient
{
    // URL locale di Ollama — gira sul nostro computer
    private static final String OLLAMA_URL = "http://localhost:11434/api/chat";
    private static final String MODEL = "llama3.2:3b";
    private static final ObjectMapper mapper = new ObjectMapper();

    public String askQuestion(String prompt) throws Exception {

        // Costruiamo il body della richiesta in formato JSON
        Map<String, Object> requestBody = Map.of(
                "model", MODEL,
                "stream", false,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        // Costruiamo la richiesta HTTP POST verso Ollama
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        mapper.writeValueAsString(requestBody)
                ))
                .build();

        // Inviamo la richiesta e aspettiamo la risposta
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        // Navighiamo il JSON di risposta per estrarre il testo
        var responseJson = mapper.readTree(response.body());
        return responseJson.path("message").path("content").asText();
    }

}
