package com.project.demo.logic.entity.recommendation;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByFormFormId(Long formId);
}
