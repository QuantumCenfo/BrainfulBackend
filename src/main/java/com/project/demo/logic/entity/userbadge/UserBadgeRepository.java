package com.project.demo.logic.entity.userbadge;

import com.project.demo.logic.entity.badge.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    @Query("SELECT b FROM UserBadge bu INNER JOIN Badge b ON bu.badge.badgeId = b.badgeId WHERE bu.user.id = ?1  ")
    List<Badge> findByUserId(Long userId);

}


