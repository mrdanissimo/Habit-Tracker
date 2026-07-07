package com.mrdanissimo.habit_tracker.controller;

import com.mrdanissimo.habit_tracker.dto.StatsResponse;
import com.mrdanissimo.habit_tracker.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
@Tag(name = "Статистика привычек", description = "Аналитика по конкретным привычкам")
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/{id}/stats")
    @Operation(summary = "Получить статистику конкретной привычки", description = "Возвращает детальную аналитику и прогресс выполнения для одной привычки по её ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статистика по привычке успешно получена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Привычка с указанным ID не найдена")
    })
    public ResponseEntity<StatsResponse> getStats(@PathVariable Long id) {
        return ResponseEntity.ok(statsService.getStats(id));
    }
}
