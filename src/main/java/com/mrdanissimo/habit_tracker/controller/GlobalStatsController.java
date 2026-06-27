package com.mrdanissimo.habit_tracker.controller;

import com.mrdanissimo.habit_tracker.dto.DailyStatsResponse;
import com.mrdanissimo.habit_tracker.dto.WeeklyStatsResponse;
import com.mrdanissimo.habit_tracker.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class GlobalStatsController {

    private final StatsService statsService;

    @GetMapping("/daily")
    public ResponseEntity<DailyStatsResponse> getDailyStats() {
        return ResponseEntity.ok(statsService.getDailyStats());
    }

    @GetMapping("/week")
    public ResponseEntity<WeeklyStatsResponse> getWeeklyStats() {
        return ResponseEntity.ok(statsService.getWeeklyStats());
    }
}