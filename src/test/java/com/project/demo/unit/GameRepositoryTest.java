package com.project.demo.unit;

import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.game.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
@EntityScan("com.project.demo.logic.entity.game")
@EnableJpaRepositories("com.project.demo.logic.entity.game")
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    void testFindByName() {
        Game game = new Game();
        game.setName("Zumba");
        game.setDescription("Dance");
        game.setTipoEjercicio("Cardio");

        gameRepository.save(game);

        assertTrue(gameRepository.findByName("Zumba").isPresent());
    }

    @Test
    void testFindGameWithCharacterInName() {
        Game g1 = new Game();
        g1.setName("Yoga");
        g1.setDescription("Relax");
        g1.setTipoEjercicio("Flexibility");

        Game g2 = new Game();
        g2.setName("Power Yoga");
        g2.setDescription("Strength");
        g2.setTipoEjercicio("Strength");

        gameRepository.save(g1);
        gameRepository.save(g2);

        List<Game> result = gameRepository.findGameWithCharacterInName("yog");

        assertEquals(2, result.size());
    }
}
