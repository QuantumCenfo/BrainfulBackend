package com.project.demo.logic.entity.challengegame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ChallengeGameRepository extends JpaRepository<ChallengeGame, Long> {

    @Query("SELECT cg FROM ChallengeGame cg WHERE cg.endDate > :currentDate")
    List<ChallengeGame> findAllActiveChallenges(@Param("currentDate") Date currentDate);

    @Query("SELECT cg FROM ChallengeGame cg WHERE cg.endDate <= :currentDate")
    List<ChallengeGame> findAllInactiveChallenges(@Param("currentDate") Date currentDate);

}
