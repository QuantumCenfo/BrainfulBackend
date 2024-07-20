package com.project.demo.logic.entity.challengegame;

import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.game.Game;
import jakarta.persistence.*;

import java.util.Date;

@Table(name = "Game_challenges")
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
    @JoinColumn(name = "badge_id", referencedColumnName = "badgeId", nullable = false)
    private Badge badgeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", referencedColumnName = "gameId", nullable = false)
    private Game gameId;

    public ChallengeGame() {}

    public ChallengeGame(Long challengeId, String title, String description, Date startDate, Date endDate, Integer objectiveScore, Integer objectiveTime, Integer objectiveFrecuency, Badge badgeId, Game gameId) {
        this.challengeId = challengeId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.objectiveScore = objectiveScore;
        this.objectiveTime = objectiveTime;
        this.objectiveFrecuency = objectiveFrecuency;
        this.badgeId = badgeId;
        this.gameId = gameId;
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

    public Game getGameId() {
        return gameId;
    }

    public void setGameId(Game gameId) {
        this.gameId = gameId;
    }
}