package com.project.demo.logic.entity.game;

import jakarta.persistence.*;

@Table(name = "Games")
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "type_exercise", length = 50)
    private String tipoEjercicio;



    public Game() {}

    public Game(Long exerciseId, String name, String description, String tipoEjercicio) {
        this.gameId = exerciseId;
        this.name = name;
        this.description = description;
        this.tipoEjercicio = tipoEjercicio;

    }

    public Long getgameId() {
        return gameId;
    }

    public void setgameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTipoEjercicio() {
        return tipoEjercicio;
    }

    public void setTipoEjercicio(String tipoEjercicio) {
        this.tipoEjercicio = tipoEjercicio;
    }
}