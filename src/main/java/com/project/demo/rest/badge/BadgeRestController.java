package com.project.demo.rest.badge;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.Azure.AzureBlobService;
import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.badge.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/badges")
public class BadgeRestController {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private AzureBlobService azureBlobAdapter;

    @GetMapping
    public List<Badge> getBadges() {
        return badgeRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Badge addBadge(@RequestPart("badge") String badgeJson, @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // Parse the JSON string into a Badge object
        ObjectMapper objectMapper = new ObjectMapper();
        Badge badge = objectMapper.readValue(badgeJson, Badge.class);

        String imageUrl = azureBlobAdapter.upload(image);
        if (image != null && !image.isEmpty()) {
            badge.setUrl(imageUrl);
        }
        badge.setUrl(imageUrl);


        return badgeRepository.save(badge);
    }


    @GetMapping("/{badgeName}")
    public Badge getBadgeByName(@PathVariable String badgeName) {
        return badgeRepository.findByName(badgeName).orElseThrow(RuntimeException::new);
    }

    @DeleteMapping("/{id}")
    public void deleteBadge(@PathVariable Long id) {
         badgeRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Badge updateBadge(@PathVariable Long id, @RequestPart("badge") String badgeJson, @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Badge badge = objectMapper.readValue(badgeJson, Badge.class);


        String imageUrl = image !=null? azureBlobAdapter.upload(image): null;
        if (image != null ) {
            badge.setUrl(imageUrl);
        }
       
        return badgeRepository.findById(id)
                .map(existingBadge ->{
                    existingBadge.setTitle(badge.getTitle());
                    existingBadge.setDescription(badge.getDescription());
                    existingBadge.setUrl(badge.getUrl());
                    return badgeRepository.save(existingBadge);
                })
                .orElseGet(()->{
                    badge.setBadgeId(id);
                    return badgeRepository.save(badge);
                });
    }
}
