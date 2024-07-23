package com.project.demo.rest.challengegame;

import com.project.demo.logic.entity.challengegame.ChallengeGame;
import com.project.demo.logic.entity.challengegame.ChallengeGameRepository;
import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/challengeGame")
public class ChallengeGameRestController {

    @Autowired
    private ChallengeGameRepository challengeGameRepository;
    @GetMapping("/active-challenges")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<ChallengeGame> getAllActiveChallenges() {
        Date currentDate = new Date();
        return challengeGameRepository.findAllActiveChallenges(currentDate);
    }
    @GetMapping("/inactive-challenges")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<ChallengeGame> getAllInactiveChallenges() {
        Date currentDate = new Date();
        return challengeGameRepository.findAllInactiveChallenges(currentDate);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ChallengeGame addChallengeGame(@RequestBody ChallengeGame challengeGame) {
        return challengeGameRepository.save(challengeGame);
    }

    @PutMapping("/{id}")
    public ChallengeGame updateDateChallengeGame(@PathVariable Long id, @RequestBody ChallengeGame challengeGame) {
        return challengeGameRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setStartDate(challengeGame.getStartDate());
                    existingUser.setEndDate(challengeGame.getEndDate());
                    return challengeGameRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    challengeGame.setChallengeId(id);
                    return challengeGameRepository.save(challengeGame);
                });
    }
}
