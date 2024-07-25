package com.project.demo.logic.entity.participationchallengeoutdoor;

import com.project.demo.logic.entity.badge.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationOutdoorRepository extends JpaRepository<ParticipationChallengeOutdoor, Long> {
    List<ParticipationChallengeOutdoor> findByUser_IdAndChallengeOutdoor_OutdoorChallengeId(Long userId, Long challengeOutdoorId);
}
