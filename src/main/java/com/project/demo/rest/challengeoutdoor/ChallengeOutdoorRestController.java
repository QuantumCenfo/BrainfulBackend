package com.project.demo.rest.challengeoutdoor;


import com.project.demo.logic.entity.challengegame.ChallengeGame;
import com.project.demo.logic.entity.challengegame.ChallengeGameRepository;
import com.project.demo.logic.entity.challengeroutdoor.ChallengeOutdoor;
import com.project.demo.logic.entity.challengeroutdoor.ChallengeOutdoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/challengeOutdoor")
public class ChallengeOutdoorRestController {

    @Autowired
    private ChallengeOutdoorRepository challengeOutdoorRepository;

    @GetMapping("/active-challenges")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public List<ChallengeOutdoor> getAllActiveChallenges() {
        Date currentDate = new Date();
        return challengeOutdoorRepository.findAllActiveChallenges(currentDate);
    }

    @GetMapping("/inactive-challenges")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public List<ChallengeOutdoor> getAllInactiveChallenges() {
        Date currentDate = new Date();
        return challengeOutdoorRepository.findAllInactiveChallenges(currentDate);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ChallengeOutdoor addChallengeOutdoor(@RequestBody ChallengeOutdoor challengeOutdoor) {
        return challengeOutdoorRepository.save(challengeOutdoor);
    }

    @PutMapping("/{id}")
    public ChallengeOutdoor updateDateChallengeOutdoor(@PathVariable Long id, @RequestBody ChallengeOutdoor challengeOutdoor) {
        return challengeOutdoorRepository.findById(id)
                .map(existingChallengeOutdoor -> {
                    existingChallengeOutdoor.setStartDate(challengeOutdoor.getStartDate());
                    existingChallengeOutdoor.setEndDate(challengeOutdoor.getEndDate());
                    return challengeOutdoorRepository.save(existingChallengeOutdoor);
                })
                .orElseGet(() -> {
                    challengeOutdoor.setOutdoorChallengeId(id);
                    return challengeOutdoorRepository.save(challengeOutdoor);
                });
    }
}
