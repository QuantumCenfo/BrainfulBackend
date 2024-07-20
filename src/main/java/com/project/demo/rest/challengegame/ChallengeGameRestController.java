package com.project.demo.rest.challengegame;

import com.project.demo.logic.entity.challengegame.ChallengeGame;
import com.project.demo.logic.entity.challengegame.ChallengeGameRepository;
import com.project.demo.logic.entity.game.Game;
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
    @GetMapping
    public List<ChallengeGame> getAllChallenges() {
        return challengeGameRepository.findAll();
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ChallengeGame addChallengeGame(@RequestBody ChallengeGame challengeGame) {
        return challengeGameRepository.save(challengeGame);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public void deleteChallengeGame(@PathVariable Long id) {
        challengeGameRepository.deleteById(id);
    }

}
