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

    public ParticipationChallengeOutdoor() {}
}
