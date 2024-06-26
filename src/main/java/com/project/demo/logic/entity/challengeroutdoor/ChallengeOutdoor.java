package com.project.demo.logic.entity.challengeroutdoor;
import com.project.demo.logic.entity.badge.Badge;
import jakarta.persistence.*;

import java.util.Date;

@Table(name = "Outdoor_challenges")
@Entity
public class ChallengeOutdoor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outdoorChallengeId;

    private String requirement;

    @Column(length = 500)
    private String description;

    private String name;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "badge_id", referencedColumnName = "badgeId", nullable = false)
    private Badge badgeId;


    public ChallengeOutdoor() {}

    public ChallengeOutdoor(Long outdoorChallengeId, String requirement, String description, Date startDate, Date endDate, Badge badgeId,String name) {
        this.outdoorChallengeId = outdoorChallengeId;
        this.requirement = requirement;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.badgeId = badgeId;
        this.name =name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOutdoorChallengeId() {
        return outdoorChallengeId;
    }

    public void setOutdoorChallengeId(Long outdoorChallengeId) {
        this.outdoorChallengeId = outdoorChallengeId;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Badge getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(Badge badgeId) {
        this.badgeId = badgeId;
    }
}
