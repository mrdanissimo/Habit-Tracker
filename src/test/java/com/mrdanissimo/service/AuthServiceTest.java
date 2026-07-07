package com.mrdanissimo.service;

import com.mrdanissimo.habit_tracker.dto.*;
import com.mrdanissimo.habit_tracker.entity.User;
import com.mrdanissimo.habit_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(
        classes = com.mrdanissimo.habit_tracker.HabitTrackerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class AuthServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // Очищаем базу пользователей перед каждым тестом
    }

    @Test
    void testRegister_savesPasswordAsHash() {
        AuthRequest registerRequest = new AuthRequest("alice", "super-secret-password", "alice@example.com");

        ResponseEntity<UserResponse> response = restTemplate.postForEntity(
                "/api/auth/register", registerRequest, UserResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("alice", response.getBody().getUsername());

        // Проверяем, что в БД пароль захеширован, а не лежит в открытом виде
        User savedUser = userRepository.findByUsername("alice").orElseThrow();
        assertNotEquals("super-secret-password", savedUser.getPassword());
        assertTrue(passwordEncoder.matches("super-secret-password", savedUser.getPassword()));
    }

    @Test
    void testLogin_withCorrectPassword_returnsToken() {
        // Предварительно сохраняем пользователя в базу с хэшированием
        User user = new User();
        user.setUsername("alice");
        user.setPassword(passwordEncoder.encode("super-secret-password"));
        user.setEmail("alice@example.com");
        userRepository.save(user);

        AuthRequest loginRequest = new AuthRequest("alice", "super-secret-password", null);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/api/auth/login", loginRequest, AuthResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
        assertTrue(response.getBody().getToken().startsWith("eyJ"));
        assertEquals("alice", response.getBody().getUsername());
    }

    @Test
    void testLogin_withWrongPassword_returns401() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword(passwordEncoder.encode("super-secret-password"));
        user.setEmail("alice@example.com");
        userRepository.save(user);

        // Отправляем неверный пароль
        AuthRequest badLoginRequest = new AuthRequest("alice", "wrong-password", null);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/api/auth/login", badLoginRequest, AuthResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetHabits_withoutToken_isProtected() {
        ResponseEntity<Void> response = restTemplate.getForEntity("/api/habits", Void.class);

        assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED
                || response.getStatusCode() == HttpStatus.FORBIDDEN);
    }

    @Test
    void testDataIsolation_aliceDoesNotSeeBobsHabits() {
        // Создаем в базе двух пользователей
        User alice = new User();
        alice.setUsername("alice");
        alice.setPassword(passwordEncoder.encode("password123"));
        alice.setEmail("alice@example.com");
        userRepository.save(alice);

        User bob = new User();
        bob.setUsername("bob");
        bob.setPassword(passwordEncoder.encode("password456"));
        bob.setEmail("bob@example.com");
        userRepository.save(bob);

        // JWT-токен для Alice
        AuthRequest aliceLogin = new AuthRequest("alice", "password123", null);
        String aliceToken = restTemplate.postForEntity("/api/auth/login", aliceLogin, AuthResponse.class)
                .getBody().getToken();

        // JWT-токен для Bob
        AuthRequest bobLogin = new AuthRequest("bob", "password456", null);
        String bobToken = restTemplate.postForEntity("/api/auth/login", bobLogin, AuthResponse.class)
                .getBody().getToken();

        // Логинимся как Alice и создаем привычку
        HttpHeaders aliceHeaders = new HttpHeaders();
        aliceHeaders.setBearerAuth(aliceToken);

        HabitRequest habitRequest = new HabitRequest();
        habitRequest.setName("Спорт Элис");
        habitRequest.setDescription("30 минут тренировки");
        habitRequest.setTarget(3);

        HttpEntity<HabitRequest> aliceCreateEntity = new HttpEntity<>(habitRequest, aliceHeaders);
        restTemplate.postForEntity("/api/habits", aliceCreateEntity, HabitResponse.class);

        // Запрос GET /api/habits от имени Боба
        HttpHeaders bobHeaders = new HttpHeaders();
        bobHeaders.setBearerAuth(bobToken);
        HttpEntity<Void> bobGetEntity = new HttpEntity<>(bobHeaders);

        ResponseEntity<HabitResponse[]> bobResponse = restTemplate.exchange(
                "/api/habits", HttpMethod.GET, bobGetEntity, HabitResponse[].class);

        assertEquals(HttpStatus.OK, bobResponse.getStatusCode());
        assertNotNull(bobResponse.getBody());
        assertEquals(0, bobResponse.getBody().length);
    }
}