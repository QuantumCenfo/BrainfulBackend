package com.project.demo.logic.entity.recommendation;

import com.project.demo.logic.entity.challengegame.ChallengeGame;
import com.project.demo.logic.entity.form.Form;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.Date;

@Table(name = "Recomendations")
@Entity
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationId;

    @Column(name = "recommendation_type")
    private String recommendationType;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "form_id", referencedColumnName = "formId", nullable = false)
    private Form form;

    public Recommendation() {}

    public Recommendation(Long recommendationId, String recommendationType, String description, Form form) {
        this.recommendationId = recommendationId;
        this.recommendationType = recommendationType;
        this.description = description;
        this.form = form;
    }

    public Long getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(Long recommendationId) {
        this.recommendationId = recommendationId;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }
}
