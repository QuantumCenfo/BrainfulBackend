package com.project.demo.logic.entity.participationchallengeoutdoor;
import com.project.demo.logic.entity.challengegame.ChallengeGame;
import com.project.demo.logic.entity.challengeroutdoor.ChallengeOutdoor;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.Date;
@Table(name = "Participation_Outdoor_challenges")
@Entity
public class ParticipationChallengeOutdoor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participationOutdoorId;

    @Column(length = 1000)
    private String evidence;

    private String status;

    @Column(name = "publish_date")
    private Date fechaPublicacion;

    @Column(name = "fecha_revision")
    private Date fechaRevision;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "desafio_externo_id", referencedColumnName = "outdoorChallengeId", nullable = false)
    private ChallengeOutdoor challengeOutdoor;

    public ParticipationChallengeOutdoor() {

    }

    public ParticipationChallengeOutdoor(Long participationOutdoorId, String evidence, String status, Date fechaPublicacion, Date fechaRevision, User user, ChallengeOutdoor challengeOutdoor) {
        this.participationOutdoorId = participationOutdoorId;
        this.evidence = evidence;
        this.status = status;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaRevision = fechaRevision;
        this.user = user;
        this.challengeOutdoor = challengeOutdoor;
    }

    public Long getParticipationOutdoorId() {
        return participationOutdoorId;
    }

    public void setParticipationOutdoorId(Long participationOutdoorId) {
        this.participationOutdoorId = participationOutdoorId;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChallengeOutdoor getChallengeOutdoor() {
        return challengeOutdoor;
    }

    public void setChallengeOutdoor(ChallengeOutdoor challengeOutdoor) {
        this.challengeOutdoor = challengeOutdoor;
    }
}
