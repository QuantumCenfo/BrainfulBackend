package com.project.demo.rest.participationoutdoor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.Azure.AzureBlobService;
import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.badge.BadgeRepository;
import com.project.demo.logic.entity.participationchallengeoutdoor.ParticipationChallengeOutdoor;
import com.project.demo.logic.entity.participationchallengeoutdoor.ParticipationOutdoorRepository;
import com.project.demo.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/participationsOutdoor")
@CrossOrigin(origins = "http://localhost:4200")
public class ParticipationOutDoorRestController {
    @Autowired
    private ParticipationOutdoorRepository participationOutdoorRepository;
    @Autowired
    private AzureBlobService azureBlobAdapter;


    @GetMapping
    public List<ParticipationChallengeOutdoor> getParticipationsChallengeOutdoor() {
        return participationOutdoorRepository.findAll();
    }
    @PostMapping


    public ParticipationChallengeOutdoor addParticipationOutdoor(
            @RequestPart("participationOutdoor") String participationoutdoorjson,
            @RequestPart("image") MultipartFile image) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ParticipationChallengeOutdoor participationChallengeOutdoor = objectMapper.readValue(participationoutdoorjson, ParticipationChallengeOutdoor.class);

        Long userId = participationChallengeOutdoor.getUser().getId();
        Long challengeOutdoorId = participationChallengeOutdoor.getChallengeOutdoor().getOutdoorChallengeId();
        String status = participationChallengeOutdoor.getStatus();

        // Buscar participaciones existentes con el mismo userId y challengeOutdoorId
        List<ParticipationChallengeOutdoor> existingParticipations = participationOutdoorRepository.findByUser_IdAndChallengeOutdoor_OutdoorChallengeId(userId, challengeOutdoorId);


        boolean isInvalidStateExist = existingParticipations.stream()
                .anyMatch(p -> !("rechazada".equals(p.getStatus())));

        if (isInvalidStateExist) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A participation with the same user and challenge already exists with a status other than pendiente or revisado.");
        }

        // Subir imagen y guardar participación
        String imageUrl = azureBlobAdapter.upload(image);
        participationChallengeOutdoor.setEvidence(imageUrl);
        return participationOutdoorRepository.save(participationChallengeOutdoor);
    }



    @PutMapping("/{id}")
    public ParticipationChallengeOutdoor updateParticipation(
            @PathVariable Long id,
            @RequestBody ParticipationChallengeOutdoor participationChallengeOutdoor) {

        // Find the existing record
        return participationOutdoorRepository.findById(id)
                .map(existingParticipation -> {

                    existingParticipation.setStatus(participationChallengeOutdoor.getStatus());
                    existingParticipation.setFechaRevision(participationChallengeOutdoor.getFechaRevision());

                    return participationOutdoorRepository.save(existingParticipation);
                })
                .orElseGet(() -> {
                    // If not found, save the request body as a new record
                    participationChallengeOutdoor.setParticipationOutdoorId(id);
                    return participationOutdoorRepository.save(participationChallengeOutdoor);
                });
    }



}
