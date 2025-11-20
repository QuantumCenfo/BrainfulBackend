package com.project.demo.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.auth.JwtAuthenticationFilter;
import com.project.demo.logic.entity.auth.JwtService;
import com.project.demo.logic.entity.form.Form;
import com.project.demo.logic.entity.form.FormRepository;
import com.project.demo.logic.entity.recommendation.Recommendation;
import com.project.demo.logic.entity.recommendation.RecommendationRepository;
import com.project.demo.rest.recommendation.RecommendationRestController;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = RecommendationRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecommendationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationRepository recommendationRepository;

    @MockBean
    private FormRepository formRepository;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    // ---------------------------------------------
    // Helper para crear Recommendation válida
    // ---------------------------------------------
    private Recommendation createRecommendation(Long id, Long formId) {
        Recommendation r = new Recommendation();
        r.setRecommendationId(id);
        r.setRecommendationType("Health");
        r.setDate(new Date());
        r.setDescription("Mock description");

        Form f = new Form();
        f.setFormId(formId);
        r.setForm(f);

        return r;
    }

    // ---------------------------------------------------------
    // GET /recommendations/user/{userId}
    // ---------------------------------------------------------
    @Test
    void testGetRecommendationsByUserId() throws Exception {
        Form form = new Form();
        form.setFormId(10L);

        Recommendation rec = createRecommendation(1L, 10L);

        Mockito.when(formRepository.findByUserId(99L))
                .thenReturn(List.of(form));

        Mockito.when(recommendationRepository.findByFormFormId(10L))
                .thenReturn(List.of(rec));

        mockMvc.perform(get("/recommendations/user/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recommendationId").value(1L));
    }

    // ---------------------------------------------------------
    // GET /recommendations
    // ---------------------------------------------------------
    @Test
    void testGetAllRecommendations() throws Exception {
        Recommendation rec = createRecommendation(1L, 5L);

        Mockito.when(recommendationRepository.findAll())
                .thenReturn(List.of(rec));

        mockMvc.perform(get("/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recommendationId").value(1L));
    }

    // ---------------------------------------------------------
    // POST /recommendations
    // ---------------------------------------------------------
    @Test
    void testAddRecommendation() throws Exception {
        Recommendation rec = createRecommendation(1L, 5L);

        Mockito.when(recommendationRepository.save(any(Recommendation.class)))
                .thenReturn(rec);

        mockMvc.perform(post("/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rec)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationId").value(1L));
    }

    // ---------------------------------------------------------
    // GET /recommendations/{id}
    // ---------------------------------------------------------
    @Test
    void testGetRecommendationById() throws Exception {
        Recommendation rec = createRecommendation(1L, 5L);

        Mockito.when(recommendationRepository.findById(1L))
                .thenReturn(Optional.of(rec));

        mockMvc.perform(get("/recommendations/{id}", 1))   // 🔥 FIX
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationId").value(1L));
    }

    // ---------------------------------------------------------
    // DELETE /recommendations/{id}
    // ---------------------------------------------------------
    @Test
    void testDeleteRecommendation() throws Exception {
        Mockito.doNothing().when(recommendationRepository).deleteById(1L);

        mockMvc.perform(delete("/recommendations/1"))
                .andExpect(status().isOk());
    }
}
