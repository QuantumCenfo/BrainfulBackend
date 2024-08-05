package com.project.demo.rest.media;

import com.project.demo.logic.entity.media.Media;
import com.project.demo.logic.entity.media.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/medias")
public class MediaRestController {

    @Autowired
    private MediaRepository mediaRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','USER')")
    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Media addMedia(@RequestBody Media media) {
        return mediaRepository.save(media);
    }

    @PutMapping("/{id}")
    public Media updateMedia(@PathVariable Long id, @RequestBody Media media) {
        return mediaRepository.findById(id)
                .map(existingMedia -> {
                    existingMedia.setTitle(media.getTitle());
                    existingMedia.setDescription(media.getDescription());
                    existingMedia.setTypeMedia(media.getTypeMedia());
                    existingMedia.setPublishDate(media.getPublishDate());
                    existingMedia.setUrl(media.getUrl());
                    return mediaRepository.save(media);
                })
                .orElseGet(() -> {
                    media.setMediaId(id);
                    return mediaRepository.save(media);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public void deleteMedia(@PathVariable Long id) {
        mediaRepository.deleteById(id);
    }

}
