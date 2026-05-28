package com.mrdanissimo.habit_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class HabitRequest {
    // проверка на пустоту
    @NotBlank(message = "Требуется указать имя")
    private String name;
    private String description;

    // проверка цели на >0
    @Positive(message = "Target must be a positive number")
    private int target;
}
