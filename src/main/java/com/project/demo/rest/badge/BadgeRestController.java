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
    public Badge addBadge(@RequestPart("badge") String badgeJson, @RequestPart("image") MultipartFile image) throws IOException {

        // Parse the JSON string into a Badge object
        ObjectMapper objectMapper = new ObjectMapper();
        Badge badge = objectMapper.readValue(badgeJson, Badge.class);

        String imageUrl = azureBlobAdapter.upload(image);
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
}
