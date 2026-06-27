package com.mrdanissimo.habit_tracker.service;

import com.mrdanissimo.habit_tracker.dto.StatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/{id}/stats")
    public ResponseEntity<StatsResponse> getStats(@PathVariable Long id) {
        return ResponseEntity.ok(statsService.getStats(id));
    }
}
