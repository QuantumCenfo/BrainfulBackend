package com.project.demo.api;

import com.project.demo.logic.entity.auth.JwtService;
import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.badge.BadgeRepository;
import com.project.demo.rest.badge.BadgeRestController;
import com.project.demo.logic.entity.Azure.AzureBlobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BadgeRestController.class)
public class BadgeRepoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BadgeRepository badgeRepository;

    @MockBean
    private JwtService jwtService; // Mock de JwtService

    @MockBean
    private AzureBlobService azureBlobService; // Asegúrate de incluir esta línea si AzureBlobService se usa en el controlador


    @Test
    @WithMockUser(roles = "USER")
    public void shouldRetrieveAllBadges() throws Exception {
        // Arrange
        Badge badge1 = new Badge(1L, "Badge 1", "Description 1", new Date(), "Type 1");
        Badge badge2 = new Badge(2L, "Badge 2", "Description 2", new Date(), "Type 2");
        when(badgeRepository.findAll()).thenReturn(Arrays.asList(badge1, badge2));

        // Act & Assert
        mockMvc.perform(get("/badges")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Badge 1"))
                .andExpect(jsonPath("$[1].title").value("Badge 2"));
    }
}
