package com.project.demo.rest.form;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.form.Form;
import com.project.demo.logic.entity.form.FormRepository;
import com.project.demo.logic.entity.recommendation.Recommendation;
import com.project.demo.logic.entity.recommendation.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/forms")
public class FormRestController {
    @Autowired
    private FormRepository formRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private ChatGptConnection chatGptConnection;
    @GetMapping
    public List<Form> GetAllForms() {
        return formRepository.findAll();
    }
    @PostMapping
    public Form addForm(@RequestBody Form form) {
        formRepository.save(form);
        String prompt = chatGptConnection.createPrompt(form);
        String gptResponse = chatGptConnection.connectToGPT(prompt);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonResponse = objectMapper.readTree(gptResponse);
            String messageContent = jsonResponse.get("choices").get(0).get("message").get("content").asText();
            JsonNode contentArray = objectMapper.readTree(messageContent);

            for (JsonNode recommendationNode : contentArray) {
                String description = recommendationNode.get("description").asText();
                String recommendationType = recommendationNode.get("recommendation_type").asText();

                Recommendation recommendation = new Recommendation();
                recommendation.setForm(form);
                recommendation.setDate(new Date());
                recommendation.setRecommendationType(recommendationType);
                recommendation.setDescription(description);
                recommendationRepository.save(recommendation);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return form;
    }

    @GetMapping("/{id}")
    public Form getFormbyId(@PathVariable Long id) {
        return formRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @DeleteMapping("/{id}")
    public void deleteForms(@PathVariable Long id) {
        formRepository.deleteById(id);
    }
}