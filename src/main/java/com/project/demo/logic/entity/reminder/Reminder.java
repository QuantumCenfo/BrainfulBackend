package com.project.demo.logic.entity.reminder;
import com.project.demo.logic.entity.challengegame.ChallengeGame;
import com.project.demo.logic.entity.form.Form;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.Date;
@Table(name = "Reminders")
@Entity
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId;

    @Column(name = "reminder_date")
    private Date reminderDate;

    @Column(name = "reminder_type")
    private String reminderType;

    @Column(name = "reminder_details", length = 1000)
    private String reminderDetails;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private User user;

    public Reminder() {}

    public Reminder(Long reminderId, Date reminderDate, String reminderType, String reminderDetails, User user) {
        this.reminderId = reminderId;
        this.reminderDate = reminderDate;
        this.reminderType = reminderType;
        this.reminderDetails = reminderDetails;
        this.user = user;
    }

    public Long getReminderId() {
        return reminderId;
    }

    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public String getReminderDetails() {
        return reminderDetails;
    }

    public void setReminderDetails(String reminderDetails) {
        this.reminderDetails = reminderDetails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}