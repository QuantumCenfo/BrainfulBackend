package com.project.demo.logic.entity.gameresult;

import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.Date;

@Table(name = "Game_results")
@Entity
public class GameResult {
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
    @JoinColumn(name = "game_id", referencedColumnName = "gameId", nullable = false)
    private Game gameId;

    public GameResult() {}

    public GameResult(Long resultId, Date gameDate, Integer score, Integer time, String levelDifficulty, User userId, Game gameId) {
        this.resultId = resultId;
        this.gameDate = gameDate;
        this.score = score;
        this.time = time;
        this.levelDifficulty = levelDifficulty;
        this.userId = userId;
        this.gameId = gameId;
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



    public Game getGameId() {
        return gameId;
    }

    public void setGameId(Game gameId) {
        this.gameId = gameId;
    }
}
