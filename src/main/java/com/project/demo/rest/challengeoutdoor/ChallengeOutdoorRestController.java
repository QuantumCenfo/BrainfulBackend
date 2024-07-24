package com.project.demo.rest.challengeoutdoor;


import com.project.demo.logic.entity.challengegame.ChallengeGame;
import com.project.demo.logic.entity.challengegame.ChallengeGameRepository;
import com.project.demo.logic.entity.challengeroutdoor.ChallengeOutdoor;
import com.project.demo.logic.entity.challengeroutdoor.ChallengeOutdoorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/challengeOutdoor")
public class ChallengeOutdoorRestController {

    @Autowired
    private ChallengeOutdoorRepository challengeOutdoorRepository;
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public List<ChallengeOutdoor> getAllActiveChallenges() {
        Date currentDate = new Date();
        return challengeOutdoorRepository.findAllActiveChallenges(currentDate);
    }
}
