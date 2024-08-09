package com.project.demo.rest.Chat;


import com.azure.json.implementation.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.Chat.ChatRequest;
import com.project.demo.logic.entity.Chat.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController()
public class ChatRestController {

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        //create a request

        int maxTokens = 100; // or any value you prefer
        double temperature = 0.7;
        ChatRequest request = new ChatRequest(model, prompt,maxTokens,temperature);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Create an entity with headers
        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);

        //Call the api
        try{
            String jsonResponse = restTemplate.postForObject(apiUrl, entity, String.class);
            System.out.println("Response JSON: " + jsonResponse);
            ChatResponse res = restTemplate.postForObject(apiUrl, entity, ChatResponse.class);
            if (res == null || res.getChoices() == null || res.getChoices().isEmpty()) {
                return "No hay respuesta";
            }


            return res.getChoices().get(0).getMessage().getContent();
        }catch(Exception e){
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }







    }

}
