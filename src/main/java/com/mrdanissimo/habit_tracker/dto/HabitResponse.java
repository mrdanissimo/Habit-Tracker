package com.mrdanissimo.habit_tracker.dto;

import lombok.Data;

@Data
public class HabitResponse {
    private Long id;
    private String name;
    private String description;
    private int target;
}
