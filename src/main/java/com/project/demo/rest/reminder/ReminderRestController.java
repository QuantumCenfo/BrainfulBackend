package com.project.demo.rest.reminder;

import com.project.demo.logic.entity.reminder.Reminder;
import com.project.demo.logic.entity.reminder.ReminderRepository;
import com.project.demo.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderRestController {

    @Autowired
    private ReminderRepository reminderRepository;

    @GetMapping("/user/{userId}")
    public List<Reminder> getRemindersByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return reminderRepository.findByUser(user);
    }

    @GetMapping("/today")
    public List<Reminder> getRemindersForToday() {
        Date today = new Date();
        return reminderRepository.findByReminderDate(today);
    }

    @GetMapping("/upcoming")
    public List<Reminder> getUpcomingReminders() {
        Date today = new Date();
        return reminderRepository.findByReminderDateGreaterThan(today);
    }

    @PostMapping
    public Reminder addReminder(@RequestBody Reminder reminder) {
        System.out.println(reminder.getUser());
        return reminderRepository.save(reminder);
    }

    @PutMapping("/{id}")
    public Reminder updateReminder(@PathVariable Long id, @RequestBody Reminder reminder) {
        if (reminderRepository.existsById(id)) {
            reminder.setReminderId(id);
            return reminderRepository.save(reminder);
        } else {
            throw new RuntimeException("Reminder not found");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteReminder(@PathVariable Long id) {
        if (reminderRepository.existsById(id)) {
            reminderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Reminder not found");
        }
    }
}