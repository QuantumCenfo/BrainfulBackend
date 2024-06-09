package com.project.demo.logic.entity.challengegame;

import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.exercise.Exercise;
import com.project.demo.logic.entity.rol.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table(name = "Challenge_games")
@Entity
public class ChallengeGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeId;

    private String title;

    @Column(length = 500)
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "objective_score")
    private Integer objectiveScore;

    @Column(name = "objective_time")
    private Integer objectiveTime;

    @Column(name = "objective_frecuency")
    private Integer objectiveFrecuency;

    @OneToOne(fetch = FetchType.EAGER)
    @Column(name = "badge_id")
    private Badge badgeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id", referencedColumnName = "exerciseId", nullable = false)
    private Exercise exerciseId;

    public ChallengeGame() {}

    public ChallengeGame(Long challengeId, String title, String description, Date startDate, Date endDate, Integer objectiveScore, Integer objectiveTime, Integer objectiveFrecuency, Badge badgeId, Exercise exerciseId) {
        this.challengeId = challengeId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.objectiveScore = objectiveScore;
        this.objectiveTime = objectiveTime;
        this.objectiveFrecuency = objectiveFrecuency;
        this.badgeId = badgeId;
        this.exerciseId = exerciseId;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getObjectiveScore() {
        return objectiveScore;
    }

    public void setObjectiveScore(Integer objectiveScore) {
        this.objectiveScore = objectiveScore;
    }

    public Integer getObjectiveTime() {
        return objectiveTime;
    }

    public void setObjectiveTime(Integer objectiveTime) {
        this.objectiveTime = objectiveTime;
    }

    public Integer getObjectiveFrecuency() {
        return objectiveFrecuency;
    }

    public void setObjectiveFrecuency(Integer objectiveFrecuency) {
        this.objectiveFrecuency = objectiveFrecuency;
    }

    public Badge getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(Badge badgeId) {
        this.badgeId = badgeId;
    }

    public Exercise getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Exercise exerciseId) {
        this.exerciseId = exerciseId;
    }
}