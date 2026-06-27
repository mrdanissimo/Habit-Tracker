package com.mrdanissimo.habit_tracker.dto;

import lombok.Data;
import java.util.List;

@Data
public class WeeklyStatsResponse {
    private List<DailyProgress> week;
}
