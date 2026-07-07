package com.mrdanissimo.service;

import com.mrdanissimo.habit_tracker.HabitTrackerApplication;
import com.mrdanissimo.habit_tracker.dto.HabitRequest;
import com.mrdanissimo.habit_tracker.dto.HabitResponse;
import com.mrdanissimo.habit_tracker.entity.User;
import com.mrdanissimo.habit_tracker.exception.HabitNotFoundException;
import com.mrdanissimo.habit_tracker.repository.HabitRepository;
import com.mrdanissimo.habit_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.test.context.support.WithMockUser;
import com.mrdanissimo.habit_tracker.service.HabitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@WithMockUser(username = "alice")
@ContextConfiguration(classes = HabitTrackerApplication.class)
class HabitServiceTests {

    @Autowired
    private HabitService habitService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitRepository habitRepository;

    @BeforeEach
    void setUp() {
        habitRepository.deleteAll();
        userRepository.deleteAll();

        // Создаем и сохраняем alice, чтобы метод сервиса мог её найти
        User testUser = new User();
        testUser.setUsername("alice");
        testUser.setPassword("password_not_important_here");
        testUser.setEmail("alice@example.com");
        userRepository.save(testUser);
    }

	@Test
    void create_shouldSaveAndReturnResponse() {
        // Создаем привычку
        HabitRequest request = new HabitRequest();
        request.setName("Пить воду");
        request.setDescription("2 литра в день");
        request.setTarget(7);

        HabitResponse response = habitService.create(request);

        // Проверяем результат
        assertNotNull(response.getId(), "БД должна была сгенерировать ID!");
        assertEquals("Пить воду", response.getName());
        assertEquals(7, response.getTarget());
    }

    @Test
    void getById_whenExists_shouldReturnResponse() {
        // Создаем привычку, чтобы она точно была в базе
        HabitRequest request = new HabitRequest();
        request.setName("Зарядка");
        request.setTarget(5);
        HabitResponse saved = habitService.create(request);

        // Ищем её по сохраненному ID
        HabitResponse found = habitService.getById(saved.getId());

        assertNotNull(found);
        assertEquals("Зарядка", found.getName());
    }

    @Test
    void getById_whenNotExists_shouldThrowHabitNotFoundException() {
        Long Id = 999L;

        // Проверяем, что при вызове метода вылетит строго HabitNotFoundException
        assertThrows(HabitNotFoundException.class, () -> {
            habitService.getById(Id);
        }, "Если привычки нет, мы обязаны бросить HabitNotFoundException!");
    }
}
