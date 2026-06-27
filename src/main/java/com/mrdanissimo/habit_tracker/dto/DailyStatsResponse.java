package com.mrdanissimo.habit_tracker.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class DailyStatsResponse {
    private LocalDate date;
    private List<HabitDailyStatus> habits;
}

