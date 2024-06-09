package com.project.demo.logic.entity.habittracker;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.Date;

@Table(name = "HabitTrackers")
@Entity
public class HabitTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long habitTrackerId;

    @Column(name = "habit_type")
    private String habitType;

    @Column(name = "habit_date")
    private Date habitDate;

    @Column(name = "habit_Details", length = 500)
    private String habitDetails;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private User user;

    public HabitTracker() {}

    public HabitTracker(Long habitTrackerId, String habitType, Date habitDate, String habitDetails, User user) {
        this.habitTrackerId = habitTrackerId;
        this.habitType = habitType;
        this.habitDate = habitDate;
        this.habitDetails = habitDetails;
        this.user = user;
    }

    public Long getHabitTrackerId() {
        return habitTrackerId;
    }

    public void setHabitTrackerId(Long habitTrackerId) {
        this.habitTrackerId = habitTrackerId;
    }

    public String getHabitType() {
        return habitType;
    }

    public void setHabitType(String habitType) {
        this.habitType = habitType;
    }

    public Date getHabitDate() {
        return habitDate;
    }

    public void setHabitDate(Date habitDate) {
        this.habitDate = habitDate;
    }

    public String getHabitDetails() {
        return habitDetails;
    }

    public void setHabitDetails(String habitDetails) {
        this.habitDetails = habitDetails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}