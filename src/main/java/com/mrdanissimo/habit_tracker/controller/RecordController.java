package com.mrdanissimo.habit_tracker.controller;

import com.mrdanissimo.habit_tracker.dto.RecordResponse;
import com.mrdanissimo.habit_tracker.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits/{habitId}/records")
@Tag(name = "Прогресс выполнения (Записи)", description = "Отметки о выполнении привычек по дням")
public class RecordController {
    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    // Отметка выполнения привычки
    @PostMapping
    @Operation(summary = "Отметить выполнение привычки", description = "Создает отметку о том, что привычка выполнена за сегодняшний день")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отметка успешно создана"),
            @ApiResponse(responseCode = "400", description = "Привычка уже была отмечена сегодня / Ошибка в бизнес-логике"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Привычка с таким ID не найдена")
    })

    public ResponseEntity<RecordResponse> markCompleted(@PathVariable Long habitId) {
        RecordResponse created = recordService.markCompleted(habitId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Получение записей всех привычек
    @GetMapping
    @Operation(summary = "Получить все отметки привычки", description = "Возвращает историю выполнения конкретной привычки по её ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "История выполнения успешно получена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Привычка не найдена")
    })
    public ResponseEntity<List<RecordResponse>> getAll(@PathVariable Long habitId) {
        List<RecordResponse> records = recordService.getAllByHabitId(habitId);
        return ResponseEntity.ok(records);
    }
}
