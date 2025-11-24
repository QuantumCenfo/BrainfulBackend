package com.project.demo.unit;

import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.badge.BadgeRepository;
import com.project.demo.logic.entity.challengegame.ChallengeGame;
import com.project.demo.logic.entity.challengegame.ChallengeGameRepository;
import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.game.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan("com.project.demo.logic.entity")
@EnableJpaRepositories("com.project.demo.logic.entity")
class ChallengeGameRepositoryTest {

    @Autowired
    private ChallengeGameRepository repository;

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private GameRepository gameRepository;

    private Badge createBadge() {
        Badge badge = new Badge();
        badge.setTitle("Test Badge");
        badge.setDescription("A test badge");
        badge.setUrl("http://example.com/test.png");
        return badgeRepository.save(badge);
    }

    private Game createGame() {
        Game game = new Game();
        game.setName("Test Game");
        game.setDescription("desc");
        game.setTipoEjercicio("Cardio");
        return gameRepository.save(game);
    }

    @Test
    void testFindAllActiveChallenges() {
        Date now = new Date();

        Badge activeBadge = createBadge();
        Game activeGame = createGame();

        ChallengeGame active = new ChallengeGame();
        active.setBadgeId(activeBadge);
        active.setGameId(activeGame);
        active.setEndDate(addDays(now, 5));
        repository.save(active);

        // Para este test, solo necesitas 1 challenge activo.
        List<ChallengeGame> result = repository.findAllActiveChallenges(now);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getEndDate().after(now));
    }

    @Test
    void testFindAllInactiveChallenges() {
        Date now = new Date();

        Badge activeBadge = createBadge();
        Game activeGame = createGame();

        ChallengeGame active = new ChallengeGame();
        active.setBadgeId(activeBadge);
        active.setGameId(activeGame);
        active.setEndDate(addDays(now, 3));
        repository.save(active);

        Badge inactiveBadge = createBadge();
        Game inactiveGame = createGame();

        ChallengeGame inactive = new ChallengeGame();
        inactive.setBadgeId(inactiveBadge);
        inactive.setGameId(inactiveGame);
        inactive.setEndDate(addDays(now, -3));
        repository.save(inactive);

        long expectedBadgeId = inactiveBadge.getBadgeId();

        List<ChallengeGame> result =
                repository.findAllInactiveChallenges(now)
                        .stream()
                        .filter(c -> c.getBadgeId().getBadgeId().equals(expectedBadgeId))
                        .toList();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getEndDate().before(now));
    }



    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
