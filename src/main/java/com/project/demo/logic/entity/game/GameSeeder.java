package com.project.demo.logic.entity.game;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GameSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final GameRepository gameRepository;

    public GameSeeder(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createGame();
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    private void createGame() {
        Game game1 = new Game();
        game1.setName("Simon Dice");
        game1.setDescription("Juego de Simon dice. Seguir la secuencia de colores que se presenta.");
        game1.setTipoEjercicio("Ejercicio Cognitivo");
        if (!gameRepository.findByName(game1.getName()).isPresent()) {
            gameRepository.save(game1);
        }

        Game game2 = new Game();
        game2.setName("Parejas");
        game2.setDescription("Juego de Parejas. Trata de encontrar todas las parejas antes que acabe el tiempo.");
        game2.setTipoEjercicio("Ejercicio Cognitivo");
        if (!gameRepository.findByName(game2.getName()).isPresent()) {
            gameRepository.save(game2);
        }

        Game game3 = new Game();
        game3.setName("Reacción");
        game3.setDescription("Reacciona lo antes posible a los botones.");
        game3.setTipoEjercicio("Ejercicio Cognitivo");
        if (!gameRepository.findByName(game3.getName()).isPresent()) {
            gameRepository.save(game3);
        }

        Game game4 = new Game();
        game4.setName("Rompecabezas");
        game4.setDescription("Juego de Rompecabezas. Completa el rompecabezas antes que acabe el tiempo.");
        game4.setTipoEjercicio("Ejercicio Cognitivo");
        if (!gameRepository.findByName(game4.getName()).isPresent()) {
            gameRepository.save(game4);
        }
    }
}
