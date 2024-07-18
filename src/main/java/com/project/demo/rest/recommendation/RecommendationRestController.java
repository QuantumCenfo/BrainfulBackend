package com.project.demo.rest.recommendation;


import com.project.demo.logic.entity.form.Form;
import com.project.demo.logic.entity.form.FormRepository;
import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.game.GameRepository;
import com.project.demo.logic.entity.recommendation.Recommendation;
import com.project.demo.logic.entity.recommendation.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommendations")
public class RecommendationRestController {
    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private FormRepository formRepository;

    @GetMapping("/{userId}")
    public List<Recommendation> getRecommendationsByUserId(@PathVariable Long userId) {
        List<Form> forms = formRepository.findByUserId(userId);
        return forms.stream()
                .flatMap(form -> recommendationRepository.findByFormFormId(form.getFormId()).stream())
                .collect(Collectors.toList());
    }

//    @GetMapping
//    public List<Recommendation> getAllRecommendations() {
//        return recommendationRepository.findAll();
//    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN,','USER')")
    public Recommendation addRecommendation(@RequestBody Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }

//    @GetMapping("/{recommendationId}")
//    public Recommendation getRecommendationById(@PathVariable Long id) {
//        return recommendationRepository.findById(id).orElseThrow(RuntimeException::new);
//    }

    @DeleteMapping("/{id}")
    public void deleteRecommendation(@PathVariable Long id) {
        recommendationRepository.deleteById(id);
    }
}