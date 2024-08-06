package com.project.demo.logic.entity.gameresult;

import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {

    @Query("SELECT GR FROM GameResult GR WHERE GR.userId.id = ?1")
    List<GameResult> findGameResultByUserId(Long userID);

}
