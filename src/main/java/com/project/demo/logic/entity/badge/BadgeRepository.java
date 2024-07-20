package com.project.demo.logic.entity.badge;

import com.project.demo.logic.entity.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BadgeRepository  extends JpaRepository<Badge, Long> {

    @Query("SELECT B FROM Badge B WHERE B.title = ?1")
    Optional<Badge> findByName(String name);

}
