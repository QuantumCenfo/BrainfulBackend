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

    @GetMapping("/challenges")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public List<ChallengeOutdoor> getChallengesByStatus(@RequestParam("status") String status) {
        Date currentDate = new Date();
        if ("active".equalsIgnoreCase(status)) {
            return challengeOutdoorRepository.findAllActiveChallenges(currentDate);
        } else if ("inactive".equalsIgnoreCase(status)) {
            return challengeOutdoorRepository.findAllInactiveChallenges(currentDate);
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
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
