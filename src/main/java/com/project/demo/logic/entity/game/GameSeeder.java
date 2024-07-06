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
        game.setName("Simon Dice ");
        game.setDescription("Juego de Simon dice. Seguir la secuencia de colores que se presenta.");
        game.setTipoEjercicio("Ejercicio Cognitivo");



        Optional<Game> optionalGame1 = gameRepository.findByName(game.getName());

        if(optionalGame1.isPresent()){
            return;
        }

        var newGame = new Game();
        newGame.setName(game.getName());
        newGame.setDescription(game.getDescription());
        newGame.setTipoEjercicio(game.getTipoEjercicio());
        gameRepository.save(newGame);




    }
}
