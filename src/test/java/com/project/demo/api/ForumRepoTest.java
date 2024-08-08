package com.project.demo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.comment.CommentRepository;
import com.project.demo.logic.entity.forum.Forum;
import com.project.demo.logic.entity.forum.ForumRepository;
import com.project.demo.logic.entity.auth.JwtService;
import com.project.demo.rest.forum.ForumRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ForumRestController.class)
public class ForumRepoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForumRepository forumRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CommentRepository commentRepository;

    @Test
    @WithMockUser(roles="USER")
    public void shouldRetrieveAllForums() throws Exception {
        // Arrange
        Forum forum1 = new Forum();
        forum1.setTitle("Título de prueba 1");
        forum1.setDescription("Descripción de prueba 1");
        forum1.setAnonymous(false);
        forum1.setCreationDate(new Date());

        Forum forum2 = new Forum();
        forum2.setTitle("Título de prueba 2");
        forum2.setDescription("Descripción de prueba 2");
        forum2.setAnonymous(false);
        forum2.setCreationDate(new Date());

        when(forumRepository.findAll()).thenReturn(Arrays.asList(forum1, forum2));

        // Act & Assert
        mockMvc.perform(get("/forums")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYW5pZWxAZ21haWwuY29tIiwiaWF0IjoxNzIzMTUxMjMxLCJleHAiOjE3MjMxNTQ4MzF9.DYb5CEjgiOVxvZR1ccn6LAo3MEpfCmh6aDlzWk8REn8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Título de prueba 1"))
                .andExpect(jsonPath("$[1].title").value("Título de prueba 2"));
    }


}
