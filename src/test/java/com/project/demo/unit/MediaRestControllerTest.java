package com.project.demo.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.auth.JwtAuthenticationFilter;
import com.project.demo.logic.entity.auth.JwtService;
import com.project.demo.logic.entity.media.Media;
import com.project.demo.logic.entity.media.MediaRepository;
import com.project.demo.rest.media.MediaRestController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MediaRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class MediaRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;
    @MockBean
    private MediaRepository mediaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Media createMedia(Long id) {
        Media m = new Media();
        m.setMediaId(id);
        m.setTitle("Sample");
        m.setDescription("Desc");
        m.setTypeMedia("Video");
        m.setPublishDate(new Date());
        m.setUrl("http://example.com");
        return m;
    }

    @Test
    void testGetAllMedia() throws Exception {
        Media media = createMedia(1L);
        when(mediaRepository.findAll()).thenReturn(List.of(media));

        mockMvc.perform(get("/media"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mediaId").value(1L));
    }

    @Test
    void testAddMedia() throws Exception {
        Media media = createMedia(1L);
        when(mediaRepository.save(any(Media.class))).thenReturn(media);

        mockMvc.perform(post("/media")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(media)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mediaId").value(1L));
    }

    @Test
    void testUpdateMedia_Exists() throws Exception {
        Media existing = createMedia(1L);
        Media updated = createMedia(1L);
        updated.setTitle("Updated Title");

        when(mediaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(mediaRepository.save(any(Media.class))).thenReturn(updated);

        mockMvc.perform(put("/media/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void testUpdateMedia_NotExists() throws Exception {
        Media newMedia = createMedia(10L);

        when(mediaRepository.findById(10L)).thenReturn(Optional.empty());
        when(mediaRepository.save(any(Media.class))).thenReturn(newMedia);

        mockMvc.perform(put("/media/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMedia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mediaId").value(10L));
    }

    @Test
    void testDeleteMedia() throws Exception {
        mockMvc.perform(delete("/media/1"))
                .andExpect(status().isOk());
    }
}
