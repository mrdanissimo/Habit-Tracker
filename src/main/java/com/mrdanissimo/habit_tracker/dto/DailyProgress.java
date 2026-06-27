package com.mrdanissimo.habit_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyProgress {
    private LocalDate date;
    private int completedCount;
    private int totalCount;
}
