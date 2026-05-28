package com.mrdanissimo.habit_tracker.controller;

import com.mrdanissimo.habit_tracker.dto.HabitRequest;
import com.mrdanissimo.habit_tracker.dto.HabitResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mrdanissimo.habit_tracker.service.HabitService;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    // Создание новой привычки
    @PostMapping
    public ResponseEntity<HabitResponse> create(@Valid @RequestBody HabitRequest request) {
        HabitResponse created = habitService.create(request); // Запрос передается в сервис
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Возвращает список привычек
    @GetMapping
    public ResponseEntity<List<HabitResponse>> getAll() {
        return ResponseEntity.ok( habitService.getAll());
    }

    // Получает привычку по ID
    @GetMapping("/{id}")
    public ResponseEntity<HabitResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(habitService.getById(id));
    }

    // Обновление привычки
    @PutMapping("/{id}")
    public ResponseEntity<HabitResponse> update(@PathVariable Long id, @Valid @RequestBody HabitRequest request) {
        return ResponseEntity.ok(habitService.update(id, request));
    }

    // Удаление привычки
    @DeleteMapping("/{id}")
    public ResponseEntity<HabitResponse> delete(@PathVariable Long id) {
        habitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
