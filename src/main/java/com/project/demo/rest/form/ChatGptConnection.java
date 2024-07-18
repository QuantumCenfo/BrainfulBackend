package com.project.demo.rest.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.form.Form;
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

    public ChatGptConnection() {
        // Constructor vacío necesario para Spring
    }

    public String createPrompt(Form form) {
        return "Por favor, genera dos recomendaciones, una a corto plazo y otra a largo plazo, en el siguiente formato JSON+\n" +
                "\"[{ \\\"description\\\": \\\"\\\", \\\"recommendation_type\\\": \\\"\\\" }, { \\\"description\\\": \\\"\\\", \\\"recommendation_type\\\": \\\"\\\" }]. \" + basado en los siguientes datos: "
                + "En recommendation_type por favor indica si es de corto plazo o largo plazo. Porfavor siempre ponlo en español " +
                "Datos: " +
                "Edad: " + form.getAge() +
                ", Horas de sueño: " + form.getSleepHours() +
                ", Días de ejercicio: " + form.getExerciseDays() +
                ", Uso de drogas: " + form.getUseDrugs() +
                ", Uso de alcohol: " + form.getUseAlcohol() +
                ", Género: " + form.getGender() +
                ", Trabajo: " + form.getJob() +
                ", Nivel educativo: " + form.getEduacationLevel() +
                ", Historia familiar: " + form.getFamilyHistory() +
                ", Condición médica: " + form.getMedicalCondition() +
                ", Enfermedad mental: " + form.getMentalIllness() +
                ", Tipo de dieta: " + form.getDietType() +
                ", Tiempo en pantalla: " + form.getScreenTime() +
                ", Manejo del estrés: " + form.getStressManagement();
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
