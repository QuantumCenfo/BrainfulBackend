package com.project.demo.rest.recommendation;


import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.game.GameRepository;
import com.project.demo.logic.entity.recommendation.Recommendation;
import com.project.demo.logic.entity.recommendation.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationRestController {
    @Autowired
    private RecommendationRepository recommendationRepository;



    @GetMapping

    public List<Recommendation> GetAllRecommendations() {
        return recommendationRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN,','USER')")
    public Recommendation addRecommendation(@RequestBody Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }

    @GetMapping("/{id}")
    public Recommendation getRecommendationbyId(@PathVariable Long id) {
        return recommendationRepository.findById(id).orElseThrow(RuntimeException::new);
    }



    @DeleteMapping("/{id}")
    public void deleteRecommendation(@PathVariable Long id) {
        recommendationRepository.deleteById(id);
    }

}
