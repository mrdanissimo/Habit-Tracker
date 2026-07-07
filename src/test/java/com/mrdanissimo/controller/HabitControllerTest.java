package com.mrdanissimo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrdanissimo.habit_tracker.controller.HabitController;
import com.mrdanissimo.habit_tracker.dto.HabitRequest;
import com.mrdanissimo.habit_tracker.dto.HabitResponse;
import com.mrdanissimo.habit_tracker.service.HabitService;
import com.mrdanissimo.habit_tracker.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HabitController.class)
@org.springframework.test.context.ContextConfiguration(classes = com.mrdanissimo.habit_tracker.HabitTrackerApplication.class)
public class HabitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HabitService habitService; // Мокаем сервис, чтобы не трогать базу данных

    @MockBean // Добавьте эту строчку, чтобы подставить мок для фильтра безопасности!
    private JwtService jwtService;

    @Test
    @WithMockUser(username = "alice") // Эмулируем авторизованного пользователя
    void testGetHabits_returns200AndData() throws Exception {
        HabitResponse habit = new HabitResponse();
        habit.setId(1L);
        habit.setName("Пить воду");
        habit.setTarget(7);

        // Обучаем мок возвращать список из одной привычки
        when(habitService.getAllMyHabits()).thenReturn(List.of(habit));

        mockMvc.perform(get("/api/habits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Пить воду"))
                .andExpect(jsonPath("$[0].target").value(7));
    }

    @Test
    @WithMockUser(username = "alice")
    void testCreateHabit_withValidData_returns201() throws Exception {
        HabitRequest request = new HabitRequest();
        request.setName("Бег");
        request.setDescription("Утром в парке");
        request.setTarget(3);

        HabitResponse response = new HabitResponse();
        response.setId(2L);
        response.setName("Бег");
        response.setTarget(3);

        when(habitService.create(any(HabitRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/habits")
                        .with(csrf()) // Прокидываем CSRF-токен для прохождения фильтров Spring Security
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Бег"));
    }

    @Test
    @WithMockUser(username = "alice")
    void testCreateHabit_withInvalidData_returns400() throws Exception {
        HabitRequest badRequest = new HabitRequest();
        badRequest.setName(""); // Невалидное пустое имя
        badRequest.setTarget(-5); // Невалидная цель

        mockMvc.perform(post("/api/habits")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest()); // Наш GlobalExceptionHandler должен вернуть 400!
    }
}