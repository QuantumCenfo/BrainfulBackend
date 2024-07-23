package com.project.demo.rest.participationoutdoor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.Azure.AzureBlobService;
import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.badge.BadgeRepository;
import com.project.demo.logic.entity.participationchallengeoutdoor.ParticipationChallengeOutdoor;
import com.project.demo.logic.entity.participationchallengeoutdoor.ParticipationOutdoorRepository;
import com.project.demo.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ParticipationChallengeOutdoor addParticipationOutdoor(@RequestPart("participationOutdoor") String participationoutdoorjson, @RequestPart("image") MultipartFile image) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ParticipationChallengeOutdoor participationChallengeOutdoor = objectMapper.readValue(participationoutdoorjson, ParticipationChallengeOutdoor.class);
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
