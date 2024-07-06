package com.project.demo.logic.entity.game;

import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT G FROM Game G WHERE LOWER(G.name) LIKE %?1%")
    List<Game> findGameWithCharacterInName(String character);

    @Query("SELECT G FROM Game G WHERE G.name = ?1")
    Optional<Game> findByName(String name);
}
