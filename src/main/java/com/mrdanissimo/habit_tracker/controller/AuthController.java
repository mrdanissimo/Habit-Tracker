package com.mrdanissimo.habit_tracker.controller;

import com.mrdanissimo.habit_tracker.dto.AuthRequest;
import com.mrdanissimo.habit_tracker.dto.AuthResponse;
import com.mrdanissimo.habit_tracker.dto.UserResponse;
import com.mrdanissimo.habit_tracker.service.HabitService;
import com.mrdanissimo.habit_tracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Авторизация и пользователи", description = "Регистрация новых аккаунтов и получение JWT-токенов")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя", description = "Создает новый аккаунт в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Пользователь с таким именем уже существует или невалидные данные")
    })
    public ResponseEntity<UserResponse> register(@RequestBody AuthRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Вход в систему (Аутентификация)", description = "Проверяет учетные данные и возвращает JWT-токен авторизации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход, токен сгенерирован"),
            @ApiResponse(responseCode = "400", description = "Неверный логин или пароль")
    })
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
