package com.project.demo.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.auth.JwtAuthenticationFilter;
import com.project.demo.logic.entity.auth.JwtService;
import com.project.demo.logic.entity.reminder.Reminder;
import com.project.demo.logic.entity.reminder.ReminderRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.rest.reminder.ReminderRestController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = ReminderRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ReminderRestController.class)
class ReminderRestControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReminderRepository reminderRepository;
    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private Reminder createReminder(Long id, Long userId) {
        Reminder r = new Reminder();
        r.setReminderId(id);
        r.setReminderDate(new Date());
        r.setReminderType("Email");
        r.setName("Test Reminder");
        r.setReminderDetails("Details test");

        User u = new User();
        u.setId(userId);
        r.setUser(u);

        return r;
    }

    @Test
    void testGetRemindersByUser() throws Exception {
        Reminder reminder = createReminder(1L, 10L);

        Mockito.when(reminderRepository.findByUser(any(User.class)))
                .thenReturn(List.of(reminder));

        mockMvc.perform(get("/api/reminders/user/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reminderId").value(1L));
    }

    @Test
    void testGetRemindersForToday() throws Exception {
        Reminder reminder = createReminder(2L, 5L);

        Mockito.when(reminderRepository.findByReminderDate(any(Date.class)))
                .thenReturn(List.of(reminder));

        mockMvc.perform(get("/api/reminders/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reminderId").value(2L));
    }

    @Test
    void testGetUpcomingReminders() throws Exception {
        Reminder reminder = createReminder(3L, 7L);

        Mockito.when(reminderRepository.findByReminderDateGreaterThan(any(Date.class)))
                .thenReturn(List.of(reminder));

        mockMvc.perform(get("/api/reminders/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reminderId").value(3L));
    }

    @Test
    void testAddReminder() throws Exception {
        Reminder reminder = createReminder(1L, 20L);

        Mockito.when(reminderRepository.save(any(Reminder.class)))
                .thenReturn(reminder);

        mockMvc.perform(post("/api/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reminder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reminderId").value(1L));
    }

    @Test
    void testUpdateReminder() throws Exception {
        Reminder reminder = createReminder(1L, 20L);

        Mockito.when(reminderRepository.existsById(1L)).thenReturn(true);
        Mockito.when(reminderRepository.save(any(Reminder.class))).thenReturn(reminder);

        mockMvc.perform(put("/api/reminders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reminder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reminderId").value(1L));
    }

    @Test
    void testUpdateReminder_NotFound() throws Exception {
        Reminder reminder = createReminder(null, 20L);

        Mockito.when(reminderRepository.existsById(999L)).thenReturn(false);

        mockMvc.perform(put("/api/reminders/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reminder)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testDeleteReminder() throws Exception {
        Mockito.when(reminderRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/reminders/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteReminder_NotFound() throws Exception {
        Mockito.when(reminderRepository.existsById(55L)).thenReturn(false);

        mockMvc.perform(delete("/api/reminders/55"))
                .andExpect(status().isInternalServerError());
    }
}