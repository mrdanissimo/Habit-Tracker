package com.mrdanissimo.habit_tracker.controller;

import com.mrdanissimo.habit_tracker.dto.RecordResponse;
import com.mrdanissimo.habit_tracker.service.RecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits/{habitId}/records")
public class RecordController {
    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    // Отметка выполнения привычки
    @PostMapping
    public ResponseEntity<RecordResponse> markCompleted(@PathVariable Long habitId) {
        RecordResponse created = recordService.markCompleted(habitId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Получение записей всех привычек
    @GetMapping
    public ResponseEntity<List<RecordResponse>> getAll(@PathVariable Long habitId) {
        List<RecordResponse> records = recordService.getAllByHabitId(habitId);
        return ResponseEntity.ok(records);
    }
}
