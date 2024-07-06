package com.project.demo.logic.entity.game;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GameSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final  GameRepository gameRepository;

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

    private void createGame(){
        Game game = new Game();
        game.setName("Simon Dice Facil");
        game.setDescription("Juego de Simon dice. Seguir la secuencia de colores que se presenta.");
        game.setLevelDifficulty("Facil");
        game.setTipoEjercicio("Ejercicio Cognitivo");

        Game game2 = new Game();
        game2.setName("Simon Dice Medio");
        game2.setDescription("Juego de Simon dice. Seguir la secuencia de colores que se presenta.");
        game2.setLevelDifficulty("Medio");
        game2.setTipoEjercicio("Ejercicio Cognitivo");

        Game game3 = new Game();
        game3.setName("Simon Dice Dificl");
        game3.setDescription("Juego de Simon dice. Seguir la secuencia de colores que se presenta.");
        game3.setLevelDifficulty("Dificil");
        game3.setTipoEjercicio("Ejercicio Cognitivo");

        Optional<Game> optionalGame1 = gameRepository.findByName(game.getName());
        Optional<Game> optionalGame2 = gameRepository.findByName(game2.getName());
        Optional<Game> optionalGame3 = gameRepository.findByName(game3.getName());
        if(optionalGame1.isPresent() && optionalGame2.isPresent() && optionalGame3.isPresent()){
            return;
        }

        var newGame = new Game();
        newGame.setName(game.getName());
        newGame.setDescription(game.getDescription());
        newGame.setLevelDifficulty(game.getLevelDifficulty());
        newGame.setTipoEjercicio(game.getTipoEjercicio());
        gameRepository.save(newGame);

        var newGame2 = new Game();
        newGame2.setName(game2.getName());
        newGame2.setDescription(game2.getDescription());
        newGame2.setLevelDifficulty(game2.getLevelDifficulty());
        newGame2.setTipoEjercicio(game2.getTipoEjercicio());
        gameRepository.save(newGame2);

        var newGame3 = new Game();
        newGame3.setName(game3.getName());
        newGame3.setDescription(game3.getDescription());
        newGame3.setLevelDifficulty(game3.getLevelDifficulty());
        newGame3.setTipoEjercicio(game3.getTipoEjercicio());
        gameRepository.save(newGame3);


    }
}
