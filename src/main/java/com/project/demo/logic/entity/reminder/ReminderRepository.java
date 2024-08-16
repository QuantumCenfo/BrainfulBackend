package com.project.demo.logic.entity.reminder;

import com.project.demo.logic.entity.reminder.Reminder;
import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByUser(User user);
    List<Reminder> findByReminderDate(Date reminderDate);
    List<Reminder> findByReminderDateGreaterThan(Date reminderDate);
}