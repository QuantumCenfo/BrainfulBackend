package com.project.demo.logic.entity.participationchallenge;

import com.project.demo.logic.entity.challengegame.ChallengeGame;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.Date;

@Table(name = "Participation_challenges")
@Entity
public class ParticipationChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participationGameId;

    private String status;

    @Column(name = "level_difficulty", length = 50)
    private String levelDifficulty;
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "objective_score_challenge")
    private Integer objectiveScoreChallenge;

    @Column(name = "objective_time_challenge")
    private Integer objectiveTimeChallenge;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challenge_id", referencedColumnName = "challengeId", nullable = false)
    private ChallengeGame challengeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User userId;

    public ParticipationChallenge() {}

    public ParticipationChallenge(Long participationGameId, String status, String levelDifficulty, Date endDate, Integer objectiveScoreChallenge, Integer objectiveTimeChallenge, ChallengeGame challengeId, User userId) {
        this.participationGameId = participationGameId;
        this.status = status;
        this.levelDifficulty = levelDifficulty;
        this.endDate = endDate;
        this.objectiveScoreChallenge = objectiveScoreChallenge;
        this.objectiveTimeChallenge = objectiveTimeChallenge;
        this.challengeId = challengeId;
        this.userId = userId;
    }

    public Long getParticipationId() {
        return participationGameId;
    }

    public void setParticipationId(Long participationGameId) {
        this.participationGameId = participationGameId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getObjectiveScoreChallenge() {
        return objectiveScoreChallenge;
    }

    public void setObjectiveScoreChallenge(Integer objectiveScoreChallenge) {
        this.objectiveScoreChallenge = objectiveScoreChallenge;
    }

    public Long getParticipationGameId() {
        return participationGameId;
    }

    public void setParticipationGameId(Long participationGameId) {
        this.participationGameId = participationGameId;
    }

    public String getLevelDifficulty() {
        return levelDifficulty;
    }

    public void setLevelDifficulty(String levelDifficulty) {
        this.levelDifficulty = levelDifficulty;
    }

    public Integer getObjectiveTimeChallenge() {
        return objectiveTimeChallenge;
    }

    public void setObjectiveTimeChallenge(Integer objectiveTimeChallenge) {
        this.objectiveTimeChallenge = objectiveTimeChallenge;
    }

    public ChallengeGame getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(ChallengeGame challengeId) {
        this.challengeId = challengeId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
