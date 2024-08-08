package com.project.demo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.game.Game;
import com.project.demo.logic.entity.game.GameRepository;
import com.project.demo.rest.game.GameRestController;
import com.project.demo.logic.entity.auth.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameRestController.class)
public class GamesRepoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private JwtService jwtService; // Mock de JwtService

    @Test
    @WithMockUser(roles="USER")
    public void shouldRetrieveAllGames() throws Exception {
        // Arrange
        Game game1 = new Game(1L, "Game 1", "Description 1", "Type 1");
        Game game2 = new Game(2L, "Game 2", "Description 2", "Type 2");
        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2));

        // Act & Assert
        mockMvc.perform(get("/games")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYW5pZWxAZ21haWwuY29tIiwiaWF0IjoxNzIzMTUxMjMxLCJleHAiOjE3MjMxNTQ4MzF9.DYb5CEjgiOVxvZR1ccn6LAo3MEpfCmh6aDlzWk8REn8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Game 1"))
                .andExpect(jsonPath("$[1].name").value("Game 2"));
    }
}
