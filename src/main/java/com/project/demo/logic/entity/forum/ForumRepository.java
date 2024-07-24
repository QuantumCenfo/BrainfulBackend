package com.project.demo.logic.entity.forum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface ForumRepository extends JpaRepository<Forum, Long> {
    List<Forum> findByUserId(Long userId);
}