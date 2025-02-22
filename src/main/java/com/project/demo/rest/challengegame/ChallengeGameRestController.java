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
    @GetMapping("/challenges")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public List<ChallengeGame> getChallengesByStatus(@RequestParam("status") String status) {
        Date currentDate = new Date();
        if ("active".equalsIgnoreCase(status)) {
            return challengeGameRepository.findAllActiveChallenges(currentDate);
        } else if ("inactive".equalsIgnoreCase(status)) {
            return challengeGameRepository.findAllInactiveChallenges(currentDate);
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public void deleteChallengeGame(@PathVariable Long id) {
        challengeGameRepository.deleteById(id);
    }
}
