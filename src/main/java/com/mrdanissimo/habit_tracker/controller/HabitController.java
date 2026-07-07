package com.mrdanissimo.habit_tracker.controller;

import com.mrdanissimo.habit_tracker.dto.HabitRequest;
import com.mrdanissimo.habit_tracker.dto.HabitResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mrdanissimo.habit_tracker.service.HabitService;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@Tag(name = "Привычки", description = "Управление привычками пользователя")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    // Создание новой привычки
    @PostMapping
    @Operation(summary = "Создать новую привычку", description = "Добавляет новую привычку в профиль авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Привычка успешно создана"),
            @ApiResponse(responseCode = "400", description = "Невалидные входные данные (например, пустое имя или отрицательная цель)"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public ResponseEntity<HabitResponse> create(@Valid @RequestBody HabitRequest request) {
        HabitResponse created = habitService.create(request); // Запрос передается в сервис
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Возвращает список привычек
    @GetMapping
    @Operation(summary = "Получить все привычки", description = "Возвращает список всех привычек текущего авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список привычек успешно получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public ResponseEntity<List<HabitResponse>> getAll() {
        return ResponseEntity.ok(habitService.getAllMyHabits());
    }

    // Получает привычку по ID
    @GetMapping("/{id}")
    @Operation(summary = "Получить привычку по ID", description = "Возвращает информацию о конкретной привычке по её уникальному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Привычка успешно найдена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Привычка с указанным ID не найдена")
    })
    public ResponseEntity<HabitResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(habitService.getById(id));
    }

    // Обновление привычки
    @PutMapping("/{id}")
    @Operation(summary = "Обновить привычку", description = "Редактирует параметры существующей привычки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Привычка успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные для обновления"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Привычка не найдена")
    })
    public ResponseEntity<HabitResponse> update(@PathVariable Long id, @Valid @RequestBody HabitRequest request) {
        return ResponseEntity.ok(habitService.update(id, request));
    }

    // Удаление привычки
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить привычку", description = "Удаляет привычку текущего пользователя по её ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Привычка успешно удалена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (попытка удалить чужую привычку)"),
            @ApiResponse(responseCode = "404", description = "Привычка не найдена")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        habitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
