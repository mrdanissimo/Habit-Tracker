package com.mrdanissimo.habit_tracker.dto;

import lombok.Data;

@Data
public class StatsResponse {
    private int currentStreak;
    private int bestStreak;
    private double completionRate;
    private int totalCompletion;

}
