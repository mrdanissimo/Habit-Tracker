package com.mrdanissimo.habit_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HabitDailyStatus {
    private Long id;
    private String name;
    private boolean completed;
}
