package com.project.demo.logic.entity.exerciseresult;

import com.project.demo.logic.entity.exercise.Exercise;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table(name = "Exercise_results")
@Entity
public class ExerciseResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @Column(name = "game_date")
    private Date gameDate;

    private Integer score;

    private Integer time;

    @Column(name = "level_difficulty", length = 50)
    private String levelDifficulty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id", referencedColumnName = "exerciseId", nullable = false)
    private Exercise exerciseId;

    public ExerciseResult() {}

    public ExerciseResult(Long resultId, Date gameDate, Integer score, Integer time, String levelDifficulty, User userId, Exercise exerciseId) {
        this.resultId = resultId;
        this.gameDate = gameDate;
        this.score = score;
        this.time = time;
        this.levelDifficulty = levelDifficulty;
        this.userId = userId;
        this.exerciseId = exerciseId;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getLevelDifficulty() {
        return levelDifficulty;
    }

    public void setLevelDifficulty(String levelDifficulty) {
        this.levelDifficulty = levelDifficulty;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Exercise getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Exercise exerciseId) {
        this.exerciseId = exerciseId;
    }
}
