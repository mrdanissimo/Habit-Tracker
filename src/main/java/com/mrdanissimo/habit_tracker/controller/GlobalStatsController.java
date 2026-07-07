package com.mrdanissimo.habit_tracker.controller;

import com.mrdanissimo.habit_tracker.dto.DailyStatsResponse;
import com.mrdanissimo.habit_tracker.dto.WeeklyStatsResponse;
import com.mrdanissimo.habit_tracker.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "Глобальная статистика", description = "Аналитика и статистика выполнения привычек")
public class GlobalStatsController {

    private final StatsService statsService;

    @GetMapping("/daily")
    @Operation(summary = "Получить статистику за текущий день", description = "Возвращает агрегированные данные по выполнению всех привычек пользователя за сегодняшний день")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статистика успешно сгенерирована и получена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public ResponseEntity<DailyStatsResponse> getDailyStats() {
        return ResponseEntity.ok(statsService.getDailyStats());
    }

    @GetMapping("/week")
    @Operation(summary = "Получить статистику за текущую неделю", description = "Возвращает детальную аналитику прогресса по всем привычкам за последние 7 дней")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Недельная статистика успешно сгенерирована и получена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public ResponseEntity<WeeklyStatsResponse> getWeeklyStats() {
        return ResponseEntity.ok(statsService.getWeeklyStats());
    }
}