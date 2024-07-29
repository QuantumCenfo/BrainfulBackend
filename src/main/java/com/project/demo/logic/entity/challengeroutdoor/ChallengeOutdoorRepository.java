package com.project.demo.logic.entity.challengeroutdoor;

import com.project.demo.logic.entity.challengegame.ChallengeGame;
import com.project.demo.logic.entity.form.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ChallengeOutdoorRepository extends JpaRepository<ChallengeOutdoor, Long> {
    @Query("SELECT co FROM ChallengeOutdoor co WHERE co.endDate > :currentDate")
    List<ChallengeOutdoor> findAllActiveChallenges(@Param("currentDate") Date currentDate);

    @Query("SELECT co FROM ChallengeOutdoor co WHERE co.endDate <= :currentDate")
    List<ChallengeOutdoor> findAllInactiveChallenges(@Param("currentDate") Date currentDate);
}
