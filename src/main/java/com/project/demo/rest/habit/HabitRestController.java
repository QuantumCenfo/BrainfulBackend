package com.project.demo.rest.habit;

import com.project.demo.logic.entity.habittracker.HabitTracker;
import com.project.demo.logic.entity.habittracker.HabitTrackerRepository;
import com.project.demo.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habitTrackers")
public class HabitRestController {

    @Autowired
    private HabitTrackerRepository habitTrackerRepository;

    @GetMapping
    public List<HabitTracker> getAllHabitTrackers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return habitTrackerRepository.findByUser(currentUser);
    }

    @PostMapping
    public HabitTracker addHabitTracker(@RequestBody HabitTracker habitTracker) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        habitTracker.setUser(currentUser);
        return habitTrackerRepository.save(habitTracker);
    }

    @GetMapping("/{id}")
    public HabitTracker getHabitTrackerById(@PathVariable Long id) {
        return habitTrackerRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PutMapping("/{id}")
    public HabitTracker updateHabitTracker(@PathVariable Long id, @RequestBody HabitTracker habitTracker) {
        return habitTrackerRepository.findById(id)
                .map(existingHabitTracker -> {
                    existingHabitTracker.setHabitType(habitTracker.getHabitType());
                    existingHabitTracker.setHabitDate(habitTracker.getHabitDate());
                    existingHabitTracker.setHabitDetails(habitTracker.getHabitDetails());
                    return habitTrackerRepository.save(existingHabitTracker);
                })
                .orElseGet(() -> {
                    habitTracker.setHabitTrackerId(id);
                    return habitTrackerRepository.save(habitTracker);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteHabitTracker(@PathVariable Long id) {
        habitTrackerRepository.deleteById(id);
    }
}
