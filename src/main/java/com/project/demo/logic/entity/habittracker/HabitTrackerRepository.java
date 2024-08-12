package com.project.demo.logic.entity.habittracker;

import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitTrackerRepository extends JpaRepository<HabitTracker, Long> {
    List<HabitTracker> findByUser(User user);
}