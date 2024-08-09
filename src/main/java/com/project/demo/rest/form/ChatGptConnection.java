package com.project.demo.rest.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.form.Form;
import com.project.demo.logic.entity.prompt.PromptRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChatGptConnection {
    @Value("${openai.api.key}")
    private String apiKey;

    private final PromptRepository promptRepository;

    public ChatGptConnection(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }

    public String createPrompt(Form form) {
        String promptTemplate = promptRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Prompt no encontrado"))
                .getText();

        return String.format(promptTemplate,
                form.getAge(),
                form.getSleepHours(),
                form.getExerciseDays(),
                form.getUseDrugs() ? "Sí" : "No",
                form.getUseAlcohol() ? "Sí" : "No",
                form.getGender(),
                form.getJob(),
                form.getEduacationLevel(),
                form.getFamilyHistory(),
                form.getMedicalCondition(),
                form.getMentalIllness(),
                form.getDietType(),
                form.getScreenTime(),
                form.getStressManagement()
        );
    }



    public String connectToGPT(String prompt) {
        String url = "https://api.openai.com/v1/chat/completions";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Crear el cuerpo de la solicitud
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo"); // Especificar el modelo aquí
        body.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        body.put("max_tokens", 100);

        // Convertir el cuerpo de la solicitud a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir el cuerpo de la solicitud a JSON", e);
        }

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Error en la solicitud a la API: " + response.getStatusCode());
        }
    }
}
