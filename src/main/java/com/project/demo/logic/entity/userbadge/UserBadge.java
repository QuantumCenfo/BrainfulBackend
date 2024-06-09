package com.project.demo.logic.entity.userbadge;
import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.challengegame.ChallengeGame;
import com.project.demo.logic.entity.form.Form;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import java.util.Date;

@Table(name = "User_badges")
@Entity

public class UserBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userBadgeId;

    @Column(name = "obtained_date")
    private Date obtainedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "badge_id", referencedColumnName = "badgeId", nullable = false)
    private Badge badge;

    public UserBadge() {}

    public UserBadge(Long userBadgeId, Date obtainedDate, User user, Badge badge) {
        this.userBadgeId = userBadgeId;
        this.obtainedDate = obtainedDate;
        this.user = user;
        this.badge = badge;
    }


    public Long getUserBadgeId() {
        return userBadgeId;
    }

    public void setUserBadgeId(Long userBadgeId) {
        this.userBadgeId = userBadgeId;
    }

    public Date getObtainedDate() {
        return obtainedDate;
    }

    public void setObtainedDate(Date obtainedDate) {
        this.obtainedDate = obtainedDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }
}
